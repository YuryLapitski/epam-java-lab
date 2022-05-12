package com.epam.esm.repository.dao;

import com.epam.esm.entity.Tag;

import java.util.Optional;

/**
 * The TagDao interface provides methods for creating, reading,
 * and deleting tags.
 *
 * @author Yury Lapitski
 * @version 1.0
 */
public interface TagDao extends EntityDao<Tag> {

    /**
     * Searches for tag by tag name
     *
     * @param name name of the tag to find
     * @return founded Optional of tag
     */
    Optional<Tag> findByName(String name);
}
