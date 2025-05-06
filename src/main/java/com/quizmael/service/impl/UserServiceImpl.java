package com.quizmael.service.impl;

import com.quizmael.dao.UserDao;
import com.quizmael.dao.impl.UserDaoImpl;
import com.quizmael.service.AuthService;
import com.quizmael.model.User;
import com.quizmael.service.UserService;
import com.quizmael.service.enums.ChangePasswordResult;
import com.quizmael.util.PasswordUtils;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the {@link UserService} interface.
 * Manages user profile operations such as lookup, update and deletion,
 * excluding authentication and password recovery, which are handled by {@link AuthService}.
 *
 * @author Ismael Reina
 * @version 2.0
 */
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl() {
        this.userDao = new UserDaoImpl();
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

    // ------------------------------------------------------------
    //                      Profile Management
    // ------------------------------------------------------------

    @Override
    public void updateUser(User user) {
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

    @Override
    public boolean changeSecretAnswer(int userId, String newSecretAnswer) {
        Optional<User> userOpt = userDao.findById(userId);

        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();
        user.setSecretAnswer(PasswordUtils.hashPassword(newSecretAnswer));
        userDao.update(user);

        return true;
    }

}
