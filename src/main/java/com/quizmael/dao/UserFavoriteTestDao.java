package com.quizmael.dao;

import com.quizmael.model.UserFavoriteTest;
import com.quizmael.model.UserFavoriteTestId;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) interface for managing {@link UserFavoriteTest} entity.
 * Provides methods to perform CRUD operations on UserFavoriteTest entities.
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public interface UserFavoriteTestDao {

    /**
     * Saves a new UserFavoriteTest entity to the database.
     *
     * @param entity the UserFavoriteTest entity to save
     */
    void save(UserFavoriteTest entity);

    /**
     * Deletes a UserFavoriteTest entity from the database.
     *
     * @param entity the UserFavoriteTest entity to delete
     */
    void delete(UserFavoriteTest entity);

    /**
     * Finds a UserFavoriteTest entity by its composite key.
     *
     * @param id the composite key of the UserFavoriteTest entity
     * @return an Optional containing the found UserFavoriteTest entity, or empty if not found
     */
    Optional<UserFavoriteTest> findById(UserFavoriteTestId id);

    /**
     * Finds all UserFavoriteTest entities in the database.
     *
     * @return a list of all UserFavoriteTest entities
     */
    List<UserFavoriteTest> findAll();

}
