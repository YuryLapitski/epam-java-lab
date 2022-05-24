package com.epam.esm.service.impl;

import com.epam.esm.entity.User;
import com.epam.esm.pagination.CustomPagination;
import com.epam.esm.repository.dao.UserDao;
import com.epam.esm.repository.dao.impl.UserDaoImpl;
import com.epam.esm.service.exception.FieldValidationException;
import com.epam.esm.service.exception.UserAlreadyExistException;
import com.epam.esm.service.exception.UserNotFoundException;
import com.epam.esm.service.validator.PaginationValidator;
import com.epam.esm.service.validator.UserValidator;
import com.epam.esm.service.validator.impl.PaginationValidatorImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceImplTest {
    private static final long ID = 1L;
    private static final String FIST_NAME = "Ivan";
    private static final String LAST_NAME = "Ivanov";
    private static final String LOGIN = "I-Ivan";
    private static final int PAGE = 1;
    private static final int SIZE = 10;
    private static final Long ENTITIES_NUMBER = 1L;
    private UserDao userDao;
    private User user;
    private UserValidator userValidator;
    private CustomPagination pagination;
    private PaginationValidator paginationValidator;
    private List<User> userList;
    private UserServiceImpl userService;

    @BeforeAll
    void beforeAll() {
        userDao = mock(UserDaoImpl.class);
        user = new User();
        user.setId(ID);
        user.setFirstName(FIST_NAME);
        user.setLastName(LAST_NAME);
        user.setLogin(LOGIN);
        userValidator = mock(UserValidator.class);
        pagination = new CustomPagination();
        pagination.setPage(PAGE);
        pagination.setSize(SIZE);
        paginationValidator = mock(PaginationValidatorImpl.class);
        userList = new ArrayList<>();
        userList.add(user);
        userService = new UserServiceImpl(userDao, userValidator, paginationValidator);
    }

    @Test
    void testCreate() {
        when(userValidator.isFirstNameValid(FIST_NAME)).thenReturn(true);
        when(userValidator.isLastNameValid(LAST_NAME)).thenReturn(true);
        when(userValidator.isLoginValid(LOGIN)).thenReturn(true);
        when(userDao.findByLogin(LOGIN)).thenReturn(Optional.empty());
        when(userDao.create(user)).thenReturn(user);
        User actualResult = userService.create(user);
        assertEquals(user, actualResult);
    }

    @Test
    void testCreateShouldTrowFirstNameFieldValidationException() {
        when(userValidator.isFirstNameValid(FIST_NAME)).thenReturn(false);
        when(userDao.create(user)).thenReturn(user);
        assertThrows(FieldValidationException.class, () -> userService.create(user));
    }

    @Test
    void testCreateShouldTrowLastNameFieldValidationException() {
        when(userValidator.isFirstNameValid(FIST_NAME)).thenReturn(true);
        when(userValidator.isLastNameValid(LAST_NAME)).thenReturn(false);
        when(userDao.create(user)).thenReturn(user);
        assertThrows(FieldValidationException.class, () -> userService.create(user));
    }

    @Test
    void testCreateShouldTrowLoginFieldValidationException() {
        when(userValidator.isFirstNameValid(FIST_NAME)).thenReturn(true);
        when(userValidator.isLastNameValid(LAST_NAME)).thenReturn(true);
        when(userValidator.isLoginValid(LOGIN)).thenReturn(false);
        when(userDao.create(user)).thenReturn(user);
        assertThrows(FieldValidationException.class, () -> userService.create(user));
    }

    @Test
    void testCreateShouldTrowUserAlreadyExistException() {
        when(userValidator.isFirstNameValid(FIST_NAME)).thenReturn(true);
        when(userValidator.isLastNameValid(LAST_NAME)).thenReturn(true);
        when(userValidator.isLoginValid(LOGIN)).thenReturn(true);
        when(userDao.findByLogin(LOGIN)).thenReturn(Optional.of(user));
        when(userDao.create(user)).thenReturn(user);
        assertThrows(UserAlreadyExistException.class, () -> userService.create(user));
    }

    @Test
    void testFindAll() {
        when(userDao.findAll(pagination, User.class)).thenReturn(userList);
        when(userDao.getEntitiesNumber(User.class)).thenReturn(ENTITIES_NUMBER);
        when(paginationValidator.validatePagination(pagination, ENTITIES_NUMBER)).thenReturn(pagination);
        List<User> actualResult = userService.findAll(pagination);
        assertEquals(userList, actualResult);
    }

    @Test
    void testFindById() {
        when(userDao.findById(ID, User.class)).thenReturn(Optional.ofNullable(user));
        User actualResult = userService.findById(ID);
        assertEquals(user, actualResult);
    }

    @Test
    void testFindByIdShouldTrowUserNotFoundException() {
        when(userDao.findById(ID, User.class)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.findById(ID));
    }
}