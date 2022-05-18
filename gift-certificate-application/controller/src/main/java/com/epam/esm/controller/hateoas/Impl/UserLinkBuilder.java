package com.epam.esm.controller.hateoas.Impl;

import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.UserController;
import com.epam.esm.entity.User;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class UserLinkBuilder extends AbstractLinkBuilder<User> {

    @Override
    public void setLinks(User user) {
        setIdLinks(UserController.class, user, user.getId(), SELF);
    }

    @Override
    public void setLinks(List<User> users) {
        users.forEach(user -> setIdLinks(OrderController.class, user, user.getId(), SELF, DELETE));
    }
}
