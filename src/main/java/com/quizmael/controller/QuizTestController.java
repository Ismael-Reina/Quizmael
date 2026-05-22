package com.quizmael.controller;

import com.quizmael.model.QuizTest;
import com.quizmael.model.enums.Language;
import com.quizmael.model.enums.State;
import com.quizmael.service.QuizTestService;

import java.util.List;
import java.util.Set;

/**
 * <strong>Controller</strong> responsible for quiz test operations and UI data binding.
 *
 * <p>Handles the retrieval and filtering of tests for the selection panels,
 * abstracting the search logic from the view layer. It also serves as the entry
 * point for future test creation and management operations.</p>
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public class QuizTestController {

    private final QuizTestService quizTestService;

    /**
     * Constructs a new QuizTestController with its required dependencies.
     *
     * @param quizTestService the service responsible for quiz test data and filtering logic.
     */
    public QuizTestController(QuizTestService quizTestService) {
        this.quizTestService = quizTestService;
    }

    // ------------------------------------------------------------
    //                  Data Retrieval
    // ------------------------------------------------------------
    /**
     * Fetches all community quizzes from the service layer for administrative moderation.
     * @return a list of all quiz tests
     */
    public List<QuizTest> findAllTests() {
        // Delegates directly to the business logic layer
        return quizTestService.findAllTests();
    }

    /**
     * Finds a list of public tests based on the selected filters from the UI.
     *
     * <p>This method bridges the UI filter selection with the business logic.
     * If a filter is not selected (e.g., "All topics"), a {@code null} value
     * should be passed for that specific parameter.</p>
     *
     * @param topicName   the selected topic name, or null
     * @param creatorName the selected creator name, or null
     * @param difficulty  the selected difficulty level, or null
     * @param languages   the set of selected languages
     * @return a list of {@link QuizTest} matching the criteria.
     */
    public List<QuizTest> findFilteredPublicTests(String topicName, String creatorName, Integer difficulty, Set<Language> languages) {
        return quizTestService.findPublicTestsByFilters(topicName, creatorName, difficulty, languages);
    }

    // ------------------------------------------------------------
    //                  Administrative Operations
    // ------------------------------------------------------------

    /**
     * Deletes a quiz and cascades the deletion to all its questions and answers.
     */
    public boolean deleteTest(Integer testId) {
        return quizTestService.deleteTest(testId);
    }

    /**
     * Updates the moderation state of a specific quiz test.
     * @param testId the ID of the target test
     * @param newState the new lifecycle State to apply
     * @return true if the update transaction succeeded, false otherwise
     */
    public boolean updateTestState(Integer testId, State newState) {
        // Delegates directly to the existing method in your service implementation
        return quizTestService.updateTestState(testId, newState);
    }
}