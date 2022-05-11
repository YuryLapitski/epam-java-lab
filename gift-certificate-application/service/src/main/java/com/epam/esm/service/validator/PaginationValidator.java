package com.epam.esm.service.validator;

import com.epam.esm.pagination.CustomPagination;

public interface PaginationValidator {

    CustomPagination validatePagination (CustomPagination pagination, Long entitiesNumber);

}
