package com.epam.esm.controller;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/gift-certificates")
public class GiftCertificateController {
    private final GiftCertificateService giftCertificateService;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificate create(@RequestBody (required = false) GiftCertificate giftCertificate) {
        return giftCertificateService.create(giftCertificate);
    }

    @PutMapping("/{id}")
    public GiftCertificate update(@PathVariable Long id, @RequestBody GiftCertificate giftCertificate) {
        return giftCertificateService.update(id, giftCertificate);
    }

    @GetMapping("/{id}")
    public GiftCertificate findById(@PathVariable Long id) {
        return giftCertificateService.findByGiftCertificateId(id);
    }

    @GetMapping
    public List<GiftCertificate> findByAttributes(
            @RequestParam(required = false, name = "name") String name,
            @RequestParam(required = false, name = "tagName") String tagName,
            @RequestParam(required = false, name = "columnName") String columnName,
            @RequestParam(required = false, name = "sortType") String sortType) {
        return giftCertificateService.findByAttributes(name, tagName, columnName, sortType);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGiftCertificate(@PathVariable long id) {
        giftCertificateService.delete(id);
    }
}
