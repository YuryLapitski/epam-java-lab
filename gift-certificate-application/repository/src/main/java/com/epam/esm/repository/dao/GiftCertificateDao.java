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
     * Searches for gift certificate by tag names
     *
     * @param tagNames tag names to find gift certificate
     * @return founded list of gift certificates
     */
    List<GiftCertificate> findGiftCertificatesByTagNames(List<String> tagNames);

    /**
     * Searches for gift certificate by name. It can be part of name
     *
     * @param name name (part of name) of the gift certificate to find
     * @return founded list of gift certificates
     */
    List<GiftCertificate> findByPartOfName(String name);

    /**
     * Searches for gift certificates by attributes
     *
     * @param name gift certificate name
     * @param tagList list of tags
     * @param columnNames column names to sorting by
     * @param sortType sorting type. Can be ASC or DESC.
     * @param pagination pagination parameters
     * @return founded list of gift certificates
     */
    List<GiftCertificate> findByAttributes (String name, List<String> tagList, List<String> columnNames,
                                            String sortType, CustomPagination pagination);

    /**
     * Searches number of found gift certificates
     *
     * @param name gift certificate name
     * @param tagList list of tags
     * @return number of found gift certificates
     */
    Long findByAttributesNumber(String name, List<String> tagList);
}
