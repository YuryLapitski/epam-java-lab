package com.epam.esm.service.exception;

public class NoMatchingGiftCertificateException extends RuntimeException {
    public NoMatchingGiftCertificateException(String message) {
        super(message);
    }
}
