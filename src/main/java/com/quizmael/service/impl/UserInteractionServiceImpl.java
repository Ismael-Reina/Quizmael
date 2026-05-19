package com.quizmael.service.impl;

import com.quizmael.dao.QuizTestDao;
import com.quizmael.dao.UserDao;
import com.quizmael.model.QuizTest;
import com.quizmael.model.User;
import com.quizmael.service.UserInteractionService;
import com.quizmael.util.LoggerUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserInteractionServiceImpl implements UserInteractionService {

    private final UserDao userDao;
    private final QuizTestDao quizTestDao;

    /**
     * Constructs the UserInteractionServiceImpl with dependencies for handling user-test interactions.
     *
     * @param userDao the DAO for user profile data.
     * @param quizTestDao the DAO for accessing test information.
     */
    public UserInteractionServiceImpl(UserDao userDao, QuizTestDao quizTestDao) {
        this.userDao = userDao;
        this.quizTestDao = quizTestDao;
    }

    @Override
    public boolean addFavoriteTest(int userId, int testId) {
        LoggerUtil.info(UserInteractionServiceImpl.class, "Adding test ID " + testId + " to favorites for user ID " + userId);

        // 1. Fetch user with favorites pre-loaded
        User user = userDao.findByIdWithFavorites(userId).orElseThrow(() -> {
            LoggerUtil.warn(UserInteractionServiceImpl.class, "Favorite failed: User " + userId + " not found.");
            return new IllegalArgumentException("error.user.not_found");
        });

        // 2. Fetch the test
        QuizTest test = quizTestDao.findById(testId).orElseThrow(() -> {
            LoggerUtil.warn(UserInteractionServiceImpl.class, "Favorite failed: Test " + testId + " not found.");
            return new IllegalArgumentException("error.test.not_found");
        });

        // 3. Logic check
        if (user.getFavoriteTests().contains(test)) {
            LoggerUtil.debug(UserInteractionServiceImpl.class, "Test already in favorites. No action taken.");
            return false;
        }

        // 4. Persist
        try {
            user.getFavoriteTests().add(test);
            userDao.update(user);
            LoggerUtil.info(UserInteractionServiceImpl.class, "Test added to favorites successfully.");
            return true;
        } catch (Exception e) {
            LoggerUtil.error(UserInteractionServiceImpl.class, "Database error adding favorite.", e);
            throw new RuntimeException("error.database.internal", e);
        }
    }

    @Override
    public boolean removeFavoriteTest(int userId, int testId) {
        LoggerUtil.info(UserInteractionServiceImpl.class, "Removing test ID " + testId + " from favorites for user ID " + userId);

        // 1. Fetch user with favorites pre-loaded
        User user = userDao.findByIdWithFavorites(userId).orElseThrow(() -> {
            LoggerUtil.warn(UserInteractionServiceImpl.class, "Remove favorite failed: User " + userId + " not found.");
            return new IllegalArgumentException("error.user.not_found");
        });

        // 2. Fetch the test
        QuizTest test = quizTestDao.findById(testId).orElseThrow(() -> {
            LoggerUtil.warn(UserInteractionServiceImpl.class, "Remove favorite failed: Test " + testId + " not found.");
            return new IllegalArgumentException("error.test.not_found");
        });

        // 3. Logic check
        if (!user.getFavoriteTests().contains(test)) {
            LoggerUtil.debug(UserInteractionServiceImpl.class, "Test not found in favorites. No action taken.");
            return false;
        }

        // 4. Persist
        try {
            user.getFavoriteTests().remove(test);
            userDao.update(user);
            LoggerUtil.info(UserInteractionServiceImpl.class, "Test removed from favorites successfully.");
            return true;
        } catch (Exception e) {
            LoggerUtil.error(UserInteractionServiceImpl.class, "Database error removing favorite.", e);
            throw new RuntimeException("error.database.internal", e);
        }
    }

    @Override
    public List<QuizTest> getFavoriteTests(int userId) {
        LoggerUtil.debug(UserInteractionServiceImpl.class, "Fetching favorite tests for user ID: " + userId);

        if (!userDao.findById(userId).isPresent()) {
            LoggerUtil.error(UserInteractionServiceImpl.class, "Fetch favorites failed: User ID " + userId + " not found.");
            throw new IllegalArgumentException("error.user.not_found");
        }

        return quizTestDao.findFavoritesByUserId(userId);
    }

    @Override
    public boolean isFavoriteTest(int userId, int testId) {
        LoggerUtil.debug(UserInteractionServiceImpl.class, "Checking if test ID " + testId + " is favorite for user ID " + userId);

        Optional<User> userOpt = userDao.findById(userId);
        Optional<QuizTest> testOpt = quizTestDao.findById(testId);

        if (userOpt.isPresent() && testOpt.isPresent()) {
            return userOpt.get().getFavoriteTests().contains(testOpt.get());
        }
        return false;
    }
}