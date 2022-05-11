package com.epam.esm.repository.dao;

import com.epam.esm.entity.User;
import com.epam.esm.pagination.CustomPagination;

import java.util.List;
import java.util.Optional;

/**
 * The UserDao interface provides methods for creating and reading users.
 *
 * @author Yury Lapitski
 * @version 1.0
 */
public interface UserDao {
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
     * @return founded Optional of user
     */
    Optional<User> findById(Long id);

    /**
     * Searches for user by login
     *
     * @param login login of the user to find
     * @return founded Optional of user
     */
    Optional<User> findByLogin(String login);

    Long findUsersNumber();
}
