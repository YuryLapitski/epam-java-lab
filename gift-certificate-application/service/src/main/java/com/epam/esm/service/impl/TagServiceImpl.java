package com.epam.esm.service.impl;

import com.epam.esm.pagination.CustomPagination;
import com.epam.esm.repository.dao.GiftCertificateDao;
import com.epam.esm.repository.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.FieldValidationException;
import com.epam.esm.service.exception.TagAlreadyExistException;
import com.epam.esm.service.exception.TagNotFoundException;
import com.epam.esm.service.exception.TagToGiftCertificateReferenceException;
import com.epam.esm.service.validator.PaginationValidator;
import com.epam.esm.service.validator.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    private static final String TAG_NOT_FOUND_MSG = "Tag with id=%d not found.";
    private static final String INVALID_TAG_NAME_MSG = "Invalid tag name";
    private static final String CANNOT_BE_DELETED_TAG_MSG = "The tag cannot be deleted because " +
            "a gift certificate reference exists.";
    private static final String TAG_ALREADY_EXIST_MSG = "Tag with the name '%s' " +
            "already exists";
    private static final String MOST_POPULAR_TAG_NOT_FOUND_MSG = "Tag not found";
    private final TagDao tagDao;
    private final GiftCertificateDao giftCertificateDao;
    private final TagValidator tagValidator;
    private final PaginationValidator paginationValidator;

    @Autowired
    public TagServiceImpl(TagDao tagDao, GiftCertificateDao giftCertificateDao, TagValidator tagValidator, PaginationValidator paginationValidator) {
        this.tagDao = tagDao;
        this.giftCertificateDao = giftCertificateDao;
        this.tagValidator = tagValidator;
        this.paginationValidator = paginationValidator;
    }

    @Transactional
    @Override
    public Tag create(Tag tag) {
        if (!tagValidator.isNameValid(tag.getName())) {
            throw new FieldValidationException(INVALID_TAG_NAME_MSG);
        }

        if (tagDao.findByName(tag.getName()).isPresent()) {
            throw new TagAlreadyExistException(String
                    .format(TAG_ALREADY_EXIST_MSG, tag.getName()));
        }

        return tagDao.create(tag);
    }

    @Override
    public List<Tag> findAll(CustomPagination pagination) {
        Long tagsNumber = tagDao.findEntitiesNumber(Tag.class);
        pagination = paginationValidator.validatePagination(pagination, tagsNumber);

        return tagDao.findAll(pagination, Tag.class);
    }

    @Override
    public Tag findById(Long id) {
        return tagDao.findById(id, Tag.class).orElseThrow(() ->
                new TagNotFoundException(String.format(TAG_NOT_FOUND_MSG, id)));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Tag tag = tagDao.findById(id, Tag.class).orElseThrow(() ->
                new TagNotFoundException(String.format(TAG_NOT_FOUND_MSG, id)));

        List<String> tagNames = new ArrayList<>();
        tagNames.add(tag.getName());
        if (!giftCertificateDao.findGiftCertificatesByTagNames(tagNames).isEmpty()) {
            throw new TagToGiftCertificateReferenceException(CANNOT_BE_DELETED_TAG_MSG);
        }

        tagDao.delete(id, Tag.class);
    }

    @Override
    public Tag findMostPopularTagWithHighestOrderCost() {
        return tagDao.findMostPopularTagWithHighestOrderCost()
                .orElseThrow(() -> new TagNotFoundException(MOST_POPULAR_TAG_NOT_FOUND_MSG));
    }
}
