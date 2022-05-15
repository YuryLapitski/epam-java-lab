package com.epam.esm.controller.hateoas.Impl;

import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.TagController;
import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TagLinkBuilder extends AbstractLinkBuilder<Tag> {

    @Override
    public void setLinks(Tag tag) {
        setIdLinks(TagController.class, tag, tag.getId(), SELF, DELETE);
    }

    @Override
    public void setLinks(List<Tag> tags) {
        tags.forEach(tag -> setIdLinks(OrderController.class, tag, tag.getId(), SELF, DELETE));
    }
}
