package com.epam.esm.pagination;

import lombok.Data;

import java.util.Objects;

@Data
public class CustomPagination {
    private static final Integer DEFAULT_SIZE = 10;
    private static final Integer DEFAULT_PAGE = 0;

    private Integer size = DEFAULT_SIZE;
    private Integer page = DEFAULT_PAGE;

    public void setSize(Integer size) {
        this.size = Objects.nonNull(size) ? size : DEFAULT_SIZE;;
    }

    public void setPage(Integer page) {
        this.page = Objects.nonNull(page) ? page - 1 : DEFAULT_PAGE;
    }
}
