package com.quizmael.service;

import com.quizmael.model.User;
import com.quizmael.model.enums.Role;
import com.quizmael.service.enums.ChangePasswordResult;

import java.util.Optional;
import java.util.List;

/**
 * Interface for managing user-related operations.
 *
 * @author Ismael Reina Muñoz
 * @version 2.0
 */
public interface UserService {

    // ------------------------------------------------------------
    //              User Lookup, Update and Deletion
    // ------------------------------------------------------------

    /**
     * Finds a user by ID.
     *
     * @param id the user ID
     * @return the user, if found
     */
    Optional<User> findById(int id);

    /**
     * Finds a user by name.
     *
     * @param name the username
     * @return the user, if found
     */
    Optional<User> findByName(String name);

    /**
     * Returns all users in the system.
     *
     * @return list of all users
     */
    List<User> findAll();

    /**
     * Updates an existing user's information.
     * @param user the user with updated data.
     */
    void update(User user);

    /**
     * Deletes a user by ID.
     *
     * @param id the ID of the user to delete
     * @return true if the user was deleted successfully, false otherwise
     */
    boolean deleteById(int id);

    // ------------------------------------------------------------
    //                  Profile Management
    // ------------------------------------------------------------

    /**
     * Updates user profile fields except password and secret answer,
     * which have their own dedicated methods.
     *
     * @param user the user with updated fields
     */
    void updateUser(User user);

    /**
     * Updates the password for a given user.
     *
     * @param userId      the ID of the user
     * @param newPassword the new password
     * @return true if the password was updated successfully, false otherwise
     */
    public boolean forcePasswordReset(int userId, String newPassword);

    /**
     * Updates the role of a given user.
     *
     * @param userId the ID of the user
     * @param newRole the new role to assign
     * @return true if the role was updated successfully, false otherwise
     */
    public boolean updateRole(int userId, Role newRole);

    /**
     * Changes the password for a given user.
     *
     * @param userId      the ID of the user
     * @param oldPassword the old password
     * @param newPassword the new password
     *
     * @throws IllegalArgumentException if the old password does not match
     * @return a {@link ChangePasswordResult} indicating whether the password was changed,
     *         or the reason why it wasn't (e.g., user not found or old password incorrect)
     */
    ChangePasswordResult changePassword(int userId, String oldPassword, String newPassword);

    /**
     * Changes the secret question for a given user.
     *
     * @param userId          the ID of the user
     * @param newSecretAnswer the new secret question
     * @return true if the secret question was changed successfully, false otherwise
     */
    boolean changeSecretAnswer(int userId, String newSecretAnswer);

}
