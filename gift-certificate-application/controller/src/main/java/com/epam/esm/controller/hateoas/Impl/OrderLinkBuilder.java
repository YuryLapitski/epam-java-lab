package com.epam.esm.controller.hateoas.Impl;

import com.epam.esm.controller.OrderController;
import com.epam.esm.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderLinkBuilder extends AbstractLinkBuilder<Order> {
    private final UserLinkBuilder userLinkBuilder;
    private final GiftCertificateLinkBuilder giftCertificateLinkBuilder;

    @Autowired
    public OrderLinkBuilder(UserLinkBuilder userLinkBuilder,
                            GiftCertificateLinkBuilder giftCertificateLinkBuilder) {
        this.userLinkBuilder = userLinkBuilder;
        this.giftCertificateLinkBuilder = giftCertificateLinkBuilder;
    }

    @Override
    public void setLinks(Order order) {
        setIdLinks(OrderController.class, order, order.getId(), SELF, DELETE);
        userLinkBuilder.setLinks(order.getUser());
        giftCertificateLinkBuilder.setLinks(order.getGiftCertificate());
    }
}
