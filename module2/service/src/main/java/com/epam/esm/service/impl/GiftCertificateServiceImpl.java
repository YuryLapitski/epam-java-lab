package com.epam.esm.service.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.dao.TagDao;
import com.epam.esm.repository.dao.TagToGiftCertificateDao;
import com.epam.esm.service.TagToGiftCertificateService;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.exception.*;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.validator.GiftCertificateValidator;
import com.epam.esm.service.validator.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private static final String GIFT_CERTIFICATE_ID_NOT_FOUND_MSG = "Gift certificate with id=%d not found.";
    private static final String GIFT_CERTIFICATE_NAME_NOT_FOUND_MSG = "Gift certificate with name '%s' not found.";
    private static final String GIFT_CERTIFICATE_ALREADY_EXIST_MSG = "Gift certificate with the name '%s' " +
            "already exists";
    private static final String CANNOT_UPDATE_GIFT_CERTIFICATE_MSG = "Cannot update gift certificate";
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
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String PRICE = "price";
    private static final String DURATION = "duration";
    private static final String LAST_UPDATE_DATE = "last_update_date";
    private static final double ZERO_DURATION = 0;
    private final GiftCertificateDao giftCertificateDao;
    private final TagDao tagDao;
    private final TagToGiftCertificateDao tagToGiftCertificateDao;
    private final TagToGiftCertificateService tagToGiftCertificateService;
    private final GiftCertificateValidator giftCertificateValidator;
    private final TagValidator tagValidator;


    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao,
                                      TagDao tagDao,
                                      TagToGiftCertificateDao tagToGiftCertificateDao,
                                      TagToGiftCertificateService tagToGiftCertificateService, GiftCertificateValidator giftCertificateValidator,
                                      TagValidator tagValidator) {
        this.giftCertificateDao = giftCertificateDao;
        this.tagDao = tagDao;
        this.tagToGiftCertificateDao = tagToGiftCertificateDao;
        this.tagToGiftCertificateService = tagToGiftCertificateService;
        this.giftCertificateValidator = giftCertificateValidator;
        this.tagValidator = tagValidator;
    }

    @Transactional
    @Override
    public GiftCertificateDto create(GiftCertificateDto giftCertificateDto) {
        if (giftCertificateDto.getGiftCertificate().getName() == null
                || giftCertificateDto.getGiftCertificate().getDescription() == null
                || giftCertificateDto.getGiftCertificate().getPrice() == null
                || giftCertificateDto.getGiftCertificate().getDuration() == 0) {
            throw new FieldValidationException(CANNOT_BE_EMPTY_FIELDS_MSG);
        }

        if (giftCertificateDto.getTags() == null) {
            giftCertificateDto.setTags(new ArrayList<>());
        }

        if (!isValidGiftCertificate(giftCertificateDto.getGiftCertificate())
                || !isValidTags(giftCertificateDto.getTags()) && giftCertificateDto.getTags() != null) {
            throw new FieldValidationException(INVALID_FIELDS_MSG);
        }


        if (isGiftCertificateExist(giftCertificateDto)) {
            throw new GiftCertificateAlreadyExistException(String
                    .format(GIFT_CERTIFICATE_ALREADY_EXIST_MSG, giftCertificateDto.getGiftCertificate().getName()));
        }

        GiftCertificate giftCertificate = giftCertificateDao.create(giftCertificateDto.getGiftCertificate());
        List<Tag> tagList = createTagsList(giftCertificateDto);
        giftCertificateDto.setGiftCertificate(giftCertificate);
        giftCertificateDto.setTags(tagList);

        return giftCertificateDto;
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

    private boolean isGiftCertificateExist(GiftCertificateDto giftCertificateDto) {
        String giftCertificateName = giftCertificateDto.getGiftCertificate().getName();

        return giftCertificateDao.findByName(giftCertificateName).isPresent();
    }

    private List<Tag> createTagsList(GiftCertificateDto giftCertificateDto) {
        if (giftCertificateDto.getTags() == null) {
            return Collections.emptyList();
        }

        GiftCertificate giftCertificate = giftCertificateDto.getGiftCertificate();

        List<Tag> tagList = new ArrayList<>();

        for (Tag tag : giftCertificateDto.getTags()) {
            Optional<Tag> optionalTag = tagDao.findByName(tag.getName());
            if (optionalTag.isPresent()) {
                tag = optionalTag.get();
            } else {
                tag = tagDao.create(tag);
            }
            tagList.add(tag);
            long tagId = tag.getId();
            tagToGiftCertificateDao.createTagToGiftCertificateRelation(tagId, giftCertificate.getId());
        }

        return tagList;
    }

    @Override
    public List<GiftCertificateDto> findAll() {
        List <GiftCertificate> giftCertificateList = giftCertificateDao.findAll();

        return createGiftCertificateDtoList(giftCertificateList);
    }

    @Override
    public GiftCertificateDto findByGiftCertificateId(Long id) {
        GiftCertificate giftCertificate = giftCertificateDao.findById(id)
                .orElseThrow(() -> new GiftCertificateNotFoundException(String
                .format(GIFT_CERTIFICATE_ID_NOT_FOUND_MSG, id)));

        return createGiftCertificateDto(giftCertificate);
    }

    @Override
    public List<GiftCertificateDto> findByPartOfName(String name) {
        List<GiftCertificate> giftCertificates = giftCertificateDao.findByPartOfName(name);

        if (giftCertificates.isEmpty()) {
            throw new GiftCertificateNotFoundException(String.format(GIFT_CERTIFICATE_NAME_NOT_FOUND_MSG, name));
        }

        return createGiftCertificateDtoList(giftCertificates);
    }

    @Override
    public List<GiftCertificateDto> findAllWithSort(String columnName, String sortType) {
        if (!giftCertificateValidator.isColumnNameValid(columnName)) {
            throw new InvalidColumnNameException(INVALID_COLUMN_NAME_MSG);
        }

        if (!giftCertificateValidator.isSortTypeValid(sortType)) {
            throw new InvalidSortTypeException(INVALID_SORT_TYPE_MSG);
        }

        List<GiftCertificate> giftCertificates = giftCertificateDao.findAllWithSort(columnName, sortType);
        return createGiftCertificateDtoList(giftCertificates);
    }

    @Transactional
    @Override
    public boolean delete(Long id) {
        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateDao.findById(id);

        if (!optionalGiftCertificate.isPresent()) {
            String msg = String.format(GIFT_CERTIFICATE_ID_NOT_FOUND_MSG, id);
            throw new GiftCertificateNotFoundException(msg);
        }

        tagToGiftCertificateDao.deleteByGiftCertificateId(id);
        return giftCertificateDao.delete(id);
    }

    @Transactional
    @Override
    public GiftCertificateDto update(Long giftCertificateId, GiftCertificateDto giftCertificateDto) {
        if (!giftCertificateDao.findById(giftCertificateId).isPresent()) {
            String msg = String.format(GIFT_CERTIFICATE_ID_NOT_FOUND_MSG, giftCertificateId);
            throw new GiftCertificateNotFoundException(msg);
        }

        Map<String, Object> paramMap = fillMap(giftCertificateDto.getGiftCertificate());
        GiftCertificate giftCertificate = giftCertificateDao.update(giftCertificateId, paramMap).orElseThrow(() ->
                new CannotUpdateException(CANNOT_UPDATE_GIFT_CERTIFICATE_MSG));

        giftCertificate.setId(giftCertificateId);
        giftCertificateDto.setGiftCertificate(giftCertificate);

        tagToGiftCertificateDao.deleteByGiftCertificateId(giftCertificateId);
        List<Tag> tagList;
        if (giftCertificateDto.getTags() == null) {
            tagList = Collections.emptyList();
        } else {
            tagList = createTagsList(giftCertificateDto);
        }

        giftCertificateDto.setTags(tagList);

        return giftCertificateDto;
    }

    @Override
    public List<GiftCertificateDto> findByAttributes(String name, String tagName,
                                                     String columnName, String sortType) {
        List<GiftCertificateDto> giftCertificateDtoList = new ArrayList<>();

        if (name != null) {
            giftCertificateDtoList = findByPartOfName(name);
        }
        if (tagName != null) {
            giftCertificateDtoList = tagToGiftCertificateService.findGiftCertificatesByTagName(tagName);
        }
        if (columnName != null && sortType != null) {
            giftCertificateDtoList = findAllWithSort(columnName, sortType);
        }
        if (name == null && tagName == null && columnName == null && sortType == null) {
            giftCertificateDtoList = findAll();
        }

        return giftCertificateDtoList;
    }

    private Map<String, Object> fillMap(GiftCertificate giftCertificate) {
        Map<String, Object> filledMap = new HashMap<>();
        if (giftCertificate.getName() != null) {
            if (!giftCertificateValidator.isNameValid(giftCertificate.getName())) {
                throw new FieldValidationException(INVALID_NAME_MSG);
            }
            filledMap.put(NAME, giftCertificate.getName());
        }
        if (giftCertificate.getDescription() != null) {
            if (!giftCertificateValidator.isDescriptionValid(giftCertificate.getDescription())) {
                throw new FieldValidationException(INVALID_DESCRIPTION_MSG);
            }
            filledMap.put(DESCRIPTION, giftCertificate.getDescription());
        }
        if (giftCertificate.getPrice() != null) {
            if (!giftCertificateValidator.isPriceValid(giftCertificate.getPrice())) {
                throw new FieldValidationException(INVALID_PRICE_MSG);
            }
            filledMap.put(PRICE, giftCertificate.getPrice());
        }
        if (giftCertificate.getDuration() != ZERO_DURATION) {
            if (!giftCertificateValidator.isDurationValid(giftCertificate.getDuration())) {
                throw new FieldValidationException(INVALID_DURATION_MSG);
            }
            filledMap.put(DURATION, giftCertificate.getDuration());
        }

        Timestamp lastUpdateDate = Timestamp.valueOf(LocalDateTime.now());
        filledMap.put(LAST_UPDATE_DATE, lastUpdateDate);

        return filledMap;
    }

    private GiftCertificateDto createGiftCertificateDto(GiftCertificate giftCertificate) {
        List<Tag> tags = new ArrayList<>(tagToGiftCertificateDao.findByGiftCertificateId(giftCertificate.getId()));
        GiftCertificateDto giftCertificateDto = new GiftCertificateDto();
        giftCertificateDto.setGiftCertificate(giftCertificate);
        giftCertificateDto.setTags(tags);

        return giftCertificateDto;
    }

    private List<GiftCertificateDto> createGiftCertificateDtoList(List<GiftCertificate> giftCertificates) {
        List<GiftCertificateDto> giftCertificateDtoList = new ArrayList<>();
        for (GiftCertificate giftCertificate : giftCertificates) {
            GiftCertificateDto giftCertificateDto = createGiftCertificateDto(giftCertificate);
            giftCertificateDtoList.add(giftCertificateDto);
        }
        return giftCertificateDtoList;
    }
}
