package com.epam.esm.service.exception;

public class PageSizeValidationException extends RuntimeException {
    public PageSizeValidationException(String message) {
        super(message);
    }
}
