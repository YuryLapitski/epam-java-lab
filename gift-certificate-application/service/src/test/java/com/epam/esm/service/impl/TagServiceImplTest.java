package com.epam.esm.service.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.entity.TagToGiftCertificateRelation;
import com.epam.esm.repository.dao.TagDao;
import com.epam.esm.repository.dao.TagToGiftCertificateDao;
import com.epam.esm.repository.dao.impl.TagDaoImpl;
import com.epam.esm.service.exception.FieldValidationException;
import com.epam.esm.service.exception.TagNotFoundException;
import com.epam.esm.service.exception.TagToGiftCertificateReferenceException;
import com.epam.esm.service.validator.TagValidator;
import com.epam.esm.service.validator.impl.TagValidatorImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TagServiceImplTest {
    private static final long ID = 10L;
    private static final String NAME = "Some tag";
    private static final long GIFT_CERTIFICATE_ID = 1L;
    private static final long TAG_ID = 1L;
    private Tag tag;
    private TagDao tagDao;
    private TagToGiftCertificateDao tagToGiftCertificateDao;
    private TagValidator tagValidator;
    private TagServiceImpl tagService;
    private List<Tag> tagList;
    private List<TagToGiftCertificateRelation> tagToGiftCertificateRelationList;

    @BeforeAll
    void beforeAll() {
        tag = new Tag();
        tag.setId(ID);
        tag.setName(NAME);
        TagToGiftCertificateRelation tagToGiftCertificateRelation = new TagToGiftCertificateRelation();
        tagToGiftCertificateRelation.setGiftCertificateId(GIFT_CERTIFICATE_ID);
        tagToGiftCertificateRelation.setTagId(TAG_ID);
        tagDao = mock(TagDaoImpl.class);
        tagToGiftCertificateDao = mock(TagToGiftCertificateDao.class);
        tagValidator = mock(TagValidatorImpl.class);
        tagService = new TagServiceImpl(tagDao, tagToGiftCertificateDao, tagValidator);
        tagList = new ArrayList<>();
        tagList.add(tag);
        tagToGiftCertificateRelationList = new ArrayList<>();
        tagToGiftCertificateRelationList.add(tagToGiftCertificateRelation);
    }

    @Test
    void testCreate() {
        Tag expectedResult = tag;
        when(tagValidator.isNameValid(any())).thenReturn(true);
        when(tagDao.create(any())).thenReturn(expectedResult);
        Tag actualResult = tagService.create(tag);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void testCreateShouldThrowException() {
        when(tagValidator.isNameValid(any())).thenReturn(false);
        assertThrows(FieldValidationException.class, () -> tagService.create(tag));
    }

    @Test
    void testFindAll() {
        List<Tag> expectedResult = tagList;
        when(tagDao.findAll()).thenReturn(expectedResult);
        List<Tag> actualResult = tagService.findAll();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void testFindById() {
        Tag expectedResult = tag;
        when(tagDao.findById(anyLong())).thenReturn(Optional.ofNullable(expectedResult));
        Tag actualResult = tagService.findById(tag.getId());
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void testFindByIdShouldThrowException() {
        when(tagDao.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(TagNotFoundException.class, () -> tagService.findById(tag.getId()));
    }

    @Test
    void testDelete() {
        when(tagDao.findById(anyLong())).thenReturn(Optional.ofNullable(tag));
        when(tagToGiftCertificateDao.findByTagId(anyLong())).thenReturn(Collections.emptyList());
        when(tagDao.delete(anyLong())).thenReturn(true);
        assertTrue(tagService.delete(tag.getId()));
    }

    @Test
    void testDeleteShouldThrowTagNotFoundException() {
        when(tagDao.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(TagNotFoundException.class, () -> tagService.delete(tag.getId()));
    }

    @Test
    void testDeleteShouldThrowTagToGiftCertificateReferenceException() {
        when(tagDao.findById(anyLong())).thenReturn(Optional.ofNullable(tag));
        when(tagToGiftCertificateDao.findByTagId(anyLong()))
                .thenReturn(tagToGiftCertificateRelationList);
        assertThrows(TagToGiftCertificateReferenceException.class, () -> tagService.delete(tag.getId()));
    }
}
