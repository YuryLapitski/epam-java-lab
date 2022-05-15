package com.epam.esm.controller.hateoas.Impl;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.hateoas.LinkBuilder;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
        if (!CollectionUtils.isEmpty(giftCertificate.getTagList())) {
            giftCertificate.getTagList().forEach(tagLinkBuilder::setLinks);
        }
    }
}
