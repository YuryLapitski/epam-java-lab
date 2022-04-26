package com.epam.esm.service.exception;

public class InvalidColumnNameException extends RuntimeException {
    public InvalidColumnNameException(String message) {
        super(message);
    }
}
