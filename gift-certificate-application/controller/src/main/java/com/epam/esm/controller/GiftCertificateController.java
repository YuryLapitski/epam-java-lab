package com.epam.esm.controller;

import com.epam.esm.controller.hateoas.LinkBuilder;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.pagination.CustomPagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/gift-certificates")
public class GiftCertificateController {
    private final GiftCertificateService giftCertificateService;
    private final LinkBuilder<GiftCertificate> giftCertificateLinkBuilder;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService, LinkBuilder<GiftCertificate> giftCertificateLinkBuilder) {
        this.giftCertificateService = giftCertificateService;
        this.giftCertificateLinkBuilder = giftCertificateLinkBuilder;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('gift-certificates:create')")
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificate create(@RequestBody GiftCertificate giftCertificate) {
        GiftCertificate createdGiftCertificate = giftCertificateService.create(giftCertificate);
        giftCertificateLinkBuilder.setLinks(createdGiftCertificate);

        return createdGiftCertificate;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('gift-certificates:update')")
    public GiftCertificate update(@PathVariable Long id, @RequestBody GiftCertificate giftCertificate) {
        GiftCertificate updatedGiftCertificate = giftCertificateService.update(id, giftCertificate);
        giftCertificateLinkBuilder.setLinks(updatedGiftCertificate);

        return updatedGiftCertificate;
    }

    @GetMapping("/{id}")
    public GiftCertificate findById(@PathVariable Long id) {
        GiftCertificate giftCertificate = giftCertificateService.findByGiftCertificateId(id);
        giftCertificateLinkBuilder.setLinks(giftCertificate);

        return giftCertificate;
    }

    @GetMapping
    public List<GiftCertificate> findByAttributes(
            @RequestParam(required = false, name = "name") String name,
            @RequestParam(required = false, name = "tagName") List<String> tagNames,
            @RequestParam(required = false, name = "columnName") List<String> columnNames,
            @RequestParam(required = false, name = "sortType") String sortType,
            CustomPagination pagination) {
        List<GiftCertificate> giftCertificates =
                giftCertificateService.findByAttributes(name, tagNames, columnNames, sortType, pagination);
        giftCertificateLinkBuilder.setLinks(giftCertificates);

        return giftCertificates;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('gift-certificates:update')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGiftCertificate(@PathVariable long id) {
        giftCertificateService.delete(id);
    }
}
