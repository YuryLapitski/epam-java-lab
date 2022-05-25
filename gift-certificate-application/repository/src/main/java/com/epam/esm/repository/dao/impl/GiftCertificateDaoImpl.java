package com.epam.esm.repository.dao.impl;

import com.epam.esm.pagination.CustomPagination;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.dao.AbstractEntityDao;
import com.epam.esm.repository.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class GiftCertificateDaoImpl extends AbstractEntityDao<GiftCertificate> implements GiftCertificateDao {
    private static final String NAME_FIELD_NAME = "name";
    private static final String TAG_LIST_FIELD_NAME = "tagList";
    private static final String PART_OF_NAME = "%%%s%%";
    private static final String DEFAULT_SORT = "asc";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final Long ZERO_ENTITIES_NUMBER = 0L;

    public GiftCertificateDaoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<GiftCertificate> findByPartOfName(String name) {
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);
        criteriaQuery.select(root);
        Predicate searchPartPredicate = predicateFindByPartOfName(root, name);
        criteriaQuery.where(searchPartPredicate);

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public Optional<GiftCertificate> findByName(String name) {
        Optional<GiftCertificate> optionalGiftCertificate;

        try {
            CriteriaQuery<GiftCertificate> criteriaQuery =
                    prepareWhereCriteriaQuery(GiftCertificate.class, NAME_FIELD_NAME, name);

            GiftCertificate giftCertificate = entityManager.createQuery(criteriaQuery).getSingleResult();

            optionalGiftCertificate = Optional.of(giftCertificate);
        } catch (NoResultException e) {
            optionalGiftCertificate = Optional.empty();
        }

        return optionalGiftCertificate;
    }

    @Override
    public List<GiftCertificate> findGiftCertificatesByTagNames(List<String> tagNames) {
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);

        criteriaQuery.select(root)
                .where(predicateFindByTagNames(root, tagNames));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public GiftCertificate update(GiftCertificate giftCertificate) {
        return entityManager.merge(giftCertificate);
    }

    @Override
    public Long findByAttributesNumber(String name, List<String> tagList) {
        Long entitiesNumber;
        try {
            CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
            Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);
            criteriaQuery.select(criteriaBuilder.count(root));
            criteriaQuery.where(predicateFindByAttributes(root, name, tagList));

            entitiesNumber = entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException e) {
            entitiesNumber = ZERO_ENTITIES_NUMBER;
        }

        return entitiesNumber;
    }

    @Override
    public List<GiftCertificate> findByAttributes (String name, List<String> tagList, List<String> columnNames,
                                                    String sortType, CustomPagination pagination) {
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);
        criteriaQuery.select(root);
        criteriaQuery.where(predicateFindByAttributes(root, name, tagList));
        criteriaQuery.orderBy(createOrderListByFields(root, columnNames, sortType));

        return prepareTypedQuery(criteriaQuery, pagination).getResultList();
    }

    private Predicate predicateFindByAttributes(Root<GiftCertificate> root, String name, List<String> tagList) {
        Predicate tagNamePredicate = predicateFindByTagNames(root, tagList);
        Predicate searchPartPredicate = predicateFindByPartOfName(root, name);

        return criteriaBuilder.and(tagNamePredicate, searchPartPredicate);
    }

    private Predicate predicateFindByTagNames(Root<GiftCertificate> root, List<String> tagList) {
        Predicate predicate;

        if (CollectionUtils.isEmpty(tagList)) {
            predicate = criteriaBuilder.conjunction();
        } else {
            predicate = tagList.stream().map(tagName -> {
                Join<GiftCertificate, Tag> tagJoin = root.join(TAG_LIST_FIELD_NAME);
                return criteriaBuilder.equal(tagJoin.get(NAME), tagName);
            }).reduce(criteriaBuilder.conjunction(), criteriaBuilder::and);
        }

        return predicate;
    }


    private Predicate predicateFindByPartOfName(Root<GiftCertificate> root, String name) {
        return name == null ? criteriaBuilder.conjunction() :
                criteriaBuilder.like(root.get(NAME), String.format(PART_OF_NAME, name));
    }

    private List<Order> createOrderListByFields(Root<GiftCertificate> root,
                                               List<String> columnNames,
                                               String sortType) {
        String orderSort = sortType != null ? sortType : DEFAULT_SORT;

        List<Order> orderList = new ArrayList<>();
        if (CollectionUtils.isEmpty(columnNames)) {
            orderList.add(createOrderByField(root, ID, orderSort));
        } else {
            columnNames.forEach(columnName -> orderList.add(createOrderByField(root, columnName, orderSort)));
        }

        return orderList;
    }

    private Order createOrderByField(Root<GiftCertificate> root, String columnName, String sortType) {
        Path<String> name = root.get(columnName);

        return sortType.equals(DEFAULT_SORT) ? criteriaBuilder.asc(name) : criteriaBuilder.desc(name);
    }
}
