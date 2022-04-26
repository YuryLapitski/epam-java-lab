package com.epam.esm.repository.dao.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.TagToGiftCertificateRelation;
import com.epam.esm.repository.config.SpringJdbcConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringJdbcConfig.class})
@ActiveProfiles("dev")
public class TagToGiftCertificateDaoImplTest {
    private static final Long NEW_TAG_ID = 4L;
    private static final Long NEW_GIFT_CERTIFICATE_ID = 4L;
    private static final Long TAG_ID = 1L;
    private static final Long GIFT_CERTIFICATE_ID = 2L;
    private static final String TAG_NAME = "first";
    private static final int EXPECTED_LIST_SIZE = 3;

    private final TagToGiftCertificateDaoImpl tagToGiftCertificateDao;

    @Autowired
    public TagToGiftCertificateDaoImplTest(TagToGiftCertificateDaoImpl tagToGiftCertificateDao) {
        this.tagToGiftCertificateDao = tagToGiftCertificateDao;
    }

    @BeforeAll
    static void beforeAll() {
        TagToGiftCertificateRelation tagToGiftCertificateRelation = new TagToGiftCertificateRelation();
        tagToGiftCertificateRelation.setTagId(NEW_TAG_ID);
        tagToGiftCertificateRelation.setGiftCertificateId(NEW_GIFT_CERTIFICATE_ID);
    }

    @Test
    void createTagToGiftCertificateRelation() {
        TagToGiftCertificateRelation tagToGiftCertificateRelation = new TagToGiftCertificateRelation();
        tagToGiftCertificateRelation.setTagId(NEW_TAG_ID);
        tagToGiftCertificateRelation.setGiftCertificateId(NEW_GIFT_CERTIFICATE_ID);
        TagToGiftCertificateRelation actual = tagToGiftCertificateDao
                .createTagToGiftCertificateRelation(NEW_TAG_ID, NEW_GIFT_CERTIFICATE_ID);
        assertEquals(tagToGiftCertificateRelation, actual);
    }

    @Test
    void findGiftCertificatesByTagName() {
        List<GiftCertificate> giftCertificates = tagToGiftCertificateDao.findGiftCertificatesByTagName(TAG_NAME);
        assertEquals(EXPECTED_LIST_SIZE, giftCertificates.size());

    }

    @Test
    void findByTagId() {
        List<TagToGiftCertificateRelation> tagToGiftCertificate = tagToGiftCertificateDao.findByTagId(TAG_ID);
        assertEquals(EXPECTED_LIST_SIZE, tagToGiftCertificate.size());
    }

    @Test
    void findByGiftCertificateId() {
        List<Tag> tagToGiftCertificate = tagToGiftCertificateDao.findByGiftCertificateId(GIFT_CERTIFICATE_ID);
        assertEquals(EXPECTED_LIST_SIZE, tagToGiftCertificate.size());
    }

    @Test
    void deleteByGiftCertificateId() {
        tagToGiftCertificateDao.createTagToGiftCertificateRelation(NEW_TAG_ID, NEW_GIFT_CERTIFICATE_ID);
        assertTrue(tagToGiftCertificateDao.deleteByGiftCertificateId(NEW_GIFT_CERTIFICATE_ID));
    }
}
