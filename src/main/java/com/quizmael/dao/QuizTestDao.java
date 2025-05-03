package com.quizmael.dao;

import com.quizmael.model.QuizTest;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for {@link QuizTest} entity.
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public interface QuizTestDao {

    /**
     * Saves a new test to the database.
     *
     * @param test the test to save
     */
    void save(QuizTest test);

    /**
     * Updates an existing test in the database.
     *
     * @param test the test to update
     */
    void update(QuizTest test);

    /**
     * Deletes a test from the database.
     *
     * @param testId the test to delete
     */
    void delete(int testId);

    /**
     * Finds a test by its ID.
     *
     * @param testId the ID of the test to find
     * @return an Optional containing the found test, or empty if not found
     */
    Optional<QuizTest> findById(int testId);

    /**
     * Finds all tests created by a specific user.
     *
     * @param userId the ID of the user
     * @return a list of tests created by the user
     */
    List<QuizTest> findByCreatorId(int userId);

    /**
     * Finds all tests in the database.
     *
     * @return a list of all tests
     */
    List<QuizTest> findAll();

}
