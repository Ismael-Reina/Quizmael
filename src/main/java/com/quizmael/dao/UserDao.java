package com.quizmael.dao;

import com.quizmael.model.User;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) interface for managing {@link User} entities.
 * Provides standard CRUD operations for users.
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public interface UserDao {

    /**
     * Saves a new user into the database.
     *
     * @param user The user to save.
     * @return The saved user, possibly with its generated ID.
     */
    User save(User user);

    /**
     * Updates an existing user in the database.
     *
     * @param user The user to update.
     */
    void update(User user);

    /**
     * Deletes a user from the database.
     *
     * @param user The user to delete.
     */
    void delete(User user);

    /**
     * Finds a user by its ID.
     *
     * @param id The ID of the user.
     * @return An {@link Optional} containing the found user, or empty if not found.
     */
    Optional<User> findById(Integer id);

    /**
     * Finds a user by its user name.
     *
     * @param name The user name of the user.
     * @return An {@link Optional} containing the found user, or empty if not found.
     */
    Optional<User> findByName(String name);

    /**
     * Retrieves all users from the database.
     *
     * @return A list of all users.
     */
    List<User> findAll();
}
