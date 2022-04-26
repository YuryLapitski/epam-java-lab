package com.epam.esm.service;

import com.epam.esm.entity.TagToGiftCertificateRelation;
import com.epam.esm.service.dto.GiftCertificateDto;
import java.util.List;

/**
 * The TagToGiftCertificateService interface provides methods for creating
 * and searching tag to gift certificate relation.
 *
 * @author Yury Lapitski
 * @version 1.0
 */
public interface TagToGiftCertificateService {

    /**
     * Creates new tag to gift certificate relation
     *
     * @param tagId tag to create
     * @return tag to gift certificate relation
     */
    TagToGiftCertificateRelation createTagToGiftCertificateRelation(Long tagId, Long giftCertificateId);

    /**
     * Searches for gift certificates by tag name
     *
     * @param tagName name of the tag to find gift certificates
     * @return founded tag
     */
    List<GiftCertificateDto> findGiftCertificatesByTagName(String tagName);
}
