package com.epam.esm.service.impl;

import com.epam.esm.pagination.CustomPagination;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.repository.dao.OrderDao;
import com.epam.esm.repository.dao.UserDao;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.exception.OrderNotFoundException;
import com.epam.esm.service.exception.PageNumberValidationException;
import com.epam.esm.service.exception.PageSizeValidationException;
import com.epam.esm.service.exception.UserHasNoOrdersException;
import com.epam.esm.service.exception.UserNotFoundException;
import com.epam.esm.service.util.Message;
import com.epam.esm.service.validator.PaginationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao;
    private final UserDao userDao;
    private final UserService userService;
    private final GiftCertificateService giftCertificateService;
    private final PaginationValidator paginationValidator;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao,
                            UserDao userDao,
                            UserService userService,
                            GiftCertificateService giftCertificateService,
                            PaginationValidator paginationValidator) {
        this.orderDao = orderDao;
        this.userDao = userDao;
        this.userService = userService;
        this.giftCertificateService = giftCertificateService;
        this.paginationValidator = paginationValidator;
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

        return orderDao.create(order);
    }

    @Override
    public List<Order> findAll(CustomPagination pagination) {
        if (!paginationValidator.isSizeValid(pagination)) {
            throw new PageSizeValidationException(Message.PAGE_SIZE_INVALID_MSG);
        }

        Long ordersNumber = orderDao.getEntitiesNumber(Order.class);
        int lastPage = (int) Math.ceil((double) ordersNumber / pagination.getSize());

        if (!paginationValidator.isPageValid(pagination, lastPage)) {
            throw new PageNumberValidationException(String.format(Message.PAGE_NUMBER_INVALID_MSG, lastPage));
        }

        return orderDao.findAll(pagination, Order.class);
    }

    @Override
    public Order findById(Long id) {
        return orderDao.findById(id, Order.class).orElseThrow(() ->
                new OrderNotFoundException(String.format(Message.ORDER_ID_NOT_FOUND_MSG, id)));
    }

    @Override
    public List<Order> findByUserId(Long userId, CustomPagination pagination) {
        if (!userDao.findById(userId, User.class).isPresent()) {
            throw  new UserNotFoundException(String.format(Message.USER_ID_NOT_FOUND_MSG, userId));
        }

        if (!hasUserOrders(userId, pagination)) {
            String msg = String.format(Message.USER_HAS_NO_ORDERS_MSG, userId);
            throw new UserHasNoOrdersException(msg);
        }

        if (!paginationValidator.isSizeValid(pagination)) {
            throw new PageSizeValidationException(Message.PAGE_SIZE_INVALID_MSG);
        }

        Long ordersNumber = orderDao.findUserOrdersNumber(userId);
        int lastPage = (int) Math.ceil((double) ordersNumber / pagination.getSize());

        if (!paginationValidator.isPageValid(pagination, lastPage)) {
            throw new PageNumberValidationException(String.format(Message.PAGE_NUMBER_INVALID_MSG, lastPage));
        }

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
        if (!orderDao.findById(id, Order.class).isPresent()) {
            throw new OrderNotFoundException(String.format(Message.ORDER_ID_NOT_FOUND_MSG, id));
        }

        orderDao.delete(id, Order.class);
    }

    private boolean hasUserOrders (Long userId, CustomPagination pagination) {
        List<Order> orderList = orderDao.findByUserId(userId, pagination);
        return !orderList.isEmpty();
    }
}
