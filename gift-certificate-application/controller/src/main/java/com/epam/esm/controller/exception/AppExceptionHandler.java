package com.epam.esm.controller.exception;

import com.epam.esm.service.exception.CannotUpdateException;
import com.epam.esm.service.exception.FieldValidationException;
import com.epam.esm.service.exception.GiftCertificateAlreadyExistException;
import com.epam.esm.service.exception.GiftCertificateNotFoundException;
import com.epam.esm.service.exception.GiftCertificatesNotFoundException;
import com.epam.esm.service.exception.InvalidColumnNameException;
import com.epam.esm.service.exception.InvalidSortTypeException;
import com.epam.esm.service.exception.TagNotFoundException;
import com.epam.esm.service.exception.TagToGiftCertificateReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static java.time.LocalDateTime.now;

@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(TagNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleTagNotFoundException(TagNotFoundException notFoundException) {
        return ErrorResponse.builder()
                .errorMessage(notFoundException.getMessage())
                .errorStatus(HttpStatus.NOT_FOUND)
                .timestamp(now())
                .errorCode(40401)
                .build();
    }

    @ExceptionHandler(GiftCertificateNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleGiftCertificateNotFoundException(GiftCertificateNotFoundException notFoundException) {
        return ErrorResponse.builder()
                .errorMessage(notFoundException.getMessage())
                .errorStatus(HttpStatus.NOT_FOUND)
                .timestamp(now())
                .errorCode(40402)
                .build();
    }

    @ExceptionHandler(GiftCertificatesNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleGiftCertificatesNotFoundException(GiftCertificatesNotFoundException notFoundException) {
        return ErrorResponse.builder()
                .errorMessage(notFoundException.getMessage())
                .errorStatus(HttpStatus.NOT_FOUND)
                .timestamp(now())
                .errorCode(40403)
                .build();
    }

    @ExceptionHandler(FieldValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleFieldValidationException(FieldValidationException fieldValidationException) {
        return ErrorResponse.builder()
                .errorMessage(fieldValidationException.getMessage())
                .errorStatus(HttpStatus.BAD_REQUEST)
                .timestamp(now())
                .errorCode(40001)
                .build();
    }

    @ExceptionHandler(GiftCertificateAlreadyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleGiftCertificateAlreadyExistException(
            GiftCertificateAlreadyExistException alreadyExistException) {
        return ErrorResponse.builder()
                .errorMessage(alreadyExistException.getMessage())
                .errorStatus(HttpStatus.BAD_REQUEST)
                .timestamp(now())
                .errorCode(40002)
                .build();
    }

    @ExceptionHandler(TagToGiftCertificateReferenceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleTagToGiftCertificateReferenceException(
            TagToGiftCertificateReferenceException tagToGiftCertificateReferenceException) {
        return ErrorResponse.builder()
                .errorMessage(tagToGiftCertificateReferenceException.getMessage())
                .errorStatus(HttpStatus.BAD_REQUEST)
                .timestamp(now())
                .errorCode(40003)
                .build();
    }

    @ExceptionHandler(CannotUpdateException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleCannotUpdateException(CannotUpdateException cannotUpdateException) {
        return ErrorResponse.builder()
                .errorMessage(cannotUpdateException.getMessage())
                .errorStatus(HttpStatus.NOT_FOUND)
                .timestamp(now())
                .errorCode(40404)
                .build();
    }

    @ExceptionHandler(InvalidColumnNameException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleCannotUpdateException(InvalidColumnNameException columnNameException) {
        return ErrorResponse.builder()
                .errorMessage(columnNameException.getMessage())
                .errorStatus(HttpStatus.BAD_REQUEST)
                .timestamp(now())
                .errorCode(40004)
                .build();
    }

    @ExceptionHandler(InvalidSortTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleCannotUpdateException(InvalidSortTypeException sortTypeException) {
        return ErrorResponse.builder()
                .errorMessage(sortTypeException.getMessage())
                .errorStatus(HttpStatus.BAD_REQUEST)
                .timestamp(now())
                .errorCode(40005)
                .build();
    }
}
