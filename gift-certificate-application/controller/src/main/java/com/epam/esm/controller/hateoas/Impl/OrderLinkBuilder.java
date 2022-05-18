package com.epam.esm.controller.hateoas.Impl;

import com.epam.esm.controller.OrderController;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Override
    public void setLinks(List<Order> orders) {
        orders.forEach(order -> setIdLinks(OrderController.class, order, order.getId(), SELF, DELETE));

        Set<User> userSet = new HashSet<>();
        orders.stream().map(Order::getUser).forEachOrdered(userSet::add);
        userSet.forEach(userLinkBuilder::setLinks);

        List<GiftCertificate> giftCertificateList = new ArrayList<>();
        orders.stream().map(Order::getGiftCertificate).forEachOrdered(giftCertificateList::add);
        giftCertificateLinkBuilder.setLinks(giftCertificateList);
    }
}
