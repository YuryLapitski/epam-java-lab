package com.epam.esm.repository.dao.impl;

import com.epam.esm.pagination.CustomPagination;
import com.epam.esm.entity.Order;
import com.epam.esm.repository.dao.AbstractEntityDao;
import com.epam.esm.repository.dao.OrderDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class OrderDaoImpl extends AbstractEntityDao<Order> implements OrderDao {
    private static final String USER_FIELD = "user";

    public OrderDaoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<Order> findByUserId(Long userId, CustomPagination pagination) {
        CriteriaQuery<Order> criteriaQuery = prepareWhereCriteriaQuery(Order.class, USER_FIELD, userId);
        return prepareTypedQuery(criteriaQuery, pagination).getResultList();
    }

    @Override
    public Long findUserOrdersNumber(Long userId) {
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        criteriaQuery.select(criteriaBuilder.count(root));
        criteriaQuery.where(criteriaBuilder.equal(root.get(USER_FIELD), userId));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }
}
