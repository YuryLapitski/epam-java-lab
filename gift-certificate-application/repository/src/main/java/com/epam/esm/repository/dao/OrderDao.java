package com.epam.esm.repository.dao;

import com.epam.esm.pagination.CustomPagination;
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
public interface OrderDao extends EntityDao<Order> {

    /**
     * Searches for order by user ID
     *
     * @param userId id of the user to find orders
     * @return founded list of orders
     */
    List<Order> findByUserId(Long userId, CustomPagination pagination);

    List<Order> findByGiftCertificateId(Long giftCertificateId);

    Long findUserOrdersNumber(Long userId);
}
