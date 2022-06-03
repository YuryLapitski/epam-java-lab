package com.epam.esm.service.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.pagination.CustomPagination;
import com.epam.esm.repository.dao.GiftCertificateRepository;
import com.epam.esm.repository.dao.OrderRepository;
import com.epam.esm.repository.dao.TagRepository;
//import com.epam.esm.repository.dao.impl.GiftCertificateDaoImpl;
//import com.epam.esm.repository.dao.impl.OrderDaoImpl;
//import com.epam.esm.repository.dao.impl.TagDaoImpl;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.exception.*;
import com.epam.esm.service.pagination.PaginationConverter;
import com.epam.esm.service.validator.GiftCertificateValidator;
import com.epam.esm.service.validator.PaginationValidator;
import com.epam.esm.service.validator.TagValidator;
import com.epam.esm.service.validator.impl.GiftCertificateValidatorImpl;
import com.epam.esm.service.validator.impl.PaginationValidatorImpl;
import com.epam.esm.service.validator.impl.TagValidatorImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyShort;
import static org.mockito.ArgumentMatchers.anyString;
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
    private static final int PAGE = 1;
    private static final int SIZE = 10;
    private static final Long ENTITIES_NUMBER = 1L;
    private static final int LAST_PAGE = 1;
    private GiftCertificate giftCertificate;
    private GiftCertificateRepository giftCertificateDao;
    private Tag tag;
    private TagRepository tagDao;
    private OrderRepository orderDao;
    private GiftCertificateValidator giftCertificateValidator;
    private TagValidator tagValidator;
    private PaginationValidator paginationValidator;
    private GiftCertificateService giftCertificateService;
    private List<GiftCertificate> giftCertificateList;
    private CustomPagination pagination;
    private List<String> tagNames;
    private List<String> columnNames;
    private PaginationConverter paginationConverter;

    @BeforeAll
    void beforeAll() {
//        giftCertificateDao = mock(GiftCertificateDaoImpl.class);
//        tagDao = mock(TagDaoImpl.class);
//        orderDao = mock(OrderDaoImpl.class);
        giftCertificateValidator = mock(GiftCertificateValidatorImpl.class);
        tagValidator = mock(TagValidatorImpl.class);
        paginationValidator = mock(PaginationValidatorImpl.class);
        giftCertificateService = new GiftCertificateServiceImpl(giftCertificateDao, tagDao, orderDao,
                giftCertificateValidator, tagValidator, paginationValidator, paginationConverter);
        pagination = new CustomPagination();
        pagination.setPage(PAGE);
        pagination.setSize(SIZE);
        tag = new Tag();
        tag.setId(TAG_ID);
        tag.setName(TAG_NAME);
        List<Tag> tagList = new ArrayList<>();
        tagList.add(tag);
        giftCertificate = new GiftCertificate();
        giftCertificate.setId(ID);
        giftCertificate.setName(NAME);
        giftCertificate.setDescription(DESCRIPTION);
        giftCertificate.setPrice(PRICE);
        giftCertificate.setDuration(DURATION);
        giftCertificate.setTagList(tagList);
        giftCertificateList = new ArrayList<>();
        giftCertificateList.add(giftCertificate);
        tagNames = tagList.stream().map(tagName -> tag.getName()).collect(Collectors.toList());
        columnNames = new ArrayList<>(Collections.singleton(ANY_STRING));
    }

    @Test
    void testCreate() {
        when(tagDao.findByName(TAG_NAME)).thenReturn(Optional.ofNullable(tag));
        when(tagDao.save(tag)).thenReturn(tag);
        when(tagValidator.isNameValid(TAG_NAME)).thenReturn(true);
        when(giftCertificateValidator.validateAll(NAME, DESCRIPTION, PRICE, DURATION)).thenReturn(true);
        when(giftCertificateDao.findByName(NAME)).thenReturn(Optional.empty());
        when(giftCertificateDao.save(giftCertificate)).thenReturn(giftCertificate);
        GiftCertificate actualResult = giftCertificateService.create(giftCertificate);
        assertEquals(giftCertificate, actualResult);
    }

    @Test
    void testCreateShouldThrowFieldValidationExceptionWhenTagInvalid() {
        when(tagValidator.isNameValid(anyString())).thenReturn(false);
        when(giftCertificateValidator.validateAll(anyString(), anyString(), any(), anyShort())).thenReturn(true);
        assertThrows(FieldValidationException.class, () -> giftCertificateService.create(giftCertificate));
    }

    @Test
    void testCreateShouldThrowFieldValidationExceptionWhenGiftCertificateInvalid() {
        when(tagValidator.isNameValid(anyString())).thenReturn(true);
        when(giftCertificateValidator.validateAll(anyString(), anyString(), any(), anyShort())).thenReturn(false);
        assertThrows(FieldValidationException.class, () -> giftCertificateService.create(giftCertificate));
    }

    @Test
    void testCreateShouldThrowGiftCertificateAlreadyExistException() {
        when(tagValidator.isNameValid(anyString())).thenReturn(true);
        when(giftCertificateValidator.validateAll(anyString(), anyString(), any(), anyShort())).thenReturn(true);
        when(giftCertificateDao.findByName(anyString())).thenReturn(Optional.of(giftCertificate));
        assertThrows(GiftCertificateAlreadyExistException.class,
                () -> giftCertificateService.create(giftCertificate));
    }

