package com.epam.esm.repository.dao;

import com.epam.esm.entity.User;
import java.util.Optional;

/**
 * The UserDao interface provides methods for creating and reading users.
 *
 * @author Yury Lapitski
 * @version 1.0
 */
public interface UserDao extends EntityDao<User> {

    /**
     * Searches for user by login
     *
     * @param login login of the user to find
     * @return founded Optional of user
     */
    Optional<User> findByLogin(String login);
}
