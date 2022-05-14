package com.epam.esm.service.exception;

public class UserHasNoOrdersException extends RuntimeException {
    public UserHasNoOrdersException(String message) {
        super(message);
    }
}
