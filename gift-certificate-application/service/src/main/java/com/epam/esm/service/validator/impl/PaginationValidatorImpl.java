package com.epam.esm.service.validator.impl;

import com.epam.esm.pagination.CustomPagination;
import com.epam.esm.service.validator.PaginationValidator;
import org.springframework.stereotype.Component;

@Component
public class PaginationValidatorImpl implements PaginationValidator {
    private static final int FIRST_PAGE = 1;
    private static final int DEFAULT_SIZE = 10;
    private static final int MIN_SIZE = 1;

    @Override
    public CustomPagination validatePagination(CustomPagination pagination, Long entitiesNumber) {
        int pageSize = pagination.getSize();
        int page = pagination.getPage();

        if (pageSize < MIN_SIZE) {
            pagination.setSize(DEFAULT_SIZE);
        }

        int lastPage = (int) Math.ceil((double) (entitiesNumber / pageSize));
        if (page < FIRST_PAGE) {
            pagination.setPage(FIRST_PAGE);
        }
        if (page >= lastPage) {
            pagination.setPage(lastPage);
        }

        return pagination;
    }
}
