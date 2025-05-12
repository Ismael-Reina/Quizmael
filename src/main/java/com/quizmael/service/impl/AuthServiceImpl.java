package com.quizmael.service.impl;

import com.quizmael.dao.UserDao;
import com.quizmael.dao.impl.UserDaoImpl;
import com.quizmael.model.User;
import com.quizmael.service.AuthService;
import com.quizmael.service.enums.PasswordResetStatus;
import com.quizmael.util.PasswordUtils;


import java.util.Optional;
import java.util.List;

/**
 * Implementation of the {@link AuthService} interface.
 * Handles user-related operations such as authentication, registration,
 * password recovery, and data updates using a {@link UserDao}.
 * Passwords are managed securely through hashing and comparison utilities.
 *
 * @author Ismael Reina
 * @version 1.0
 */
public class AuthServiceImpl implements AuthService {

    private final UserDao userDao;

    public AuthServiceImpl() {
        this.userDao = new UserDaoImpl();
    }

    // ------------------------------------------------------------
    //                      Registration
    // ------------------------------------------------------------

    @Override
    public Optional<User> register(User user) {
        if (userDao.findByName(user.getName()).isPresent()) {
            return Optional.empty(); // username already exists
        }

        user.setPassword(PasswordUtils.hashPassword(user.getPassword()));
        User savedUser = userDao.save(user);
        return Optional.of(savedUser);
    }

    // ------------------------------------------------------------
    //                 Authentication / Login
    // ------------------------------------------------------------

    @Override
    public Optional<User> login(String name, String password) {
        Optional<User> user = userDao.findByName(name);
        return user.filter(u -> PasswordUtils.checkPassword(password, u.getPassword()));
    }

    @Override
    public Optional<User> loginWithSecretAnswer(String name, String secretAnswer) {
        Optional<User> user = userDao.findByName(name);
        return user.filter(u -> u.getSecretAnswer() != null && PasswordUtils.checkPassword(secretAnswer, u.getSecretAnswer()));
    }

    // ------------------------------------------------------------
    //                      Account Recovery
    // ------------------------------------------------------------

    @Override
    public Optional<String> getPasswordHint(String name) {
        return userDao.findByName(name).map(User::getPasswordHint);
    }

    @Override
    public Optional<String> getSecretQuestion(String name) {
        return userDao.findByName(name).map(User::getSecretQuestion);
    }

    @Override
    public PasswordResetStatus  resetPasswordWithEmail(String name) {
        Optional<User> userOpt = userDao.findByName(name);

        // a) The username does not exist
        if (userOpt.isEmpty()) {
            return PasswordResetStatus.USER_NOT_FOUND;
        }

        User user = userOpt.get();

        // b) The user exists but has no associated email
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            return PasswordResetStatus.EMAIL_NOT_SET;
        }

        // c) Valid user with email: generate and set a new temporary password
        String tempPassword = PasswordUtils.generateRandomPassword();
        user.setPassword(PasswordUtils.hashPassword(tempPassword));
        userDao.update(user);

        // Simulate email sending
        System.out.println("Simulated email to " + user.getEmail() + ": new temp password is " + tempPassword);
        // TODO: Implement actual email sending logic

        return PasswordResetStatus.RESET_EMAIL_SENT;
    }

}
