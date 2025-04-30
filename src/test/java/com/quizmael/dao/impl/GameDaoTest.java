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

    @Test
    void testSaveAndFindById() {
        Game game = new Game();
        game.setScore(10.00);

        gameDao.save(game);

        Optional<Game> retrieved = gameDao.findById(game.getId());
        assertTrue(retrieved.isPresent());
        assertEquals(10.00, retrieved.get().getScore());
    }

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

    @Test
    void testDelete() {
        Game game = new Game();
        game.setScore(0.00);

        gameDao.save(game);
        gameDao.delete(game);

        Optional<Game> deleted = gameDao.findById(game.getId());
        assertFalse(deleted.isPresent());
    }
}
