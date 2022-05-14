package com.epam.esm.repository.dao.impl;

import com.epam.esm.pagination.CustomPagination;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.dao.AbstractEntityDao;
import com.epam.esm.repository.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class GiftCertificateDaoImpl extends AbstractEntityDao<GiftCertificate> implements GiftCertificateDao {
    private static final String NAME_FIELD = "name";
    private static final String TAG_LIST_FIELD = "tagList";
    public static final String PART_OF_NAME = "%%%s%%";
    public static final String DEFAULT_SORT = "asc";
    public static final String ID = "id";
    public static final String NAME = "name";

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
        CriteriaQuery<GiftCertificate> criteriaQuery =
                prepareWhereCriteriaQuery(GiftCertificate.class, NAME_FIELD, name);
        return entityManager.createQuery(criteriaQuery).getResultStream().findFirst();
    }

    @Override
    public List<GiftCertificate> findGiftCertificatesByTagNames(List<String> tagNames) {
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);
        criteriaQuery.select(root);
        criteriaQuery.where(predicateFindByTagNames(root, tagNames));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public GiftCertificate update(GiftCertificate giftCertificate) {
        entityManager.merge(giftCertificate);
        return giftCertificate;
    }

    @Override
    public Long findByAttributesNumber(String name, List<String> tagList) {
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);
        criteriaQuery.select(criteriaBuilder.count(root));
        criteriaQuery.where(predicateFindByAttributes(root, name, tagList));

        return entityManager.createQuery(criteriaQuery).getSingleResult();
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
            if (CollectionUtils.isEmpty(tagList)) return criteriaBuilder.conjunction();
            return tagList.stream().map(tagName -> {
                Join<GiftCertificate, Tag> tagJoin = root.join(TAG_LIST_FIELD);
                return criteriaBuilder.equal(tagJoin.get(NAME), tagName);
            }).reduce(criteriaBuilder.conjunction(), criteriaBuilder::and);
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
            for (String columnName : columnNames) {
                orderList.add(createOrderByField(root, columnName, orderSort));
            }
        }

        return orderList;
    }

    private Order createOrderByField(Root<GiftCertificate> root, String columnName, String sortType) {
        return sortType.equals(DEFAULT_SORT) ?
                criteriaBuilder.asc(root.get(columnName)) :
                criteriaBuilder.desc(root.get(columnName));
    }
}
