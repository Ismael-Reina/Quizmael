package com.quizmael.dao;

import com.quizmael.model.QuizTest;
import com.quizmael.model.enums.State;

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

    /**
     * Finds all public tests (those validated and not in draft state).
     *
     * @return a list of public tests
     */
    public List<QuizTest> findAllPublic();

    /**
     * Finds public tests by topic name.
     *
     * @param topicName the name of the topic
     * @return a list of public tests related to the given topic
     */
    public List<QuizTest> findByTopic(String topicName);

    /**
     * Finds public tests by title.
     *
     * @param title the title of the test
     * @return a list of public tests related to the given title
     */
    public List<QuizTest> findByTitleContaining(String title);

    /**
     * Retrieves all tests that are in the given state.
     *
     * @param state the state to filter tests by
     * @return a list of tests in the specified state
     */
    List<QuizTest> findByState(State state);

    /**
     * Retrieves all rejected tests created by a specific user.
     *
     * @param userId the ID of the user who created the tests
     * @return list of rejected tests authored by the user
     */
    List<QuizTest> findRejectedTestsByUser(int userId);

    /**
     * Retrieves all tests moderated by a specific moderator.
     *
     * @param moderatorId the ID of the moderator
     * @return list of tests moderated by the given moderator
     */
    List<QuizTest> findModeratedBy(int moderatorId);


}
