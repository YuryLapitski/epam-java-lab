package com.epam.esm.service.validator;

import com.epam.esm.pagination.CustomPagination;

/**
 * The PaginationValidator interface provides method for validation of
 * pagination parameters.
 *
 * @author Yury Lapitski
 * @version 1.0
 */
public interface PaginationValidator {

    /**
     * Validates pagination parameters
     *
     * @param pagination pagination parameters
     * @param entitiesNumber number of found entities
     * @return CustomPagination object
     */
    CustomPagination validatePagination (CustomPagination pagination, Long entitiesNumber);

}
