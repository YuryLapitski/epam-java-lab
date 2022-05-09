package com.epam.esm.service.impl;

import com.epam.esm.entity.Order;
import com.epam.esm.repository.dao.OrderDao;
import com.epam.esm.repository.dao.UserDao;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.exception.OrderNotFoundException;
import com.epam.esm.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    private static final String ORDER_ID_NOT_FOUND_MSG = "Order with id=%d not found.";
    private final OrderDao orderDao;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public Order create(Order order) {
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
    public void delete(Long id) {
        orderDao.findById(id).orElseThrow(() ->
                new OrderNotFoundException(String.format(ORDER_ID_NOT_FOUND_MSG, id)));
        orderDao.delete(id);
    }
}
