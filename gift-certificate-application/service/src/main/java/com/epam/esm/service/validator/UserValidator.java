package com.epam.esm.service.validator;

/**
 * The UserValidator interface provides method for validation of user parameters,
 *
 * @author Yury Lapitski
 * @version 1.0
 */
public interface UserValidator {

    /**
     * Validates entered user first name
     *
     * @param firstName user first name to validate
     * @return true if the user first name is valid
     */
    boolean isFirstNameValid(String firstName);

    /**
     * Validates entered user last name
     *
     * @param lastName user last name to validate
     * @return true if the user last name is valid
     */
    boolean isLastNameValid(String lastName);

    /**
     * Validates entered user login
     *
     * @param login user login to validate
     * @return true if the user login is valid
     */
    boolean isLoginValid(String login);
}
