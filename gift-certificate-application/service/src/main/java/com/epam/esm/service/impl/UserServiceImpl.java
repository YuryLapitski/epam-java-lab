package com.epam.esm.service.impl;

import com.epam.esm.entity.User;
import com.epam.esm.repository.dao.UserRepository;
import com.epam.esm.service.exception.FieldValidationException;
import com.epam.esm.service.exception.PageNumberValidationException;
import com.epam.esm.service.exception.UserAlreadyExistException;
import com.epam.esm.service.exception.UserNotFoundException;
import com.epam.esm.service.pagination.CustomPagination;
import com.epam.esm.service.UserService;
import com.epam.esm.service.pagination.PaginationConverter;
import com.epam.esm.service.util.Message;
import com.epam.esm.service.validator.PaginationValidator;
import com.epam.esm.service.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static final int ENCODER_STRENGTH = 12;
    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final PaginationValidator paginationValidator;
    private final PaginationConverter paginationConverter;

    @Autowired
    public UserServiceImpl(UserRepository userDao,
                           UserValidator userValidator,
                           PaginationValidator paginationValidator,
                           PaginationConverter paginationConverter) {
        this.userRepository = userDao;
        this.userValidator = userValidator;
        this.paginationValidator = paginationValidator;
        this.paginationConverter = paginationConverter;
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

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(ENCODER_STRENGTH);
        String encodedPassword = encoder.encode(user.getPassword());

        user.setPassword(encodedPassword);
        user.setRole(User.Role.USER);

        if (userRepository.findByLogin(user.getLogin()).isPresent()) {
            String msg = String.format(Message.USER_ALREADY_EXIST_MSG, user.getLogin());
            throw new UserAlreadyExistException(msg);
        }

        return userRepository.save(user);
    }

    @Override
    public List<User> findAll(CustomPagination pagination) {
        Pageable pageable = paginationConverter.convert(pagination);
        Page<User> userPage = userRepository.findAll(pageable);

        int lastPage = userPage.getTotalPages();
        if(!paginationValidator.isPageValid(pagination, lastPage)) {
            String message = String.format(Message.PAGE_NUMBER_INVALID_MSG, lastPage);
            throw new PageNumberValidationException(message);
        }

        return userPage.getContent();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException(String.format(Message.USER_ID_NOT_FOUND_MSG, id)));
    }

    @Override
    public User findByLogin(String login) {
        return userRepository.findByLogin(login).orElseThrow(() ->
                new UserNotFoundException(String.format(Message.USER_LOGIN_NOT_FOUND_MSG, login)));
    }
}
