package com.epam.esm.service.impl;

import com.epam.esm.service.exception.FieldValidationException;
import com.epam.esm.service.exception.GiftCertificateAlreadyExistException;
import com.epam.esm.service.exception.GiftCertificateNotFoundException;
import com.epam.esm.service.exception.HasOrderToGiftCertificateException;
import com.epam.esm.service.exception.InvalidColumnNameException;
import com.epam.esm.service.exception.InvalidSortTypeException;
import com.epam.esm.service.exception.NoMatchingGiftCertificateException;
import com.epam.esm.service.exception.PageNumberValidationException;
import com.epam.esm.service.exception.PageSizeValidationException;
import com.epam.esm.service.exception.PaginationException;
import com.epam.esm.service.exception.TagDoesNotExistException;
import com.epam.esm.service.pagination.CustomPagination;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.dao.GiftCertificateRepository;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.dao.OrderRepository;
import com.epam.esm.repository.dao.TagRepository;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.pagination.PaginationConverter;
import com.epam.esm.service.util.Message;
import com.epam.esm.service.validator.GiftCertificateValidator;
import com.epam.esm.service.validator.PaginationValidator;
import com.epam.esm.service.validator.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final GiftCertificateRepository giftCertificateRepository;
    private final TagRepository tagRepository;
    private final OrderRepository orderRepository;
    private final GiftCertificateValidator giftCertificateValidator;
    private final TagValidator tagValidator;
    private final PaginationValidator paginationValidator;
    private final PaginationConverter paginationConverter;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepository giftCertificateRepository,
                                      TagRepository tagRepository,
                                      OrderRepository orderDao,
                                      GiftCertificateValidator giftCertificateValidator,
                                      TagValidator tagValidator,
                                      PaginationValidator paginationValidator,
                                      PaginationConverter paginationConverter) {
        this.giftCertificateRepository = giftCertificateRepository;
        this.tagRepository = tagRepository;
        this.orderRepository = orderDao;
        this.giftCertificateValidator = giftCertificateValidator;
        this.tagValidator = tagValidator;
        this.paginationValidator = paginationValidator;
        this.paginationConverter = paginationConverter;
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

        return giftCertificateRepository.save(giftCertificate);
    }

    private boolean isGiftCertificateExist(GiftCertificate giftCertificate) {
        String giftCertificateName = giftCertificate.getName();
        return giftCertificateRepository.findByName(giftCertificateName).isPresent();
    }

    private List<Tag> createTagsList(GiftCertificate giftCertificate) {
        List<Tag> createdTagList = new ArrayList<>();
        for (Tag tag : giftCertificate.getTagList()) {
            String name = tag.getName();
            if (!tagValidator.isNameValid(name)) {
                throw new FieldValidationException(Message.INVALID_TAG_NAME_MSG);
            }
            Optional<Tag> optionalTag = tagRepository.findByName(name);
            if (optionalTag.isPresent()) {
                tag = optionalTag.get();
            } else {
                tag = tagRepository.save(tag);
            }
            createdTagList.add(tag);
        }

        return createdTagList;
    }

    @Override
    public List<GiftCertificate> findAll(CustomPagination pagination) {
        Pageable pageable = paginationConverter.convert(pagination);
        Page<GiftCertificate> giftCertificatePage = giftCertificateRepository.findAll(pageable);

        int lastPage = giftCertificatePage.getTotalPages();
        if(!paginationValidator.isPageValid(pagination, lastPage)) {
            String message = String.format(Message.PAGE_NUMBER_INVALID_MSG, lastPage);
            throw new PageNumberValidationException(message);
        }

        return giftCertificatePage.getContent();
    }

    @Override
    public GiftCertificate findByGiftCertificateId(Long id) {
        return giftCertificateRepository.findById(id)
                .orElseThrow(() -> new GiftCertificateNotFoundException(String
                .format(Message.GIFT_CERTIFICATE_ID_NOT_FOUND_MSG, id)));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        GiftCertificate giftCertificate = giftCertificateRepository.findById(id).orElseThrow(() ->
                new GiftCertificateNotFoundException(String.format(Message.GIFT_CERTIFICATE_ID_NOT_FOUND_MSG, id)));

        if (!orderRepository.findByGiftCertificateId(id).isEmpty()) {
            throw new HasOrderToGiftCertificateException(Message.CANNOT_BE_DELETED_GIFT_CERTIFICATE_MSG);
        }

        giftCertificateRepository.delete(giftCertificate);
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

        return giftCertificateRepository.save(updatedGiftCertificate);
    }

    @Transactional
    @Override
    public List<GiftCertificate> findByAttributes(String name, List<String> tagNames, List<String> columnNames,
                                                  String sortType, CustomPagination pagination) {
        checkIfExistsGiftCertificateByName(name);
        checkTagsByName(tagNames);
        checkColumnNames(columnNames);
        checkSortType(sortType);

        if (pagination.getPage() == 0 && pagination.getSize() == 0) {
            throw new PaginationException(Message.CHOOSE_PAGINATION_MSG);
        }

        Pageable pageable;
        if (columnNames != null) {
            Sort sort = createSort(columnNames, sortType);
            pageable = createPageableWithSort(pagination, sort);
        } else {
            pageable = paginationConverter.convert(pagination);
        }

        Page<GiftCertificate> giftCertificatePage;
        if (name == null && tagNames ==null) {
            giftCertificatePage = giftCertificateRepository.findAll(pageable);
        } else {
            giftCertificatePage = giftCertificateRepository.findByAttributes(name, tagNames, pageable);
        }

        int lastPage = giftCertificatePage.getTotalPages();
        if(!paginationValidator.isPageValid(pagination, lastPage)) {
            String message = String.format(Message.PAGE_NUMBER_INVALID_MSG, lastPage);
            throw new PageNumberValidationException(message);
        }

        return giftCertificatePage.getContent();
    }

    private Pageable createPageableWithSort(CustomPagination pagination, Sort sort) {
        if(!paginationValidator.isSizeValid(pagination)) {
            throw new PageSizeValidationException(Message.PAGE_SIZE_INVALID_MSG);
        }

        int page = pagination.getPage();
        int size = pagination.getSize();

        try {
            return PageRequest.of(page, size, sort);
        } catch (IllegalArgumentException e) {
            throw new PageNumberValidationException(Message.NEGATIVE_PAGE_NUMBER);
        }
    }

    private Sort createSort (List<String> columnNames, String sortType) {
        List<Sort.Order> orderList = columnNames.stream().map(Sort.Order::by).collect(Collectors.toList());

        Sort sort = null;
        if (sortType == null || sortType.equalsIgnoreCase("asc")) {
            sort = Sort.by(orderList).ascending();
        } else {
            if (sortType.equalsIgnoreCase("desc")) {
                sort = Sort.by(orderList).descending();
            }
        }

        return sort;
    }

    private void checkIfExistsGiftCertificateByName(String name) {
        if (name != null) {
            if (giftCertificateRepository.findByPartOfName(name).isEmpty()) {
                throw new GiftCertificateNotFoundException(String.format(Message.GIFT_CERTIFICATE_NAME_NOT_FOUND_MSG, name));
            }
        }
    }

    private void checkTagsByName(List<String> tagNames) {
        if (tagNames != null) {
            tagNames.forEach(tagName -> tagRepository.findByName(tagName)
                    .orElseThrow(() -> new TagDoesNotExistException(String
                            .format(Message.TAG_DOES_NOT_EXIST_MSG, tagName))));

            if (giftCertificateRepository.findGiftCertificatesByTagNames(tagNames).isEmpty()) {
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

