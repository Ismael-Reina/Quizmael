package com.quizmael.dao.impl;

import com.quizmael.dao.GameDao;
import com.quizmael.dao.GameQuestionDao;
import com.quizmael.dao.QuestionDao;
import com.quizmael.model.*;

import java.util.Optional;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for {@link GameQuestionDaoImpl}.
 * Verifies CRUD operations on GameQuestion entities.
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
class GameQuestionDaoTest {

    private GameQuestionDao gameQuestionDao;

    private Game game;
    private Question question;

    @BeforeEach
    void setUp() {
        gameQuestionDao = new GameQuestionDaoImpl();
        GameDao gameDao = new GameDaoImpl();
        QuestionDao questionDao = new QuestionDaoImpl();

        // Create and save real Game and Question
        game = new Game();
        game.setScore(7.5);
        gameDao.save(game);

        question = new Question();
        question.setText("What is the capital of France?");
        questionDao.save(question);
    }

    // Save a GameQuestion and verify it can be retrieved
    @Test
    void testSaveAndFindById() {
        GameQuestionId id = new GameQuestionId();
        id.setGameId(game.getId());
        id.setQuestionId(question.getId());

        GameQuestion gq = new GameQuestion();
        gq.setId(id);
        gq.setGame(game);
        gq.setQuestion(question);
        gq.setIsCorrect(true);

        gameQuestionDao.save(gq);

        Optional<GameQuestion> found = gameQuestionDao.findById(id);
        assertTrue(found.isPresent());
        assertEquals(true, found.get().getIsCorrect());
    }

    // Save and update GameQuestion, then verify changes
    @Test
    void testUpdate() {
        GameQuestionId id = new GameQuestionId();
        id.setGameId(game.getId());
        id.setQuestionId(question.getId());

        GameQuestion gq = new GameQuestion();
        gq.setId(id);
        gq.setGame(game);
        gq.setQuestion(question);
        gq.setIsCorrect(false);
        gameQuestionDao.save(gq);

        gq.setIsCorrect(true);
        gameQuestionDao.update(gq);

        Optional<GameQuestion> updated = gameQuestionDao.findById(id);
        assertTrue(updated.isPresent());
        assertTrue(updated.get().getIsCorrect());
    }

    // Save and delete GameQuestion, then ensure it no longer exists
    @Test
    void testDelete() {
        GameQuestionId id = new GameQuestionId();
        id.setGameId(game.getId());
        id.setQuestionId(question.getId());

        GameQuestion gq = new GameQuestion();
        gq.setId(id);
        gq.setGame(game);
        gq.setQuestion(question);
        gq.setIsCorrect(true);
        gameQuestionDao.save(gq);

        gameQuestionDao.delete(gq);

        Optional<GameQuestion> deleted = gameQuestionDao.findById(id);
        assertFalse(deleted.isPresent());
    }

    // Attempt to retrieve a non-existing GameQuestion
    @Test
    void testFindByIdNotFound() {
        GameQuestionId id = new GameQuestionId();
        id.setGameId(9999);
        id.setQuestionId(9999);

        Optional<GameQuestion> result = gameQuestionDao.findById(id);
        assertTrue(result.isEmpty());
    }

    // Try to save GameQuestion with non-existent Game and Question
    @Test
    void testSaveWithNonExistingReferencesShouldFail() {
        Game fakeGame = new Game();
        fakeGame.setId(123456); // Not saved in DB

        Question fakeQuestion = new Question();
        fakeQuestion.setId(654321); // Not saved in DB

        GameQuestionId id = new GameQuestionId();
        id.setGameId(fakeGame.getId());
        id.setQuestionId(fakeQuestion.getId());

        GameQuestion gq = new GameQuestion();
        gq.setId(id);
        gq.setGame(fakeGame);
        gq.setQuestion(fakeQuestion);
        gq.setIsCorrect(false);

        assertThrows(Exception.class, () -> gameQuestionDao.save(gq));
    }
}
