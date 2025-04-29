package com.quizmael.dao;

import com.quizmael.model.Answer;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) interface for the {@link Answer} entity.
 * <p>Defines basic CRUD operations and queries related to answers.</p>
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public interface AnswerDao {

    /**
     * Saves an answer into the database.
     *
     * @param answer The {@link Answer} entity to be saved.
     */
    void save(Answer answer);

    /**
     * Updates an existing answer in the database.
     *
     * @param answer The {@link Answer} entity to be updated.
     */
    void update(Answer answer);

    /**
     * Deletes an answer from the database.
     *
     * @param answer The {@link Answer} entity to be deleted.
     */
    void delete(Answer answer);

    /**
     * Finds an answer by its ID.
     *
     * @param id The ID of the answer.
     * @return An {@link Optional} containing the found answer, or empty if not found.
     */
    Optional<Answer> findById(Integer id);

    /**
     * Retrieves all answers from the database.
     *
     * @return A list of all {@link Answer} entities.
     */
    List<Answer> findAll();
}
