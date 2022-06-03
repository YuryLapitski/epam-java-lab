package com.epam.esm.service.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.pagination.CustomPagination;
import com.epam.esm.repository.dao.GiftCertificateRepository;
import com.epam.esm.repository.dao.TagRepository;
import com.epam.esm.service.exception.FieldValidationException;
import com.epam.esm.service.exception.TagNotFoundException;
import com.epam.esm.service.exception.TagToGiftCertificateReferenceException;
import com.epam.esm.service.pagination.PaginationConverter;
import com.epam.esm.service.validator.PaginationValidator;
import com.epam.esm.service.validator.TagValidator;
import com.epam.esm.service.validator.impl.PaginationValidatorImpl;
import com.epam.esm.service.validator.impl.TagValidatorImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TagServiceImplTest {
    private static final long ID = 10L;
    private static final String NAME = "Some tag";
    private static final String GIFT_CERTIFICATE_NAME = "Gift certificate";
    private static final String DESCRIPTION = "description";
    private static final BigDecimal PRICE = BigDecimal.valueOf(99.99);
    private static final short DURATION = 100;
    private static final Long GIFT_CERTIFICATE_ID = 1L;
    private static final int PAGE = 1;
    private static final int SIZE = 10;
    private static final int LAST_PAGE = 1;
    private Tag tag;
    private TagRepository tagRepository;
    private GiftCertificateRepository giftCertificateDao;
    private TagValidator tagValidator;
    private PaginationValidator paginationValidator;
    private TagServiceImpl tagService;
    private List<Tag> tagList;
    private Page<Tag> tagPage;
    private List<GiftCertificate> giftCertificateList;
    private CustomPagination pagination;
    private Pageable pageable;
    private List<String> tagNames;
    private PaginationConverter paginationConverter;

    @BeforeAll
    void beforeAll() {
        tag = new Tag();
        tag.setId(ID);
        tag.setName(NAME);
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(GIFT_CERTIFICATE_ID);
        giftCertificate.setName(GIFT_CERTIFICATE_NAME);
        giftCertificate.setDescription(DESCRIPTION);
        giftCertificate.setPrice(PRICE);
        giftCertificate.setDuration(DURATION);
        tagRepository = mock(TagRepository.class);
        giftCertificateDao = mock(GiftCertificateRepository.class);
        tagValidator = mock(TagValidatorImpl.class);
        paginationValidator = mock(PaginationValidatorImpl.class);
        paginationConverter = mock(PaginationConverter.class);
        tagService = new TagServiceImpl(tagRepository, giftCertificateDao, tagValidator, paginationValidator, paginationConverter);
        tagList = new ArrayList<>();
        tagList.add(tag);
        tagPage = new PageImpl<>(tagList);
        giftCertificateList = new ArrayList<>();
        giftCertificateList.add(giftCertificate);
        pagination = new CustomPagination();
        pagination.setPage(PAGE);
        pagination.setSize(SIZE);
        pageable = PageRequest.of(PAGE, SIZE);
        tagNames = tagList.stream().map(name -> tag.getName()).collect(Collectors.toList());
    }

    @Test
    void testCreate() {
        when(tagValidator.isNameValid(any())).thenReturn(true);
        when(tagRepository.save(any())).thenReturn(tag);
        Tag actualResult = tagService.create(tag);
        assertEquals(tag, actualResult);
    }

    @Test
    void testCreateShouldThrowException() {
        when(tagValidator.isNameValid(any())).thenReturn(false);
        assertThrows(FieldValidationException.class, () -> tagService.create(tag));
    }

    @Test
    void testFindAll() {
        when(tagRepository.findAll(pageable)).thenReturn(tagPage);
        when(paginationConverter.convert(pagination)).thenReturn(pageable);
        when(paginationValidator.isSizeValid(pagination)).thenReturn(true);
        when(paginationValidator.isPageValid(pagination, LAST_PAGE)).thenReturn(true);
        List<Tag> actualResult = tagService.findAll(pagination);
        assertEquals(tagList, actualResult);
    }

    @Test
    void testFindById() {
        when(tagRepository.findById(ID)).thenReturn(Optional.ofNullable(tag));
        Tag actualResult = tagService.findById(tag.getId());
        assertEquals(tag, actualResult);
    }

    @Test
    void testFindByIdShouldThrowException() {
        when(tagRepository.findById(ID)).thenReturn(Optional.empty());
        assertThrows(TagNotFoundException.class, () -> tagService.findById(tag.getId()));
    }

    @Test
    void testDeleteShouldThrowTagNotFoundException() {
        when(tagRepository.findById(ID)).thenReturn(Optional.empty());
        assertThrows(TagNotFoundException.class, () -> tagService.delete(tag.getId()));
    }

    @Test
    void testDeleteShouldThrowTagToGiftCertificateReferenceException() {
        when(tagRepository.findById(ID)).thenReturn(Optional.ofNullable(tag));
        when(giftCertificateDao.findGiftCertificatesByTagNames(tagNames))
                .thenReturn(giftCertificateList);
        assertThrows(TagToGiftCertificateReferenceException.class, () -> tagService.delete(tag.getId()));
    }
}
