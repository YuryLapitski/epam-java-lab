package com.epam.esm.service.validator.impl;

import com.epam.esm.service.pagination.CustomPagination;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PaginationValidatorImplTest {
    private static final int LAST_PAGE = 10;
    private PaginationValidatorImpl paginationValidator;
    private CustomPagination pagination;

    @BeforeEach
    void beforeEach() {
        paginationValidator = new PaginationValidatorImpl();
        pagination = new CustomPagination();
        pagination.setPage(1);
        pagination.setSize(10);
    }

    @Test
    void testIsSizeValid() {
        assertTrue(paginationValidator.isSizeValid(pagination));
    }

    @Test
    void testIsPageValid() {
        assertTrue(paginationValidator.isPageValid(pagination, LAST_PAGE));
    }
}