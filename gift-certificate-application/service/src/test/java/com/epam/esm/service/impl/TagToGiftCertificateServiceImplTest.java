package com.epam.esm.service.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.TagToGiftCertificateRelation;
import com.epam.esm.repository.dao.GiftCertificateDao;
import com.epam.esm.repository.dao.TagDao;
import com.epam.esm.repository.dao.TagToGiftCertificateDao;
import com.epam.esm.repository.dao.impl.TagDaoImpl;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.exception.GiftCertificateNotFoundException;
import com.epam.esm.service.exception.GiftCertificatesNotFoundException;
import com.epam.esm.service.exception.TagNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TagToGiftCertificateServiceImplTest {
    private static final long TAG_ID = 1L;
    private static final String TAG_NAME = "Some tag";
    private static final long GIFT_CERTIFICATE_ID = 10L;
    private static final String GIFT_CERTIFICATE_NAME = "Tag certificate";
    private Tag tag;
    private GiftCertificate giftCertificate;
    private TagToGiftCertificateRelation tagToGiftCertificateRelation;
    private TagDao tagDao;
    private TagToGiftCertificateDao tagToGiftCertificateDao;
    private GiftCertificateDao giftCertificateDao;
    private TagToGiftCertificateServiceImpl tagToGiftCertificateService;
    private List<GiftCertificate> giftCertificateList;
    private List<Tag> tagList;
    private List<GiftCertificateDto> giftCertificateDtoList;

    @BeforeAll
    void beforeAll() {
        tag = new Tag();
        tag.setId(TAG_ID);
        tag.setName(TAG_NAME);
        giftCertificate = new GiftCertificate();
        giftCertificate.setId(GIFT_CERTIFICATE_ID);
        giftCertificate.setName(GIFT_CERTIFICATE_NAME);
        tagToGiftCertificateRelation = new TagToGiftCertificateRelation();
        tagToGiftCertificateRelation.setTagId(TAG_ID);
        tagToGiftCertificateRelation.setGiftCertificateId(GIFT_CERTIFICATE_ID);
        tagDao = mock(TagDaoImpl.class);
        tagToGiftCertificateDao = mock(TagToGiftCertificateDao.class);
        giftCertificateDao = mock(GiftCertificateDao.class);
        tagToGiftCertificateService = new TagToGiftCertificateServiceImpl(tagToGiftCertificateDao,
                giftCertificateDao, tagDao);
        giftCertificateList = new ArrayList<>();
        giftCertificateList.add(giftCertificate);
        tagList = new ArrayList<>();
        tagList.add(tag);
        GiftCertificateDto giftCertificateDto = new GiftCertificateDto();
        giftCertificateDto.setGiftCertificate(giftCertificate);
        giftCertificateDto.setTags(tagList);
        giftCertificateDtoList = new ArrayList<>();
        giftCertificateDtoList.add(giftCertificateDto);
    }

    @Test
    void testCreateTagToGiftCertificateRelation() {
        TagToGiftCertificateRelation expectedResult = tagToGiftCertificateRelation;
        when(tagDao.findById(anyLong())).thenReturn(Optional.ofNullable(tag));
        when(giftCertificateDao.findById(anyLong())).thenReturn(Optional.ofNullable(giftCertificate));
        when(tagToGiftCertificateDao.createTagToGiftCertificateRelation(anyLong(), anyLong()))
                .thenReturn(tagToGiftCertificateRelation);
        TagToGiftCertificateRelation actualResult = tagToGiftCertificateService
                .createTagToGiftCertificateRelation(TAG_ID, GIFT_CERTIFICATE_ID);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void testCreateTagToGiftCertificateRelationShouldThrowTagNotFoundException() {
        when(tagDao.findById(anyLong())).thenReturn(Optional.empty());
        when(giftCertificateDao.findById(anyLong())).thenReturn(Optional.ofNullable(giftCertificate));
        assertThrows(TagNotFoundException.class, () -> tagToGiftCertificateService
                .createTagToGiftCertificateRelation(tag.getId(), giftCertificate.getId()));
    }

    @Test
    void testCreateTagToGiftCertificateRelationShouldThrowGiftCertificateNotFoundException() {
        when(tagDao.findById(anyLong())).thenReturn(Optional.ofNullable(tag));
        when(giftCertificateDao.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(GiftCertificateNotFoundException.class, () -> tagToGiftCertificateService
                .createTagToGiftCertificateRelation(tag.getId(), giftCertificate.getId()));
    }

    @Test
    void testFindGiftCertificatesByTagName() {
        List<GiftCertificateDto> expectedResult = giftCertificateDtoList;
        when(tagToGiftCertificateDao.findGiftCertificatesByTagName(anyString())).thenReturn(giftCertificateList);
        when(tagToGiftCertificateDao.findByGiftCertificateId(anyLong())).thenReturn(tagList);
        List<GiftCertificateDto> actualResult = tagToGiftCertificateService
                .findGiftCertificatesByTagName(TAG_NAME);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void testFindGiftCertificatesByTagNameShouldThrowGiftCertificatesNotFoundException() {
        when(tagToGiftCertificateDao.findGiftCertificatesByTagName(anyString())).thenReturn(Collections.emptyList());
        assertThrows(GiftCertificatesNotFoundException.class, () -> tagToGiftCertificateService
                .findGiftCertificatesByTagName(TAG_NAME));
    }
}
