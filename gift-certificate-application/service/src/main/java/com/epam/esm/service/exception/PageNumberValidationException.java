package com.epam.esm.service.exception;

public class PageNumberValidationException extends RuntimeException {
    public PageNumberValidationException(String message) {
        super(message);
    }
}
