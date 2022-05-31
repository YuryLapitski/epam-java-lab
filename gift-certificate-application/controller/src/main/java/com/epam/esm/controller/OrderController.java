package com.epam.esm.controller;

import com.epam.esm.controller.hateoas.LinkBuilder;
import com.epam.esm.pagination.CustomPagination;
import com.epam.esm.entity.Order;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.dto.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/orders")
public class OrderController {
    private final OrderService orderService;
    private final LinkBuilder<Order> orderLinkBuilder;

    @Autowired
    public OrderController(OrderService orderService, LinkBuilder<Order> orderLinkBuilder) {
        this.orderService = orderService;
        this.orderLinkBuilder = orderLinkBuilder;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('orders:read')")
    public Order findById(@PathVariable Long id) {
        Order order = orderService.findById(id);
        orderLinkBuilder.setLinks(order);

        return order;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('orders:create')")
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

    @GetMapping
    @PreAuthorize("hasAuthority('orders:read')")
    public List<Order> findByAttributes(@RequestParam(required = false, name = "user-id") Long userId,
                                    CustomPagination pagination) {
        List<Order> orderList = orderService.findByAttributes(userId, pagination);
        orderLinkBuilder.setLinks(orderList);
        
        return orderList;
    }
}
