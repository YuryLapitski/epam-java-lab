package com.epam.esm.repository.dao;

import com.epam.esm.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * The OrderRepository interface provides methods for creating, reading,
 * and deleting orders.
 *
 * @author Yury Lapitski
 * @version 1.0
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Searches for order by user ID
     *
     * @param userId id of the user to find orders
     * @return founded list of orders
     */
    Page<Order> findByUserId(Long userId, Pageable pageable);

    /**
     * Searches for order by gift certificate ID
     *
     * @param giftCertificateId id of the gift certificate to find orders
     * @return founded list of orders
     */
    List<Order> findByGiftCertificateId(Long giftCertificateId);
}
