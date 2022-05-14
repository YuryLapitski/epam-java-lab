package com.epam.esm.service.exception;

public class TagDoesNotExistException extends RuntimeException {
    public TagDoesNotExistException(String message) {
        super(message);
    }
}
