package com.epam.esm.repository.dao;

import com.epam.esm.entity.Order;

import java.util.List;
import java.util.Optional;

/**
 * The OrderDao interface provides methods for creating, reading,
 * and deleting orders.
 *
 * @author Yury Lapitski
 * @version 1.0
 */
public interface OrderDao {
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
     * @return founded Optional of order
     */
    Optional<Order> findById(Long id);

    /**
     * Deletes order
     *
     * @param id id of the order to delete
     */
    void delete(Long id);
}
