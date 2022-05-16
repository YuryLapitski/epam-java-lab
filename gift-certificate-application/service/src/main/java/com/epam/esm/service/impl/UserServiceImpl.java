package com.epam.esm.service.impl;

import com.epam.esm.entity.User;
import com.epam.esm.repository.dao.OrderDao;
import com.epam.esm.repository.dao.UserDao;
import com.epam.esm.pagination.CustomPagination;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exception.FieldValidationException;
import com.epam.esm.service.exception.UserAlreadyExistException;
import com.epam.esm.service.exception.UserNotFoundException;
import com.epam.esm.service.validator.PaginationValidator;
import com.epam.esm.service.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static final String USER_ID_NOT_FOUND_MSG = "User with id=%d not found.";
    private static final String INVALID_FIRST_NAME_MSG = "Invalid first name";
    private static final String INVALID_LAST_NAME_MSG = "Invalid last name";
    private static final String INVALID_LOGIN_MSG = "Invalid login name";
    private static final String USER_ALREADY_EXIST_MSG = "User with the login '%s' already exists";
    private final UserDao userDao;
    private final UserValidator userValidator;
    private final PaginationValidator paginationValidator;

    @Autowired
    public UserServiceImpl(UserDao userDao, UserValidator userValidator, PaginationValidator paginationValidator) {
        this.userDao = userDao;
        this.userValidator = userValidator;
        this.paginationValidator = paginationValidator;
    }

    @Transactional
    @Override
    public User create(User user) {
        if (!userValidator.isFirstNameValid(user.getFirstName())) {
            throw new FieldValidationException(INVALID_FIRST_NAME_MSG);
        }

        if (!userValidator.isLastNameValid(user.getLastName())) {
            throw new FieldValidationException(INVALID_LAST_NAME_MSG);
        }

        if (!userValidator.isLoginValid(user.getLogin())) {
            throw new FieldValidationException(INVALID_LOGIN_MSG);
        }

        if (userDao.findByLogin(user.getLogin()).isPresent()) {
            String msg = String.format(USER_ALREADY_EXIST_MSG, user.getLogin());
            throw new UserAlreadyExistException(msg);
        }

        return userDao.create(user);
    }

    @Override
    public List<User> findAll(CustomPagination pagination) {
        Long usersNumber = userDao.findEntitiesNumber(User.class);
        pagination = paginationValidator.validatePagination(pagination, usersNumber);

        return userDao.findAll(pagination, User.class);
    }

    @Override
    public User findById(Long id) {
        return userDao.findById(id, User.class).orElseThrow(() ->
                new UserNotFoundException(String.format(USER_ID_NOT_FOUND_MSG, id)));
    }
}