//    @Test
//    void testFindAll() {
//        when(giftCertificateDao.findAll(pagination, GiftCertificate.class)).thenReturn(giftCertificateList);
//        when(giftCertificateDao.getEntitiesNumber(GiftCertificate.class)).thenReturn(ENTITIES_NUMBER);
//        when(paginationValidator.isSizeValid(pagination)).thenReturn(true);
//        when(paginationValidator.isPageValid(pagination, LAST_PAGE)).thenReturn(true);
//        List<GiftCertificate> actualResult = giftCertificateService.findAll(pagination);
//        assertEquals(giftCertificateList, actualResult);
//    }

    @Test
    void testFindById() {

        when(giftCertificateDao.findById(GIFT_CERTIFICATE_ID))
                .thenReturn(Optional.ofNullable(giftCertificate));
        GiftCertificate actualResult = giftCertificateService
                .findByGiftCertificateId(GIFT_CERTIFICATE_ID);
        assertEquals(giftCertificate, actualResult);
    }

    @Test
    void testFindByIdShouldThrowGiftCertificateNotFoundException() {
        when(giftCertificateDao.findById(GIFT_CERTIFICATE_ID)).thenReturn(Optional.empty());
        assertThrows(GiftCertificateNotFoundException.class, () -> giftCertificateService
                .findByGiftCertificateId(tag.getId()));
    }

//    @Test
//    void testFindByAttributes() {
//        when(giftCertificateDao.findByPartOfName(NAME)).thenReturn(giftCertificateList);
//        when(giftCertificateDao.findByAttributesNumber(NAME, tagNames)).thenReturn(ENTITIES_NUMBER);
//        when(paginationValidator.isSizeValid(pagination)).thenReturn(true);
//        when(paginationValidator.isPageValid(pagination, LAST_PAGE)).thenReturn(true);
//        when(tagDao.findByName(TAG_NAME)).thenReturn(Optional.of(tag));
//        when(giftCertificateDao.findGiftCertificatesByTagNames(tagNames)).thenReturn(giftCertificateList);
//        when(giftCertificateValidator.isColumnNameValid(ANY_STRING)).thenReturn(true);
//        when(giftCertificateValidator.isSortTypeValid(ANY_STRING)).thenReturn(true);
//        when(giftCertificateDao.findByAttributes(NAME, tagNames, columnNames, ANY_STRING, pagination))
//                .thenReturn(giftCertificateList);
//        List<GiftCertificate> actualResult = giftCertificateService
//                .findByAttributes(giftCertificate.getName(), tagNames, columnNames, ANY_STRING, pagination);
//        assertEquals(giftCertificateList, actualResult);
//    }

