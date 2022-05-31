package com.epam.esm.service.impl;

import com.epam.esm.entity.User;
import com.epam.esm.repository.dao.UserDao;
import com.epam.esm.pagination.CustomPagination;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exception.*;
import com.epam.esm.service.util.Message;
import com.epam.esm.service.validator.PaginationValidator;
import com.epam.esm.service.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
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
            throw new FieldValidationException(Message.INVALID_FIRST_NAME_MSG);
        }

        if (!userValidator.isLastNameValid(user.getLastName())) {
            throw new FieldValidationException(Message.INVALID_LAST_NAME_MSG);
        }

        if (!userValidator.isLoginValid(user.getLogin())) {
            throw new FieldValidationException(Message.INVALID_LOGIN_MSG);
        }

        if (userDao.findByLogin(user.getLogin()).isPresent()) {
            String msg = String.format(Message.USER_ALREADY_EXIST_MSG, user.getLogin());
            throw new UserAlreadyExistException(msg);
        }

        return userDao.create(user);
    }

    @Override
    public List<User> findAll(CustomPagination pagination) {
        if (!paginationValidator.isSizeValid(pagination)) {
            throw new PageSizeValidationException(Message.PAGE_SIZE_INVALID_MSG);
        }

        Long usersNumber = userDao.getEntitiesNumber(User.class);
        int lastPage = (int) Math.ceil((double) usersNumber / pagination.getSize());

        if (!paginationValidator.isPageValid(pagination, lastPage)) {
            throw new PageNumberValidationException(String.format(Message.PAGE_NUMBER_INVALID_MSG, lastPage));
        }

        return userDao.findAll(pagination, User.class);
    }

    @Override
    public User findById(Long id) {
        return userDao.findById(id, User.class).orElseThrow(() ->
                new UserNotFoundException(String.format(Message.USER_ID_NOT_FOUND_MSG, id)));
    }

    @Override
    public User findByLogin(String login) {
        return userDao.findByLogin(login).orElseThrow(() ->
                new UserNotFoundException(String.format(Message.USER_LOGIN_NOT_FOUND_MSG, login)));
    }
}
