package com.epam.esm.repository.dao.impl;

import com.epam.esm.entity.User;
import com.epam.esm.repository.dao.UserDao;
import com.epam.esm.pagination.CustomPagination;
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
public class UserDaoImpl implements UserDao {
    private static final String NAME_FIELD = "login";

    @PersistenceContext
    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    @Autowired
    public UserDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    @Override
    public User create(User user) {
        entityManager.persist(user);
        return user;
    }

    @Override
    public List<User> findAll(CustomPagination pagination) {
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = criteriaQuery.from(User.class);
        criteriaQuery.select(userRoot);

        int pageSize = pagination.getSize();
        int pagesCount = pagination.getPage() * pageSize;

        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(pagesCount)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public Optional<User> findByLogin(String login) {
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = criteriaQuery.from(User.class);
        criteriaQuery.select(userRoot);
        criteriaQuery.where(criteriaBuilder.equal(userRoot.get(NAME_FIELD), login));
        return entityManager.createQuery(criteriaQuery).getResultStream().findFirst();
    }

    @Override
    public Long findUsersNumber() {
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<User> userRoot = criteriaQuery.from(User.class);
        criteriaQuery.select(criteriaBuilder.count(userRoot));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }
}
