package com.quizmael.service.impl;

import com.quizmael.dao.UserDao;
import com.quizmael.dao.impl.UserDaoImpl;
import com.quizmael.model.User;
import com.quizmael.model.enums.Role;
import com.quizmael.service.AuthService;
import com.quizmael.service.enums.PasswordResetStatus;
import com.quizmael.util.PasswordUtils;
import com.quizmael.util.LoggerUtil;
import com.quizmael.util.ValidationConstants;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the {@link AuthService} interface.
 * Handles user-related operations such as authentication, registration,
 * and password recovery using a UserDao.
 *
 * @author Ismael Reina
 * @version 1.0
 */
public class AuthServiceImpl implements AuthService {

    private final UserDao userDao;

    /**
     * Constructs the AuthService by injecting its dependencies.
     * @param userDao the data access object for users.
     */
    public AuthServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }
    // ------------------------------------------------------------
    //                      Registration
    // ------------------------------------------------------------

    @Override
    public User register(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User object cannot be null");
        }

        LoggerUtil.info(getClass(), "Starting registration process for user: " + user.getName());

        // 1. Check mandatory fields (Null or Empty)
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            LoggerUtil.warn(getClass(), "Registration failed: Missing mandatory fields for user: " + user.getName());
            throw new IllegalArgumentException("error.validation.mandatory_fields");
        }

        // 2. Validate format (Username, Email & Password)
        if (!user.getName().matches(ValidationConstants.USERNAME_REGEX)) {
            LoggerUtil.warn(getClass(), "Registration failed: Invalid username format: " + user.getName());
            throw new IllegalArgumentException("error.validation.username_format");
        }

        if (user.getEmail() != null && !user.getEmail().trim().isEmpty() && !user.getEmail().matches(ValidationConstants.EMAIL_REGEX)) {
            LoggerUtil.warn(getClass(), "Registration failed: Invalid email format for user: " + user.getName());
            throw new IllegalArgumentException("error.validation.email");
        }

        // Note: This assumes the password in the 'user' object is still plain text at this point
        if (!user.getPassword().matches(ValidationConstants.PASSWORD_REGEX)) {
            LoggerUtil.warn(getClass(), "Registration failed: Insecure password for user: " + user.getName());
            throw new IllegalArgumentException("error.validation.password_strength");
        }

        // 3. Check uniqueness using the 'name' field
        if (userDao.findByName(user.getName()).isPresent()) {
            LoggerUtil.warn(getClass(), "Registration failed: Username '" + user.getName() + "' already exists.");
            throw new IllegalArgumentException("error.registration.user_exists");
        }

        // 4. Apply Hashing and default values
        // We hash both the password and the secret answer for security
        user.setPassword(PasswordUtils.hashPassword(user.getPassword()));
        user.setSecretAnswer(PasswordUtils.hashPassword(user.getSecretAnswer()));

        // Set default role to USER
        user.setRole(Role.REGISTERED);

        // 5. Persist
        try {
            User savedUser = userDao.save(user);
            LoggerUtil.info(getClass(), "User '" + user.getName() + "' registered successfully with ID: " + savedUser.getId());
            return savedUser;
        } catch (Exception e) {
            LoggerUtil.error(getClass(), "Critical database error while saving user '" + user.getName() + "'.", e);
            throw new RuntimeException("error.database.internal");
        }

    }

    // ------------------------------------------------------------
    //                 Authentication / Login
    // ------------------------------------------------------------

    @Override
    public Optional<User> login(String name, String password) {
        LoggerUtil.info(getClass(), "Login attempt for user: " + name);

        // Retrieve the user from the database by their unique username
        Optional<User> userOpt = userDao.findByName(name);

        // Check if the user exists in the system
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // GUEST BYPASS: If the user is 'Anonymous' does not have a password set in the database, we allow login without password verification
            if ("Anonymous".equals(user.getName()) && user.getPassword() == null) {
                LoggerUtil.info(AuthServiceImpl.class, "Anonymous guest access granted.");
                return Optional.of(user);
            }

            // SECURE VALIDATION: Verify the provided plaintext password against the hashed password stored in the DB
            if (PasswordUtils.checkPassword(password, user.getPassword())) {
                LoggerUtil.info(getClass(), "User '" + name + "' logged in successfully.");
                return Optional.of(user);
            } else {
                LoggerUtil.warn(getClass(), "Login failed for user '" + name + "': Incorrect password.");
            }
        } else {
            LoggerUtil.warn(getClass(), "Login failed: User '" + name + "' not found.");
        }
        // Return an empty Optional if any authentication stage fails
        return Optional.empty();
    }

    @Override
    public Optional<User> loginWithSecretAnswer(String name, String secretAnswer) {
        LoggerUtil.info(getClass(), "Alternative login attempt (secret answer) for user: " + name);

        Optional<User> userOpt = userDao.findByName(name);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Ensure the user has a secret answer configured and it matches
            if (user.getSecretAnswer() != null &&
                    PasswordUtils.checkPassword(secretAnswer, user.getSecretAnswer())) {
                LoggerUtil.info(getClass(), "User '" + name + "' logged in successfully using secret answer.");
                return Optional.of(user);
            } else {
                LoggerUtil.warn(getClass(), "Alternative login failed for user '" + name + "': Incorrect secret answer.");
            }
        } else {
            LoggerUtil.warn(getClass(), "Alternative login failed: User '" + name + "' not found.");
        }

        return Optional.empty();
    }

    @Override
    public Optional<User> loginAsGuest() {
        LoggerUtil.info(getClass(), "Attempting guest login.");

        Optional<User> guestUser = userDao.findByName("Anonymous");

        if (guestUser.isPresent()) {
            LoggerUtil.info(getClass(), "Guest login successful.");
        } else {
            LoggerUtil.error(getClass(), "Guest login failed: 'Anonymous' user not found in the database. Check data.sql initialization.");
        }

        return guestUser;
    }

    // ------------------------------------------------------------
    //                      Account Recovery
    // ------------------------------------------------------------

    @Override
    public Optional<String> getPasswordHint(String name) {
        LoggerUtil.debug(getClass(), "Fetching password hint for user: " + name);
        return userDao.findByName(name).map(User::getPasswordHint);
    }

    @Override
    public Optional<String> getSecretQuestion(String name) {
        LoggerUtil.debug(getClass(), "Fetching secret question for user: " + name);
        return userDao.findByName(name).map(User::getSecretQuestion);
    }

/*
     * TODO: This method is currently disabled as it is not part of the MVP.
     * The business rule states that emails are not strictly unique, so a reset
     * should ideally be triggered by username, not email.
     *
    @Override
    public PasswordResetStatus resetPassword(String username) {
        // Implementation pending
    }
    */

}
