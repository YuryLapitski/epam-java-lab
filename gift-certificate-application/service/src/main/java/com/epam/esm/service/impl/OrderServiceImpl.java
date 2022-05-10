package com.epam.esm.service.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.repository.dao.OrderDao;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.exception.OrderNotFoundException;
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

    @Autowired
    public OrderServiceImpl(OrderDao orderDao,
                            UserService userService,
                            GiftCertificateService giftCertificateService) {
        this.orderDao = orderDao;
        this.userService = userService;
        this.giftCertificateService = giftCertificateService;
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
    public List<Order> findAll() {
        return orderDao.findAll();
    }

    @Override
    public Order findById(Long id) {
        return orderDao.findById(id).orElseThrow(() ->
                new OrderNotFoundException(String.format(ORDER_ID_NOT_FOUND_MSG, id)));
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        return orderDao.findByUserId(userId);
    }

    @Override
    public List<Order> findByAttributes(Long userId) {
        List<Order> orderList;
        if (userId == null) {
            orderList = findAll();
        } else {
            orderList = findByUserId(userId);
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
