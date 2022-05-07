package com.epam.esm.service.exception;

public class TagAlreadyExistException extends RuntimeException {
    public TagAlreadyExistException(String message) {
        super(message);
    }
}
