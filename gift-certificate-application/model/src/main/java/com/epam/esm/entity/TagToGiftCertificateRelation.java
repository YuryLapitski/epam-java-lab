package com.epam.esm.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@Entity
@Table(name = "tag_gift_certificate")
public class TagToGiftCertificateRelation {
    @Id
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tag_id", nullable = false)
    @JoinTable(name = "tag",
            joinColumns = @JoinColumn(name = "id", referencedColumnName = "tag_id"))
    private Long tagId;

    @Column(name = "gift_certificate_id", nullable = false)
    @JoinTable(name = "gift_certificate",
            joinColumns = @JoinColumn(name = "id", referencedColumnName = "gift_certificate_id"))
    private Long giftCertificateId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TagToGiftCertificateRelation that = (TagToGiftCertificateRelation) o;

        return Objects.equals(tagId, that.tagId) &&
                Objects.equals(giftCertificateId, that.giftCertificateId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagId, giftCertificateId);
    }
}
