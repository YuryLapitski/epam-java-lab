package com.epam.esm.service;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.service.dto.OrderDto;

import java.util.List;

/**
 * The OrderService interface provides methods for creating, reading,
 * and deleting orders.
 *
 * @author Yury Lapitski
 * @version 1.0
 */
public interface OrderService {
    /**
     * Creates new order
     *
     * @param orderDto orderDto to create
     * @return Order
     */
    Order create(OrderDto orderDto);

    /**
     * Searches for all orders
     *
     * @return founded list of orders
     */
    List<Order> findAll();

    /**
     * Searches for order by ID
     *
     * @param id id of the order to find
     * @return founded order
     */
    Order findById(Long id);

    /**
     * Searches for order by user ID
     *
     * @param userId id of the user to find orders
     * @return founded list of orders
     */
    List<Order> findByUserId(Long userId);

    /**
     * Searches for orders by attributes
     *
     * @param userId user id to find orders
     * @return founded list of orders
     */
    List<Order> findByAttributes(Long userId);

    /**
     * Deletes order
     *
     * @param id id of the order to delete
     */
    void delete(Long id);
}
