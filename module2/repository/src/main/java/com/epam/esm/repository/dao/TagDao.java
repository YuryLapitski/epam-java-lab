package com.epam.esm.repository.dao;

import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;

/**
 * The TagDao interface provides methods for creating, reading,
 * and deleting tags.
 *
 * @author Yury Lapitski
 * @version 1.0
 */
public interface TagDao {

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
     * @return founded Optional of tag
     */
    Optional<Tag> findById(Long id);

    /**
     * Searches for tag by tag name
     *
     * @param name name of the tag to find
     * @return founded Optional of tag
     */
    Optional<Tag> findByName(String name);

    /**
     * Deletes tag
     *
     * @param id id of the tag to delete
     * @return true if the tag was created
     */
    boolean delete(Long id);
}
