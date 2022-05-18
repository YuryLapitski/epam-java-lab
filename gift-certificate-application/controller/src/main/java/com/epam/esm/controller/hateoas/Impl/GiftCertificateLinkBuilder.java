package com.epam.esm.controller.hateoas.Impl;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.hateoas.LinkBuilder;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class GiftCertificateLinkBuilder extends AbstractLinkBuilder<GiftCertificate> {
    private final LinkBuilder<Tag> tagLinkBuilder;

    @Autowired
    public GiftCertificateLinkBuilder(LinkBuilder<Tag> tagLinkBuilder) {
        this.tagLinkBuilder = tagLinkBuilder;
    }

    @Override
    public void setLinks(GiftCertificate giftCertificate) {
        setIdLinks(GiftCertificateController.class, giftCertificate, giftCertificate.getId(), SELF, UPDATE, DELETE);
        Set<Tag> tagSet = new HashSet<>(giftCertificate.getTagList());
        tagSet.forEach(tagLinkBuilder::setLinks);
    }

    @Override
    public void setLinks(List<GiftCertificate> giftCertificates) {
        Set<GiftCertificate> giftCertificateSet = new HashSet<>(giftCertificates);
        giftCertificateSet.forEach(giftCertificate ->
                setIdLinks(GiftCertificateController.class, giftCertificate,
                        giftCertificate.getId(), SELF, UPDATE, DELETE));

        Set<Tag> tagSet = new HashSet<>();
        giftCertificates.stream().map(GiftCertificate::getTagList).forEachOrdered(tagSet::addAll);
        tagSet.forEach(tagLinkBuilder::setLinks);
    }
}
