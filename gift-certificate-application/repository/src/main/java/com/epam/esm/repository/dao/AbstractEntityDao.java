package com.epam.esm.repository.dao;

import com.epam.esm.pagination.CustomPagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public abstract class AbstractEntityDao<T> implements EntityDao<T> {
    @PersistenceContext
    protected final EntityManager entityManager;
    protected final CriteriaBuilder criteriaBuilder;

    @Autowired
    public AbstractEntityDao(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    @Override
    public T create(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public List<T> findAll(CustomPagination pagination, Class<T> entityClass) {
        CriteriaQuery<T> criteriaQuery = prepareSelectCriteriaQuery(entityClass);
        return prepareTypedQuery(criteriaQuery, pagination).getResultList();
    }

    @Override
    public Optional<T> findById(Long id, Class<T> entityClass) {
        return Optional.ofNullable(entityManager.find(entityClass, id));
    }

    @Override
    public void delete(Long id, Class<T> entityClass) {
        entityManager.remove(entityManager.find(entityClass, id));
    }

    @Override
    public Long findEntitiesNumber(Class<T> entityClass) {
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<T> orderRoot = criteriaQuery.from(entityClass);
        criteriaQuery.select(criteriaBuilder.count(orderRoot));

        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    protected TypedQuery<T> prepareTypedQuery (CriteriaQuery<T> criteriaQuery,
                                               CustomPagination pagination) {
        int pageSize = pagination.getSize();
        int pagesCount = pagination.getPage() * pageSize;

        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(pagesCount)
                .setMaxResults(pageSize);
    }

    protected CriteriaQuery<T> prepareSelectCriteriaQuery(Class<T> entityClass) {
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<T> root = criteriaQuery.from(entityClass);

        return criteriaQuery.select(root);
    }

    protected CriteriaQuery<T> prepareWhereCriteriaQuery(Class<T> entityClass,
                                                         String field,
                                                         Object object) {
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<T> root = criteriaQuery.from(entityClass);
        criteriaQuery.select(root);

        return criteriaQuery.where(criteriaBuilder.equal(root.get(field), object));
    }
}
