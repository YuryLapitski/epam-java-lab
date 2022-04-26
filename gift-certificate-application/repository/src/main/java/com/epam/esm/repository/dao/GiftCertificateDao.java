package com.epam.esm.repository.dao;

import com.epam.esm.entity.GiftCertificate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The GiftCertificateDao interface provides methods for creating, reading,
 * updating and deleting gift certificates.
 *
 * @author Yury Lapitski
 * @version 1.0
 */
public interface GiftCertificateDao {

    /**
     * Creates new gift certificate
     *
     * @param giftCertificate tag to create
     * @return gift certificate
     */
    GiftCertificate create(GiftCertificate giftCertificate);

    /**
     * Searches for all gift certificates
     *
     * @return founded list of gift certificates
     */
    List<GiftCertificate> findAll();

    /**
     * Searches for gift certificate by ID
     *
     * @param id id of the gift certificate to find
     * @return founded Optional of gift certificate
     */
    Optional<GiftCertificate> findById(Long id);

    /**
     * Deletes gift certificate
     *
     * @param id id of the gift certificate to delete
     * @return true if the gift certificate was deleted
     */
    boolean delete(Long id);

    /**
     * Updates existing gift certificate
     *
     * @param id id of the gift certificate for update
     * @param paramForUpdate parameters of gift certificate for update
     * @return updated GiftCertificateDto
     */
    Optional<GiftCertificate> update(Long id, Map<String, Object> paramForUpdate);

    /**
     * Searches for gift certificate by name
     *
     * @param name name of the gift certificate to find
     * @return founded Optional of gift certificate
     */
    Optional<GiftCertificate> findByName(String name);

    /**
     * Searches for gift certificate by name. It can be part of name
     *
     * @param name name (part of name) of the gift certificate to find
     * @return founded list of gift certificate
     */
    List<GiftCertificate> findByPartOfName(String name);

    /**
     * Searches for gift certificates with sorting
     *
     * @param columnName column name to sorting by
     * @param sortType sorting type. Can be ASC or DESC.
     * @return founded list of gift certificate
     */
    List<GiftCertificate> findAllWithSort(String columnName, String sortType);
}
