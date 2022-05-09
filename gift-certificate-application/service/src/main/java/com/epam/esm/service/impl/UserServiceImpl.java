package com.epam.esm.service.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.User;
import com.epam.esm.repository.dao.UserDao;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static final String USER_ID_NOT_FOUND_MSG = "User with id=%d not found.";
    private static final String USER_LOGIN_NOT_FOUND_MSG = "User with name '%s' not found.";
    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional
    @Override
    public User create(User user) {
        return userDao.create(user);
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public User findById(Long id) {
        return userDao.findById(id).orElseThrow(() ->
                new UserNotFoundException(String.format(USER_ID_NOT_FOUND_MSG, id)));
    }

    @Override
    public User findByLogin(String login) {
        return userDao.findByLogin(login).orElseThrow(() ->
                new UserNotFoundException(String.format(USER_LOGIN_NOT_FOUND_MSG, login)));
    }

    @Override
    public List<User> findByAttributes(String login) {
        List<User> userList = new ArrayList<>();
        if (login == null) {
            userList = findAll();
        } else {
            userList.add(findByLogin(login));
        }

        return userList;
    }
}
