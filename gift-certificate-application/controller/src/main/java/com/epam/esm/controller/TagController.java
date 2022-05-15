package com.epam.esm.controller;

import com.epam.esm.controller.hateoas.LinkBuilder;
import com.epam.esm.pagination.CustomPagination;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/tags")
public class TagController {
    private final TagService tagService;
    private final LinkBuilder<Tag> linkBuilder;

    @Autowired
    public TagController(TagService tagService, LinkBuilder<Tag> linkBuilder) {
        this.tagService = tagService;
        this.linkBuilder = linkBuilder;
    }

    @GetMapping("/{id}")
    public Tag findById(@PathVariable Long id) {
        Tag tag = tagService.findById(id);
        linkBuilder.setLinks(tag);
        return tag;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Tag create(@RequestBody Tag tag) {
        Tag createdTag = tagService.create(tag);
        linkBuilder.setLinks(createdTag);
        return createdTag;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTag(@PathVariable Long id) {
        tagService.delete(id);
    }

    @GetMapping
    public List<Tag> findAll(CustomPagination pagination) {
        return tagService.findAll(pagination)
                .stream()
                .peek(linkBuilder::setLinks)
                .collect(Collectors.toList());
    }

    @GetMapping("/most-popular-tag-with-highest-order-cost")
    public Tag getWidelyUsedTagWithHighestOrderCost() {
        Tag tag = tagService.findMostPopularTagWithHighestOrderCost();
        linkBuilder.setLinks(tag);
        return tag;
    }
}
