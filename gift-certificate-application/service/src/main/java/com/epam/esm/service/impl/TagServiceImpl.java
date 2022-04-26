package com.epam.esm.service.impl;

import com.epam.esm.repository.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.dao.TagToGiftCertificateDao;
import com.epam.esm.service.exception.TagNotFoundException;
import com.epam.esm.service.exception.FieldValidationException;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.TagToGiftCertificateReferenceException;
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
    private final TagDao tagDao;
    private final TagToGiftCertificateDao tagToGiftCertificateDao;
    private final TagValidator tagValidator;

    @Autowired
    public TagServiceImpl(TagDao tagDao, TagToGiftCertificateDao tagToGiftCertificateDao, TagValidator tagValidator) {
        this.tagDao = tagDao;
        this.tagToGiftCertificateDao = tagToGiftCertificateDao;
        this.tagValidator = tagValidator;
    }

    @Override
    public Tag create(Tag tag) {
        if (!tagValidator.isNameValid(tag.getName())) {
            throw new FieldValidationException(INVALID_TAG_NAME_MSG);
        }

        return tagDao.create(tag);
    }

    @Override
    public List<Tag> findAll() {
        return tagDao.findAll();
    }

    @Override
    public Tag findById(Long id) {
        return tagDao.findById(id).orElseThrow(() -> new TagNotFoundException(String.format(TAG_NOT_FOUND_MSG, id)));
    }

    @Transactional
    @Override
    public boolean delete(Long id) {
        Optional<Tag> optionalTag = tagDao.findById(id);
        if (optionalTag.isPresent()) {
            if (tagToGiftCertificateDao.findByTagId(id).isEmpty()) {
                return tagDao.delete(id);
            } else {
                throw new TagToGiftCertificateReferenceException(CANNOT_BE_DELETED_TAG_MSG);
            }
        } else {
            throw new TagNotFoundException(String.format(TAG_NOT_FOUND_MSG, id));
        }
    }
}
