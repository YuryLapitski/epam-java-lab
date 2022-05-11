package com.epam.esm.repository.dao.impl;

import com.epam.esm.pagination.CustomPagination;
import com.epam.esm.entity.Order;
import com.epam.esm.repository.dao.OrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderDaoImpl implements OrderDao {
    private static final String USER_FIELD = "user";

    @PersistenceContext
    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    @Autowired
    public OrderDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    @Override
    public Order create(Order order) {
        entityManager.persist(order);
        return order;
    }

    @Override
    public List<Order> findAll(CustomPagination pagination) {
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> orderRoot = criteriaQuery.from(Order.class);
        criteriaQuery.select(orderRoot);

        int pageSize = pagination.getSize();
        int pagesCount = pagination.getPage() * pageSize;

        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(pagesCount)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public Optional<Order> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Order.class, id));
    }

    @Override
    public List<Order> findByUserId(Long userId, CustomPagination pagination) {
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> orderRoot = criteriaQuery.from(Order.class);
        criteriaQuery.select(orderRoot);
        criteriaQuery.where(criteriaBuilder.equal(orderRoot.get(USER_FIELD), userId));

        int pageSize = pagination.getSize();
        int pagesCount = pagination.getPage() * pageSize;

        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(pagesCount)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public void delete(Long id) {
        entityManager.remove(entityManager.find(Order.class, id));
    }

    @Override
    public Long findOrdersNumber() {
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Order> orderRoot = criteriaQuery.from(Order.class);
        criteriaQuery.select(criteriaBuilder.count(orderRoot));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    @Override
    public Long findUserOrdersNumber(Long userId) {
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Order> orderRoot = criteriaQuery.from(Order.class);
        criteriaQuery.select(criteriaBuilder.count(orderRoot));
        criteriaQuery.where(criteriaBuilder.equal(orderRoot.get(USER_FIELD), userId));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }
}
