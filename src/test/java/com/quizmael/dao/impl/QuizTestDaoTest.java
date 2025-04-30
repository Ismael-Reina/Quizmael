package com.quizmael.dao.impl;

import com.quizmael.dao.QuizTestDao;
import com.quizmael.dao.UserDao;
import com.quizmael.model.QuizTest;
import com.quizmael.model.User;
import com.quizmael.model.enums.Role;
import com.quizmael.model.enums.Language;
import com.quizmael.model.enums.State;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link QuizTestDaoImpl}.
 *
 * @author Ismael
 * @version 1.0
 */
class QuizTestDaoTest {

    private QuizTestDao quizTestDao;
    private User user;

    @BeforeEach
    void setUp() {
        quizTestDao = new QuizTestDaoImpl();
        UserDao userDao = new UserDaoImpl();

        // Create user to associate as the test creator
        user = new User();
        user.setName("test_user");
        user.setRole(Role.REGISTERED);
        userDao.save(user);
    }

    @Test
    void testSaveAndFindById() {
        // Save a new test and retrieve it by ID
        QuizTest quizTest = new QuizTest();
        quizTest.setCreator(user);
        quizTest.setTitle("Título de prueba");
        quizTest.setLanguage(Language.ES);
        quizTest.setState(State.DRAFT);
        quizTest.setOptionsCount(3);
        quizTest.setTimeLimit(30);

        quizTestDao.save(quizTest);

        Optional<QuizTest> retrieved = quizTestDao.findById(quizTest.getId());
        assertTrue(retrieved.isPresent());
        assertEquals("Título de prueba", retrieved.get().getTitle());
    }

    @Test
    void testFindAll() {
        // Save two tests and verify both are found
        QuizTest quizTest1 = new QuizTest();
        quizTest1.setCreator(user);
        quizTest1.setTitle("Test 1");
        quizTest1.setLanguage(Language.ES);
        quizTest1.setState(State.DRAFT);
        quizTest1.setOptionsCount(4);
        quizTest1.setTimeLimit(30);
        quizTestDao.save(quizTest1);

        QuizTest quizTest2 = new QuizTest();
        quizTest2.setCreator(user);
        quizTest2.setTitle("Test 2");
        quizTest2.setLanguage(Language.EN);
        quizTest2.setState(State.DRAFT);
        quizTest2.setOptionsCount(4);
        quizTest2.setTimeLimit(45);
        quizTestDao.save(quizTest2);

        List<QuizTest> tests = quizTestDao.findAll();
        assertTrue(tests.size() >= 2);
    }

    @Test
    void testUpdate() {
        // Update test fields and verify the changes persist
        QuizTest quizTest = new QuizTest();
        quizTest.setCreator(user);
        quizTest.setTitle("Antiguo título");
        quizTest.setLanguage(Language.ES);
        quizTest.setState(State.DRAFT);
        quizTest.setOptionsCount(3);
        quizTest.setTimeLimit(20);
        quizTestDao.save(quizTest);

        quizTest.setTitle("Nuevo título");
        quizTest.setTimeLimit(90);
        quizTestDao.update(quizTest);

        Optional<QuizTest> updated = quizTestDao.findById(quizTest.getId());
        assertTrue(updated.isPresent());
        assertEquals("Nuevo título", updated.get().getTitle());
        assertEquals(90, updated.get().getTimeLimit());
    }

    @Test
    void testDelete() {
        // Delete test and ensure it no longer exists
        QuizTest quizTest = new QuizTest();
        quizTest.setCreator(user);
        quizTest.setTitle("A eliminar");
        quizTest.setLanguage(Language.ES);
        quizTest.setState(State.DRAFT);
        quizTest.setOptionsCount(2);
        quizTest.setTimeLimit(15);
        quizTestDao.save(quizTest);

        quizTestDao.delete(quizTest);

        Optional<QuizTest> deleted = quizTestDao.findById(quizTest.getId());
        assertTrue(deleted.isEmpty());
    }
}