package com.epam.esm.repository.dao.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.dao.AbstractEntityDao;
import com.epam.esm.repository.dao.TagDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import java.util.Optional;

@Repository
public class TagDaoImpl extends AbstractEntityDao<Tag> implements TagDao {
    private static final String NAME_FIELD = "name";

    public TagDaoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Optional<Tag> findByName(String name) {
        CriteriaQuery<Tag> criteriaQuery = prepareWhereCriteriaQuery(Tag.class, NAME_FIELD, name);
        return entityManager.createQuery(criteriaQuery).getResultStream().findFirst();
    }
}

