package com.epam.esm.entity;

import com.epam.esm.listener.OrderListener;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@Entity
@EntityListeners(OrderListener.class)
@Table(name = "orders")
public class Order {
    @Id
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "gift_certificate_id")
    private GiftCertificate giftCertificate;
    @Column(name = "price", nullable = false)
    private BigDecimal price;
    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Order order = (Order) o;

        return Objects.equals(id, order.id) &&
                Objects.equals(user, order.user) &&
                Objects.equals(giftCertificate, order.giftCertificate) &&
                Objects.equals(price, order.price) &&
                Objects.equals(createDate, order.createDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, giftCertificate, price, createDate);
    }
}
