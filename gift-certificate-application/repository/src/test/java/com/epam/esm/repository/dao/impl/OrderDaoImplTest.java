package com.epam.esm.repository.dao.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.pagination.CustomPagination;
import com.epam.esm.repository.config.TestConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {TestConfig.class, OrderDaoImpl.class})
@ExtendWith(SpringExtension.class)
@Transactional
@ActiveProfiles("dev")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderDaoImplTest {
    private static final String NEW_CERTIFICATE_NAME = "newName";
    private static final String NEW_CERTIFICATE_DESCRIPTION = "newDescription";
    private static final BigDecimal NEW_CERTIFICATE_PRICE = BigDecimal.valueOf(99.99);
    private static final short NEW_CERTIFICATE_DURATION = 60;
    private static final String CREATED_USER_FIRST_NAME = "Ivan";
    private static final String CREATED_USER_LAST_NAME = "Sidorov";
    private static final String CREATED_LOGIN_NAME = "ISid";
    private static final int EXPECTED_LIST_SIZE = 3;
    private static final int EXPECTED_LIST_SIZE_FIND_BY_ID = 1;
    private static final Long ORDER_ID = 1L;
    private static final Long USER_ID = 1L;
    private static final Long GIFT_CERTIFICATE_ID = 1L;
    private static final Long EXPECT_NUMBER = 1L;

    private final OrderDaoImpl orderDao;
    private CustomPagination pagination;

    @Autowired
    public OrderDaoImplTest(OrderDaoImpl orderDao) {
        this.orderDao = orderDao;
    }

    @BeforeAll
    void beforeAll() {
        pagination = new CustomPagination();
        pagination.setPage(1);
        pagination.setSize(10);
    }

    @Test
    void create() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(GIFT_CERTIFICATE_ID);
        giftCertificate.setName(NEW_CERTIFICATE_NAME);
        giftCertificate.setDescription(NEW_CERTIFICATE_DESCRIPTION);
        giftCertificate.setPrice(NEW_CERTIFICATE_PRICE);
        giftCertificate.setDuration(NEW_CERTIFICATE_DURATION);

        User user = new User();
        user.setId(USER_ID);
        user.setFirstName(CREATED_USER_FIRST_NAME);
        user.setLastName(CREATED_USER_LAST_NAME);
        user.setLogin(CREATED_LOGIN_NAME);

        Order order = new Order();
        order.setUser(user);
        order.setGiftCertificate(giftCertificate);
        order.setPrice(NEW_CERTIFICATE_PRICE);

        Order actual = orderDao.create(order);
        assertEquals(order, actual);
    }

    @Test
    void findAll() {
        List<Order> orders = orderDao.findAll(pagination, Order.class);
        assertEquals(EXPECTED_LIST_SIZE, orders.size());
    }

    @Test
    void findById() {
        Optional<Order> optionalOrder = orderDao.findById(ORDER_ID, Order.class);
        assertTrue(optionalOrder.isPresent());
    }

    @Test
    void findByUserId() {
        List<Order> orders = orderDao.findByUserId(USER_ID, pagination);
        assertEquals(EXPECTED_LIST_SIZE_FIND_BY_ID, orders.size());
    }

    @Test
    void findByGiftCertificateId() {
        List<Order> orders = orderDao.findByGiftCertificateId(GIFT_CERTIFICATE_ID);
        assertEquals(EXPECTED_LIST_SIZE_FIND_BY_ID, orders.size());
    }

    @Test
    void findUserOrdersNumber() {
        Long number = orderDao.findUserOrdersNumber(USER_ID);
        assertEquals(EXPECT_NUMBER, number);
    }
}