package com.epam.esm.controller;

import com.epam.esm.controller.hateoas.LinkBuilder;
import com.epam.esm.service.pagination.CustomPagination;
import com.epam.esm.entity.Order;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.dto.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/v1/orders")
public class OrderController {
    private final OrderService orderService;
    private final LinkBuilder<Order> orderLinkBuilder;

    @Autowired
    public OrderController(OrderService orderService, LinkBuilder<Order> orderLinkBuilder) {
        this.orderService = orderService;
        this.orderLinkBuilder = orderLinkBuilder;
    }

    @GetMapping
    @PostAuthorize("hasAuthority('orders:read')")
    public List<Order> findAll(CustomPagination pagination) {
        List<Order> orderList = orderService.findAll(pagination);
        orderLinkBuilder.setLinks(orderList);

        return orderList;
    }

    @GetMapping("/{id}")
    @PostAuthorize("hasAuthority('orders:read') || returnObject.user.id.equals(authentication.principal.userId)")
    public Order findById(@PathVariable Long id) {
        Order order = orderService.findById(id);
        orderLinkBuilder.setLinks(order);

        return order;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('orders:create') || #orderDto.userId.equals(authentication.principal.userId)")
    @ResponseStatus(HttpStatus.CREATED)
    public Order create(@RequestBody OrderDto orderDto) {
        Order order = orderService.create(orderDto);
        orderLinkBuilder.setLinks(order);

        return order;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('orders:delete')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        orderService.delete(id);
    }
}
