package com.epam.esm.service.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.service.exception.FieldValidationException;
import com.epam.esm.service.exception.PageNumberValidationException;
import com.epam.esm.service.exception.TagAlreadyExistException;
import com.epam.esm.service.exception.TagNotFoundException;
import com.epam.esm.service.exception.TagToGiftCertificateReferenceException;
import com.epam.esm.service.pagination.CustomPagination;
import com.epam.esm.repository.dao.GiftCertificateRepository;
import com.epam.esm.repository.dao.TagRepository;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.pagination.PaginationConverter;
import com.epam.esm.service.util.Message;
import com.epam.esm.service.validator.PaginationValidator;
import com.epam.esm.service.validator.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final GiftCertificateRepository giftCertificateDao;
    private final TagValidator tagValidator;
    private final PaginationValidator paginationValidator;
    private final PaginationConverter paginationConverter;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, GiftCertificateRepository giftCertificateDao,
                          TagValidator tagValidator, PaginationValidator paginationValidator, PaginationConverter paginationConverter) {
        this.tagRepository = tagRepository;
        this.giftCertificateDao = giftCertificateDao;
        this.tagValidator = tagValidator;
        this.paginationValidator = paginationValidator;
        this.paginationConverter = paginationConverter;
    }

    @Transactional
    @Override
    public Tag create(Tag tag) {
        String tagName = tag.getName();

        if (!tagValidator.isNameValid(tagName)) {
            throw new FieldValidationException(Message.INVALID_TAG_NAME_MSG);
        }

        tagRepository.findByName(tagName).ifPresent(t -> {
            throw new TagAlreadyExistException(String.format(Message.TAG_ALREADY_EXIST_MSG, tagName));
        });

        return tagRepository.save(tag);
    }

    @Override
    public List<Tag> findAll(CustomPagination pagination) {
        Pageable pageable = paginationConverter.convert(pagination);
        Page<Tag> tagPage = tagRepository.findAll(pageable);

        int lastPage = tagPage.getTotalPages();
        if(!paginationValidator.isPageValid(pagination, lastPage)) {
            String message = String.format(Message.PAGE_NUMBER_INVALID_MSG, lastPage);
            throw new PageNumberValidationException(message);
        }

        return tagPage.getContent();
    }

    @Override
    public Tag findById(Long id) {
        return tagRepository.findById(id).orElseThrow(() ->
                new TagNotFoundException(String.format(Message.TAG_NOT_FOUND_MSG, id)));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Tag tag = tagRepository.findById(id).orElseThrow(() ->
                new TagNotFoundException(String.format(Message.TAG_NOT_FOUND_MSG, id)));

        List<String> tagNames = Collections.singletonList(tag.getName());

        List<GiftCertificate> giftCertificates = giftCertificateDao.findGiftCertificatesByTagNames(tagNames);
        if (!giftCertificates.isEmpty()) {
            throw new TagToGiftCertificateReferenceException(Message.CANNOT_BE_DELETED_TAG_MSG);
        }

        tagRepository.delete(tag);
    }

    @Override
    public Tag findMostPopularTagWithHighestOrderCost() {
        return tagRepository.findMostPopularTagWithHighestOrderCost()
                .orElseThrow(() -> new TagNotFoundException(Message.MOST_POPULAR_TAG_NOT_FOUND_MSG));
    }
}
