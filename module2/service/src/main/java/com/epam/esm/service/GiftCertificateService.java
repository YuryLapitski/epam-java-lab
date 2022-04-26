package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.service.dto.GiftCertificateDto;
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
     * @param giftCertificateDto certificate to create
     * @return GiftCertificateDto
     */
    GiftCertificateDto create(GiftCertificateDto giftCertificateDto);

    /**
     * Searches for all gift certificates
     *
     * @return founded list of GiftCertificateDto
     */
    List<GiftCertificateDto> findAll();

    /**
     * Searches for gift certificate by gift certificate id
     *
     * @param id id of the gift certificate to find
     * @return founded GiftCertificateDto
     */
    GiftCertificateDto findByGiftCertificateId(Long id);

    /**
     * Searches for gift certificate by name
     *
     * @param name name of the gift certificate to find (it can be part of name)
     * @return founded list of GiftCertificateDto
     */
    List<GiftCertificateDto> findByPartOfName(String name);

    /**
     * Searches for gift certificates with sorting
     *
     * @param columnName column name to sorting by
     * @param sortType sorting type. Can be ASC or DESC.
     * @return founded list of GiftCertificateDto
     */
    List<GiftCertificateDto> findAllWithSort(String columnName, String sortType);

    /**
     * Deletes gift certificate
     *
     * @param id id of the gift certificate to delete
     */
    boolean delete(Long id);

    /**
     * Updates existing gift certificate
     * If certificateDto contain new tags, they will be created as well
     *
     * @param giftCertificateId id of the gift certificate to update
     * @param giftCertificateDto gift certificate for update
     * @return updated GiftCertificateDto
     */
    GiftCertificateDto update(Long giftCertificateId, GiftCertificateDto giftCertificateDto);

    /**
     * Searches for certificates by attributes
     *
     * @param name name of the certificate (it can be part of name)
     * @param tagName name of the certificate tag to contain (it can be part of name)
     * @param columnName column name to sorting by
     * @param sortType sorting type. Can be ASC or DESC
     * @return list of founded lis of GiftCertificateDto
     */
    List<GiftCertificateDto> findByAttributes(String name, String tagName,
                                              String columnName, String sortType);
}
