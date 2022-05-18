package com.epam.esm.repository.dao;

import com.epam.esm.pagination.CustomPagination;
import java.util.List;
import java.util.Optional;

public interface EntityDao<T> {

    /**
     * Creates new entity
     *
     * @param entity entity to create
     * @return Entity
     */
    T create(T entity);

    /**
     * Searches for all entities
     *
     * @param pagination pagination parameters
     * @param entityClass class of the entity
     * @return founded list of entities
     */
    List<T> findAll(CustomPagination pagination, Class<T> entityClass);

    /**
     * Searches for entity by ID
     *
     * @param id id of the entity to find
     * @param entityClass class of the entity
     * @return founded Optional of entity
     */
    Optional<T> findById(Long id, Class<T> entityClass);

    /**
     * Deletes entity
     *
     * @param id id of the entity to delete
     * @param entityClass class of the entity
     */
    void delete(Long id, Class<T> entityClass);

    /**
     * Searches for number of entities in data base
     *
     * @param entityClass class of the entity to find number of entities
     * @return number of entities
     */
    Long findEntitiesNumber(Class<T> entityClass);
}
