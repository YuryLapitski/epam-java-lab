package com.epam.esm.service.impl;

import com.epam.esm.pagination.CustomPagination;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.dao.TagDao;
import com.epam.esm.service.exception.*;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.validator.GiftCertificateValidator;
import com.epam.esm.service.validator.PaginationValidator;
import com.epam.esm.service.validator.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private static final String GIFT_CERTIFICATE_ID_NOT_FOUND_MSG = "Gift certificate with id=%d not found.";
    private static final String GIFT_CERTIFICATE_NAME_NOT_FOUND_MSG = "Gift certificate with name '%s' not found.";
    private static final String GIFT_CERTIFICATE_ALREADY_EXIST_MSG = "Gift certificate with the name '%s' " +
            "already exists";
    private static final String INVALID_FIELDS_MSG = "Invalid fields";
    private static final String INVALID_NAME_MSG = "Invalid name";
    private static final String INVALID_DESCRIPTION_MSG = "Invalid description";
    private static final String INVALID_PRICE_MSG = "Invalid price";
    private static final String INVALID_DURATION_MSG = "Invalid duration";
    private static final String INVALID_COLUMN_NAME_MSG = "Invalid column name. Please choose 'name', " +
            "'description', 'price', 'duration', 'create_date', 'last_update_date' columns";
    private static final String INVALID_SORT_TYPE_MSG = "Invalid sort type. Sort type can be only 'asc' or 'desc'.";
    private static final String CANNOT_BE_EMPTY_FIELDS_MSG = "Fields 'Name', 'Description', 'Price', 'Duration' " +
            "cannot be empty";
    private static final String GIFT_CERTIFICATES_NOT_FOUND_MSG = "Gift certificate with tag name '%s' not found.";
    private final GiftCertificateDao giftCertificateDao;
    private final TagDao tagDao;
    private final GiftCertificateValidator giftCertificateValidator;
    private final TagValidator tagValidator;
    private final PaginationValidator paginationValidator;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao,
                                      TagDao tagDao,
                                      GiftCertificateValidator giftCertificateValidator,
                                      TagValidator tagValidator, PaginationValidator paginationValidator) {
        this.giftCertificateDao = giftCertificateDao;
        this.tagDao = tagDao;
        this.giftCertificateValidator = giftCertificateValidator;
        this.tagValidator = tagValidator;
        this.paginationValidator = paginationValidator;
    }

    @Transactional
    @Override
    public GiftCertificate create(GiftCertificate giftCertificate) {
        if (giftCertificate.getName() == null
                || giftCertificate.getDescription() == null
                || giftCertificate.getPrice() == null
                || giftCertificate.getDuration() == null) {
            throw new FieldValidationException(CANNOT_BE_EMPTY_FIELDS_MSG);
        }

        if (giftCertificate.getTagList() == null) {
            giftCertificate.setTagList(new ArrayList<>());
        }

        if (!isValidGiftCertificate(giftCertificate)
                || !isValidTags(giftCertificate.getTagList()) && giftCertificate.getTagList() != null) {
            throw new FieldValidationException(INVALID_FIELDS_MSG);
        }


        if (isGiftCertificateExist(giftCertificate)) {
            throw new GiftCertificateAlreadyExistException(String
                    .format(GIFT_CERTIFICATE_ALREADY_EXIST_MSG, giftCertificate.getName()));
        }

        List<Tag> tagList = createTagsList(giftCertificate);
        giftCertificate.setTagList(tagList);

        return giftCertificateDao.create(giftCertificate);
    }

    private boolean isValidTags(List<Tag> tags) {
        if (tags == null) {
            return true;
        }
        return tags.stream().allMatch(tag -> tagValidator.isNameValid(tag.getName()));
    }

    private boolean isValidGiftCertificate(GiftCertificate giftCertificate) {
        return giftCertificateValidator.validateAll(giftCertificate.getName(),
                giftCertificate.getDescription(),
                giftCertificate.getPrice(),
                giftCertificate.getDuration());
    }

    private boolean isGiftCertificateExist(GiftCertificate giftCertificate) {
        String giftCertificateName = giftCertificate.getName();
        return giftCertificateDao.findByName(giftCertificateName).isPresent();
    }

    private List<Tag> createTagsList(GiftCertificate giftCertificate) {
        if (giftCertificate.getTagList() == null) {
            return Collections.emptyList();
        }

        List<Tag> tagList = new ArrayList<>();
        for (Tag tag : giftCertificate.getTagList()) {
            Optional<Tag> optionalTag = tagDao.findByName(tag.getName());
            if (optionalTag.isPresent()) {
                tag = optionalTag.get();
            } else {
                tag = tagDao.create(tag);
            }
            tagList.add(tag);
        }

        return tagList;
    }

    @Override
    public List<GiftCertificate> findAll(CustomPagination pagination) {
        Long gcNumber = giftCertificateDao.findEntitiesNumber(GiftCertificate.class);
        pagination = paginationValidator.validatePagination(pagination, gcNumber);

        return giftCertificateDao.findAll(pagination, GiftCertificate.class);
    }

    @Override
    public GiftCertificate findByGiftCertificateId(Long id) {
        return giftCertificateDao.findById(id, GiftCertificate.class)
                .orElseThrow(() -> new GiftCertificateNotFoundException(String
                .format(GIFT_CERTIFICATE_ID_NOT_FOUND_MSG, id)));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateDao.findById(id, GiftCertificate.class);

        if (!optionalGiftCertificate.isPresent()) {
            String msg = String.format(GIFT_CERTIFICATE_ID_NOT_FOUND_MSG, id);
            throw new GiftCertificateNotFoundException(msg);
        }

        giftCertificateDao.delete(id, GiftCertificate.class);
    }

    @Transactional
    @Override
    public GiftCertificate update(Long giftCertificateId, GiftCertificate giftCertificate) {
        GiftCertificate newGiftCertificate = findByGiftCertificateId(giftCertificateId);
        if (giftCertificate.getName() != null) {
            if (!giftCertificateValidator.isNameValid(giftCertificate.getName())) {
                throw new FieldValidationException(INVALID_NAME_MSG);
            }
            newGiftCertificate.setName(giftCertificate.getName());
        }
        if (giftCertificate.getDescription() != null) {
            if (!giftCertificateValidator.isDescriptionValid(giftCertificate.getDescription())) {
                throw new FieldValidationException(INVALID_DESCRIPTION_MSG);
            }
            newGiftCertificate.setDescription(giftCertificate.getDescription());
        }
        if (giftCertificate.getPrice() != null) {
            if (!giftCertificateValidator.isPriceValid(giftCertificate.getPrice())) {
                throw new FieldValidationException(INVALID_PRICE_MSG);
            }
            newGiftCertificate.setPrice(giftCertificate.getPrice());
        }
        if (giftCertificate.getDuration() != null) {
            if (!giftCertificateValidator.isDurationValid(giftCertificate.getDuration())) {
                throw new FieldValidationException(INVALID_DURATION_MSG);
            }
            newGiftCertificate.setDuration(giftCertificate.getDuration());
        }

        List<Tag> tagList;
        if (giftCertificate.getTagList() == null) {
            tagList = Collections.emptyList();
        } else {
            tagList = createTagsList(giftCertificate);
        }
        newGiftCertificate.setTagList(tagList);

        LocalDateTime lastUpdateDate = LocalDateTime.now();
        newGiftCertificate.setLastUpdateDate(lastUpdateDate);

        return giftCertificateDao.update(newGiftCertificate);
    }

    @Override
    public List<GiftCertificate> findByAttributes(String name, List<String> tagNames,
                                                  List<String> columnNames, String sortType,
                                                  CustomPagination pagination) {
        Long gcNumber = giftCertificateDao.findByAttributesNumber(name, tagNames);
        pagination = paginationValidator.validatePagination(pagination, gcNumber);

        List<GiftCertificate> giftCertificates = giftCertificateDao.findByPartOfName(name);
        if (giftCertificates.isEmpty()) {
            throw new GiftCertificateNotFoundException(String.format(GIFT_CERTIFICATE_NAME_NOT_FOUND_MSG, name));
        }

        for (String columnName : columnNames) {
            if (!giftCertificateValidator.isColumnNameValid(columnName)) {
                throw new InvalidColumnNameException(INVALID_COLUMN_NAME_MSG);
            }
        }

        if (!giftCertificateValidator.isSortTypeValid(sortType)) {
            throw new InvalidSortTypeException(INVALID_SORT_TYPE_MSG);
        }

        return giftCertificateDao.findByAttributes(name, tagNames, columnNames, sortType, pagination);
    }

//    @Override
//    public List<GiftCertificate> findByAttributes(String name, String tagName,
//                                                  String columnName, String sortType,
//                                                  CustomPagination pagination) {
//        List<GiftCertificate> giftCertificateList = new ArrayList<>();
//
//        if (name != null) {
//            giftCertificateList = findByPartOfName(name, pagination);
//        }
//        if (tagName != null) {
//            giftCertificateList = findGiftCertificatesByTagName(tagName, pagination);
//        }
//        if (columnName != null && sortType != null) {
//            giftCertificateList = findAllWithSort(columnName, sortType, pagination);
//        }
//        if (name == null && tagName == null && columnName == null && sortType == null) {
//            giftCertificateList = findAll(pagination);
//        }
//
//        return giftCertificateList;
//    }
}