//    @Test
//    void testFindByAttributesShouldTrowGiftCertificateNotFoundException() {
//        when(giftCertificateDao.findByPartOfName(anyString())).thenReturn(Collections.emptyList());
//        assertThrows(GiftCertificateNotFoundException.class,
//                () -> giftCertificateService.findByAttributes(NAME, tagNames, columnNames, ANY_STRING, pagination));
//    }

//    @Test
//    void testFindByAttributesShouldThrowTagDoesNotExistException() {
//        when(giftCertificateDao.findByPartOfName(NAME)).thenReturn(giftCertificateList);
//        when(tagDao.findByName(TAG_NAME)).thenReturn(Optional.empty());
//        assertThrows(TagDoesNotExistException.class,
//                () -> giftCertificateService.findByAttributes(NAME, tagNames, columnNames, ANY_STRING, pagination));
//    }

//    @Test
//    void testFindByAttributesShouldThrowNoMatchingGiftCertificateException() {
//        when(giftCertificateDao.findByPartOfName(NAME)).thenReturn(giftCertificateList);
//        when(tagDao.findByName(TAG_NAME)).thenReturn(Optional.of(tag));
//        when(giftCertificateDao.findGiftCertificatesByTagNames(tagNames)).thenReturn(Collections.emptyList());
//        assertThrows(NoMatchingGiftCertificateException.class,
//                () -> giftCertificateService.findByAttributes(NAME, tagNames, columnNames, ANY_STRING, pagination));
//    }

//    @Test
//    void testFindByAttributesShouldThrowInvalidColumnNameException() {
//        when(giftCertificateDao.findByPartOfName(NAME)).thenReturn(giftCertificateList);
//        when(tagDao.findByName(TAG_NAME)).thenReturn(Optional.of(tag));
//        when(giftCertificateDao.findGiftCertificatesByTagNames(tagNames)).thenReturn(giftCertificateList);
//        when(giftCertificateValidator.isColumnNameValid(ANY_STRING)).thenReturn(false);
//        assertThrows(InvalidColumnNameException.class,
//                () -> giftCertificateService.findByAttributes(NAME, tagNames, columnNames, ANY_STRING, pagination));
//    }

