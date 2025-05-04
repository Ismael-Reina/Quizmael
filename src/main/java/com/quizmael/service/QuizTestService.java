package com.quizmael.service;

import com.quizmael.model.QuizTest;

import java.util.List;
import java.util.Optional;

/**
 * <strong>QuizTestService</strong> defines the contract for operations related to test creation,
 * retrieval, moderation, gameplay, and user interactions like favorites.
 *
 * <p>This service abstracts the business logic and acts as a bridge between the controller layer
 * and the data access layer for all test-related operations.</p>
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public interface QuizTestService {

    // ------------------------------------------------------------
    //           Management of User-Created Tests
    // ------------------------------------------------------------

    /**
     * Finds all tests created by a specific user.
     *
     * @param userId the ID of the user who created the tests
     * @return a list of tests created by the given user
     */
    List<QuizTest> findTestsByCreator(int userId);

    /**
     * Finds a test by its unique ID.
     *
     * @param testId the ID of the test to retrieve
     * @return an {@link Optional} containing the found test, or empty if not found
     */
    Optional<QuizTest> findTestById(int testId);

    /**
     * Creates a new test.
     *
     * @param test the test to create
     */
    void createTest(QuizTest test);

    /**
     * Updates an existing test.
     *
     * @param test the test with updated information
     */
    void updateTest(QuizTest test);

    /**
     * Deletes a test by its ID.
     *
     * @param testId the ID of the test to delete
     */
    void deleteTest(int testId);

    // ------------------------------------------------------------
    //                Retrieving Public Tests
    // ------------------------------------------------------------

    /**
     * Retrieves all public tests (those validated and not in draft state).
     *
     * @return a list of public tests
     */
    List<QuizTest> findAllPublicTests();

    /**
     * Finds public tests by topic name.
     *
     * @param topicName the name of the topic to search by
     * @return a list of tests related to the given topic
     */
    List<QuizTest> findTestsByTopic(String topicName);

    /**
     * Finds public tests by title.
     *
     * @param title the title to search for
     * @return a list of tests matching the title
     */
    List<QuizTest> findTestsByTitle(String title);

}