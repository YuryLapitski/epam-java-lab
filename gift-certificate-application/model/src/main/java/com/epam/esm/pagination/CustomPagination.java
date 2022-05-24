package com.epam.esm.pagination;

import lombok.Data;

@Data
public class CustomPagination {
    private static final int DEFAULT_COUNT_ELEMENTS_ON_PAGE = 10;
    private static final int DEFAULT_PAGE = 0;

    private int size = DEFAULT_COUNT_ELEMENTS_ON_PAGE;
    private int page = DEFAULT_PAGE;

    public void setSize(int size) {
        this.size = size > 0 ? size : DEFAULT_COUNT_ELEMENTS_ON_PAGE;
    }

    public void setPage(int page) {
        this.page = page > 0 ? page - 1 : DEFAULT_PAGE;
    }
}
