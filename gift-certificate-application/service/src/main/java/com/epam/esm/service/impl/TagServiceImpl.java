package com.epam.esm.service.impl;

import com.epam.esm.pagination.CustomPagination;
import com.epam.esm.repository.dao.GiftCertificateDao;
import com.epam.esm.repository.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.exception.*;
import com.epam.esm.service.TagService;
import com.epam.esm.service.validator.PaginationValidator;
import com.epam.esm.service.validator.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {
    private static final String TAG_NOT_FOUND_MSG = "Tag with id=%d not found.";
    private static final String INVALID_TAG_NAME_MSG = "Invalid tag name";
    private static final String CANNOT_BE_DELETED_TAG_MSG = "The tag cannot be deleted because " +
            "a gift certificate reference exists.";
    private static final String TAG_ALREADY_EXIST_MSG = "Tag with the name '%s' " +
            "already exists";
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
        Long tagsNumber = tagDao.findTagsNumber();
        pagination = paginationValidator.validatePagination(pagination, tagsNumber);

        return tagDao.findAll(pagination);
    }

    @Override
    public Tag findById(Long id) {
        return tagDao.findById(id).orElseThrow(() -> new TagNotFoundException(String.format(TAG_NOT_FOUND_MSG, id)));
    }

    @Transactional
    @Override
    public boolean delete(Long id) {
        Optional<Tag> optionalTag = tagDao.findById(id);
        if (!optionalTag.isPresent()) {
            throw new TagNotFoundException(String.format(TAG_NOT_FOUND_MSG, id));
        }

        CustomPagination pagination = new CustomPagination();
        if (!giftCertificateDao.findGiftCertificatesByTagName(optionalTag.get().getName(), pagination).isEmpty()) {
            throw new TagToGiftCertificateReferenceException(CANNOT_BE_DELETED_TAG_MSG);
        }

        return tagDao.delete(id);
    }
}
