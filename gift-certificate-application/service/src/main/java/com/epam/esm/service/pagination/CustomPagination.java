package com.epam.esm.service.pagination;

import lombok.Data;

@Data
public class CustomPagination {
    private static final int SHIFT_NUMBER_BY_ONE = 1;
    private int size;
    private int page;

    public void setPage(int page) {
        this.page = page - SHIFT_NUMBER_BY_ONE;
    }
}
