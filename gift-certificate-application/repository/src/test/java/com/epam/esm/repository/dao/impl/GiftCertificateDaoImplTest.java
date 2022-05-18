package com.epam.esm.repository.dao.impl;

import com.epam.esm.pagination.CustomPagination;
import com.epam.esm.repository.config.TestConfig;
import com.epam.esm.entity.GiftCertificate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {TestConfig.class})
@Transactional
@ActiveProfiles("dev")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GiftCertificateDaoImplTest {
    private static final String PART_OF_SEARCH = "ficat";
    private static final Long CERTIFICATE_ID = 3L;
    private static final String CERTIFICATE_NAME = "certificate 3";
    private static final String CERTIFICATE_DESCRIPTION = "description 3";
    private static final BigDecimal CERTIFICATE_PRICE = BigDecimal.valueOf(30.99);
    private static final short CERTIFICATE_DURATION = 10;
    private static final String NEW_CERTIFICATE_NAME = "newName";
    private static final String NEW_CERTIFICATE_DESCRIPTION = "newDescription";
    private static final BigDecimal NEW_CERTIFICATE_PRICE = BigDecimal.valueOf(99.99);
    private static final short NEW_CERTIFICATE_DURATION = 60;
    private static final int EXPECTED_LIST_SIZE = 3;

    private final GiftCertificateDaoImpl giftCertificateDao;
    private GiftCertificate expectedGiftCertificate;
    private CustomPagination pagination;

    @Autowired
    public GiftCertificateDaoImplTest(GiftCertificateDaoImpl giftCertificateDao) {
        this.giftCertificateDao = giftCertificateDao;
    }

    @BeforeAll
    void beforeAll() {
        expectedGiftCertificate = new GiftCertificate();
        expectedGiftCertificate.setId(CERTIFICATE_ID);
        expectedGiftCertificate.setName(CERTIFICATE_NAME);
        expectedGiftCertificate.setDescription(CERTIFICATE_DESCRIPTION);
        expectedGiftCertificate.setPrice(CERTIFICATE_PRICE);
        expectedGiftCertificate.setDuration(CERTIFICATE_DURATION);
        pagination = new CustomPagination();
        pagination.setPage(1);
        pagination.setSize(10);
    }

    @Test
    void create() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName(NEW_CERTIFICATE_NAME);
        giftCertificate.setDescription(NEW_CERTIFICATE_DESCRIPTION);
        giftCertificate.setPrice(NEW_CERTIFICATE_PRICE);
        giftCertificate.setDuration(NEW_CERTIFICATE_DURATION);
        GiftCertificate actual = giftCertificateDao.create(giftCertificate);
        assertEquals(giftCertificate, actual);
    }

    @Test
    void findAll() {
        List<GiftCertificate> giftCertificates = giftCertificateDao.findAll(pagination, GiftCertificate.class);
        assertEquals(EXPECTED_LIST_SIZE, giftCertificates.size());
    }

    @Test
    void findById() {
        Optional<GiftCertificate> optionalTag = giftCertificateDao.findById(2L, GiftCertificate.class);
        assertTrue(optionalTag.isPresent());
    }

    @Test
    void findByName() {
        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateDao.findByName(CERTIFICATE_NAME);
        assertTrue(optionalGiftCertificate.isPresent());
    }

    @Test
    void update() {
        GiftCertificate actual =
                giftCertificateDao.update(expectedGiftCertificate);
        assertEquals(expectedGiftCertificate, actual);
    }

    @Test
    void findByPartOfName() {
        List<GiftCertificate> giftCertificates = giftCertificateDao.findByPartOfName(PART_OF_SEARCH);
        assertEquals(EXPECTED_LIST_SIZE, giftCertificates.size());
    }
}
