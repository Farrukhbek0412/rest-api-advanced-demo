package com.epam.esm.repository;

import java.util.List;
import java.util.Optional;

/**
 *
 * @param <T> 'T' is Entity type for CRUD operations
 * @param <Id> Primary key of T (object)
 */

public interface CRUDRepository<T, Id>{
    /**
     * Creates a new instance of an entity in the database.
     * @param t Object to create
     * @return optional id of created object
     */
    T create(T t);

    /**
     * Gets all existing entities with provided type and provided limit and offset.
     * @param pageSize count of entities
     * @param pageNo page for the entities
     * @return list of entities
     */
    List<T> getAll(int pageSize, int pageNo);

    /**
     * Gets entity with the provided id.
     * @param id id of the needed object
     * @return optional object of provided type
     */
    Optional<T> findById(Id id);

    /**
     * Updates provided entity object.
     * @param t entity with fields that needed to be updated
     * @return the number of rows affected
     */
    T update(T t);

    /**
     * Deletes entity with the provided id.
     * @param id id of the object to be deleted
     * @return the number of rows affected
     */
    int delete(Id id);
}
