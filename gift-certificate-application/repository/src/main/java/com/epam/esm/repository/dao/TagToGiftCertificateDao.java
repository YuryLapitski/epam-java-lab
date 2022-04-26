package com.epam.esm.repository.dao;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.TagToGiftCertificateRelation;

import java.util.List;

/**
 * The TagToGiftCertificateDao interface provides methods for creating, reading,
 * and deleting gift certificates.
 *
 * @author Yury Lapitski
 * @version 1.0
 */
public interface TagToGiftCertificateDao {

    /**
     * Creates new tag gift certificate relation
     *
     * @param tagId tag ID to create
     * @param giftCertificateId gift certificate ID to create
     * @return TagToGiftCertificateRelation
     */
    TagToGiftCertificateRelation createTagToGiftCertificateRelation(Long tagId, Long giftCertificateId);

    /**
     * Searches for gift certificate by tag name
     *
     * @param tagName tag name to find gift certificate
     * @return founded list of gift certificate
     */
    List<GiftCertificate> findGiftCertificatesByTagName(String tagName);

    /**
     * Searches for tag to gift certificate relation by tag ID
     *
     * @param tagId tag id to find tag to gift certificate relation
     * @return founded list of tag to gift certificate relations
     */
    List<TagToGiftCertificateRelation> findByTagId(Long tagId);

    /**
     * Searches for tags by gift certificate ID
     *
     * @param giftCertificateId id of the gift certificate to find tags
     * @return founded list of tags
     */
    List<Tag> findByGiftCertificateId(Long giftCertificateId);

    /**
     * Deletes tag to gift certificate relations by gift certificate ID
     *
     * @param giftCertificateId id of the gift certificate to delete tag to gift certificate relations
     * @return true if the tag to gift certificate was deleted
     */
    boolean deleteByGiftCertificateId(Long giftCertificateId);
}
