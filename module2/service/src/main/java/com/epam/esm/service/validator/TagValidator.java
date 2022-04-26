package com.epam.esm.service.validator;

/**
 * The TagValidator interface provides method for tag name validation,
 *
 * @author Yury Lapitski
 * @version 1.0
 */
public interface TagValidator {

    /**
     * Validates entered tag name
     *
     * @param name tag name to validate
     * @return true if the tag name is valid
     */
    boolean isNameValid(String name);
}
