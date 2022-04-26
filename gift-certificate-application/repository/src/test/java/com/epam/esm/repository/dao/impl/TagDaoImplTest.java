package com.epam.esm.repository.dao.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.config.SpringJdbcConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringJdbcConfig.class})
@ActiveProfiles("dev")
public class TagDaoImplTest {
    private static final int EXPECTED_LIST_SIZE = 4;
    private static final Long EXPECTED_TAG_ID = 4L;
    private static final String TAG_NAME = "second";
    private static final Long TAG_ID = 2L;
    private static final Long DELETE_TAG_ID = 5L;
    private static final String EXPECTED_TAG_NAME = "fourth";
    private static final String CREATED_TAG_NAME = "newTag";

    private final TagDaoImpl tagDao;

    @BeforeAll
    static void beforeAll() {
        Tag expectedTag = new Tag();
        expectedTag.setId(EXPECTED_TAG_ID);
        expectedTag.setName(EXPECTED_TAG_NAME);
    }

    @Autowired
    public TagDaoImplTest(TagDaoImpl tagDao) {
        this.tagDao = tagDao;
    }

    @Test
    void create() {
        Tag tag = new Tag();
        tag.setName(CREATED_TAG_NAME);
        Tag actual = tagDao.create(tag);
        assertEquals(CREATED_TAG_NAME, actual.getName());
    }

    @Test
    void delete() {
        assertTrue(tagDao.delete(DELETE_TAG_ID));
    }

    @Test
    void findAll() {
        List<Tag> tags = tagDao.findAll();
        assertEquals(EXPECTED_LIST_SIZE, tags.size());
    }

    @Test
    void findById() {
        Optional<Tag> optionalTag = tagDao.findById(TAG_ID);
        assertTrue(optionalTag.isPresent());
    }

    @Test
    void findByName() {
        Optional<Tag> optionalTag = tagDao.findByName(TAG_NAME);
        assertTrue(optionalTag.isPresent());
    }
}
