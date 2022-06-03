package com.epam.esm.controller.exception;

import com.epam.esm.service.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;

@RestControllerAdvice
public class AppExceptionHandler {
    private static final int FIELD_VALIDATION_ERROR_CODE = 40001;
    private static final int INVALID_COLUMN_NAME_ERROR_CODE = 40002;
    private static final int INVALID_SORT_TYPE_ERROR_CODE = 40003;
    private static final int INVALID_PAGE_SIZE_ERROR_CODE = 40004;
    private static final int INVALID_PAGE_NUMBER_ERROR_CODE = 40005;
    private static final int INVALID_PAGINATION_ERROR_CODE = 40006;
    private static final int TAG_NOT_FOUND_ERROR_CODE = 40401;
    private static final int GIFT_CERTIFICATE_NOT_FOUND_ERROR_CODE = 40402;
    private static final int GIFT_CERTIFICATES_NOT_FOUND_ERROR_CODE = 40403;
    private static final int CANNOT_UPDATE_ERROR_CODE = 40404;
    private static final int USER_HAS_NO_ORDER_ERROR_CODE = 40405;
    private static final int USER_NOT_FOUND_ERROR_CODE = 40406;
    private static final int ORDER_NOT_FOUND_ERROR_CODE = 40407;
    private static final int TAG_DOES_NOT_EXIST_ERROR_CODE = 40408;
    private static final int NO_GIFT_CERTIFICATE_MATCHING_ERROR_CODE = 40409;
    private static final int GIFT_CERTIFICATE_ALREADY_EXIST_ERROR_CODE = 40901;
    private static final int TAG_ALREADY_EXIST_ERROR_CODE = 40902;
    private static final int TAG_TO_GIFT_CERTIFICATE_REFERENCE_ERROR_CODE = 40903;
    private static final int USER_ALREADY_EXIST_ERROR_CODE = 40904;
    private static final int HAS_ORDER_TO_GIFT_CERTIFICATE_ERROR_CODE = 40905;

