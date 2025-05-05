package com.quizmael.service.impl;

import com.quizmael.dao.QuizTestDao;
import com.quizmael.dao.UserDao;
import com.quizmael.model.QuizTest;
import com.quizmael.model.User;
import com.quizmael.service.UserInteractionService;

import java.util.ArrayList;
import java.util.List;

public class UserInteractionServiceImpl implements UserInteractionService {

    private final UserDao userDao;
    private final QuizTestDao quizTestDao;

    public UserInteractionServiceImpl(UserDao userDao, QuizTestDao quizTestDao) {
        this.userDao = userDao;
        this.quizTestDao = quizTestDao;
    }

    @Override
    public void addFavoriteTest(int userId, int testId) {
        User user = userDao.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        QuizTest test = quizTestDao.findById(testId).orElseThrow(() -> new IllegalArgumentException("Test not found"));

        if (!user.getFavoriteTests().contains(test)) {
            user.getFavoriteTests().add(test);
            userDao.update(user);
        }
    }

    @Override
    public void removeFavoriteTest(int userId, int testId) {
        User user = userDao.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        QuizTest test = quizTestDao.findById(testId).orElseThrow(() -> new IllegalArgumentException("Test not found"));

        if (user.getFavoriteTests().contains(test)) {
            user.getFavoriteTests().remove(test);
            userDao.update(user);
        }
    }

    @Override
    public List<QuizTest> getFavoriteTests(int userId) {
        User user = userDao.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return new ArrayList<>(user.getFavoriteTests());
    }
}