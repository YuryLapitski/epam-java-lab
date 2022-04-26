package com.epam.esm.repository.dao.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.dao.TagToGiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.TagToGiftCertificateRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

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
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Tag> tagRowMapper;
    private final RowMapper<GiftCertificate> giftCertificateRowMapper;
    private final RowMapper<TagToGiftCertificateRelation> tagToGiftCertificateRelationRowMapper;

    @Autowired
    public TagToGiftCertificateDaoImpl(JdbcTemplate jdbcTemplate,
                                       RowMapper<Tag> tagRowMapper,
                                       RowMapper<GiftCertificate> giftCertificateRowMapper,
                                       RowMapper<TagToGiftCertificateRelation> tagToGiftCertificateRelationRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.tagRowMapper = tagRowMapper;
        this.giftCertificateRowMapper = giftCertificateRowMapper;
        this.tagToGiftCertificateRelationRowMapper = tagToGiftCertificateRelationRowMapper;
    }

    @Override
    public TagToGiftCertificateRelation createTagToGiftCertificateRelation(Long tagId, Long giftCertificateId) {
        jdbcTemplate.update(CREATE_TAG_TO_GIFT_CERTIFICATE_RELATION, tagId, giftCertificateId);
        TagToGiftCertificateRelation tagToGiftCertificate = new TagToGiftCertificateRelation();
        tagToGiftCertificate.setTagId(tagId);
        tagToGiftCertificate.setGiftCertificateId(giftCertificateId);
        return tagToGiftCertificate;
    }

    @Override
    public List<GiftCertificate> findGiftCertificatesByTagName(String tagName) {
        String resultString = String.format(FIND_GIFT_CERTIFICATE_BY_TAG_NAME, tagName);
        return jdbcTemplate.query(resultString, giftCertificateRowMapper);
    }

    @Override
    public List<TagToGiftCertificateRelation> findByTagId(Long tagID) {
        return jdbcTemplate.query(FIND_BY_TAG_ID, tagToGiftCertificateRelationRowMapper, tagID);
    }

    @Override
    public List<Tag> findByGiftCertificateId(Long giftCertificateId) {
        return jdbcTemplate.query(FIND_BY_GIFT_CERTIFICATE_ID, tagRowMapper, giftCertificateId);
    }

    @Override
    public boolean deleteByGiftCertificateId(Long giftCertificateId) {
        return jdbcTemplate.update(DELETE_BY_GIFT_CERTIFICATE_ID, giftCertificateId) >= NUMBER_OF_CHANGED_ROWS;
    }
}
