package com.epam.esm.repository.dao.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.dao.TagDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class TagDaoImpl implements TagDao {
    private static final String FIND_TAG_BY_ID = "SELECT id, name FROM tag WHERE id = ?";
    private static final String FIND_TAG_BY_NAME = "SELECT id, name FROM tag WHERE name = ?";
    private static final String CREATE_TAG = "INSERT INTO tag (name) VALUES (?)";
    private static final String DELETE_TAG_BY_ID = "DELETE FROM tag WHERE id = ?";
    private static final String FIND_ALL_TAGS = "SELECT id, name FROM tag";
    private static final int FIRST_INDEX = 1;
    private static final int NUMBER_OF_CHANGED_ROWS = 1;

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Tag> rowMapper;

    @Autowired
    public TagDaoImpl(JdbcTemplate jdbcTemplate, RowMapper<Tag> rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

    @Override
    public Tag create(Tag entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(CREATE_TAG, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(FIRST_INDEX, entity.getName());

            return preparedStatement;
        }, keyHolder);

        entity.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        return entity;
    }

    @Override
    public List<Tag> findAll() {
        return jdbcTemplate.query(FIND_ALL_TAGS, rowMapper);
    }

    @Override
    public Optional<Tag> findById(Long id) {
        Optional<Tag> optionalTag;

        try {
            optionalTag = Optional.ofNullable(jdbcTemplate.queryForObject(FIND_TAG_BY_ID, rowMapper, id));
        } catch(DataAccessException e) {
            optionalTag = Optional.empty();
        }

        return optionalTag;
    }

    @Override
    public Optional<Tag> findByName(String name) {
        Optional<Tag> optionalTag;

        try {
            optionalTag = Optional.ofNullable(jdbcTemplate.queryForObject(FIND_TAG_BY_NAME, rowMapper, name));
        } catch(DataAccessException e) {
            optionalTag = Optional.empty();
        }

        return optionalTag;
    }

    @Override
    public boolean delete(Long id) {
        return jdbcTemplate.update(DELETE_TAG_BY_ID, id) == NUMBER_OF_CHANGED_ROWS;
    }
}

