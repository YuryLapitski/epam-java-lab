package com.epam.esm.controller;

import com.epam.esm.controller.hateoas.LinkBuilder;
import com.epam.esm.entity.User;
import com.epam.esm.service.pagination.CustomPagination;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/users")
public class UserController {
    private final UserService userService;
    private final LinkBuilder<User> userLinkBuilder;

    @Autowired
    public UserController(UserService userService, LinkBuilder<User> userLinkBuilder) {
        this.userService = userService;
        this.userLinkBuilder = userLinkBuilder;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('users:read') || #id.equals(authentication.principal.userId)")
    public User findById(@PathVariable Long id) {
        User user = userService.findById(id);
        userLinkBuilder.setLinks(user);

        return user;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('users:read')")
    public List<User> findAll(CustomPagination pagination) {
        List<User> userList = userService.findAll(pagination);
        userLinkBuilder.setLinks(userList);

        return userList;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('users:create')")
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        User createdUser = userService.create(user);
        userLinkBuilder.setLinks(createdUser);

        return createdUser;
    }
}
