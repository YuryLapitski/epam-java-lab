package com.epam.esm.service.pagination;

import com.epam.esm.service.exception.PageNumberValidationException;
import com.epam.esm.service.exception.PageSizeValidationException;
import com.epam.esm.service.exception.PaginationException;
import com.epam.esm.service.util.Message;
import com.epam.esm.service.validator.PaginationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class PaginationConverter {
    private final PaginationValidator paginationValidator;

    @Autowired
    public PaginationConverter(PaginationValidator paginationValidator) {
        this.paginationValidator = paginationValidator;
    }

    public Pageable convert(CustomPagination pagination) throws IllegalArgumentException {
        int page = pagination.getPage();
        int size = pagination.getSize();

        if (page == 0 && size == 0) {
            throw new PaginationException(Message.CHOOSE_PAGINATION_MSG);
        }

        if(!paginationValidator.isSizeValid(pagination)) {
            throw new PageSizeValidationException(Message.PAGE_SIZE_INVALID_MSG);
        }



        try {
            return PageRequest.of(page, size);
        } catch (IllegalArgumentException e) {
            throw new PageNumberValidationException(Message.NEGATIVE_PAGE_NUMBER);
        }
    }
}
