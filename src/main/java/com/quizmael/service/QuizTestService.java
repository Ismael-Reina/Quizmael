package com.quizmael.service;

import com.quizmael.model.QuizTest;
import com.quizmael.model.enums.Language;
import com.quizmael.model.enums.State;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    //                  Test Retrieval & Filtering
    // ------------------------------------------------------------

    /**
     * Retrieves all tests from the system (admin use).
     *
     * @return a list of all tests
     */
    List<QuizTest> findAllTests();

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
     * Finds public tests by various filters.
     *
     * @param topicName   the name of the topic, or null if no topic filter is applied
     * @param creatorName the name of the creator, or null if no creator filter is applied
     * @param difficulty  the difficulty level, or null if no difficulty filter is applied
     * @param languages   a set of languages to include
     * @return a list of public tests matching the filters
     */
    List<QuizTest> findPublicTestsByFilters(String topicName, String creatorName, Integer difficulty, Set<Language> languages);

    /**
     * Retrieves all distinct creators of published tests.
     *
     * @return a list of unique creator names
     */
    List<String> findAllCreatorsOfPublishedTests();

    // ------------------------------------------------------------
    //                  Test Lifecycle & Management
    // ------------------------------------------------------------
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
     * Updates the state of a test with the specified ID.
     * If the test exists, its state and last update timestamp are modified.
     *
     * @param testId   the ID of the test to update
     * @param newState the new state to set (e.g., DRAFT, PENDING, PUBLISHED, REJECTED)
     */
    boolean updateTestState(Integer testId, State newState);

    /**
     * Deletes a test by its ID.
     *
     * @param testId the ID of the test to delete
     */
    boolean deleteTest(int testId);

    // ------------------------------------------------------------
    //                  User Interactions & Favorites
    // ------------------------------------------------------------
    // Here I will place methods like toggleFavorite, etc.
}