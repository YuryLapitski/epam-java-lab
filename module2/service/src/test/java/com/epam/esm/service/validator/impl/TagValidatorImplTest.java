package com.epam.esm.service.validator.impl;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TagValidatorImplTest {
    TagValidatorImpl tagValidator;

    @BeforeAll
    void init() {
        tagValidator = new TagValidatorImpl();
    }

    @Test
    void isNameValidPositive() {
        String name = "name";
        assertTrue(tagValidator.isNameValid(name));
    }

    @Test
    void isNameValidNegative() {
        String name = "@#$%^&*";
        assertFalse(tagValidator.isNameValid(name));
    }

    @Test
    void isNameNull() {
        assertFalse(tagValidator.isNameValid(null));
    }
}
