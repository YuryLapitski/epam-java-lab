package com.epam.esm.service.validator.impl;

import com.epam.esm.pagination.CustomPagination;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PaginationValidatorImplTest {

    @Test
    void testValidatePagination() {
        PaginationValidatorImpl paginationValidator = new PaginationValidatorImpl();
        CustomPagination pagination = new CustomPagination();
        pagination.setPage(1);
        pagination.setSize(10);
        Long entitiesNumber = 15L;
        CustomPagination actualResult = paginationValidator.validatePagination(pagination, entitiesNumber);
        assertEquals(pagination, actualResult);
    }
}