    @ExceptionHandler(TagNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleTagNotFoundException(TagNotFoundException notFoundException) {
        return handleCommonException(notFoundException, HttpStatus.NOT_FOUND, TAG_NOT_FOUND_ERROR_CODE);
    }

    @ExceptionHandler(GiftCertificateNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleGiftCertificateNotFoundException(GiftCertificateNotFoundException notFoundException) {
        return handleCommonException(notFoundException, HttpStatus.NOT_FOUND, GIFT_CERTIFICATE_NOT_FOUND_ERROR_CODE);
    }

    @ExceptionHandler(GiftCertificatesNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleGiftCertificatesNotFoundException(GiftCertificatesNotFoundException notFoundException) {
        return handleCommonException(notFoundException, HttpStatus.NOT_FOUND, GIFT_CERTIFICATES_NOT_FOUND_ERROR_CODE);
    }

    @ExceptionHandler(FieldValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleFieldValidationException(FieldValidationException fieldValidationException) {
        return handleCommonException(fieldValidationException, HttpStatus.BAD_REQUEST, FIELD_VALIDATION_ERROR_CODE);
    }

    @ExceptionHandler(GiftCertificateAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleGiftCertificateAlreadyExistException(
            GiftCertificateAlreadyExistException alreadyExistException) {
        return handleCommonException(alreadyExistException, HttpStatus.CONFLICT,
                GIFT_CERTIFICATE_ALREADY_EXIST_ERROR_CODE);
    }

    @ExceptionHandler(TagAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleTagAlreadyExistException(TagAlreadyExistException alreadyExistException) {
        return handleCommonException(alreadyExistException, HttpStatus.CONFLICT, TAG_ALREADY_EXIST_ERROR_CODE);
    }

    @ExceptionHandler(TagToGiftCertificateReferenceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleTagToGiftCertificateReferenceException(
            TagToGiftCertificateReferenceException tagToGiftCertificateReferenceException) {
        return handleCommonException(tagToGiftCertificateReferenceException, HttpStatus.CONFLICT,
                TAG_TO_GIFT_CERTIFICATE_REFERENCE_ERROR_CODE);
    }

    @ExceptionHandler(CannotUpdateException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleCannotUpdateException(CannotUpdateException cannotUpdateException) {
        return handleCommonException(cannotUpdateException, HttpStatus.NOT_FOUND, CANNOT_UPDATE_ERROR_CODE);
    }

    @ExceptionHandler(InvalidColumnNameException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse InvalidColumnNameException(InvalidColumnNameException columnNameException) {
        return handleCommonException(columnNameException, HttpStatus.BAD_REQUEST, INVALID_COLUMN_NAME_ERROR_CODE);
    }

    @ExceptionHandler(InvalidSortTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidSortTypeException(InvalidSortTypeException sortTypeException) {
        return handleCommonException(sortTypeException, HttpStatus.BAD_REQUEST, INVALID_SORT_TYPE_ERROR_CODE);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(UserNotFoundException notFoundException) {
        return handleCommonException(notFoundException, HttpStatus.NOT_FOUND, USER_NOT_FOUND_ERROR_CODE);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleOrderNotFoundException(OrderNotFoundException notFoundException) {
        return handleCommonException(notFoundException, HttpStatus.NOT_FOUND, ORDER_NOT_FOUND_ERROR_CODE);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleUserAlreadyExistException(UserAlreadyExistException alreadyExistException) {
        return handleCommonException(alreadyExistException, HttpStatus.CONFLICT, USER_ALREADY_EXIST_ERROR_CODE);
    }

    @ExceptionHandler(UserHasNoOrdersException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserHasNoOrdersException(UserHasNoOrdersException noOrdersException) {
        return handleCommonException(noOrdersException, HttpStatus.NOT_FOUND, USER_HAS_NO_ORDER_ERROR_CODE);
    }

    @ExceptionHandler(HasOrderToGiftCertificateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleHasOrderToGiftCertificateException(
            HasOrderToGiftCertificateException hasOrderToGiftCertificateException) {
        return handleCommonException(hasOrderToGiftCertificateException, HttpStatus.CONFLICT,
                HAS_ORDER_TO_GIFT_CERTIFICATE_ERROR_CODE);
    }

    @ExceptionHandler(TagDoesNotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleTagDoesNotExistException(TagDoesNotExistException tagDoesNotExistException) {
        return handleCommonException(tagDoesNotExistException, HttpStatus.NOT_FOUND,
                TAG_DOES_NOT_EXIST_ERROR_CODE);
    }

    @ExceptionHandler(NoMatchingGiftCertificateException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoMatchingGiftCertificateException(
            NoMatchingGiftCertificateException noMatchingGiftCertificateException) {
        return handleCommonException(noMatchingGiftCertificateException, HttpStatus.NOT_FOUND,
                NO_GIFT_CERTIFICATE_MATCHING_ERROR_CODE);
    }

    @ExceptionHandler(PageSizeValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlePageSizeValidationException(
            PageSizeValidationException pageSizeValidationException) {
        return handleCommonException(pageSizeValidationException, HttpStatus.BAD_REQUEST,
                INVALID_PAGE_SIZE_ERROR_CODE);
    }

    @ExceptionHandler(PageNumberValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlePageNumberValidationException(
            PageNumberValidationException pageNumberValidationException) {
        return handleCommonException(pageNumberValidationException, HttpStatus.BAD_REQUEST,
                INVALID_PAGE_NUMBER_ERROR_CODE);
    }

    @ExceptionHandler(PaginationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlePaginationException(
            PaginationException paginationException) {
        return handleCommonException(paginationException, HttpStatus.BAD_REQUEST,
                INVALID_PAGINATION_ERROR_CODE);
    }

    private ErrorResponse handleCommonException(Exception exception, HttpStatus status, int errorCode) {
        return ErrorResponse.builder()
                .errorMessage(exception.getMessage())
                .errorStatus(status)
                .timestamp(LocalDateTime.now())
                .errorCode(errorCode)
                .build();
    }
}
