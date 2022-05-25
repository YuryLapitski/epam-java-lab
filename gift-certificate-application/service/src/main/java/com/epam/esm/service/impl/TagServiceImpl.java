package com.epam.esm.service.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.pagination.CustomPagination;
import com.epam.esm.repository.dao.GiftCertificateDao;
import com.epam.esm.repository.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.FieldValidationException;
import com.epam.esm.service.exception.PageNumberValidationException;
import com.epam.esm.service.exception.PageSizeValidationException;
import com.epam.esm.service.exception.TagAlreadyExistException;
import com.epam.esm.service.exception.TagNotFoundException;
import com.epam.esm.service.exception.TagToGiftCertificateReferenceException;
import com.epam.esm.service.util.Message;
import com.epam.esm.service.validator.PaginationValidator;
import com.epam.esm.service.validator.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    private final TagDao tagDao;
    private final GiftCertificateDao giftCertificateDao;
    private final TagValidator tagValidator;
    private final PaginationValidator paginationValidator;

    @Autowired
    public TagServiceImpl(TagDao tagDao, GiftCertificateDao giftCertificateDao,
                          TagValidator tagValidator, PaginationValidator paginationValidator) {
        this.tagDao = tagDao;
        this.giftCertificateDao = giftCertificateDao;
        this.tagValidator = tagValidator;
        this.paginationValidator = paginationValidator;
    }

    @Transactional
    @Override
    public Tag create(Tag tag) {
        String tagName = tag.getName();

        if (!tagValidator.isNameValid(tagName)) {
            throw new FieldValidationException(Message.INVALID_TAG_NAME_MSG);
        }

        tagDao.findByName(tagName).ifPresent(t -> {
            throw new TagAlreadyExistException(String.format(Message.TAG_ALREADY_EXIST_MSG, tagName));
        });

        return tagDao.create(tag);
    }

    @Override
    public List<Tag> findAll(CustomPagination pagination) {
        if (!paginationValidator.isSizeValid(pagination)) {
            throw new PageSizeValidationException(Message.PAGE_SIZE_INVALID_MSG);
        }

        Long tagsNumber = tagDao.getEntitiesNumber(Tag.class);
        int lastPage = (int) Math.ceil((double) tagsNumber / pagination.getSize());

        if (!paginationValidator.isPageValid(pagination, lastPage)) {
            throw new PageNumberValidationException(String.format(Message.PAGE_NUMBER_INVALID_MSG, lastPage));
        }

        return tagDao.findAll(pagination, Tag.class);
    }

    @Override
    public Tag findById(Long id) {
        return tagDao.findById(id, Tag.class).orElseThrow(() ->
                new TagNotFoundException(String.format(Message.TAG_NOT_FOUND_MSG, id)));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Tag tag = tagDao.findById(id, Tag.class).orElseThrow(() ->
                new TagNotFoundException(String.format(Message.TAG_NOT_FOUND_MSG, id)));

        List<String> tagNames = Collections.singletonList(tag.getName());

        List<GiftCertificate> giftCertificates = giftCertificateDao.findGiftCertificatesByTagNames(tagNames);
        if (!giftCertificates.isEmpty()) {
            throw new TagToGiftCertificateReferenceException(Message.CANNOT_BE_DELETED_TAG_MSG);
        }

        tagDao.delete(id, Tag.class);
    }

    @Override
    public Tag findMostPopularTagWithHighestOrderCost() {
        return tagDao.findMostPopularTagWithHighestOrderCost()
                .orElseThrow(() -> new TagNotFoundException(Message.MOST_POPULAR_TAG_NOT_FOUND_MSG));
    }
}