//    @Test
//    void testFindByAttributesShouldThrowInvalidSortTypeException() {
//        when(giftCertificateDao.findByPartOfName(NAME)).thenReturn(giftCertificateList);
//        when(tagDao.findByName(TAG_NAME)).thenReturn(Optional.of(tag));
//        when(giftCertificateDao.findGiftCertificatesByTagNames(tagNames)).thenReturn(giftCertificateList);
//        when(giftCertificateValidator.isColumnNameValid(ANY_STRING)).thenReturn(true);
//        when(giftCertificateValidator.isSortTypeValid(ANY_STRING)).thenReturn(false);
//        assertThrows(InvalidSortTypeException.class,
//                () -> giftCertificateService.findByAttributes(NAME, tagNames, columnNames, ANY_STRING, pagination));
//    }

    @Test
    void testDeleteShouldTrowGiftCertificateNotFoundException() {
        when(giftCertificateDao.findById(GIFT_CERTIFICATE_ID)).thenReturn(Optional.empty());
        assertThrows(GiftCertificateNotFoundException.class,
                () -> giftCertificateService.delete(giftCertificate.getId()));
    }

    @Test
    void testDeleteShouldTrowHasOrderToGiftCertificateException() {
        Optional<GiftCertificate> optionalGiftCertificate = Optional.of(giftCertificate);
        when(giftCertificateDao.findById(GIFT_CERTIFICATE_ID))
                .thenReturn(optionalGiftCertificate);
        when(orderDao.findByGiftCertificateId(GIFT_CERTIFICATE_ID)).thenReturn(Collections.emptyList());
        assertThrows(GiftCertificateNotFoundException.class,
                () -> giftCertificateService.delete(giftCertificate.getId()));
    }

    @Test
    void testUpdate() {
        when(giftCertificateDao.findById(GIFT_CERTIFICATE_ID))
                .thenReturn(Optional.ofNullable(giftCertificate));
//        when((giftCertificateDao.update(giftCertificate))).thenReturn(giftCertificate);
        when(giftCertificateValidator.isNameValid(anyString())).thenReturn(true);
        when(giftCertificateValidator.isDescriptionValid(anyString())).thenReturn(true);
        when(giftCertificateValidator.isPriceValid(any())).thenReturn(true);
        when(giftCertificateValidator.isDurationValid(anyShort())).thenReturn(true);
        GiftCertificate actualResult = giftCertificateService.update(GIFT_CERTIFICATE_ID, giftCertificate);
        assertEquals(giftCertificate, actualResult);
    }

    @Test
    void testUpdateShouldThrowGiftCertificateNotFoundException() {
        when(giftCertificateDao.findById(GIFT_CERTIFICATE_ID)).thenReturn(Optional.empty());
        assertThrows(GiftCertificateNotFoundException.class, () -> giftCertificateService.update(GIFT_CERTIFICATE_ID,
                giftCertificate));
    }

    @Test
    void testUpdateShouldThrowNameFieldValidationException() {
        when(giftCertificateDao.findById(GIFT_CERTIFICATE_ID))
                .thenReturn(Optional.ofNullable(giftCertificate));
        when(giftCertificateValidator.isNameValid(NAME)).thenReturn(false);
        assertThrows(FieldValidationException.class, () -> giftCertificateService.update(GIFT_CERTIFICATE_ID,
                giftCertificate));
    }

    @Test
    void testUpdateShouldThrowDescriptionFieldValidationException() {
        when(giftCertificateDao.findById(GIFT_CERTIFICATE_ID))
                .thenReturn(Optional.ofNullable(giftCertificate));
        when(giftCertificateValidator.isNameValid(NAME)).thenReturn(true);
        when(giftCertificateValidator.isDescriptionValid(DESCRIPTION)).thenReturn(false);
        assertThrows(FieldValidationException.class, () -> giftCertificateService.update(GIFT_CERTIFICATE_ID,
                giftCertificate));
    }

    @Test
    void testUpdateShouldThrowPriceFieldValidationException() {
        when(giftCertificateDao.findById(GIFT_CERTIFICATE_ID))
                .thenReturn(Optional.ofNullable(giftCertificate));
        when(giftCertificateValidator.isNameValid(NAME)).thenReturn(true);
        when(giftCertificateValidator.isDescriptionValid(DESCRIPTION)).thenReturn(true);
        when(giftCertificateValidator.isPriceValid(PRICE)).thenReturn(false);
        assertThrows(FieldValidationException.class, () -> giftCertificateService.update(GIFT_CERTIFICATE_ID,
                giftCertificate));
    }

    @Test
    void testUpdateShouldThrowDurationFieldValidationException() {
        when(giftCertificateDao.findById(GIFT_CERTIFICATE_ID))
                .thenReturn(Optional.ofNullable(giftCertificate));
        when(giftCertificateValidator.isNameValid(NAME)).thenReturn(true);
        when(giftCertificateValidator.isDescriptionValid(DESCRIPTION)).thenReturn(true);
        when(giftCertificateValidator.isPriceValid(PRICE)).thenReturn(true);
        when(giftCertificateValidator.isDurationValid(DURATION)).thenReturn(false);
        assertThrows(FieldValidationException.class, () -> giftCertificateService.update(GIFT_CERTIFICATE_ID,
                giftCertificate));
    }
}
