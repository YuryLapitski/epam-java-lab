package com.epam.esm.repository.dao;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * The GiftCertificateDao interface provides methods for creating, reading,
 * updating and deleting gift certificates.
 *
 * @author Yury Lapitski
 * @version 1.0
 */
public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Long> {
    String FIND_CERTIFICATES_BY_TAG_NAMES =
            "SELECT c FROM GiftCertificate c JOIN c.tagList tl WHERE tl.name IN :tagNameList" +
                    " AND (c.name LIKE %:searchPart% OR c.description LIKE %:searchPart%) GROUP BY c HAVING COUNT(tl)>=:tagNumber";

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
    @Query(value = "SELECT gc FROM GiftCertificate gc JOIN gc.tagList tl WHERE tl.name IN :tagNames")
    List<GiftCertificate> findGiftCertificatesByTagNames(@Param("tagNames") List<String> tagNames);

    /**
     * Searches for gift certificate by name. It can be part of name
     *
     * @param partOfName name (part of name) of the gift certificate to find
     * @return founded list of gift certificates
     */
    @Query("SELECT g FROM GiftCertificate g WHERE g.name LIKE %:partOfName%")
    List<GiftCertificate> findByPartOfName(@Param("partOfName")String partOfName);

    /**
     * Searches for gift certificates by attributes
     *
     * @param name gift certificate name
     * @param tagNames tag names to find gift certificate
     * @param pageable pagination parameters
     * @return founded list of gift certificates
     */
    @Query("SELECT gc FROM GiftCertificate gc JOIN gc.tagList tl WHERE tl.name IN :tagNames " +
            "OR gc.name LIKE %:name%")
    Page<GiftCertificate> findByAttributes (@Param("name")String name, @Param("tagNames") List<String> tagNames,
                                            Pageable pageable);

//    /**
//     * Searches number of found gift certificates
//     *
//     * @param name gift certificate name
//     * @param tagList list of tags
//     * @return number of found gift certificates
//     */
//    Long findByAttributesNumber(String name, List<String> tagList);
}
