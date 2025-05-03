package com.quizmael.service.impl;

import com.quizmael.dao.UserDao;
import com.quizmael.dao.impl.UserDaoImpl;
import com.quizmael.model.User;
import com.quizmael.service.UserService;
import com.quizmael.service.enums.ChangePasswordResult;
import com.quizmael.service.enums.PasswordResetStatus;
import com.quizmael.util.PasswordUtils;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the {@link UserService} interface.
 * Handles user-related operations such as authentication, registration,
 * password recovery, and data updates using a {@link UserDao}.
 * Passwords are managed securely through hashing and comparison utilities.
 *
 * @author Ismael Reina
 * @version 1.0
 */
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl() {
        this.userDao = new UserDaoImpl();
    }

    // ------------------------------------------------------------
    //                 Authentication and Login
    // ------------------------------------------------------------

    @Override
    public Optional<User> login(String name, String password) {
        Optional<User> user = userDao.findByName(name);
        return user.filter(u -> PasswordUtils.checkPassword(password, u.getPassword()));
    }

    @Override
    public Optional<User> loginWithSecretAnswer(String name, String secretAnswer) {
        Optional<User> user = userDao.findByName(name);
        return user.filter(u -> u.getSecretAnswer() != null && u.getSecretAnswer().equals(secretAnswer));
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

    @Override
    public Optional<String> getPasswordHint(String name) {
        return userDao.findByName(name).map(User::getPasswordHint);
    }

    @Override
    public Optional<String> getSecretQuestion(String name) {
        return userDao.findByName(name).map(User::getSecretQuestion);
    }

    // ------------------------------------------------------------
    //           User Registration and Profile Management
    // ------------------------------------------------------------

    @Override
    public User register(User user) {
        user.setPassword(PasswordUtils.hashPassword(user.getPassword()));
        return userDao.save(user);
    }

    @Override
    public void updateUser(User user) {
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            user.setPassword(PasswordUtils.hashPassword(user.getPassword()));
        }
        userDao.update(user);
    }

    @Override
    public ChangePasswordResult changePassword(int userId, String oldPassword, String newPassword) {
        Optional<User> userOpt = userDao.findById(userId);

        if (userOpt.isEmpty()) {
            return ChangePasswordResult.USER_NOT_FOUND;
        }

        User user = userOpt.get();

        if (!PasswordUtils.checkPassword(oldPassword, user.getPassword())) {
            return ChangePasswordResult.OLD_PASSWORD_INCORRECT;
        }

        user.setPassword(PasswordUtils.hashPassword(newPassword));
        userDao.update(user);

        return ChangePasswordResult.SUCCESS;
    }

    // ------------------------------------------------------------
    //                 User Lookup and Deletion
    // ------------------------------------------------------------

    @Override
    public Optional<User> findById(int id) {
        return userDao.findById(id);
    }

    @Override
    public Optional<User> findByName(String name) {
        return userDao.findByName(name);
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public void deleteById(int id) {
        userDao.findById(id).ifPresent(userDao::delete);
    }
}
