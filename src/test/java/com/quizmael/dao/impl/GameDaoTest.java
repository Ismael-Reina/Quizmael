package com.quizmael.dao.impl;

import com.quizmael.dao.GameDao;
import com.quizmael.model.Game;

import java.util.Optional;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link GameDao}.
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
class GameDaoTest {

    private static GameDao gameDao;

    @BeforeAll
    static void setUp() {
        gameDao = new GameDaoImpl();
    }

    // Verifies that a game can be saved and correctly retrieved by its ID.
    @Test
    void testSaveAndFindById() {
        Game game = new Game();
        game.setScore(10.00);

        gameDao.save(game);

        Optional<Game> retrieved = gameDao.findById(game.getId());
        assertTrue(retrieved.isPresent());
        assertEquals(10.00, retrieved.get().getScore());
    }

    // Verifies that questionCount and correctAnswers are correctly stored and retrieved.
    @Test
    void testSaveGameWithQuestionAndCorrectAnswers() {
        Game game = new Game();
        game.setScore(7.25);
        game.setQuestionCount(10);
        game.setCorrectAnswers(8);

        gameDao.save(game);

        Optional<Game> retrieved = gameDao.findById(game.getId());
        assertTrue(retrieved.isPresent());
        assertEquals(10, retrieved.get().getQuestionCount());
        assertEquals(8, retrieved.get().getCorrectAnswers());
    }

    // Verifies that an existing game can be updated and changes are persisted.
    @Test
    void testUpdate() {
        Game game = new Game();
        game.setScore(4.71);

        gameDao.save(game);

        game.setScore(5.23);
        gameDao.update(game);

        Optional<Game> updated = gameDao.findById(game.getId());
        assertTrue(updated.isPresent());
        assertEquals(5.23, updated.get().getScore());
    }

    // Verifies that a game can be deleted and cannot be retrieved afterward.
    @Test
    void testDelete() {
        Game game = new Game();
        game.setScore(0.00);

        gameDao.save(game);
        gameDao.delete(game);

        Optional<Game> deleted = gameDao.findById(game.getId());
        assertFalse(deleted.isPresent());
    }

    // Verifies that searching for a non-existent game returns an empty Optional.
    @Test
    void testFindByIdNotFound() {
        Optional<Game> result = gameDao.findById(9999);
        assertTrue(result.isEmpty());
    }

}
