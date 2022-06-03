package com.epam.esm.repository.dao;

import com.epam.esm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The UserDao interface provides methods for creating and reading users.
 *
 * @author Yury Lapitski
 * @version 1.0
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Searches for user by login
     *
     * @param login login of the user to find
     * @return founded Optional of user
     */
    Optional<User> findByLogin(String login);
}
