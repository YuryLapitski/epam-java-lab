package com.epam.esm.listener;

import com.epam.esm.entity.Order;
import javax.persistence.PrePersist;
import java.time.LocalDateTime;

public class OrderListener {

    @PrePersist
    public void beforeCreate(Order order) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        order.setCreateDate(currentDateTime);
    }
}
