package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.User;

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
     * @return founded list of users
     */
    List<User> findAll();

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

    /**
     * Searches for users by attributes
     *
     * @param login login of the user
     * @return founded list of users
     */
    List<User> findByAttributes(String login);
}
