package com.epam.esm.repository.dao.impl;

import com.epam.esm.repository.config.TestConfig;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.dao.TagRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {TestConfig.class})
@ExtendWith(SpringExtension.class)
@Transactional
@ActiveProfiles("dev")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TagDaoImplTest {
    private static final int EXPECTED_LIST_SIZE = 4;
    private static final String TAG_NAME = "second";
    private static final Long TAG_ID = 2L;
    private static final String CREATED_TAG_NAME = "newTag";

    private final TagRepository tagDaoImpl;
//    private CustomPagination pagination;

    @Autowired
    public TagDaoImplTest(TagRepository tagDaoImpl) {
        this.tagDaoImpl = tagDaoImpl;
    }

    @BeforeAll
    void beforeAll() {
//        pagination = new CustomPagination();
//        pagination.setPage(1);
//        pagination.setSize(10);
    }

//    @Test
//    void create() {
//        Tag tag = new Tag();
//        tag.setName(CREATED_TAG_NAME);
//        Tag actual = tagDaoImpl.create(tag);
//        assertEquals(CREATED_TAG_NAME, actual.getName());
//    }
//
//    @Test
//    void findAll() {
//        List<Tag> tags = tagDaoImpl.findAll(pagination, Tag.class);
//        assertEquals(EXPECTED_LIST_SIZE, tags.size());
//    }

//    @Test
//    void findById() {
//        Optional<Tag> optionalTag = tagDaoImpl.findById(TAG_ID, Tag.class);
//        assertTrue(optionalTag.isPresent());
//    }

    @Test
    void findByName() {
        Optional<Tag> optionalTag = tagDaoImpl.findByName(TAG_NAME);
        assertTrue(optionalTag.isPresent());
    }
}
