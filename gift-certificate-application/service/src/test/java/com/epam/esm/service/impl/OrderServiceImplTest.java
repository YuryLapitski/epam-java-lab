package com.epam.esm.service.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.pagination.CustomPagination;
import com.epam.esm.repository.dao.OrderDao;
import com.epam.esm.repository.dao.UserDao;
import com.epam.esm.repository.dao.impl.OrderDaoImpl;
import com.epam.esm.repository.dao.impl.UserDaoImpl;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.exception.OrderNotFoundException;
import com.epam.esm.service.exception.UserHasNoOrdersException;
import com.epam.esm.service.validator.PaginationValidator;
import com.epam.esm.service.validator.impl.PaginationValidatorImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderServiceImplTest {
    private static final long ID = 1L;
    private static final long USER_ID = 1L;
    private static final String FIST_NAME = "Ivan";
    private static final String LAST_NAME = "Ivanov";
    private static final String LOGIN = "I-Ivan";
    private static final String NAME = "Gift certificate";
    private static final String DESCRIPTION = "description";
    private static final BigDecimal PRICE = BigDecimal.valueOf(99.99);
    private static final short DURATION = 100;
    private static final Long GIFT_CERTIFICATE_ID = 1L;
    private static final Long TAG_ID = 1L;
    private static final String TAG_NAME = "Tag";
    private static final int PAGE = 1;
    private static final int SIZE = 10;
    private static final Long ENTITIES_NUMBER = 1L;
    private OrderDao orderDao;
    private UserDao userDao;
    private OrderService orderService;
    private Order order;
    private User user;
    private GiftCertificate giftCertificate;
    private OrderDto orderDto;
    private PaginationValidator paginationValidator;
    private UserService userService;
    private GiftCertificateService giftCertificateService;
    private List<Order> orderList;
    private CustomPagination pagination;

    @BeforeAll
    void beforeAll() {
        orderDao = mock(OrderDaoImpl.class);
        userDao = mock(UserDaoImpl.class);
        paginationValidator = mock(PaginationValidatorImpl.class);
        userService = mock(UserServiceImpl.class);
        giftCertificateService = mock(GiftCertificateServiceImpl.class);
        paginationValidator = mock(PaginationValidatorImpl.class);

        user = new User();
        user.setId(USER_ID);
        user.setFirstName(FIST_NAME);
        user.setLastName(LAST_NAME);
        user.setLogin(LOGIN);

        Tag tag = new Tag();
        tag.setId(TAG_ID);
        tag.setName(TAG_NAME);
        List<Tag> tagList = new ArrayList<>();
        tagList.add(tag);

        giftCertificate = new GiftCertificate();
        giftCertificate.setId(GIFT_CERTIFICATE_ID);
        giftCertificate.setName(NAME);
        giftCertificate.setDescription(DESCRIPTION);
        giftCertificate.setPrice(PRICE);
        giftCertificate.setDuration(DURATION);
        giftCertificate.setTagList(tagList);

        order = new Order();
        order.setId(ID);
        order.setUser(user);
        order.setGiftCertificate(giftCertificate);
        order.setPrice(giftCertificate.getPrice());
        order.setCreateDate(LocalDateTime.now());

        orderDto = new OrderDto();
        orderDto.setUserId(USER_ID);
        orderDto.setGiftCertificateId(GIFT_CERTIFICATE_ID);

        orderList = new ArrayList<>();
        orderList.add(order);

        pagination = new CustomPagination();
        pagination.setPage(PAGE);
        pagination.setSize(SIZE);

        orderService = new OrderServiceImpl(orderDao, userDao, userService, giftCertificateService, paginationValidator);
    }

    @Test
    void testFindAll() {
        when(orderDao.findAll(pagination, Order.class)).thenReturn(orderList);
        when(orderDao.getEntitiesNumber(Order.class)).thenReturn(ENTITIES_NUMBER);
        when(paginationValidator.validatePagination(pagination, ENTITIES_NUMBER)).thenReturn(pagination);
        List<Order> actualResult = orderService.findAll(pagination);
        assertEquals(orderList, actualResult);
    }

    @Test
    void testFindById() {
        when(orderDao.findById(ID, Order.class)).thenReturn(Optional.ofNullable(order));
        Order actualResult = orderService.findById(ID);
        assertEquals(order, actualResult);
    }

    @Test
    void testFindByIdShouldTrowOrderNotFoundException() {
        when(orderDao.findById(ID, Order.class)).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () -> orderService.findById(ID));
    }

    @Test
    void testFindByUserId() {
        when(userDao.findById(USER_ID, User.class)).thenReturn(Optional.ofNullable(user));
        when(orderDao.findByUserId(USER_ID, pagination)).thenReturn(orderList);
        when(orderDao.findUserOrdersNumber(USER_ID)).thenReturn(ENTITIES_NUMBER);
        when(paginationValidator.validatePagination(pagination, ENTITIES_NUMBER)).thenReturn(pagination);
        when(orderDao.findByUserId(USER_ID, pagination)).thenReturn(orderList);
        List<Order> actualResult = orderService.findByUserId(USER_ID, pagination);
        assertEquals(orderList, actualResult);
    }

    @Test
    void testFindByUserIdShouldTrowUserHasNoOrdersException() {
        when(userDao.findById(USER_ID, User.class)).thenReturn(Optional.ofNullable(user));
        assertThrows(UserHasNoOrdersException.class, () -> orderService.findByUserId(USER_ID, pagination));
    }

    @Test
    void testFindByAttributes() {
        when(userDao.findById(USER_ID, User.class)).thenReturn(Optional.ofNullable(user));
        when(orderDao.getEntitiesNumber(Order.class)).thenReturn(ENTITIES_NUMBER);
        when(orderDao.findUserOrdersNumber(USER_ID)).thenReturn(ENTITIES_NUMBER);
        when(orderDao.findByUserId(USER_ID, pagination)).thenReturn(orderList);
        when(orderService.findByUserId(USER_ID, pagination)).thenReturn(orderList);
        when(orderService.findAll(pagination)).thenReturn(orderList);
        when(paginationValidator.validatePagination(pagination, ENTITIES_NUMBER)).thenReturn(pagination);
        List<Order> actualResult = orderService.findByAttributes(USER_ID, pagination);
        assertEquals(orderList, actualResult);
    }

    @Test
    void testCreate() {
        when(userService.findById(orderDto.getUserId())).thenReturn(user);
        when(giftCertificateService.findByGiftCertificateId(orderDto.getGiftCertificateId())).thenReturn(giftCertificate);
        when(orderService.create(orderDto)).thenReturn(order);
        Order actualResult = orderService.create(orderDto);
        assertEquals(order, actualResult);
    }
}
