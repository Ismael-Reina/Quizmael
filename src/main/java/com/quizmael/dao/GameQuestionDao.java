package com.quizmael.dao;

import com.quizmael.model.GameQuestion;
import com.quizmael.model.GameQuestionId;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for {@link GameQuestion} entity.
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public interface GameQuestionDao {

    /**
     * Saves a new GameQuestion entity to the database.
     *
     * @param gameQuestion the GameQuestion entity to save
     */
    void save(GameQuestion gameQuestion);

    /**
     * Updates an existing GameQuestion entity in the database.
     *
     * @param gameQuestion the GameQuestion entity to update
     */
    void update(GameQuestion gameQuestion);

    /**
     * Deletes a GameQuestion entity from the database.
     *
     * @param gameQuestion the GameQuestion entity to delete
     */
    void delete(GameQuestion gameQuestion);

    /**
     * Finds a GameQuestion entity by its ID.
     *
     * @param id the ID of the GameQuestion entity to find
     * @return an Optional containing the found GameQuestion entity, or empty if not found
     */
    Optional<GameQuestion> findById(GameQuestionId id);

    /**
     * Finds all GameQuestion entities in the database.
     *
     * @return a List of all GameQuestion entities
     */
    List<GameQuestion> findAll();

}
