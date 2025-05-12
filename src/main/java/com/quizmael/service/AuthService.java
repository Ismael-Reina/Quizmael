package com.quizmael.service;

import com.quizmael.model.User;
import com.quizmael.service.enums.PasswordResetStatus;


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
     * Registers a new user in the system with all necessary details.
     *
     * @param name the username
     * @param email the email address
     * @param password the plain password
     * @param passwordHint the password hint
     * @param secretQuestion the secret question
     * @param secretAnswer the secret answer
     * @param birthDate the birth date in ISO format (yyyy-MM-dd)
     * @return the created User with assigned ID
     */
    User register(String name, String email, String password, String passwordHint,
                  String secretQuestion, String secretAnswer, String birthDate);

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
     * Sends a new random password to the user's email if present.
     *
     * @param email the user's email
     * @return a {@link PasswordResetStatus} indicating the result of the operation
     */
    PasswordResetStatus resetPasswordWithEmail(String email);

}
