package com.epam.esm.repository.dao.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
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

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public GiftCertificate create(GiftCertificate giftCertificate) {
        entityManager.persist(giftCertificate);
        return giftCertificate;
    }

    @Override
    public List<GiftCertificate> findAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
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
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> giftCertificateRoot = criteriaQuery.from(GiftCertificate.class);
        criteriaQuery.select(giftCertificateRoot);
        criteriaQuery.where(criteriaBuilder.equal(giftCertificateRoot.get("name"), name));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<GiftCertificate> findAllWithSort(String columnName, String sortType) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);

        Order order;
        Path<String> path = root.get(columnName);

        if (sortType.equalsIgnoreCase("asc")) {
            order = criteriaBuilder.asc(path);
        } else {
            order = criteriaBuilder.desc(path);
        }

        criteriaQuery.orderBy(order).select(root);

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public Optional<GiftCertificate> findByName(String name) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> giftCertificateRoot = criteriaQuery.from(GiftCertificate.class);
        criteriaQuery.select(giftCertificateRoot);
        criteriaQuery.where(criteriaBuilder.equal(giftCertificateRoot.get("name"), name));
        return entityManager.createQuery(criteriaQuery).getResultStream().findFirst();
    }

    @Override
    public List<GiftCertificate> findGiftCertificatesByTagName(String tagName) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);
        Join<GiftCertificate, Tag> joinRelation = root.join("tagList");
        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.equal(joinRelation.get("name"), tagName));

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
