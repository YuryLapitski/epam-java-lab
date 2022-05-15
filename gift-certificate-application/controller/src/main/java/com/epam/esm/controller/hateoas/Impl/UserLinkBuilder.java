package com.epam.esm.controller.hateoas.Impl;

import com.epam.esm.controller.UserController;
import com.epam.esm.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserLinkBuilder extends AbstractLinkBuilder<User> {

    @Override
    public void setLinks(User user) {
        setIdLinks(UserController.class, user, user.getId(), SELF);
    }
}
