package com.quizmael.dao.impl;

import com.quizmael.dao.QuestionDao;
import com.quizmael.dao.QuizTestDao;
import com.quizmael.model.Question;
import com.quizmael.model.QuizTest;
import com.quizmael.model.enums.Language;
import com.quizmael.model.enums.State;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link QuestionDaoImpl}.
 *
 * @author Ismael
 * @version 1.0
 */
class QuestionDaoTest {

    private QuestionDao questionDao;
    private QuizTest test;

    @BeforeEach
    void setUp() {
        questionDao = new QuestionDaoImpl();
        QuizTestDao quizTestDao = new QuizTestDaoImpl();

        // Create test to associate with questions
        test = new QuizTest();
        test.setTitle("Test de ejemplo");
        test.setLanguage(Language.ES);
        test.setState(State.DRAFT);
        test.setOptionsCount(4);
        test.setTimeLimit(60);
        quizTestDao.save(test);
    }

    // Save a question and retrieve it by ID
    @Test
    void testSaveAndFindById() {
        Question question = new Question();
        question.setTest(test);
        question.setText("¿Cuál es la capital de España?");
        questionDao.save(question);

        Optional<Question> retrieved = questionDao.findById(question.getId());
        assertTrue(retrieved.isPresent());
        assertEquals("¿Cuál es la capital de España?", retrieved.get().getText());
    }

    // Save two questions and verify both are found
    @Test
    void testFindAll() {
        Question question1 = new Question();
        question1.setTest(test);
        question1.setText("Pregunta 1");
        questionDao.save(question1);

        Question question2 = new Question();
        question2.setTest(test);
        question2.setText("Pregunta 2");
        questionDao.save(question2);

        List<Question> all = questionDao.findAll();
        assertTrue(all.size() >= 2);
    }

    // Update question text and verify changes
    @Test
    void testUpdate() {
        Question question = new Question();
        question.setTest(test);
        question.setText("Texto original");
        questionDao.save(question);

        question.setText("Texto modificado");
        questionDao.update(question);

        Optional<Question> updated = questionDao.findById(question.getId());
        assertTrue(updated.isPresent());
        assertEquals("Texto modificado", updated.get().getText());
    }

    // Delete a question and ensure it no longer exists
    @Test
    void testDelete() {
        Question question = new Question();
        question.setTest(test);
        question.setText("A eliminar");
        questionDao.save(question);

        questionDao.delete(question);
        Optional<Question> deleted = questionDao.findById(question.getId());
        assertTrue(deleted.isEmpty());
    }
}