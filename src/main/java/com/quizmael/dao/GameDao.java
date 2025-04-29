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

    void save(Game game);

    void update(Game game);

    void delete(Game game);

    Optional<Game> findById(Integer id);

    List<Game> findAll();
}