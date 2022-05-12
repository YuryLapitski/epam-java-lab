package com.epam.esm.repository.dao.impl;

import com.epam.esm.pagination.CustomPagination;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.dao.AbstractEntityDao;
import com.epam.esm.repository.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class GiftCertificateDaoImpl extends AbstractEntityDao<GiftCertificate> implements GiftCertificateDao {
    private static final String NAME_FIELD = "name";
    private static final String SORT_TYPE_DESC = "desc";
    private static final String TAG_LIST_FIELD = "tagList";

    public GiftCertificateDaoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<GiftCertificate> findByPartOfName(String name, CustomPagination pagination) {
        CriteriaQuery<GiftCertificate> criteriaQuery =
                prepareWhereCriteriaQuery(GiftCertificate.class, NAME_FIELD, name);
        return prepareTypedQuery(criteriaQuery, pagination).getResultList();
    }

    @Override
    public List<GiftCertificate> findAllWithSort(String columnName, String sortType, CustomPagination pagination) {
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);

        Order order;
        Path<String> path = root.get(columnName);

        if (sortType.equalsIgnoreCase(SORT_TYPE_DESC)) {
            order = criteriaBuilder.desc(path);
        } else {
            order = criteriaBuilder.asc(path);
        }

        criteriaQuery.orderBy(order).select(root);

        return prepareTypedQuery(criteriaQuery, pagination).getResultList();
    }

    @Override
    public Optional<GiftCertificate> findByName(String name) {
        CriteriaQuery<GiftCertificate> criteriaQuery =
                prepareWhereCriteriaQuery(GiftCertificate.class, NAME_FIELD, name);
        return entityManager.createQuery(criteriaQuery).getResultStream().findFirst();
    }

    @Override
    public List<GiftCertificate> findGiftCertificatesByTagName(String tagName, CustomPagination pagination) {
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);
        Join<GiftCertificate, Tag> joinRelation = root.join(TAG_LIST_FIELD);
        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.equal(joinRelation.get(NAME_FIELD), tagName));

        return prepareTypedQuery(criteriaQuery, pagination).getResultList();
    }

    @Override
    public GiftCertificate update(GiftCertificate giftCertificate) {
        entityManager.merge(giftCertificate);
        return giftCertificate;
    }

    @Override
    public Long findGiftCertificatesByNameNumber(String name) {
        CriteriaQuery<Long> criteriaQuery = prepareWhereCriteriaQueryForCount(GiftCertificate.class,
                NAME_FIELD, name);
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    @Override
    public Long findGiftCertificatesByTagNameNumber(String tagName) {
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<GiftCertificate> giftCertificateRoot = criteriaQuery.from(GiftCertificate.class);
        Join<GiftCertificate, Tag> joinRelation = giftCertificateRoot.join(TAG_LIST_FIELD);
        criteriaQuery.select(criteriaBuilder.count(giftCertificateRoot));
        criteriaQuery.where(criteriaBuilder.equal(joinRelation.get(NAME_FIELD), tagName));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }
}
