package com.epam.esm.service.validator.impl;

import com.epam.esm.service.validator.TagValidator;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TagValidatorImpl implements TagValidator {
    private static final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z0-9]{2,50}");
    private static final String SPACE_REGEX = "\\s+";
    private static final String EMPTY_STRING = "";

    @Override
    public boolean isNameValid(String name) {
        if (name == null) {
            return false;
        }
        Matcher matcher = NAME_PATTERN.matcher(name.replaceAll(SPACE_REGEX, EMPTY_STRING));
        return matcher.matches();
    }
}
