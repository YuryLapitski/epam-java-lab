package com.epam.esm.service.validator;

import java.math.BigDecimal;

/**
 * The GiftCertificateValidator interface provides method for validation of
 * gift certificate name, description, price, duration. Also it validates entered type of
 * sorting and column names.
 *
 * @author Yury Lapitski
 * @version 1.0
 */
public interface GiftCertificateValidator {

    /**
     * Validates entered gift certificate name
     *
     * @param name gift certificate name to validate
     * @return true if the gift certificate name is valid
     */
    boolean isNameValid(String name);

    /**
     * Validates entered gift certificate description
     *
     * @param description gift certificate description to validate
     * @return true if the gift certificate description is valid
     */
    boolean isDescriptionValid(String description);

    /**
     * Validates entered gift certificate price
     *
     * @param price gift certificate price to validate
     * @return true if the gift certificate price is valid
     */
    boolean isPriceValid(BigDecimal price);

    /**
     * Validates entered gift certificate duration
     *
     * @param duration gift certificate duration to validate
     * @return true if the gift certificate duration is valid
     */
    boolean isDurationValid(short duration);

    /**
     * Validates entered all of the gift certificate parameters
     *
     * @param name gift certificate name to validate
     * @param description gift certificate description to validate
     * @param price gift certificate price to validate
     * @param duration gift certificate duration to validate
     * @return true if all of the gift certificate parameters is valid
     */
    boolean validateAll(String name, String description, BigDecimal price, short duration);

    /**
     * Validates entered type of sorting
     *
     * @param sortType type of sorting to validate
     * @return true if the type of sorting is valid
     */
    boolean isSortTypeValid(String sortType);

    /**
     * Validates entered column name
     *
     * @param columnName column name to validate
     * @return true if the column name is valid
     */
    boolean isColumnNameValid(String columnName);
}
