package com.epam.esm.controller;

import com.epam.esm.controller.hateoas.LinkBuilder;
import com.epam.esm.service.pagination.CustomPagination;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/v1/tags")
public class TagController {
    private final TagService tagService;
    private final LinkBuilder<Tag> tagLinkBuilder;

    @Autowired
    public TagController(TagService tagService, LinkBuilder<Tag> linkBuilder) {
        this.tagService = tagService;
        this.tagLinkBuilder = linkBuilder;
    }

    @GetMapping("/{id}")
    public Tag findById(@PathVariable Long id) {
        Tag tag = tagService.findById(id);
        tagLinkBuilder.setLinks(tag);

        return tag;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('tags:create')")
    @ResponseStatus(HttpStatus.CREATED)
    public Tag create(@RequestBody Tag tag) {
        Tag createdTag = tagService.create(tag);
        tagLinkBuilder.setLinks(createdTag);

        return createdTag;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('tags:delete')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTag(@PathVariable Long id) {
        tagService.delete(id);
    }

    @GetMapping
    public List<Tag> findAll(CustomPagination pagination) {
        List<Tag> tagList = tagService.findAll(pagination);
        tagLinkBuilder.setLinks(tagList);

        return tagList;
    }

    @GetMapping("/most-popular-tag-with-highest-order-cost")
    public Tag getWidelyUsedTagWithHighestOrderCost() {
        Tag tag = tagService.findMostPopularTagWithHighestOrderCost();
        tagLinkBuilder.setLinks(tag);

        return tag;
    }
}
