package com.quizmael.service;

import com.quizmael.model.User;
import com.quizmael.service.enums.PasswordResetStatus;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


/**
 * Interface for managing registration, authentication, and password recovery.
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public interface AuthService {

    // ------------------------------------------------------------
    //                      Registration
    // ------------------------------------------------------------

    /**
     * Registers a new user in the system.
     *
     * @param user The user entity containing all registration details.
     * @return The persisted User with its generated ID.
     * @throws IllegalArgumentException if validation fails or username exists.
     */
    User register(User user);

    // ------------------------------------------------------------
    //                 Authentication / Login
    // ------------------------------------------------------------

    /**
     * Logs in a user by verifying name and password credentials.
     *
     * @param name the username
     * @param password the raw password
     * @return the authenticated user if credentials are valid
     */
    Optional<User> login(String name, String password);

    /**
     * Authenticates a user by verifying their secret answer.
     *
     * @param name the username
     * @param secretAnswer the answer to the secret question
     * @return the authenticated user, if the answer is correct
     */
    Optional<User> loginWithSecretAnswer(String name, String secretAnswer);

    /**
     * Logs in a user as a guest, creating a temporary account.
     *
     * @return the guest user
     */
    public Optional<User> loginAsGuest();

    // ------------------------------------------------------------
    //                      Account Recovery
    // ------------------------------------------------------------

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

    /**
     * TODO: This method is currently disabled as it is not part of the MVP. Mail de momento no es único, por eso el hacerlo con el name.
     * Sends a new random password to the user's email if present.
     *
     * @param name the user's name
     * @return a {@link PasswordResetStatus} indicating the result of the operation
     */
    // PasswordResetStatus resetPasswordWithEmail(String name);

}
