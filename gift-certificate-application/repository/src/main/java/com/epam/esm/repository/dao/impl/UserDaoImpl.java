package com.epam.esm.repository.dao.impl;

import com.epam.esm.entity.User;
import com.epam.esm.repository.dao.AbstractEntityDao;
import com.epam.esm.repository.dao.UserDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import java.util.Optional;

@Repository
public class UserDaoImpl extends AbstractEntityDao<User> implements UserDao {
    private static final String LOGIN_FIELD_NAME = "login";
    private static final String UNSUPPORTED_OPERATION_EXCEPTION = "Unsupported operation exception. Cannot delete";

    public UserDaoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Transactional
    @Override
    public Optional<User> findByLogin(String login) {
        CriteriaQuery<User> criteriaQuery = prepareWhereCriteriaQuery(User.class, LOGIN_FIELD_NAME, login);
        return entityManager.createQuery(criteriaQuery)
                .getResultStream()
                .findFirst();
    }

    @Override
    public void delete(Long id, Class<User> entityClass) {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_EXCEPTION);
    }
}
