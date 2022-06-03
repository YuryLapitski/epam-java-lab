package com.epam.esm.repository.dao.impl;

import com.epam.esm.entity.User;
import com.epam.esm.repository.config.TestConfig;
import com.epam.esm.repository.dao.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {TestConfig.class})
@ExtendWith(SpringExtension.class)
@Transactional
@ActiveProfiles("dev")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDaoImplTest {
    private static final String CREATED_USER_FIRST_NAME = "Ivan";
    private static final String CREATED_USER_LAST_NAME = "Sidorov";
    private static final String CREATED_LOGIN_NAME = "ISid";
    private static final String LOGIN = "SmithT";
    private static final Long USER_ID = 1L;
    private static final int EXPECTED_LIST_SIZE = 3;
    private final UserRepository userDao;
//    private CustomPagination pagination;

    @Autowired
    public UserDaoImplTest(UserRepository userDao) {
        this.userDao = userDao;
    }

    @BeforeAll
    void beforeAll() {
//        pagination = new CustomPagination();
//        pagination.setPage(1);
//        pagination.setSize(10);
    }

//    @Test
//    void create() {
//        User user = new User();
//        user.setFirstName(CREATED_USER_FIRST_NAME);
//        user.setLastName(CREATED_USER_LAST_NAME);
//        user.setLogin(CREATED_LOGIN_NAME);
//        User actual = userDao.create(user);
//        assertEquals(user, actual);
//    }
//
//    @Test
//    void findAll() {
//        List<User> users = userDao.findAll(pagination, User.class);
//        assertEquals(EXPECTED_LIST_SIZE, users.size());
//    }

//    @Test
//    void findById() {
//        Optional<User> optionalUser = userDao.findById(USER_ID, User.class);
//        assertTrue(optionalUser.isPresent());
//    }

    @Test
    void findByLogin() {
        Optional<User> optionalUser = userDao.findByLogin(LOGIN);
        assertTrue(optionalUser.isPresent());
    }
}


