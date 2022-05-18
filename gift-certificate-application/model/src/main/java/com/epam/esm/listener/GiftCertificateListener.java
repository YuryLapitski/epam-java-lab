package com.epam.esm.listener;

import com.epam.esm.entity.GiftCertificate;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

public class GiftCertificateListener {

    @PrePersist
    public void beforeCreate(GiftCertificate giftCertificate) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        giftCertificate.setCreateDate(currentDateTime);
        giftCertificate.setLastUpdateDate(currentDateTime);
    }

    @PreUpdate
    public void beforeUpdate(GiftCertificate giftCertificate) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        giftCertificate.setLastUpdateDate(currentDateTime);
    }
}
