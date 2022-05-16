package com.epam.esm.service.validator.impl;

import com.epam.esm.service.validator.UserValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserValidatorImplTest {
    private UserValidatorImpl userValidator;

    @BeforeAll
    void init() {
        userValidator = new UserValidatorImpl();
    }

    @Test
    void isFirstNameValidPositive() {
        String name = "Name";
        assertTrue(userValidator.isFirstNameValid(name));
    }

    @Test
    void isFirstNameValidNegative() {
        String name = "!@#$%^";
        assertFalse(userValidator.isFirstNameValid(name));
    }

    @Test
    void isLastNameValidPositive() {
        String name = "Name";
        assertTrue(userValidator.isLastNameValid(name));
    }

    @Test
    void isLastNameValidNegative() {
        String name = "!@#$%^";
        assertFalse(userValidator.isLastNameValid(name));
    }

    @Test
    void isLoginValidPositive() {
        String name = "Name";
        assertTrue(userValidator.isLoginValid(name));
    }

    @Test
    void isLoginValidNegative() {
        String name = "!@#$%^";
        assertFalse(userValidator.isLoginValid(name));
    }
}

