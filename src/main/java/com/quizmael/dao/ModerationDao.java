package com.quizmael.dao;

import com.quizmael.model.Moderation;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for {@link Moderation} entity.
 * Provides methods to interact with moderation records.
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public interface ModerationDao {

    /**
     * Saves a new moderation record to the database.
     *
     * @param moderation the moderation record to save
     */
    void save(Moderation moderation);

    /**
     * Updates an existing moderation record in the database.
     *
     * @param moderation the moderation record to update
     */
    void update(Moderation moderation);

    /**
     * Deletes a moderation record from the database.
     *
     * @param moderation the moderation record to delete
     */
    void delete(Moderation moderation);

    /**
     * Finds a moderation record by its ID.
     *
     * @param id the ID of the moderation record to find
     * @return an Optional containing the found moderation record, or empty if not found
     */
    Optional<Moderation> findById(Integer id);

    /**
     * Finds all moderation records in the database.
     *
     * @return a list of all moderation records
     */
    List<Moderation> findAll();
}
