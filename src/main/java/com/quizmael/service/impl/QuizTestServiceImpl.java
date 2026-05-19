package com.quizmael.service.impl;

import com.quizmael.dao.QuizTestDao;
import com.quizmael.model.QuizTest;
import com.quizmael.model.enums.Language;
import com.quizmael.model.enums.State;
import com.quizmael.service.QuizTestService;
import com.quizmael.util.LoggerUtil;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Implementation of the {@link QuizTestService} interface.
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public class QuizTestServiceImpl implements QuizTestService {

    private final QuizTestDao quizTestDao;

    /**
     * Constructs the QuizTestServiceImpl with the required test data access object.
     *
     * @param quizTestDao the DAO responsible for test-related data operations.
     */
    public QuizTestServiceImpl(QuizTestDao quizTestDao) {
        this.quizTestDao = quizTestDao;
    }

    // ------------------------------------------------------------
    //                  Test Retrieval & Filtering
    // ------------------------------------------------------------

    @Override
    public List<QuizTest> findAllTests() {
        LoggerUtil.debug(QuizTestServiceImpl.class, "Fetching all tests from the system.");
        // Note: The DAO should handle JOIN FETCH for creator to avoid LazyInitializationException
        return quizTestDao.findAll();
    }

    @Override
    public List<QuizTest> findAllPublicTests() {
        LoggerUtil.debug(QuizTestServiceImpl.class, "Fetching all public tests.");
        return quizTestDao.findAllPublic();
    }

    @Override
    public List<QuizTest> findTestsByTopic(String topicName) {
        LoggerUtil.debug(QuizTestServiceImpl.class, "Fetching tests by topic: " + topicName);
        return quizTestDao.findByTopic(topicName);
    }

    @Override
    public List<QuizTest> findTestsByTitle(String title) {
        LoggerUtil.debug(QuizTestServiceImpl.class, "Fetching tests by title containing: " + title);
        return quizTestDao.findByTitleContaining(title);
    }

    @Override
    public List<QuizTest> findTestsByCreator(int userId) {
        LoggerUtil.debug(QuizTestServiceImpl.class, "Fetching tests created by user ID: " + userId);
        return quizTestDao.findByCreatorId(userId);
    }

    @Override
    public Optional<QuizTest> findTestById(int testId) {
        LoggerUtil.debug(QuizTestServiceImpl.class, "Fetching test by ID: " + testId);
        return quizTestDao.findById(testId);
    }

    @Override
    public List<QuizTest> findPublicTestsByFilters(String topicName, String creatorName, Integer difficulty, Set<Language> languages) {
        return quizTestDao.findPublicByFilters(topicName, creatorName, difficulty, languages);
    }

    @Override
    public List<String> findAllCreatorsOfPublishedTests() {
        return quizTestDao.findAllCreatorsOfPublishedTests();
    }

    // ------------------------------------------------------------
    //                  Test Lifecycle & Management
    // ------------------------------------------------------------
    @Override
    public void createTest(QuizTest test) {
        LoggerUtil.info(QuizTestServiceImpl.class, "Attempting to create a new test.");
        validateTest(test);
        try {
            quizTestDao.save(test);
            LoggerUtil.info(QuizTestServiceImpl.class, "Test created successfully: " + test.getTitle());
        } catch (Exception e) {
            LoggerUtil.error(QuizTestServiceImpl.class, "Error creating test: " + test.getTitle(), e);
            throw new RuntimeException("error.database.internal", e);
        }
    }

    @Override
    public void updateTest(QuizTest test) {
        LoggerUtil.info(QuizTestServiceImpl.class, "Attempting to update test ID: " + test.getId());
        validateTest(test);
        try {
            test.setUpdatedAt(Instant.now());
            quizTestDao.update(test);
            LoggerUtil.info(QuizTestServiceImpl.class, "Test updated successfully ID: " + test.getId());
        } catch (Exception e) {
            LoggerUtil.error(QuizTestServiceImpl.class, "Error updating test ID: " + test.getId(), e);
            throw new RuntimeException("error.database.internal", e);
        }
    }

    @Override
    public boolean updateTestState(Integer testId, State newState) {
        LoggerUtil.info(QuizTestServiceImpl.class, "Attempting to update state for test ID: " + testId + " to " + newState);
        Optional<QuizTest> testOpt = quizTestDao.findById(testId);

        if (testOpt.isPresent()) {
            QuizTest test = testOpt.get();
            test.setState(newState);
            test.setUpdatedAt(Instant.now());
            try {
                quizTestDao.update(test);
                LoggerUtil.info(QuizTestServiceImpl.class, "Test state updated successfully.");
                return true;
            } catch (Exception e) {
                LoggerUtil.error(QuizTestServiceImpl.class, "Error updating test state.", e);
                throw new RuntimeException("error.database.internal", e);
            }
        }
        LoggerUtil.warn(QuizTestServiceImpl.class, "Test not found for state update. ID: " + testId);
        return false;
    }

    @Override
    public boolean deleteTest(int testId) {
        LoggerUtil.info(QuizTestServiceImpl.class, "Attempting to delete test ID: " + testId);

        Optional<QuizTest> testOpt = quizTestDao.findById(testId);

        if (testOpt.isPresent()) {
            try {
                quizTestDao.delete(testOpt.get());
                LoggerUtil.info(QuizTestServiceImpl.class, "Test deleted successfully.");
                return true;
            } catch (Exception e) {
                LoggerUtil.error(QuizTestServiceImpl.class, "Error deleting test ID: " + testId, e);
                return false;
            }
        }
        LoggerUtil.warn(QuizTestServiceImpl.class, "Test not found for deletion. ID: " + testId);
        return false;
    }

    // ------------------------------------------------------------
    //                  User Interactions & Favorites
    // ------------------------------------------------------------
    // Here I will place methods like toggleFavorite, etc.

    // ------------------------------------------------------------
    //                      Private Methods
    // ------------------------------------------------------------

    /**
     * Validates the mandatory fields of a QuizTest.
     * Throws an IllegalArgumentException if validation fails.
     */
    private void validateTest(QuizTest test) {
        if (test == null) {
            throw new IllegalArgumentException("error.validation.test.null");
        }
        if (test.getTitle() == null || test.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("error.validation.test.title_empty");
        }
        if (test.getCreator() == null) {
            throw new IllegalArgumentException("error.validation.test.no_creator");
        }
    }

}
