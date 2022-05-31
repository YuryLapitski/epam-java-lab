package com.epam.esm.controller.security;

import com.epam.esm.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUserBuilder {

    public static UserDetails build(User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getLogin(), user.getPassword(),
                user.getRole().getAuthorities());
    }
}
