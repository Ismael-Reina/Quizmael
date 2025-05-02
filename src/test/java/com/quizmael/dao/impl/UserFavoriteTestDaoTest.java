package com.quizmael.dao.impl;


import com.quizmael.dao.QuizTestDao;
import com.quizmael.dao.UserDao;
import com.quizmael.dao.UserFavoriteTestDao;
import com.quizmael.model.QuizTest;
import com.quizmael.model.User;
import com.quizmael.model.UserFavoriteTest;
import com.quizmael.model.UserFavoriteTestId;
import com.quizmael.model.enums.Role;
import com.quizmael.model.enums.State;

import java.util.Optional;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class UserFavoriteTestDaoTest {

    private UserFavoriteTestDao favoriteDao;

    private User user;
    private QuizTest test;

    @BeforeEach
    void setUp() {
        UserDao userDao = new UserDaoImpl();
        QuizTestDao testDao = new QuizTestDaoImpl();
        favoriteDao = new UserFavoriteTestDaoImpl();

        user = new User();
        user.setName("favorite_user");
        user.setPassword("1234");
        user.setRole(Role.REGISTERED);
        userDao.save(user);

        test = new QuizTest();
        test.setTitle("Geography Test");
        test.setState(State.PUBLISHED);
        testDao.save(test);
    }

    // Save and retrieve a favorite
    @Test
    void testSaveAndFindById() {
        UserFavoriteTestId id = new UserFavoriteTestId();
        id.setUserId(user.getId());
        id.setTestId(test.getId());
        UserFavoriteTest favorite = new UserFavoriteTest();
        favorite.setId(id);
        favorite.setUser(user);
        favorite.setTest(test);
        favoriteDao.save(favorite);

        Optional<UserFavoriteTest> found = favoriteDao.findById(id);
        assertTrue(found.isPresent());
        assertEquals(user.getId(), found.get().getUser().getId());
        assertEquals(test.getId(), found.get().getTest().getId());
    }

    // Save and delete a favorite, then verify it's gone
    @Test
    void testDelete() {
        UserFavoriteTestId id = new UserFavoriteTestId();
        id.setUserId(user.getId());
        id.setTestId(test.getId());
        UserFavoriteTest favorite = new UserFavoriteTest();
        favorite.setId(id);
        favorite.setUser(user);
        favorite.setTest(test);
        favoriteDao.save(favorite);

        favoriteDao.delete(favorite);
        Optional<UserFavoriteTest> result = favoriteDao.findById(id);
        assertTrue(result.isEmpty());
    }

    // Should not find a non-existent favorite
    @Test
    void testFindByIdNotFound() {
        UserFavoriteTestId id = new UserFavoriteTestId();
        id.setUserId(9999);
        id.setTestId(9999);
        Optional<UserFavoriteTest> result = favoriteDao.findById(id);
        assertTrue(result.isEmpty());
    }
}
