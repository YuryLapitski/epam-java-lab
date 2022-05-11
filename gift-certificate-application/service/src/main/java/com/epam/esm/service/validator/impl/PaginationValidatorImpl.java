package com.epam.esm.service.validator.impl;

import com.epam.esm.pagination.CustomPagination;
import com.epam.esm.service.validator.PaginationValidator;
import org.springframework.stereotype.Component;

@Component
public class PaginationValidatorImpl implements PaginationValidator {
    private static final Integer FIRST_PAGE = 1;
    private static final Integer DEFAULT_SIZE = 10;
    private static final Integer MIN_SIZE = 1;

    @Override
    public CustomPagination validatePagination(CustomPagination pagination, Long entitiesNumber) {
        if (pagination.getSize() < MIN_SIZE) {
            pagination.setSize(DEFAULT_SIZE);
        }

        int lastPage = (int) Math.ceil((double) entitiesNumber / pagination.getSize());
        if (pagination.getPage() < FIRST_PAGE) {
            pagination.setPage(FIRST_PAGE);
        }
        if (pagination.getPage() >= lastPage) {
            pagination.setPage(lastPage);
        }

        return pagination;
    }
}
