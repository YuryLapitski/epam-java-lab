package com.epam.esm.repository.dao.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.dao.AbstractEntityDao;
import com.epam.esm.repository.dao.TagDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class TagDaoImpl extends AbstractEntityDao<Tag> implements TagDao {
    private static final String NAME_FIELD = "name";
    public static final String FIND_MOST_POPULAR_TAG_OF_RICHEST_USER = "SELECT tag.id, tag.name FROM tag " +
            "JOIN tag_gift_certificate ON tag_gift_certificate.tag_id=tag.id " +
            "JOIN gift_certificate ON gift_certificate.id=tag_gift_certificate.gift_certificate_id " +
            "JOIN orders ON orders.gift_certificate_id=gift_certificate.id AND orders.user_id=" +
            "(SELECT user.id FROM user " +
            "JOIN orders ON orders.user_id=user.id " +
            "GROUP BY user.id ORDER BY SUM(orders.price) DESC LIMIT 1) " +
            "GROUP BY tag.id ORDER BY COUNT(tag.id) DESC LIMIT 1";

    public TagDaoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Optional<Tag> findByName(String name) {
        CriteriaQuery<Tag> criteriaQuery = prepareWhereCriteriaQuery(Tag.class, NAME_FIELD, name);
        return entityManager.createQuery(criteriaQuery).getResultStream().findAny();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<Tag> findMostPopularTagWithHighestOrderCost() {
        Query query = entityManager.createNativeQuery(FIND_MOST_POPULAR_TAG_OF_RICHEST_USER, Tag.class);
        List<Tag> tag = query.getResultList();
        return Optional.ofNullable(tag.get(0));
    }
}

