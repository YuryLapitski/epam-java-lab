package com.epam.esm.service;

import com.epam.esm.entity.Tag;
import java.util.List;

/**
 * The TagService interface provides methods for creating, reading,
 * and deleting tags.
 *
 * @author Yury Lapitski
 * @version 1.0
 */
public interface TagService {

    /**
     * Creates new tag
     *
     * @param tag tag to create
     * @return Tag
     */
    Tag create(Tag tag);

    /**
     * Searches for all tags
     *
     * @return founded list of tags
     */
    List<Tag> findAll();

    /**
     * Searches for tag by tag ID
     *
     * @param id id of the tag to find
     * @return founded tag
     */
    Tag findById(Long id);

    /**
     * Deletes tag
     *
     * @param id id of the tag to delete
     */
    boolean delete(Long id);
}
