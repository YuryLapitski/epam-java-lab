package com.epam.esm.service;

import com.epam.esm.entity.Order;

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
     * @param order order to create
     * @return Order
     */
    Order create(Order order);

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
     * Deletes order
     *
     * @param id id of the order to delete
     */
    void delete(Long id);
}
