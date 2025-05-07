package com.quizmael.service;

import com.quizmael.model.QuizTest;

import java.util.List;

/**
 * Service interface for managing user interactions with quiz tests,
 * such as marking tests as favorites or retrieving favorite tests.
 * <p>
 * This interface defines operations related to how users interact with tests
 * outside of gameplay, helping to personalize the user experience.
 * </p>
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public interface UserInteractionService {

    /**
     * Adds a test to the user's list of favorites.
     *
     * @param userId the ID of the user
     * @param testId the ID of the test to add as favorite
     */
    void addFavoriteTest(int userId, int testId);

    /**
     * Removes a test from the user's list of favorites.
     *
     * @param userId the ID of the user
     * @param testId the ID of the test to remove from favorites
     */
    void removeFavoriteTest(int userId, int testId);

    /**
     * Retrieves the list of favorite tests for a user.
     *
     * @param userId the ID of the user
     * @return       a list of the user's favorite tests
     */
    List<QuizTest> getFavoriteTests(int userId);

}
