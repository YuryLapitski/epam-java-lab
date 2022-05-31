package com.epam.esm.service;

import com.epam.esm.entity.User;
import com.epam.esm.pagination.CustomPagination;
import java.util.List;

/**
 * The UserService interface provides methods for creating and reading users.
 *
 * @author Yury Lapitski
 * @version 1.0
 */
public interface UserService {
    /**
     * Creates new user
     *
     * @param user user to create
     * @return Tag
     */
    User create(User user);

    /**
     * Searches for all users
     *
     * @param pagination object that contains information about page.
     * @return founded list of users
     */
    List<User> findAll(CustomPagination pagination);

    /**
     * Searches for user by ID
     *
     * @param id id of the user to find
     * @return founded user
     */
    User findById(Long id);

    /**
     * Searches for user by login
     *
     * @param login login of the user to find
     * @return founded user
     */
    User findByLogin(String login);
}
