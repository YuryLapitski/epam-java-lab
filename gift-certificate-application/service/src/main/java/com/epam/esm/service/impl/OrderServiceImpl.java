package com.epam.esm.service.impl;

import com.epam.esm.service.exception.OrderNotFoundException;
import com.epam.esm.service.exception.PageNumberValidationException;
import com.epam.esm.service.exception.UserHasNoOrdersException;
import com.epam.esm.service.exception.UserNotFoundException;
import com.epam.esm.service.pagination.CustomPagination;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.repository.dao.OrderRepository;
import com.epam.esm.repository.dao.UserRepository;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.pagination.PaginationConverter;
import com.epam.esm.service.util.Message;
import com.epam.esm.service.validator.PaginationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private static final int FIRST_PAGE = 1;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final GiftCertificateService giftCertificateService;
    private final PaginationValidator paginationValidator;
    private final PaginationConverter paginationConverter;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            UserRepository userRepository,
                            UserService userService,
                            GiftCertificateService giftCertificateService,
                            PaginationValidator paginationValidator,
                            PaginationConverter paginationConverter) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.giftCertificateService = giftCertificateService;
        this.paginationValidator = paginationValidator;
        this.paginationConverter = paginationConverter;
    }

    @Transactional
    @Override
    public Order create(OrderDto orderDto) {
        User user = userService.findById(orderDto.getUserId());

        GiftCertificate giftCertificate = giftCertificateService
                .findByGiftCertificateId(orderDto.getGiftCertificateId());

        Order order = new Order();
        order.setUser(user);
        order.setGiftCertificate(giftCertificate);
        order.setPrice(giftCertificate.getPrice());

        return orderRepository.save(order);
    }

    @Override
    public List<Order> findAll(CustomPagination pagination) {
        Pageable pageable = paginationConverter.convert(pagination);
        Page<Order> orderPage = orderRepository.findAll(pageable);

        int lastPage = orderPage.getTotalPages();
        if (!paginationValidator.isPageValid(pagination, lastPage)) {
            String message = String.format(Message.PAGE_NUMBER_INVALID_MSG, lastPage);
            throw new PageNumberValidationException(message);
        }

        return orderPage.getContent();
    }

    @Override
    public Order findById(Long id) {
        return orderRepository.findById(id).orElseThrow(() ->
                new OrderNotFoundException(String.format(Message.ORDER_ID_NOT_FOUND_MSG, id)));
    }

    @Override
    public List<Order> findByUserId(Long userId, CustomPagination pagination) {
        if (!userRepository.findById(userId).isPresent()) {
            throw  new UserNotFoundException(String.format(Message.USER_ID_NOT_FOUND_MSG, userId));
        }

        Pageable pageable = paginationConverter.convert(pagination);
        Page<Order> orderPage = orderRepository.findByUserId(userId, pageable);

        int lastPage = orderPage.getTotalPages();

        if (lastPage < FIRST_PAGE) {
            String msg = String.format(Message.USER_HAS_NO_ORDERS_MSG, userId);
            throw new UserHasNoOrdersException(msg);
        }

        if (!paginationValidator.isPageValid(pagination, lastPage)) {
            String message = String.format(Message.PAGE_NUMBER_INVALID_MSG, lastPage);
            throw new PageNumberValidationException(message);
        }

        return orderPage.getContent();
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() ->
                new OrderNotFoundException(String.format(Message.ORDER_ID_NOT_FOUND_MSG, id)));

        orderRepository.delete(order);
    }
}
