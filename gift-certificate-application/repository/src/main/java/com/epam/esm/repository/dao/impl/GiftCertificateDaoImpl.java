package com.epam.esm.repository.dao.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {
    private static final String NAME_FIELD = "name";
    private static final String SORT_TYPE_DESC = "desc";
    private static final String TAG_LIST_FIELD = "tagList";

    @PersistenceContext
    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    @Autowired
    public GiftCertificateDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    @Override
    public GiftCertificate create(GiftCertificate giftCertificate) {
        entityManager.persist(giftCertificate);
        return giftCertificate;
    }

    @Override
    public List<GiftCertificate> findAll() {
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> giftCertificateRoot = criteriaQuery.from(GiftCertificate.class);
        criteriaQuery.select(giftCertificateRoot);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public Optional<GiftCertificate> findById(Long id) {
        return Optional.ofNullable(entityManager.find(GiftCertificate.class, id));
    }

    @Override
    public List<GiftCertificate> findByPartOfName(String name) {
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> giftCertificateRoot = criteriaQuery.from(GiftCertificate.class);
        criteriaQuery.select(giftCertificateRoot);
        criteriaQuery.where(criteriaBuilder.equal(giftCertificateRoot.get(NAME_FIELD), name));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<GiftCertificate> findAllWithSort(String columnName, String sortType) {
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

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public Optional<GiftCertificate> findByName(String name) {
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> giftCertificateRoot = criteriaQuery.from(GiftCertificate.class);
        criteriaQuery.select(giftCertificateRoot);
        criteriaQuery.where(criteriaBuilder.equal(giftCertificateRoot.get(NAME_FIELD), name));
        return entityManager.createQuery(criteriaQuery).getResultStream().findFirst();
    }

    @Override
    public List<GiftCertificate> findGiftCertificatesByTagName(String tagName) {
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);
        Join<GiftCertificate, Tag> joinRelation = root.join(TAG_LIST_FIELD);
        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.equal(joinRelation.get(NAME_FIELD), tagName));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public GiftCertificate update(GiftCertificate giftCertificate) {
        entityManager.merge(giftCertificate);
        return giftCertificate;
    }

    @Override
    public boolean delete(Long id) {
        entityManager.remove(entityManager.find(GiftCertificate.class, id));
        return true;
    }
}
