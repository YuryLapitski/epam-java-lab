package com.epam.esm.repository.dao;

import com.epam.esm.pagination.CustomPagination;
import com.epam.esm.entity.Order;
import java.util.List;

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
     * @param pagination pagination parameters
     * @return founded list of orders
     */
    List<Order> findByUserId(Long userId, CustomPagination pagination);

    /**
     * Searches for order by gift certificate ID
     *
     * @param giftCertificateId id of the gift certificate to find orders
     * @return founded list of orders
     */
    List<Order> findByGiftCertificateId(Long giftCertificateId);

    /**
     * Searches number of found orders
     *
     * @param userId user ID
     * @return number of found orders
     */
    Long findUserOrdersNumber(Long userId);
}
