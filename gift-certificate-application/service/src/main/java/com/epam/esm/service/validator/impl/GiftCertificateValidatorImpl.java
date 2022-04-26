package com.epam.esm.service.validator.impl;

import com.epam.esm.service.validator.GiftCertificateValidator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class GiftCertificateValidatorImpl implements GiftCertificateValidator {
    private static final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z0-9]{2,50}");
    private static final Pattern DESCRIPTION_PATTERN = Pattern.compile("[a-zA-Z0-9.,!?&-]{2,100}");
    private static final BigDecimal MIN_PRICE = BigDecimal.valueOf(0.0);
    private static final BigDecimal MAX_PRICE = BigDecimal.valueOf(9999.99);
    private static final short MIN_DURATION = 0;
    private static final short MAX_DURATION = 365;
    private static final String SPACE_REGEX = "\\s+";
    private static final String EMPTY_STRING = "";
    private static final String SORT_ASCENDING = "asc";
    private static final String SORT_DESCENDING = "desc";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_DURATION = "duration";
    private static final String COLUMN_CREATE_DATE = "create_date";
    private static final String COLUMN_LAST_UPDATE_DATE = "last_update_date";

    @Override
    public boolean isNameValid(String name) {
        Matcher matcher = NAME_PATTERN.matcher(name.replaceAll(SPACE_REGEX, EMPTY_STRING));
        return matcher.matches();
    }

    @Override
    public boolean isDescriptionValid(String description) {
        Matcher matcher = DESCRIPTION_PATTERN.matcher(description.replaceAll(SPACE_REGEX, EMPTY_STRING));
        return matcher.matches();
    }

    @Override
    public boolean isPriceValid(BigDecimal price) {
        return price.compareTo(MIN_PRICE) >= 1 && price.compareTo(MAX_PRICE) <= 0;
    }

    @Override
    public boolean isDurationValid(short duration) {
        return duration >= MIN_DURATION && duration <= MAX_DURATION;
    }

    public boolean validateAll(String name, String description, BigDecimal price, short duration){
        return isNameValid(name)
                && isDescriptionValid(description)
                && isPriceValid(price)
                && isDurationValid(duration);
    }

    @Override
    public boolean isSortTypeValid(String sortType) {
        return sortType.equalsIgnoreCase(SORT_ASCENDING) || sortType.equalsIgnoreCase(SORT_DESCENDING);
    }

    @Override
    public boolean isColumnNameValid(String columnName) {
        return columnName.equals(COLUMN_NAME) || columnName.equals(COLUMN_DESCRIPTION)
                || columnName.equals(COLUMN_PRICE) || columnName.equals(COLUMN_DURATION)
                || columnName.equals(COLUMN_CREATE_DATE) || columnName.equals(COLUMN_LAST_UPDATE_DATE);
    }
}
