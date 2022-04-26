package com.epam.esm.repository.dao.impl;

import com.epam.esm.repository.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.util.SqlQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {

    private static final String FIND_GIFT_CERTIFICATE_BY_ID = "SELECT id, name, description, price, duration," +
            "create_date, last_update_date FROM gift_certificate WHERE id = ?";
    private static final String FIND_GIFT_CERTIFICATE_BY_NAME = "SELECT id, name, description, price, duration," +
            " create_date, last_update_date FROM gift_certificate WHERE name = ?";
    private static final String FIND_GIFT_CERTIFICATE_BY_PART_OF_NAME = "SELECT id, name, description, price, " +
            "duration, create_date, last_update_date FROM gift_certificate WHERE name LIKE ('%%' '%s' '%%')";
    private static final String CREATE_GIFT_CERTIFICATE = "INSERT INTO gift_certificate(name, description, price," +
            " duration, create_date, last_update_date) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String DELETE_GIFT_CERTIFICATE_BY_ID = "DELETE FROM gift_certificate WHERE id = ?";
    private static final String FIND_ALL_GIFT_CERTIFICATES = "SELECT id, name, description, price, duration, " +
            "create_date, last_update_date FROM gift_certificate";

    private static final int FIRST_INDEX = 1;
    private static final int SECOND_INDEX = 2;
    private static final int THIRD_INDEX = 3;
    private static final int FOURTH_INDEX = 4;
    private static final int FIFTH_INDEX = 5;
    private static final int SIXTH_INDEX = 6;
    private static final int NUMBER_OF_CHANGED_ROWS = 1;

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<GiftCertificate> rowMapper;

    @Autowired
    public GiftCertificateDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        rowMapper = BeanPropertyRowMapper.newInstance(GiftCertificate.class);
    }

    @Override
    public GiftCertificate create(GiftCertificate entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        LocalDateTime dateTime = LocalDateTime.now();

        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(CREATE_GIFT_CERTIFICATE,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(FIRST_INDEX, entity.getName());
            preparedStatement.setString(SECOND_INDEX, entity.getDescription());
            preparedStatement.setBigDecimal(THIRD_INDEX, entity.getPrice());
            preparedStatement.setShort(FOURTH_INDEX, entity.getDuration());
            preparedStatement.setTimestamp(FIFTH_INDEX, Timestamp.valueOf(dateTime));
            preparedStatement.setTimestamp(SIXTH_INDEX, Timestamp.valueOf(dateTime));

            return preparedStatement;
        }, keyHolder);

        entity.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        entity.setCreateDate(dateTime);
        entity.setLastUpdateDate(dateTime);

        return entity;
    }

    @Override
    public List<GiftCertificate> findAll() {
        return jdbcTemplate.query(FIND_ALL_GIFT_CERTIFICATES, rowMapper);
    }

    @Override
    public Optional<GiftCertificate> findById(Long id) {
        Optional<GiftCertificate> optionalGiftCertificate;

        try {
            optionalGiftCertificate = Optional.ofNullable(jdbcTemplate.
                    queryForObject(FIND_GIFT_CERTIFICATE_BY_ID, rowMapper, id));
        } catch (DataAccessException e) {
            optionalGiftCertificate = Optional.empty();
        }

        return optionalGiftCertificate;
    }

    @Override
    public List<GiftCertificate> findByPartOfName(String name) {
        String resultString = String.format(FIND_GIFT_CERTIFICATE_BY_PART_OF_NAME, name);
        return jdbcTemplate.query(resultString, rowMapper);
    }

    @Override
    public List<GiftCertificate> findAllWithSort(String columnName, String sortType) {
        SqlQueryBuilder sqlQueryBuilder = new SqlQueryBuilder();
        String findAllQuery = sqlQueryBuilder.buildFindAndSortQuery(columnName, sortType);
        return jdbcTemplate.query(findAllQuery, rowMapper);
    }

    @Override
    public Optional<GiftCertificate> findByName(String name) {
        Optional<GiftCertificate> optionalGiftCertificate;

        try {
            optionalGiftCertificate = Optional.ofNullable(jdbcTemplate.
                    queryForObject(FIND_GIFT_CERTIFICATE_BY_NAME, rowMapper, name));
        } catch (DataAccessException e) {
            optionalGiftCertificate = Optional.empty();
        }

        return optionalGiftCertificate;
    }

    @Override
    public Optional<GiftCertificate> update(Long id, Map<String, Object> paramForUpdate) {
        SqlQueryBuilder sqlQueryBuilder = new SqlQueryBuilder();
        String updateQuery = sqlQueryBuilder.buildQueryForUpdate(paramForUpdate);
        List<Object> values = new ArrayList<>(paramForUpdate.values());
        values.add(id);
        jdbcTemplate.update(updateQuery, values.toArray());

        return findById(id);
    }

    @Override
    public boolean delete(Long id) {
        return jdbcTemplate.update(DELETE_GIFT_CERTIFICATE_BY_ID, id) == NUMBER_OF_CHANGED_ROWS;
    }
}
