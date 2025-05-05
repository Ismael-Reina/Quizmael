package com.quizmael.service;

import com.quizmael.model.QuizTest;

import java.util.List;

/**
 * Service interface for handling moderation operations on quiz tests.
 * This includes reviewing, validating, and rejecting user-submitted tests.
 * Only users with the moderator or administrator role should access these methods.
 *
 * <p>Methods allow fetching pending tests, approving or rejecting them,
 * and retrieving moderation history.</p>
 *
 * @author Ismael
 * @version 1.0
 */
public interface ModerationService {

    /**
     * Retrieves all quiz tests that are pending moderation.
     *
     * @return a list of pending quiz tests
     */
    List<QuizTest> findPendingTests();

    /**
     * Validates a quiz test, marking it as approved.
     *
     * @param moderatorId the ID of the moderator who is validating the test
     * @param testId      the ID of the quiz test to validate
     */
    void validateTest(int moderatorId, int testId);

    /**
     * Rejects a quiz test, specifying a reason.
     *
     * @param moderatorId the ID of the moderator who is validating the test
     * @param testId the ID of the quiz test to reject
     * @param reason the reason for rejecting the test
     */
    void rejectTest(int moderatorId, int testId, String reason);

    /**
     * Retrieves all quiz tests that have been rejected by a specific user.
     *
     * @param userId the ID of the user who submitted the tests
     * @return a list of rejected tests submitted by the user
     */
    List<QuizTest> findRejectedTestsByUser(int userId);

    /**
     * Retrieves all tests moderated by a specific moderator.
     *
     * @param moderatorId the ID of the moderator
     * @return a list of tests moderated by the user
     */
    List<QuizTest> findModeratedBy(int moderatorId);

}
