package com.epam.esm.service.validator.impl;

import com.epam.esm.pagination.CustomPagination;
import com.epam.esm.service.validator.PaginationValidator;
import org.springframework.stereotype.Component;

@Component
public class PaginationValidatorImpl implements PaginationValidator {
    private static final int FIRST_PAGE = 0;
    private static final int MIN_ELEMENTS_ON_PAGE = 1;

    @Override
    public boolean isSizeValid(CustomPagination pagination) {
        int pageSize = pagination.getSize();

        return pageSize >= MIN_ELEMENTS_ON_PAGE;
    }

    @Override
    public boolean isPageValid(CustomPagination pagination, int lastPage) {
        int page = pagination.getPage();

        return page >= FIRST_PAGE && page < lastPage;
    }
}
