package com.quizmael.dao.impl;

import com.quizmael.dao.QuizTestDao;
import com.quizmael.dao.UserDao;
import com.quizmael.dao.UserRecentTestDao;
import com.quizmael.model.QuizTest;
import com.quizmael.model.User;
import com.quizmael.model.UserRecentTest;
import com.quizmael.model.UserRecentTestId;
import com.quizmael.model.enums.Role;
import com.quizmael.model.enums.State;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserRecentTestDaoTest {

    private UserRecentTestDao recentDao;
    private User user;
    private QuizTest quizTest;

    @BeforeEach
    void setUp() {
        UserDao userDao = new UserDaoImpl();
        QuizTestDao testDao = new QuizTestDaoImpl();
        recentDao = new UserRecentTestDaoImpl();

        user = new User();
        user.setName("recent_user");
        user.setPassword("abcd");
        user.setRole(Role.REGISTERED);
        userDao.save(user);

        quizTest = new QuizTest();
        quizTest.setTitle("Math Quiz");
        quizTest.setState(State.PUBLISHED);
        testDao.save(quizTest);
    }

    // Save and retrieve a recent test entry
    @Test
    void testSaveAndFindById() {
        UserRecentTestId id = new UserRecentTestId();
        id.setUserId(user.getId());
        id.setTestId(quizTest.getId());
        UserRecentTest recent = new UserRecentTest();
        recent.setId(id);
        recent.setUser(user);
        recent.setTest(quizTest);
        recentDao.save(recent);

        Optional<UserRecentTest> found = recentDao.findById(id);
        assertTrue(found.isPresent());
        assertEquals(user.getId(), found.get().getUser().getId());
        assertEquals(quizTest.getId(), found.get().getTest().getId());
    }

    // Save and delete a recent test, then verify deletion
    @Test
    void testDelete() {
        UserRecentTestId id = new UserRecentTestId();
        id.setUserId(user.getId());
        id.setTestId(quizTest.getId());
        UserRecentTest recent = new UserRecentTest();
        recent.setId(id);
        recent.setUser(user);
        recent.setTest(quizTest);
        recentDao.save(recent);

        recentDao.delete(recent);
        Optional<UserRecentTest> result = recentDao.findById(id);
        assertTrue(result.isEmpty());
    }

    // Should not find a recent test if it doesn’t exist
    @Test
    void testFindByIdNotFound() {
        UserRecentTestId id = new UserRecentTestId();
        id.setUserId(9999);
        id.setTestId(9999);
        Optional<UserRecentTest> result = recentDao.findById(id);
        assertTrue(result.isEmpty());
    }
}