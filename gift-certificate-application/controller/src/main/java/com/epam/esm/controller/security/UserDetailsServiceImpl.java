package com.epam.esm.controller.security;

import com.epam.esm.entity.User;
import com.epam.esm.repository.dao.UserDao;
import com.epam.esm.service.exception.UserNotFoundException;
import com.epam.esm.service.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserDao userDao;

    @Autowired
    public UserDetailsServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        String exceptionMessage = String.format(Message.USER_LOGIN_NOT_FOUND_MSG, login);
        User user = userDao.findByLogin(login).orElseThrow(() ->
                new UserNotFoundException(exceptionMessage));

        return SecurityUserBuilder.build(user);
    }
}
