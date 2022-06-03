package com.epam.esm.service.impl;

import com.epam.esm.entity.User;
import com.epam.esm.service.pagination.CustomPagination;
import com.epam.esm.repository.dao.UserRepository;
import com.epam.esm.service.exception.FieldValidationException;
import com.epam.esm.service.exception.UserAlreadyExistException;
import com.epam.esm.service.exception.UserNotFoundException;
import com.epam.esm.service.pagination.PaginationConverter;
import com.epam.esm.service.validator.PaginationValidator;
import com.epam.esm.service.validator.UserValidator;
import com.epam.esm.service.validator.impl.PaginationValidatorImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private static final int LAST_PAGE = 1;
    private UserRepository userRepository;
    private User user;
    private UserValidator userValidator;
    private CustomPagination pagination;
    private Pageable pageable;
    private PaginationValidator paginationValidator;
    private List<User> userList;
    private Page<User> userPage;
    private UserServiceImpl userService;
    private PaginationConverter paginationConverter;

    @BeforeAll
    void beforeAll() {
        userRepository = mock(UserRepository.class);
        user = new User();
        user.setId(ID);
        user.setFirstName(FIST_NAME);
        user.setLastName(LAST_NAME);
        user.setLogin(LOGIN);
        userValidator = mock(UserValidator.class);
        paginationConverter = mock(PaginationConverter.class);
        pagination = new CustomPagination();
        pagination.setPage(PAGE);
        pagination.setSize(SIZE);
        pageable = PageRequest.of(PAGE, SIZE);
        paginationValidator = mock(PaginationValidatorImpl.class);
        userList = new ArrayList<>();
        userList.add(user);
        userPage = new PageImpl<>(userList);
        userService = new UserServiceImpl(userRepository, userValidator, paginationValidator, paginationConverter);
    }

    @Test
    void testCreate() {
        when(userValidator.isFirstNameValid(FIST_NAME)).thenReturn(true);
        when(userValidator.isLastNameValid(LAST_NAME)).thenReturn(true);
        when(userValidator.isLoginValid(LOGIN)).thenReturn(true);
        when(userRepository.findByLogin(LOGIN)).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);
        User actualResult = userService.create(user);
        assertEquals(user, actualResult);
    }

    @Test
    void testCreateShouldTrowFirstNameFieldValidationException() {
        when(userValidator.isFirstNameValid(FIST_NAME)).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);
        assertThrows(FieldValidationException.class, () -> userService.create(user));
    }

    @Test
    void testCreateShouldTrowLastNameFieldValidationException() {
        when(userValidator.isFirstNameValid(FIST_NAME)).thenReturn(true);
        when(userValidator.isLastNameValid(LAST_NAME)).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);
        assertThrows(FieldValidationException.class, () -> userService.create(user));
    }

    @Test
    void testCreateShouldTrowLoginFieldValidationException() {
        when(userValidator.isFirstNameValid(FIST_NAME)).thenReturn(true);
        when(userValidator.isLastNameValid(LAST_NAME)).thenReturn(true);
        when(userValidator.isLoginValid(LOGIN)).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);
        assertThrows(FieldValidationException.class, () -> userService.create(user));
    }

    @Test
    void testCreateShouldTrowUserAlreadyExistException() {
        when(userValidator.isFirstNameValid(FIST_NAME)).thenReturn(true);
        when(userValidator.isLastNameValid(LAST_NAME)).thenReturn(true);
        when(userValidator.isLoginValid(LOGIN)).thenReturn(true);
        when(userRepository.findByLogin(LOGIN)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        assertThrows(UserAlreadyExistException.class, () -> userService.create(user));
    }

    @Test
    void testFindAll() {
        when(userRepository.findAll(pageable)).thenReturn(userPage);
        when(paginationConverter.convert(pagination)).thenReturn(pageable);
        when(paginationValidator.isSizeValid(pagination)).thenReturn(true);
        when(paginationValidator.isPageValid(pagination, LAST_PAGE)).thenReturn(true);
        List<User> actualResult = userService.findAll(pagination);
        assertEquals(userList, actualResult);
    }

    @Test
    void testFindById() {
        when(userRepository.findById(ID)).thenReturn(Optional.ofNullable(user));
        User actualResult = userService.findById(ID);
        assertEquals(user, actualResult);
    }

    @Test
    void testFindByIdShouldTrowUserNotFoundException() {
        when(userRepository.findById(ID)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.findById(ID));
    }
}