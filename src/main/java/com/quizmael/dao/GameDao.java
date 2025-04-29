package com.quizmael.dao;

import com.quizmael.model.Game;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) interface for {@link Game}.
 * Defines standard CRUD operations.
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public interface GameDao {

    /**
     * Saves a new game to the database.
     *
     * @param game The {@link Game} entity to be saved.
     */
    void save(Game game);

    /**
     * Updates an existing game in the database.
     *
     * @param game The {@link Game} entity to be updated.
     */
    void update(Game game);

    /**
     * Deletes a game from the database.
     *
     * @param game The {@link Game} entity to be deleted.
     */
    void delete(Game game);

    /**
     * Finds a game by its ID.
     *
     * @param id The ID of the game.
     * @return An {@link Optional} containing the found game, or empty if not found.
     */
    Optional<Game> findById(Integer id);

    /**
     * Retrieves all games from the database.
     *
     * @return A list of all {@link Game} entities.
     */
    List<Game> findAll();
}