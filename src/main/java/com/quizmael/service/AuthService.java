package com.quizmael.service;

import com.quizmael.dao.UserDao;
import com.quizmael.model.User;
import com.quizmael.service.enums.PasswordResetStatus;
import com.quizmael.service.enums.ChangePasswordResult;
import com.quizmael.util.PasswordUtils;


import java.util.Optional;
import java.util.List;

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
     * @param user the user to register
     * @return the registered user with assigned ID
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
