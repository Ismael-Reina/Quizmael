package com.quizmael.dao.impl;

import com.quizmael.dao.AnswerDao;
import com.quizmael.model.Answer;

import java.util.Optional;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link AnswerDao}.
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
class AnswerDaoTest {

    private static AnswerDao answerDao;

    @BeforeAll
    static void setUp() {
        answerDao = new AnswerDaoImpl();
    }

    @Test
    void testSaveAndFindById() {
        Answer answer = new Answer();
        answer.setText("Sample Answer");
        answer.setCorrect(true);

        answerDao.save(answer);

        Optional<Answer> retrieved = answerDao.findById(answer.getId());
        assertTrue(retrieved.isPresent());
        assertEquals("Sample Answer", retrieved.get().getText());
    }

    @Test
    void testUpdate() {
        Answer answer = new Answer();
        answer.setText("Original");
        answer.setCorrect(false);

        answerDao.save(answer);

        answer.setText("Updated");
        answer.setCorrect(true);
        answerDao.update(answer);

        Optional<Answer> updated = answerDao.findById(answer.getId());
        assertTrue(updated.isPresent());
        assertEquals("Updated", updated.get().getText());
        assertTrue(updated.get().getCorrect());
    }

    @Test
    void testDelete() {
        Answer answer = new Answer();
        answer.setText("To Delete");
        answer.setCorrect(false);

        answerDao.save(answer);
        answerDao.delete(answer);

        Optional<Answer> deleted = answerDao.findById(answer.getId());
        assertFalse(deleted.isPresent());
    }
}
