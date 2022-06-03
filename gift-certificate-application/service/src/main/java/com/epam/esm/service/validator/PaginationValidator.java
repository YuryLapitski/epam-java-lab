package com.epam.esm.service.validator;

import com.epam.esm.service.pagination.CustomPagination;

/**
 * The PaginationValidator interface provides methods for validation of
 * pagination parameters.
 *
 * @author Yury Lapitski
 * @version 1.0
 */
public interface PaginationValidator {

    /**
     * Validates number of elements on the page.
     *
     * @param pagination pagination parameters
     * @return true if number of elements on the page is valid.
     */
    boolean isSizeValid(CustomPagination pagination);

    /**
     * Validates page number.
     *
     * @param pagination pagination parameters
     * @param lastPage Number of the last page
     * @return true if page number is valid.
     */
    boolean isPageValid(CustomPagination pagination, int lastPage);
}
