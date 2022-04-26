package com.epam.esm.service.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.TagToGiftCertificateRelation;
import com.epam.esm.repository.dao.GiftCertificateDao;
import com.epam.esm.repository.dao.TagDao;
import com.epam.esm.repository.dao.TagToGiftCertificateDao;
import com.epam.esm.repository.dao.impl.GiftCertificateDaoImpl;
import com.epam.esm.repository.dao.impl.TagDaoImpl;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.TagToGiftCertificateService;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.exception.*;
import com.epam.esm.service.validator.GiftCertificateValidator;
import com.epam.esm.service.validator.TagValidator;
import com.epam.esm.service.validator.impl.GiftCertificateValidatorImpl;
import com.epam.esm.service.validator.impl.TagValidatorImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GiftCertificateServiceImplTest {
    private static final Long ID = 10L;
    private static final String NAME = "Gift certificate";
    private static final String DESCRIPTION = "description";
    private static final BigDecimal PRICE = BigDecimal.valueOf(99.99);
    private static final short DURATION = 100;
    private static final Long GIFT_CERTIFICATE_ID = 1L;
    private static final Long TAG_ID = 1L;
    private static final String TAG_NAME = "Tag";
    private static final String ANY_STRING = "Any string";
    private GiftCertificate giftCertificate;
    private GiftCertificateDao giftCertificateDao;
    private Tag tag;
    private TagDao tagDao;
    private GiftCertificateDto giftCertificateDto;
    private TagToGiftCertificateDao tagToGiftCertificateDao;
    private TagToGiftCertificateService tagToGiftCertificateService;
    private GiftCertificateValidator giftCertificateValidator;
    private TagValidator tagValidator;
    private GiftCertificateService giftCertificateService;
    private List<Tag> tagList;
    private List<GiftCertificate> giftCertificateList;
    private List<GiftCertificateDto> giftCertificateDtoList;

    @BeforeAll
    void beforeAll() {
        giftCertificate = new GiftCertificate();
        giftCertificate.setId(ID);
        giftCertificate.setName(NAME);
        giftCertificate.setDescription(DESCRIPTION);
        giftCertificate.setPrice(PRICE);
        giftCertificate.setDuration(DURATION);
        TagToGiftCertificateRelation tagToGiftCertificateRelation = new TagToGiftCertificateRelation();
        tagToGiftCertificateRelation.setTagId(TAG_ID);
        tagToGiftCertificateRelation.setGiftCertificateId(GIFT_CERTIFICATE_ID);
        giftCertificateDao = mock(GiftCertificateDaoImpl.class);
        tagDao = mock(TagDaoImpl.class);
        tagToGiftCertificateDao = mock(TagToGiftCertificateDao.class);
        giftCertificateValidator = mock(GiftCertificateValidatorImpl.class);
        tagValidator = mock(TagValidatorImpl.class);
        giftCertificateService = new GiftCertificateServiceImpl(giftCertificateDao, tagDao,
                tagToGiftCertificateDao, tagToGiftCertificateService, giftCertificateValidator, tagValidator);
        giftCertificateList = new ArrayList<>();
        giftCertificateList.add(giftCertificate);
    }

    @BeforeEach
    void beforeEach() {
        tag = new Tag();
        tag.setId(TAG_ID);
        tag.setName(TAG_NAME);
        tagList = new ArrayList<>();
        tagList.add(tag);
        giftCertificateDto = new GiftCertificateDto();
        giftCertificateDto.setGiftCertificate(giftCertificate);
        giftCertificateDto.setTags(tagList);
        giftCertificateDtoList = new ArrayList<>();
        giftCertificateDtoList.add(giftCertificateDto);
    }

    @Test
    void testCreate() {
        GiftCertificateDto expectedResult = giftCertificateDto;
        when(tagDao.findByName(anyString())).thenReturn(Optional.ofNullable(tag));
        when(tagDao.create(any())).thenReturn(tag);
        when(tagValidator.isNameValid(anyString())).thenReturn(true);
        when(giftCertificateValidator.validateAll(anyString(), anyString(), any(), anyShort())).thenReturn(true);
        when(giftCertificateDao.findByName(anyString())).thenReturn(Optional.empty());
        GiftCertificateDto actualResult = giftCertificateService.create(giftCertificateDto);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void testCreateShouldThrowFieldValidationExceptionWhenTagInvalid() {
        when(tagValidator.isNameValid(anyString())).thenReturn(false);
        when(giftCertificateValidator.validateAll(anyString(), anyString(), any(), anyShort())).thenReturn(true);
        assertThrows(FieldValidationException.class, () -> giftCertificateService.create(giftCertificateDto));
    }

    @Test
    void testCreateShouldThrowFieldValidationExceptionWhenGiftCertificateInvalid() {
        when(tagValidator.isNameValid(anyString())).thenReturn(true);
        when(giftCertificateValidator.validateAll(anyString(), anyString(), any(), anyShort())).thenReturn(false);
        assertThrows(FieldValidationException.class, () -> giftCertificateService.create(giftCertificateDto));
    }

    @Test
    void testCreateShouldThrowGiftCertificateAlreadyExistException() {
        when(tagValidator.isNameValid(anyString())).thenReturn(true);
        when(giftCertificateValidator.validateAll(anyString(), anyString(), any(), anyShort())).thenReturn(true);
        when(giftCertificateDao.findByName(anyString())).thenReturn(Optional.of(giftCertificate));
        assertThrows(GiftCertificateAlreadyExistException.class,
                () -> giftCertificateService.create(giftCertificateDto));
    }

    @Test
    void testFindAll() {
        List<GiftCertificateDto> expectedResult = giftCertificateDtoList;
        when(giftCertificateDao.findAll()).thenReturn(giftCertificateList);
        when(tagToGiftCertificateDao.findByGiftCertificateId(anyLong())).thenReturn(tagList);
        List<GiftCertificateDto> actualResult = giftCertificateService.findAll();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void testFindById() {
        GiftCertificateDto expectedResult = giftCertificateDto;
        when(giftCertificateDao.findById(anyLong())).thenReturn(Optional.ofNullable(giftCertificate));
        when(tagToGiftCertificateDao.findByGiftCertificateId(anyLong())).thenReturn(tagList);
        GiftCertificateDto actualResult = giftCertificateService
                .findByGiftCertificateId(GIFT_CERTIFICATE_ID);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void testFindByIdShouldThrowGiftCertificateNotFoundException() {
        when(giftCertificateDao.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(GiftCertificateNotFoundException.class, () -> giftCertificateService
                .findByGiftCertificateId(tag.getId()));
    }

    @Test
    void testFindByPartOfName() {
        List<GiftCertificateDto> expectedResult = giftCertificateDtoList;
        when(giftCertificateDao.findByPartOfName(anyString())).thenReturn(giftCertificateList);
        List<GiftCertificateDto> actualResult = giftCertificateService.findByPartOfName(giftCertificate.getName());
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void testFindByPartOfNameShouldTrowGiftCertificateNotFoundException() {
        when(giftCertificateDao.findByPartOfName(anyString())).thenReturn(Collections.emptyList());
        assertThrows(GiftCertificateNotFoundException.class,
                () -> giftCertificateService.findByPartOfName(giftCertificate.getName()));
    }

    @Test
    void testFindAllWithSort() {
        List<GiftCertificateDto> expectedResult = giftCertificateDtoList;
        when(giftCertificateValidator.isColumnNameValid(anyString())).thenReturn(true);
        when(giftCertificateValidator.isSortTypeValid(anyString())).thenReturn(true);
        when(giftCertificateDao.findAllWithSort(anyString(), anyString())).thenReturn(giftCertificateList);
        List<GiftCertificateDto> actualResult = giftCertificateService
                .findAllWithSort(ANY_STRING, ANY_STRING);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void testFindAllWithSortShouldThrowInvalidColumnNameException() {
        when(giftCertificateValidator.isColumnNameValid(anyString())).thenReturn(false);
        when(giftCertificateValidator.isSortTypeValid(anyString())).thenReturn(true);
        assertThrows(InvalidColumnNameException.class,
                () -> giftCertificateService.findAllWithSort(ANY_STRING, ANY_STRING));
    }

    @Test
    void testFindAllWithSortShouldThrowInvalidSortTypeException() {
        when(giftCertificateValidator.isColumnNameValid(anyString())).thenReturn(true);
        when(giftCertificateValidator.isSortTypeValid(anyString())).thenReturn(false);
        assertThrows(InvalidSortTypeException.class,
                () -> giftCertificateService.findAllWithSort(ANY_STRING, ANY_STRING));
    }

    @Test
    void testDelete() {
        when(giftCertificateDao.findById(anyLong())).thenReturn(Optional.ofNullable(giftCertificate));
        when(giftCertificateDao.delete(anyLong())).thenReturn(true);
        assertTrue(giftCertificateService.delete(giftCertificate.getId()));
    }

    @Test
    void testDeleteShouldTrowGiftCertificateNotFoundException() {
        when(giftCertificateDao.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(GiftCertificateNotFoundException.class,
                () -> giftCertificateService.delete(giftCertificate.getId()));
    }

    @Test
    void testUpdate() {
        GiftCertificateDto expectedResult = giftCertificateDto;
        when(giftCertificateDao.findById(anyLong())).thenReturn(Optional.ofNullable(giftCertificate));
        when((giftCertificateDao.update(anyLong(), anyMap()))).thenReturn(Optional.ofNullable(giftCertificate));
        when(giftCertificateValidator.isNameValid(anyString())).thenReturn(true);
        when(giftCertificateValidator.isDescriptionValid(anyString())).thenReturn(true);
        when(giftCertificateValidator.isPriceValid(any())).thenReturn(true);
        when(giftCertificateValidator.isDurationValid(anyShort())).thenReturn(true);
        GiftCertificateDto actualResult = giftCertificateService.update(GIFT_CERTIFICATE_ID, giftCertificateDto);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void testUpdateShouldThrowCannotUpdateException() {
        when(giftCertificateDao.findById(anyLong())).thenReturn(Optional.ofNullable(giftCertificate));
        when(giftCertificateDao.update(anyLong(), anyMap())).thenReturn(Optional.empty());
        when(giftCertificateValidator.isNameValid(anyString())).thenReturn(true);
        when(giftCertificateValidator.isDescriptionValid(anyString())).thenReturn(true);
        when(giftCertificateValidator.isPriceValid(any())).thenReturn(true);
        when(giftCertificateValidator.isDurationValid(anyShort())).thenReturn(true);
        assertThrows(CannotUpdateException.class, () -> giftCertificateService.update(GIFT_CERTIFICATE_ID,
                giftCertificateDto));
    }

    @Test
    void testUpdateShouldThrowGiftCertificateNotFoundException() {
        when(giftCertificateDao.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(GiftCertificateNotFoundException.class, () -> giftCertificateService.update(GIFT_CERTIFICATE_ID,
                giftCertificateDto));
    }
}
