package com.epam.esm.service;

import com.epam.esm.service.pagination.CustomPagination;
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
     * @param pagination pagination parameters
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
     * Searches for certificates by attributes
     *
     * @param name name of the certificate (it can be part of name)
     * @param tagNames names of the certificate tags to contain (it can be part of name)
     * @param columnNames column names to sorting by
     * @param sortType sorting type. Can be ASC or DESC
     * @param pagination pagination parameters
     * @return list of founded GiftCertificate
     */
    List<GiftCertificate> findByAttributes(String name, List<String> tagNames,
                                           List<String> columnNames, String sortType,
                                           CustomPagination pagination);
}
