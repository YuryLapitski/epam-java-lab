package com.epam.esm.service.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class OrderDto {
    private Long userId;
    private Long giftCertificateId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrderDto orderDto = (OrderDto) o;

        return Objects.equals(userId, orderDto.userId) &&
                Objects.equals(giftCertificateId, orderDto.giftCertificateId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, giftCertificateId);
    }
}
