package com.quizmael.service;

import com.quizmael.model.User;
import com.quizmael.service.enums.ChangePasswordResult;
import com.quizmael.service.enums.PasswordResetStatus;

import java.util.Optional;
import java.util.List;

/**
 * Interface for managing user-related operations.
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public interface UserService {

    // ------------------------------------------------------------
    //                 Authentication and Login
    // ------------------------------------------------------------

    /**
     * Authenticates a user by name and password.
     *
     * @param name the username
     * @param password the raw password
     * @return the authenticated user if credentials are valid
     */
    Optional<User> login(String name, String password);

    /**
     * Authenticates a user using a secret answer.
     *
     * @param name the username
     * @param secretAnswer the answer to the secret question
     * @return the authenticated user, if the answer is correct
     */
    Optional<User> loginWithSecretAnswer(String name, String secretAnswer);

    /**
     * Sends a new random password to the user's email if present.
     *
     * @param email the user's email
     * @return a {@link PasswordResetStatus} indicating the result of the operation
     */
    PasswordResetStatus resetPasswordWithEmail(String email);

    /**
     * Returns the password hint for a given username.
     *
     * @param name the username
     * @return the password hint, if available
     */
    Optional<String> getPasswordHint(String name);

    /**
     * Returns the secret question for a given username.
     *
     * @param name the username
     * @return the secret question, if available
     */
    Optional<String> getSecretQuestion(String name);

    // ------------------------------------------------------------
    //           User Registration and Profile Management
    // ------------------------------------------------------------

    /**
     * Registers a new user in the system.
     *
     * @param user the user to register
     * @return the registered user with assigned ID
     */
    User register(User user);

    /**
     * Updates any user fields, except the password.
     *
     * @param user the user with updated fields
     */
    void updateUser(User user);

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

    // ------------------------------------------------------------
    //                 User Lookup and Deletion
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
     * Deletes a user by ID.
     *
     * @param id the ID of the user to delete
     */
    void deleteById(int id);

}
