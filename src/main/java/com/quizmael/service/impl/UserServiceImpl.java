package com.quizmael.service.impl;

import com.quizmael.dao.UserDao;
import com.quizmael.dao.impl.UserDaoImpl;
import com.quizmael.model.enums.Role;
import com.quizmael.service.AuthService;
import com.quizmael.model.User;
import com.quizmael.service.UserService;
import com.quizmael.service.enums.ChangePasswordResult;
import com.quizmael.util.HibernateUtil;
import com.quizmael.util.LoggerUtil;
import com.quizmael.util.PasswordUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

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
    // Regex for password strength validation
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!._\\-]).{6,20}$";

    /**
     * Constructs the UserServiceImpl with the required user data access object.
     *
     * @param userDao the data access object used for user profile and management operations.
     */
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    // ------------------------------------------------------------
    //                 User Lookup and Deletion
    // ------------------------------------------------------------

    @Override
    public Optional<User> findById(int id) {
        LoggerUtil.debug(UserServiceImpl.class, "Finding user by ID: " + id);
        return userDao.findById(id);
    }

    @Override
    public Optional<User> findByName(String name) {
        LoggerUtil.debug(UserServiceImpl.class, "Finding user by name: " + name);
        return userDao.findByName(name);
    }

    @Override
    public List<User> findAll() {
        LoggerUtil.debug(UserServiceImpl.class, "Fetching all users");
        return userDao.findAll();
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }

    @Override
    public boolean deleteById(int id) {
        LoggerUtil.info(UserServiceImpl.class, "Initiating atomic administrative anonymization and deletion workflow for user ID: " + id);

        // Safety check: Protect system integrity by preventing the removal of reserved system accounts (IDs 1-9)
        if (id < 10) {
            LoggerUtil.warn(UserServiceImpl.class, "Aborted deletion request: Reserved system and placeholder accounts cannot be removed.");
            return false;
        }

        Optional<User> userOptional = userDao.findById(id);
        if (!userOptional.isPresent()) {
            LoggerUtil.warn(UserServiceImpl.class, "Aborted operation: Target user entity not found in persistence registry. ID: " + id);
            return false;
        }

        org.hibernate.Transaction tx = null;

        // Open a single explicit session context to wrap the entire pipeline into an atomic unit of work
        try (org.hibernate.Session session = com.quizmael.util.HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            // 1. Reassign all quizzes created by this user to the 'Deleted User' placeholder account (ID 3)
            session.createQuery("UPDATE QuizTest qt SET qt.creator.id = 3 WHERE qt.creator.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();

            // 2. Reassign all gameplay records played by this user to the 'Deleted User' placeholder account (ID 3)
            session.createQuery("UPDATE Game g SET g.user.id = 3 WHERE g.user.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();

            // 3. Clear moderation audit links to avoid ON DELETE RESTRICT blocks on the Tests table
            session.createQuery("UPDATE QuizTest qt SET qt.moderatedBy = null WHERE qt.moderatedBy.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();

            // 4. Perform the physical user deletion within the SAME active transaction boundary
            User targetUser = session.get(User.class, id);
            if (targetUser != null) {
                session.remove(targetUser); // Schedules the physical DELETE query inside the current transaction
            }

            // CRITICAL: Commit ONLY when every single dynamic mutation step above succeeds seamlessly
            tx.commit();
            LoggerUtil.info(UserServiceImpl.class, "User registry and dependencies processed atomically. Deletion success. ID: " + id);
            return true;

        } catch (Exception e) {
            // ABSOLUTE SAFEGUARD: If anything strikes down the pipeline, revert every single mutation instantly
            if (tx != null && tx.isActive()) {
                try {
                    tx.rollback();
                    LoggerUtil.info(UserServiceImpl.class, "Transaction successfully rolled back. Database state preserved.");
                } catch (Exception rollbackException) {
                    LoggerUtil.error(UserServiceImpl.class, "Fatal failure while attempting transaction rollback sequence", rollbackException);
                }
            }
            LoggerUtil.error(UserServiceImpl.class, "Critical transactional failure executing user deletion mapping flow for ID: " + id, e);
            return false;
        }
    }

    // ------------------------------------------------------------
    //                      Profile Management
    // ------------------------------------------------------------

    @Override
    public void updateUser(User user) {
        LoggerUtil.info(UserServiceImpl.class, "Updating profile for user: " + user.getName());
        try {
            userDao.update(user);
        } catch (Exception e) {
            LoggerUtil.error(UserServiceImpl.class, "Failed to update user: " + user.getName(), e);
            throw new RuntimeException("error.database.internal", e);
        }
    }

    @Override
    public boolean forcePasswordReset(int userId, String newPassword) {
        LoggerUtil.info(UserServiceImpl.class, "Admin forcing password update for user ID: " + userId);

        // Strict Rule #2: Password validation
        if (!isValidPassword(newPassword)) {
            LoggerUtil.warn(UserServiceImpl.class, "Password strength validation failed.");
            throw new IllegalArgumentException("error.validation.password_strength");
        }

        Optional<User> userOpt = userDao.findById(userId);
        if (userOpt.isEmpty()) {
            LoggerUtil.warn(UserServiceImpl.class, "User not found for password update. ID: " + userId);
            return false;
        }

        User user = userOpt.get();
        user.setPassword(PasswordUtils.hashPassword(newPassword));
        userDao.update(user);
        LoggerUtil.info(UserServiceImpl.class, "Password successfully updated for user ID: " + userId);
        return true;
    }

    @Override
    public boolean updateRole(int userId, Role newRole) { // Note: Changed String to Role Enum
        LoggerUtil.info(UserServiceImpl.class, "Updating role for user ID: " + userId + " to " + newRole);
        Optional<User> userOpt = userDao.findById(userId);
        if (userOpt.isEmpty()) {
            LoggerUtil.warn(UserServiceImpl.class, "User not found for role update. ID: " + userId);
            return false;
        }

        User user = userOpt.get();
        user.setRole(newRole);
        userDao.update(user);
        return true;
    }

    @Override
    public ChangePasswordResult changePassword(int userId, String oldPassword, String newPassword) {
        LoggerUtil.info(UserServiceImpl.class, "User requested password change. ID: " + userId);

        // Strict Rule #2: Password validation
        if (!isValidPassword(newPassword)) {
            throw new IllegalArgumentException("error.validation.password_strength");
        }

        Optional<User> userOpt = userDao.findById(userId);
        if (userOpt.isEmpty()) {
            return ChangePasswordResult.USER_NOT_FOUND;
        }

        User user = userOpt.get();
        if (!PasswordUtils.checkPassword(oldPassword, user.getPassword())) {
            LoggerUtil.warn(UserServiceImpl.class, "Old password incorrect for user ID: " + userId);
            return ChangePasswordResult.OLD_PASSWORD_INCORRECT;
        }

        user.setPassword(PasswordUtils.hashPassword(newPassword));
        userDao.update(user);
        LoggerUtil.info(UserServiceImpl.class, "User password changed successfully. ID: " + userId);

        return ChangePasswordResult.SUCCESS;
    }

    @Override
    public boolean changeSecretAnswer(int userId, String newSecretAnswer) {
        LoggerUtil.info(UserServiceImpl.class, "Updating secret answer for user ID: " + userId);
        Optional<User> userOpt = userDao.findById(userId);

        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();
        user.setSecretAnswer(PasswordUtils.hashPassword(newSecretAnswer));
        userDao.update(user);
        return true;
    }

    // ------------------------------------------------------------
    //                      Private Methods
    // ------------------------------------------------------------
    /**
     * Helper method to validate password strength.
     */
    private boolean isValidPassword(String password) {
        if (password == null) return false;
        return Pattern.compile(PASSWORD_PATTERN).matcher(password).matches();
    }
}
