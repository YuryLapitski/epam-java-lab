package com.epam.esm.service.impl;

import com.epam.esm.pagination.CustomPagination;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.dao.OrderDao;
import com.epam.esm.repository.dao.TagDao;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.exception.FieldValidationException;
import com.epam.esm.service.exception.GiftCertificateAlreadyExistException;
import com.epam.esm.service.exception.GiftCertificateNotFoundException;
import com.epam.esm.service.exception.HasOrderToGiftCertificateException;
import com.epam.esm.service.exception.InvalidColumnNameException;
import com.epam.esm.service.exception.InvalidSortTypeException;
import com.epam.esm.service.exception.NoMatchingGiftCertificateException;
import com.epam.esm.service.exception.PageNumberValidationException;
import com.epam.esm.service.exception.PageSizeValidationException;
import com.epam.esm.service.exception.TagDoesNotExistException;
import com.epam.esm.service.util.Message;
import com.epam.esm.service.validator.GiftCertificateValidator;
import com.epam.esm.service.validator.PaginationValidator;
import com.epam.esm.service.validator.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final GiftCertificateDao giftCertificateDao;
    private final TagDao tagDao;
    private final OrderDao orderDao;
    private final GiftCertificateValidator giftCertificateValidator;
    private final TagValidator tagValidator;
    private final PaginationValidator paginationValidator;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao,
                                      TagDao tagDao,
                                      OrderDao orderDao,
                                      GiftCertificateValidator giftCertificateValidator,
                                      TagValidator tagValidator,
                                      PaginationValidator paginationValidator) {
        this.giftCertificateDao = giftCertificateDao;
        this.tagDao = tagDao;
        this.orderDao = orderDao;
        this.giftCertificateValidator = giftCertificateValidator;
        this.tagValidator = tagValidator;
        this.paginationValidator = paginationValidator;
    }

    @Transactional
    @Override
    public GiftCertificate create(GiftCertificate giftCertificate) {
        String name = giftCertificate.getName();
        String description = giftCertificate.getDescription();
        BigDecimal price = giftCertificate.getPrice();
        Short duration = giftCertificate.getDuration();

        if (giftCertificateValidator.isEmptyFields(name, description, price, duration)) {
            throw new FieldValidationException(Message.CANNOT_BE_EMPTY_FIELDS_MSG);
        }

        if (!giftCertificateValidator.validateAll(name, description, price, duration)) {
            throw new FieldValidationException(Message.INVALID_FIELDS_MSG);
        }

        List<Tag> createdTagList = createTagsList(giftCertificate);
        giftCertificate.setTagList(createdTagList);

        if (isGiftCertificateExist(giftCertificate)) {
            throw new GiftCertificateAlreadyExistException(String
                    .format(Message.GIFT_CERTIFICATE_ALREADY_EXIST_MSG, giftCertificate.getName()));
        }

        return giftCertificateDao.create(giftCertificate);
    }

    private boolean isGiftCertificateExist(GiftCertificate giftCertificate) {
        String giftCertificateName = giftCertificate.getName();
        return giftCertificateDao.findByName(giftCertificateName).isPresent();
    }

    private List<Tag> createTagsList(GiftCertificate giftCertificate) {
        List<Tag> createdTagList = new ArrayList<>();
        for (Tag tag : giftCertificate.getTagList()) {
            String name = tag.getName();
            if (!tagValidator.isNameValid(name)) {
                throw new FieldValidationException(Message.INVALID_TAG_NAME_MSG);
            }
            Optional<Tag> optionalTag = tagDao.findByName(name);
            if (optionalTag.isPresent()) {
                tag = optionalTag.get();
            } else {
                tag = tagDao.create(tag);
            }
            createdTagList.add(tag);
        }

        return createdTagList;
    }

    @Override
    public List<GiftCertificate> findAll(CustomPagination pagination) {
        if (!paginationValidator.isSizeValid(pagination)) {
            throw new PageSizeValidationException(Message.PAGE_SIZE_INVALID_MSG);
        }

        Long giftCertificatesNumber = giftCertificateDao.getEntitiesNumber(GiftCertificate.class);
        int lastPage = (int) Math.ceil((double) giftCertificatesNumber / pagination.getSize());
        if (!paginationValidator.isPageValid(pagination, lastPage)) {
            throw new PageNumberValidationException(String.format(Message.PAGE_NUMBER_INVALID_MSG, lastPage));
        }

        return giftCertificateDao.findAll(pagination, GiftCertificate.class);
    }

    @Override
    public GiftCertificate findByGiftCertificateId(Long id) {
        return giftCertificateDao.findById(id, GiftCertificate.class)
                .orElseThrow(() -> new GiftCertificateNotFoundException(String
                .format(Message.GIFT_CERTIFICATE_ID_NOT_FOUND_MSG, id)));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateDao.findById(id, GiftCertificate.class);

        if (!optionalGiftCertificate.isPresent()) {
            String message = String.format(Message.GIFT_CERTIFICATE_ID_NOT_FOUND_MSG, id);
            throw new GiftCertificateNotFoundException(message);
        }

        if (!orderDao.findByGiftCertificateId(id).isEmpty()) {
            throw new HasOrderToGiftCertificateException(Message.CANNOT_BE_DELETED_GIFT_CERTIFICATE_MSG);
        }

        giftCertificateDao.delete(id, GiftCertificate.class);
    }

    @Transactional
    @Override
    public GiftCertificate update(Long giftCertificateId, GiftCertificate giftCertificate) {
        GiftCertificate updatedGiftCertificate = findByGiftCertificateId(giftCertificateId);

        String name = giftCertificate.getName();
        if (name != null) {
            if (!giftCertificateValidator.isNameValid(name)) {
                throw new FieldValidationException(Message.INVALID_NAME_MSG);
            }
            updatedGiftCertificate.setName(name);
        }

        String description = giftCertificate.getDescription();
        if (description != null) {
            if (!giftCertificateValidator.isDescriptionValid(description)) {
                throw new FieldValidationException(Message.INVALID_DESCRIPTION_MSG);
            }
            updatedGiftCertificate.setDescription(description);
        }

        BigDecimal price = giftCertificate.getPrice();
        if (price != null) {
            if (!giftCertificateValidator.isPriceValid(price)) {
                throw new FieldValidationException(Message.INVALID_PRICE_MSG);
            }
            updatedGiftCertificate.setPrice(price);
        }

        Short duration = giftCertificate.getDuration();
        if (duration != null) {
            if (!giftCertificateValidator.isDurationValid(duration)) {
                throw new FieldValidationException(Message.INVALID_DURATION_MSG);
            }
            updatedGiftCertificate.setDuration(duration);
        }

        List<Tag> createdTagList = createTagsList(giftCertificate);
        updatedGiftCertificate.setTagList(createdTagList);

        return giftCertificateDao.update(updatedGiftCertificate);
    }

    @Transactional
    @Override
    public List<GiftCertificate> findByAttributes(String name, List<String> tagNames, List<String> columnNames,
                                                  String sortType, CustomPagination pagination) {
        checkIfExistsGiftCertificateByName(name);
        checkTagsByName(tagNames);
        checkColumnNames(columnNames);
        checkSortType(sortType);

        if (!paginationValidator.isSizeValid(pagination)) {
            throw new PageSizeValidationException(Message.PAGE_SIZE_INVALID_MSG);
        }

        Long giftCertificatesNumber = giftCertificateDao.findByAttributesNumber(name, tagNames);
        int lastPage = (int) Math.ceil((double) giftCertificatesNumber / pagination.getSize());
        if (!paginationValidator.isPageValid(pagination, lastPage)) {
            throw new PageNumberValidationException(String.format(Message.PAGE_NUMBER_INVALID_MSG, lastPage));
        }

        return giftCertificateDao.findByAttributes(name, tagNames, columnNames, sortType, pagination);
    }

    private void checkIfExistsGiftCertificateByName(String name) {
        if (name != null) {
            if (giftCertificateDao.findByPartOfName(name).isEmpty()) {
                throw new GiftCertificateNotFoundException(String.format(Message.GIFT_CERTIFICATE_NAME_NOT_FOUND_MSG, name));
            }
        }
    }

    private void checkTagsByName(List<String> tagNames) {
        if (tagNames != null) {
            tagNames.forEach(tagName -> tagDao.findByName(tagName)
                    .orElseThrow(() -> new TagDoesNotExistException(String
                            .format(Message.TAG_DOES_NOT_EXIST_MSG, tagName))));
            if (giftCertificateDao.findGiftCertificatesByTagNames(tagNames).isEmpty()) {
                throw new NoMatchingGiftCertificateException(Message.NO_GIFT_CERTIFICATE_MATCHING_MSG);
            }
        }
    }

    private void checkColumnNames(List<String> columnNames) {
        if (columnNames != null) {
            for (String columnName : columnNames) {
                if (!giftCertificateValidator.isColumnNameValid(columnName)) {
                    throw new InvalidColumnNameException(Message.INVALID_COLUMN_NAME_MSG);
                }
            }
        }
    }

    private void checkSortType(String sortType) {
        if (sortType != null) {
            if (!giftCertificateValidator.isSortTypeValid(sortType)) {
                throw new InvalidSortTypeException(Message.INVALID_SORT_TYPE_MSG);
            }
        }
    }
}

