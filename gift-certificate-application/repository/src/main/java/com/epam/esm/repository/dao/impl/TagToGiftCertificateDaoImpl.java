package com.epam.esm.repository.dao.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.dao.TagToGiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.TagToGiftCertificateRelation;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.Collections;
import java.util.List;

@Repository
public class TagToGiftCertificateDaoImpl implements TagToGiftCertificateDao {
    private static final String CREATE_TAG_TO_GIFT_CERTIFICATE_RELATION = "INSERT INTO tag_gift_certificate " +
            "(tag_id, gift_certificate_id) VALUES (?, ?)";
    private static final String FIND_GIFT_CERTIFICATE_BY_TAG_NAME = "SELECT gift_certificate.id, " +
            "gift_certificate.name, description, price, duration, create_date, last_update_date FROM " +
            "gift_certificate JOIN tag_gift_certificate ON gift_certificate.id = " +
            "gift_certificate_id JOIN tag ON tag_id = tag.id WHERE tag.name LIKE ('%%' '%s' '%%')";
    private static final String DELETE_BY_GIFT_CERTIFICATE_ID = "DELETE FROM tag_gift_certificate " +
            "WHERE gift_certificate_id = ?";
    private static final String FIND_BY_TAG_ID = "SELECT tag_id, gift_certificate_id FROM " +
            "tag_gift_certificate WHERE tag_id = ?";
    private static final String FIND_BY_GIFT_CERTIFICATE_ID = "SELECT tag.id, tag.name FROM tag " +
            "JOIN tag_gift_certificate tgc ON tag.id = tgc.tag_id WHERE gift_certificate_id = ?";
    private static final int NUMBER_OF_CHANGED_ROWS = 1;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public TagToGiftCertificateRelation createTagToGiftCertificateRelation(Long tagId, Long giftCertificateId) {
        TagToGiftCertificateRelation relation = new TagToGiftCertificateRelation();
        relation.setTagId(tagId);
        relation.setGiftCertificateId(giftCertificateId);
        entityManager.persist(relation);
        return relation;
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
    public List<TagToGiftCertificateRelation> findByTagId(Long tagId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TagToGiftCertificateRelation> criteriaQuery = criteriaBuilder
                .createQuery(TagToGiftCertificateRelation.class);
        Root<TagToGiftCertificateRelation> root = criteriaQuery.from(TagToGiftCertificateRelation.class);
        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.equal(root.get("tagId"), tagId));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<Tag> findTagsByGiftCertificateId(Long giftCertificateId) {
        return Collections.emptyList();

//        return jdbcTemplate.query(FIND_BY_GIFT_CERTIFICATE_ID, tagRowMapper, giftCertificateId);
    }

    @Override
    public boolean deleteByGiftCertificateId(Long giftCertificateId) {
        for (TagToGiftCertificateRelation relation : findByGiftCertificateId(giftCertificateId)) {
            entityManager.remove(relation);
        }
        return true;
    }

    private List<TagToGiftCertificateRelation> findByGiftCertificateId(Long giftCertificateId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TagToGiftCertificateRelation> criteriaQuery = criteriaBuilder
                .createQuery(TagToGiftCertificateRelation.class);
        Root<TagToGiftCertificateRelation> root = criteriaQuery.from(TagToGiftCertificateRelation.class);
        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.equal(root.get("giftCertificateId"), giftCertificateId));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
