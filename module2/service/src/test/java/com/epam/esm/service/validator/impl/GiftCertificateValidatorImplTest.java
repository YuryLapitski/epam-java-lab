package com.epam.esm.service.validator.impl;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GiftCertificateValidatorImplTest {
    private static final String VALID_NAME = "name";
    private static final String INVALID_NAME = "@#$%^&*";
    private static final String VALID_DESCRIPTION = "description";
    private static final String INVALID_DESCRIPTION = "";
    private static final BigDecimal VALID_PRICE = BigDecimal.valueOf(99.99);
    private static final BigDecimal INVALID_HIGH_PRICE = BigDecimal.valueOf(10000.00);
    private static final BigDecimal INVALID_LOW_PRICE = BigDecimal.valueOf(-10.00);
    private static final short VALID_DURATION = 100;
    private static final short INVALID_HIGH_DURATION = 500;
    private static final short INVALID_LOW_DURATION = -10;
    private static final String VALID_ASC_SORT_TYPE = "asc";
    private static final String INVALID_ASC_SORT_TYPE = "abc";
    private static final String VALID_DESC_SORT_TYPE = "desc";
    private static final String INVALID_DESC_SORT_TYPE = "abcde";
    private static final String VALID_COLUMN_NAME = "name";
    private static final String INVALID_COLUMN_NAME = "another";
    private static final String VALID_COLUMN_DESCRIPTION = "description";
    private static final String INVALID_COLUMN_DESCRIPTION = "another";
    private static final String VALID_COLUMN_PRICE = "price";
    private static final String INVALID_COLUMN_PRICE = "another";
    private static final String VALID_COLUMN_DURATION = "duration";
    private static final String INVALID_COLUMN_DURATION = "another";
    private static final String VALID_COLUMN_CREATE_DATE = "create_date";
    private static final String INVALID_COLUMN_CREATE_DATE = "another";
    private static final String VALID_COLUMN_LAST_UPDATE_DATE = "last_update_date";
    private static final String INVALID_COLUMN_LAST_UPDATE_DATE = "another";

    private GiftCertificateValidatorImpl giftCertificateValidator;

    @BeforeAll
    void init() {
        giftCertificateValidator = new GiftCertificateValidatorImpl();
    }

    @Test
    void isNameValidPositive() {
        assertTrue(giftCertificateValidator.isNameValid(VALID_NAME));
    }

    @Test
    void isNameValidNegative() {
        assertFalse(giftCertificateValidator.isNameValid(INVALID_NAME));
    }

    @Test
    void isDescriptionValidPositive() {
        assertTrue(giftCertificateValidator.isDescriptionValid(VALID_DESCRIPTION));
    }

    @Test
    void isDescriptionValidNegative() {
        assertFalse(giftCertificateValidator.isDescriptionValid(INVALID_DESCRIPTION));
    }

    @Test
    void isPriceValidPositive() {
        assertTrue(giftCertificateValidator.isPriceValid(VALID_PRICE));
    }

    @Test
    void isPriceValidPositiveNegativeWhenPriceHigher() {
        assertFalse(giftCertificateValidator.isPriceValid(INVALID_HIGH_PRICE));
    }

    @Test
    void isPriceValidPositiveNegativeWhenPriceLower() {
        assertFalse(giftCertificateValidator.isPriceValid(INVALID_LOW_PRICE));
    }

    @Test
    void isDurationValidPositive() {
        assertTrue(giftCertificateValidator.isDurationValid(VALID_DURATION));
    }

    @Test
    void isDurationValidNegativeWhenDurationHigher() {
        assertFalse(giftCertificateValidator.isDurationValid(INVALID_HIGH_DURATION));
    }

    @Test
    void isDurationValidPositiveWhenDurationLower() {
        assertFalse(giftCertificateValidator.isDurationValid(INVALID_LOW_DURATION));
    }

    @Test
    void validateAllPositive(){
        assertTrue(giftCertificateValidator.validateAll(VALID_NAME, VALID_DESCRIPTION, VALID_PRICE, VALID_DURATION));
    }

    @Test
    void validateAllNegativeName(){
        assertFalse(giftCertificateValidator.validateAll(INVALID_NAME, VALID_DESCRIPTION,
                VALID_PRICE, VALID_DURATION));
    }

    @Test
    void validateAllNegativeDescription(){
        assertFalse(giftCertificateValidator.validateAll(VALID_NAME, INVALID_DESCRIPTION,
                VALID_PRICE, VALID_DURATION));
    }

    @Test
    void validateAllNegativePrice(){
        assertFalse(giftCertificateValidator.validateAll(VALID_NAME, VALID_DESCRIPTION,
                INVALID_HIGH_PRICE, VALID_DURATION));
    }

    @Test
    void validateAllNegativeDuration(){
        assertFalse(giftCertificateValidator.validateAll(VALID_NAME, VALID_DESCRIPTION,
                VALID_PRICE, INVALID_HIGH_DURATION));
    }

    @Test
    void isSortTypeAscValidPositive() {
        assertTrue(giftCertificateValidator.isSortTypeValid(VALID_ASC_SORT_TYPE));
    }

    @Test
    void isSortTypeAscValidNegative() {
        assertFalse(giftCertificateValidator.isSortTypeValid(INVALID_ASC_SORT_TYPE));
    }

    @Test
    void isSortTypeDescValidPositive() {
        assertTrue(giftCertificateValidator.isSortTypeValid(VALID_DESC_SORT_TYPE));
    }

    @Test
    void isSortTypeDescValidNegative() {
        assertFalse(giftCertificateValidator.isSortTypeValid(INVALID_DESC_SORT_TYPE));
    }

    @Test
    void isColumnNameNameValidPositive() {
        assertTrue(giftCertificateValidator.isColumnNameValid(VALID_COLUMN_NAME));
    }

    @Test
    void isColumnNameNameValidNegative() {
        assertFalse(giftCertificateValidator.isColumnNameValid(INVALID_COLUMN_NAME));
    }
    @Test
    void isColumnNameDescriptionValidPositive() {
        assertTrue(giftCertificateValidator.isColumnNameValid(VALID_COLUMN_DESCRIPTION));
    }

    @Test
    void isColumnNameDescriptionValidNegative() {
        assertFalse(giftCertificateValidator.isColumnNameValid(INVALID_COLUMN_DESCRIPTION));
    }

    @Test
    void isColumnNamePriceValidPositive() {
        assertTrue(giftCertificateValidator.isColumnNameValid(VALID_COLUMN_PRICE));
    }

    @Test
    void isColumnNamePriceValidNegative() {
        assertFalse(giftCertificateValidator.isColumnNameValid(INVALID_COLUMN_PRICE));
    }

    @Test
    void isColumnNameDurationValidPositive() {
        assertTrue(giftCertificateValidator.isColumnNameValid(VALID_COLUMN_DURATION));
    }

    @Test
    void isColumnNameDurationValidNegative() {
        assertFalse(giftCertificateValidator.isColumnNameValid(INVALID_COLUMN_DURATION));
    }

    @Test
    void isColumnNameCreateDateValidPositive() {
        assertTrue(giftCertificateValidator.isColumnNameValid(VALID_COLUMN_CREATE_DATE));
    }

    @Test
    void isColumnNameCreateDateValidNegative() {
        assertFalse(giftCertificateValidator.isColumnNameValid(INVALID_COLUMN_CREATE_DATE));
    }

    @Test
    void isColumnNameLastUpdateValidPositive() {
        assertTrue(giftCertificateValidator.isColumnNameValid(VALID_COLUMN_LAST_UPDATE_DATE));
    }

    @Test
    void isColumnNameLastUpdateValidNegative() {
        assertFalse(giftCertificateValidator.isColumnNameValid(INVALID_COLUMN_LAST_UPDATE_DATE));
    }
}
