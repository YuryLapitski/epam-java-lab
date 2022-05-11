package com.epam.esm.service;

import com.epam.esm.pagination.CustomPagination;
import com.epam.esm.entity.GiftCertificate;
import java.util.List;

/**
 * The GiftCertificateService interface provides methods for creating, reading,
 * updating, and deleting gift certificates.
 *
 * @author Yury Lapitski
 * @version 1.0
 */
public interface GiftCertificateService {

    /**
     * Creates new certificate
     * If certificateDto contain new tags, they will be created as well
     *
     * @param giftCertificate certificate to create
     * @return GiftCertificate
     */
    GiftCertificate create(GiftCertificate giftCertificate);

    /**
     * Searches for all gift certificates
     *
     * @return founded list of GiftCertificate
     */
    List<GiftCertificate> findAll(CustomPagination pagination);

    /**
     * Searches for gift certificate by gift certificate id
     *
     * @param id id of the gift certificate to find
     * @return founded gift certificate
     */
    GiftCertificate findByGiftCertificateId(Long id);

    /**
     * Searches for gift certificate by name
     *
     * @param name name of the gift certificate to find (it can be part of name)
     * @return founded list of GiftCertificate
     */
    List<GiftCertificate> findByPartOfName(String name, CustomPagination pagination);

    /**
     * Searches for gift certificates with sorting
     *
     * @param columnName column name to sorting by
     * @param sortType sorting type. Can be ASC or DESC.
     * @return founded list of GiftCertificate
     */
    List<GiftCertificate> findAllWithSort(String columnName, String sortType, CustomPagination pagination);

    /**
     * Deletes gift certificate
     *
     * @param id id of the gift certificate to delete
     */
    void delete(Long id);

    /**
     * Updates existing gift certificate
     * If certificateDto contain new tags, they will be created as well
     *
     * @param giftCertificateId id of the gift certificate to update
     * @param giftCertificate gift certificate for update
     * @return updated GiftCertificateDto
     */
    GiftCertificate update(Long giftCertificateId, GiftCertificate giftCertificate);

    /**
     * Searches for gift certificates by tag name
     *
     * @param tagName name of the tag to find gift certificates
     * @return founded list of gift certificates
     */
    List<GiftCertificate> findGiftCertificatesByTagName(String tagName, CustomPagination pagination);

    /**
     * Searches for certificates by attributes
     *
     * @param name name of the certificate (it can be part of name)
     * @param tagName name of the certificate tag to contain (it can be part of name)
     * @param columnName column name to sorting by
     * @param sortType sorting type. Can be ASC or DESC
     * @return list of founded list of GiftCertificate
     */
    List<GiftCertificate> findByAttributes(String name, String tagName,
                                           String columnName, String sortType,
                                           CustomPagination pagination);
}
