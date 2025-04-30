package com.quizmael.dao;

import com.quizmael.model.Question;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for {@link Question} entity.
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public interface QuestionDao {

    /**
     * Saves a new question to the database.
     *
     * @param question the question to save
     */
    void save(Question question);

    /**
     * Updates an existing question in the database.
     *
     * @param question the question to update
     */
    void update(Question question);

    /**
     * Deletes a question from the database.
     *
     * @param question the question to delete
     */
    void delete(Question question);

    /**
     * Finds a question by its ID.
     *
     * @param id the ID of the question
     * @return an Optional containing the found question, or empty if not found
     */
    Optional<Question> findById(Integer id);

    /**
     * Finds all questions in the database.
     *
     * @return a list of all questions
     */
    List<Question> findAll();

}
