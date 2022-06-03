package com.epam.esm.service.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.service.pagination.CustomPagination;
import com.epam.esm.repository.dao.OrderRepository;
import com.epam.esm.repository.dao.UserRepository;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.exception.OrderNotFoundException;
import com.epam.esm.service.pagination.PaginationConverter;
import com.epam.esm.service.validator.PaginationValidator;
import com.epam.esm.service.validator.impl.PaginationValidatorImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private static final int LAST_PAGE = 1;
    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private OrderService orderService;
    private Order order;
    private User user;
    private GiftCertificate giftCertificate;
    private OrderDto orderDto;
    private PaginationValidator paginationValidator;
    private UserService userService;
    private GiftCertificateService giftCertificateService;
    private List<Order> orderList;
    private Page<Order> orderPage;
    private CustomPagination pagination;
    private PaginationConverter paginationConverter;
    private Pageable pageable;

    @BeforeAll
    void beforeAll() {
        orderRepository = mock(OrderRepository.class);
        userRepository = mock(UserRepository.class);
        paginationValidator = mock(PaginationValidatorImpl.class);
        userService = mock(UserServiceImpl.class);
        giftCertificateService = mock(GiftCertificateServiceImpl.class);
        paginationValidator = mock(PaginationValidatorImpl.class);
        paginationConverter = mock(PaginationConverter.class);

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

        orderPage = new PageImpl<>(orderList);

        pagination = new CustomPagination();
        pagination.setPage(PAGE);
        pagination.setSize(SIZE);

        pageable = PageRequest.of(PAGE, SIZE);

        orderService = new OrderServiceImpl(orderRepository, userRepository, userService, giftCertificateService, paginationValidator, paginationConverter);
    }

    @Test
    void testFindAll() {
        when(orderRepository.findAll(pageable)).thenReturn(orderPage);
        when(paginationConverter.convert(pagination)).thenReturn(pageable);
        when(paginationValidator.isSizeValid(pagination)).thenReturn(true);
        when(paginationValidator.isPageValid(pagination, LAST_PAGE)).thenReturn(true);
        List<Order> actualResult = orderService.findAll(pagination);
        assertEquals(orderList, actualResult);
    }

    @Test
    void testFindById() {
        when(orderRepository.findById(ID)).thenReturn(Optional.ofNullable(order));
        Order actualResult = orderService.findById(ID);
        assertEquals(order, actualResult);
    }

    @Test
    void testFindByIdShouldTrowOrderNotFoundException() {
        when(orderRepository.findById(ID)).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () -> orderService.findById(ID));
    }

    @Test
    void testFindByUserId() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.ofNullable(user));
        when(orderRepository.findByUserId(USER_ID, pageable)).thenReturn(orderPage);
        when(paginationConverter.convert(pagination)).thenReturn(pageable);
        when(paginationValidator.isSizeValid(pagination)).thenReturn(true);
        when(paginationValidator.isPageValid(pagination, LAST_PAGE)).thenReturn(true);
        when(orderRepository.findByUserId(USER_ID, pageable)).thenReturn(orderPage);
        List<Order> actualResult = orderService.findByUserId(USER_ID, pagination);
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
