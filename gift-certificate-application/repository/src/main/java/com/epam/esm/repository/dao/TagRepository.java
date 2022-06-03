package com.epam.esm.repository.dao;

import com.epam.esm.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

/**
 * The TagRepository interface provides methods for creating, reading,
 * and deleting tags.
 *
 * @author Yury Lapitski
 * @version 1.0
 */
public interface TagRepository extends JpaRepository<Tag, Long> {
    /**
     * Searches for tag by tag name
     *
     * @param name name of the tag to find
     * @return founded Optional of tag
     */
    Optional<Tag> findByName(String name);

    /**
     * Searches for the most widely used tag of a user with the highest cost of all orders.
     *
     * @return founded Optional of tag
     */
    @Query(value = "SELECT tag.id, tag.name FROM tag " +
            "JOIN tag_gift_certificate ON tag_gift_certificate.tag_id=tag.id " +
            "JOIN gift_certificate ON gift_certificate.id=tag_gift_certificate.gift_certificate_id " +
            "JOIN orders ON orders.gift_certificate_id=gift_certificate.id AND orders.user_id=" +
            "(SELECT users.id FROM users " +
            "JOIN orders ON orders.user_id=users.id " +
            "GROUP BY users.id ORDER BY SUM(orders.price) DESC LIMIT 1) " +
            "GROUP BY tag.id ORDER BY COUNT(tag.id) DESC LIMIT 1", nativeQuery = true)
    Optional<Tag> findMostPopularTagWithHighestOrderCost();
}
