package com.epam.esm.service.impl;

import com.epam.esm.pagination.CustomPagination;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.repository.dao.OrderDao;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.exception.OrderNotFoundException;
import com.epam.esm.service.validator.PaginationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private static final String ORDER_ID_NOT_FOUND_MSG = "Order with id=%d not found.";
    private final OrderDao orderDao;
    private final UserService userService;
    private final GiftCertificateService giftCertificateService;
    private final PaginationValidator paginationValidator;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao,
                            UserService userService,
                            GiftCertificateService giftCertificateService,
                            PaginationValidator paginationValidator) {
        this.orderDao = orderDao;
        this.userService = userService;
        this.giftCertificateService = giftCertificateService;
        this.paginationValidator = paginationValidator;
    }

    @Transactional
    @Override
    public Order create(OrderDto orderDto) {
        Order order = new Order();
        User user = userService.findById(orderDto.getUserId());
        GiftCertificate giftCertificate = giftCertificateService
                .findByGiftCertificateId(orderDto.getGiftCertificateId());
        order.setUser(user);
        order.setGiftCertificate(giftCertificate);
        order.setPrice(giftCertificate.getPrice());
        return orderDao.create(order);
    }

    @Override
    public List<Order> findAll(CustomPagination pagination) {
        Long ordersNumber = orderDao.findOrdersNumber();
        pagination = paginationValidator.validatePagination(pagination, ordersNumber);

        return orderDao.findAll(pagination);
    }

    @Override
    public Order findById(Long id) {
        return orderDao.findById(id).orElseThrow(() ->
                new OrderNotFoundException(String.format(ORDER_ID_NOT_FOUND_MSG, id)));
    }

    @Override
    public List<Order> findByUserId(Long userId, CustomPagination pagination) {
        Long ordersNumber = orderDao.findUserOrdersNumber(userId);
        pagination = paginationValidator.validatePagination(pagination, ordersNumber);

        return orderDao.findByUserId(userId, pagination);
    }

    @Override
    public List<Order> findByAttributes(Long userId, CustomPagination pagination) {
        List<Order> orderList;
        if (userId == null) {
            orderList = findAll(pagination);
        } else {
            orderList = findByUserId(userId, pagination);
        }

        return orderList;
    }

    @Transactional
    @Override
    public void delete(Long id) {
        findById(id);
        orderDao.delete(id);
    }
}
