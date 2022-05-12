package com.epam.esm.repository.dao;

import com.epam.esm.pagination.CustomPagination;
import com.epam.esm.entity.GiftCertificate;
import java.util.List;
import java.util.Optional;

/**
 * The GiftCertificateDao interface provides methods for creating, reading,
 * updating and deleting gift certificates.
 *
 * @author Yury Lapitski
 * @version 1.0
 */
public interface GiftCertificateDao extends EntityDao<GiftCertificate> {

    /**
     * Updates existing gift certificate
     *
     * @param giftCertificate gift certificate for update
     * @return updated gift certificate
     */
    GiftCertificate update(GiftCertificate giftCertificate);

    /**
     * Searches for gift certificate by name
     *
     * @param name name of the gift certificate to find
     * @return founded Optional of gift certificate
     */
    Optional<GiftCertificate> findByName(String name);

    /**
     * Searches for gift certificate by tag name
     *
     * @param tagName tag name to find gift certificate
     * @return founded list of gift certificate
     */
    List<GiftCertificate> findGiftCertificatesByTagName(String tagName, CustomPagination pagination);

    /**
     * Searches for gift certificate by name. It can be part of name
     *
     * @param name name (part of name) of the gift certificate to find
     * @return founded list of gift certificate
     */
    List<GiftCertificate> findByPartOfName(String name, CustomPagination pagination);

    /**
     * Searches for gift certificates with sorting
     *
     * @param columnName column name to sorting by
     * @param sortType sorting type. Can be ASC or DESC.
     * @return founded list of gift certificate
     */
    List<GiftCertificate> findAllWithSort(String columnName, String sortType, CustomPagination pagination);

    Long findGiftCertificatesByNameNumber(String name);

    Long findGiftCertificatesByTagNameNumber(String tagName);
}
