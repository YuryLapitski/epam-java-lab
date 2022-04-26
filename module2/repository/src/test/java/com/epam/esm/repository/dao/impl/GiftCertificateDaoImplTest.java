package com.epam.esm.repository.dao.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.config.SpringJdbcConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringJdbcConfig.class})
@ActiveProfiles("dev")
public class GiftCertificateDaoImplTest {
    private static final String PART_OF_SEARCH = "ficat";
    private static final Long CERTIFICATE_ID = 3L;
    private static final String CERTIFICATE_NAME = "certificate 3";
    private static final String CERTIFICATE_DESCRIPTION = "description 3";
    private static final BigDecimal CERTIFICATE_PRICE = BigDecimal.valueOf(30.99);
    private static final short CERTIFICATE_DURATION = 10;
    private static final Long NEW_CERTIFICATE_ID = 4L;
    private static final String NEW_CERTIFICATE_NAME = "newName";
    private static final String NEW_CERTIFICATE_DESCRIPTION = "newDescription";
    private static final BigDecimal NEW_CERTIFICATE_PRICE = BigDecimal.valueOf(99.99);
    private static final short NEW_CERTIFICATE_DURATION = 60;
    private static final Long UPDATED_ID = 2L;
    private static final String UPDATED_NAME = "updatedName";
    private static final String UPDATED_DESCRIPTION = "updatedDescription";
    private static final BigDecimal UPDATED_PRICE = BigDecimal.valueOf(55.55);
    private static final int UPDATED_DURATION = 20;
    private static final int EXPECTED_LIST_SIZE = 3;
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_DURATION = "duration";
    private static final String SORT_DESCENDING = "desc";

    private final GiftCertificateDaoImpl giftCertificateDao;

    @BeforeAll
    static void beforeAll() {
        GiftCertificate expectedGiftCertificate = new GiftCertificate();
        expectedGiftCertificate.setId(CERTIFICATE_ID);
        expectedGiftCertificate.setName(CERTIFICATE_NAME);
        expectedGiftCertificate.setDescription(CERTIFICATE_DESCRIPTION);
        expectedGiftCertificate.setPrice(CERTIFICATE_PRICE);
        expectedGiftCertificate.setDuration(CERTIFICATE_DURATION);
    }

    @Autowired
    public GiftCertificateDaoImplTest(GiftCertificateDaoImpl giftCertificateDao) {
        this.giftCertificateDao = giftCertificateDao;
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
    void delete() {
        assertTrue(giftCertificateDao.delete(NEW_CERTIFICATE_ID));
    }

    @Test
    void findAll() {
        List<GiftCertificate> giftCertificates = giftCertificateDao.findAll();
        assertEquals(EXPECTED_LIST_SIZE, giftCertificates.size());
    }

    @Test
    void findById() {
        Optional<GiftCertificate> optionalTag = giftCertificateDao.findById(2L);
        assertTrue(optionalTag.isPresent());
    }

    @Test
    void findByName() {
        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateDao.findByName(CERTIFICATE_NAME);
        assertTrue(optionalGiftCertificate.isPresent());
    }

    @Test
    void update() {
        Map<String, Object> paramForUpdate = new HashMap<>();
        paramForUpdate.put(COLUMN_NAME, UPDATED_NAME);
        paramForUpdate.put(COLUMN_DESCRIPTION, UPDATED_DESCRIPTION);
        paramForUpdate.put(COLUMN_PRICE, UPDATED_PRICE);
        paramForUpdate.put(COLUMN_DURATION, UPDATED_DURATION);
        Optional<GiftCertificate> optionalGiftCertificate =
                giftCertificateDao.update(UPDATED_ID, paramForUpdate);
        assertTrue(optionalGiftCertificate.isPresent());
    }

    @Test
    void findByPartOfName() {
        List<GiftCertificate> giftCertificates = giftCertificateDao.findByPartOfName(PART_OF_SEARCH);
        assertEquals(EXPECTED_LIST_SIZE, giftCertificates.size());
    }

    @Test
    void findAllWithSort() {
        List<GiftCertificate> giftCertificates = giftCertificateDao
                .findAllWithSort(COLUMN_NAME, SORT_DESCENDING);
        assertEquals(EXPECTED_LIST_SIZE, giftCertificates.size());
    }
}
