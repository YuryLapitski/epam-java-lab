package com.epam.esm.service.validator.impl;

import com.epam.esm.service.validator.UserValidator;
import org.springframework.stereotype.Component;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UserValidatorImpl implements UserValidator {
    private static final Pattern FIRST_NAME_PATTERN = Pattern.compile("[a-zA-Z]{2,50}");
    private static final Pattern LAST_NAME_PATTERN = Pattern.compile("[a-zA-Z]{2,50}");
    private static final Pattern LOGIN_PATTERN = Pattern.compile("[A-Za-z0-9._-]{2,50}");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("[A-Za-z0-9._-]{2,50}");
    private static final String SPACE_REGEX = "\\s+";
    private static final String EMPTY_STRING = "";

    @Override
    public boolean isFirstNameValid(String firstName) {
        return isParamValid(firstName, FIRST_NAME_PATTERN);
    }

    @Override
    public boolean isLastNameValid(String lastName) {
        return isParamValid(lastName, LAST_NAME_PATTERN);
    }

    @Override
    public boolean isLoginValid(String login) {
        return isParamValid(login, LOGIN_PATTERN);
    }

    @Override
    public boolean isPasswordValid(String password) {
        return isParamValid(password, PASSWORD_PATTERN);
    }

    private boolean isParamValid(String param, Pattern pattern) {
        if (param == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(param.replaceAll(SPACE_REGEX, EMPTY_STRING));
        return matcher.matches();
    }
}
