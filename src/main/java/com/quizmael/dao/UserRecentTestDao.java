package com.quizmael.dao;

import com.quizmael.model.UserRecentTest;
import com.quizmael.model.UserRecentTestId;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) interface for managing {@link UserRecentTest} entity.
 * Provides methods to perform CRUD operations on UserRecentTest entities.
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public interface UserRecentTestDao {

    /**
     * Saves a new UserRecentTest entity to the database.
     *
     * @param entity the UserRecentTest entity to save
     */
    void save(UserRecentTest entity);

    /**
     * Deletes a UserRecentTest entity from the database.
     *
     * @param entity the UserRecentTest entity to delete
     */
    void delete(UserRecentTest entity);

    /**
     * Finds a UserRecentTest entity by its composite key.
     *
     * @param id the composite key of the UserRecentTest entity
     * @return an Optional containing the found UserRecentTest entity, or empty if not found
     */
    Optional<UserRecentTest> findById(UserRecentTestId id);

    /**
     * Finds all UserRecentTest entities in the database.
     *
     * @return a list of all UserRecentTest entities
     */
    List<UserRecentTest> findAll();
}