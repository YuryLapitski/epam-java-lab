package com.epam.esm.controller.security;

import com.epam.esm.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUserBuilder {
    private static final boolean IS_ACTIVE = true;

    public static UserDetails build(User user) {
        return new SecurityUser(user.getId(),
                user.getLogin(), user.getPassword(),
                user.getRole().getAuthorities(), IS_ACTIVE);
    }
}
