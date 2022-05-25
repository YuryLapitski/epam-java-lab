package com.epam.esm.repository.dao.impl;

import com.epam.esm.pagination.CustomPagination;
import com.epam.esm.entity.Order;
import com.epam.esm.repository.dao.AbstractEntityDao;
import com.epam.esm.repository.dao.OrderDao;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class OrderDaoImpl extends AbstractEntityDao<Order> implements OrderDao {
    private static final String USER_FIELD_NAME = "user";
    private static final String GIFT_CERTIFICATE_FIELD_NAME = "giftCertificate";
    private static final Long ZERO_ENTITIES_NUMBER = 0L;

    public OrderDaoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<Order> findByUserId(Long userId, CustomPagination pagination) {
        CriteriaQuery<Order> criteriaQuery = prepareWhereCriteriaQuery(Order.class, USER_FIELD_NAME, userId);
        return prepareTypedQuery(criteriaQuery, pagination).getResultList();
    }

    @Override
    public List<Order> findByGiftCertificateId(Long giftCertificateId) {
        CriteriaQuery<Order> criteriaQuery = prepareWhereCriteriaQuery(Order.class,
                GIFT_CERTIFICATE_FIELD_NAME, giftCertificateId);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public Long findUserOrdersNumber(Long userId) {
        Long entitiesNumber;
        try {
            CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
            Root<Order> root = criteriaQuery.from(Order.class);
            criteriaQuery.select(criteriaBuilder.count(root));
            criteriaQuery.where(criteriaBuilder.equal(root.get(USER_FIELD_NAME), userId));

            entitiesNumber = entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException e) {
            entitiesNumber = ZERO_ENTITIES_NUMBER;
        }

        return entitiesNumber;
    }
}
