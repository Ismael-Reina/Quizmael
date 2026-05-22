package com.quizmael.controller;

import com.quizmael.gui.helpers.PanelManager;
import com.quizmael.model.User;
import com.quizmael.model.enums.Role;
import com.quizmael.service.QuizTestService;
import com.quizmael.service.UserService;
import com.quizmael.util.I18nUtil;
import java.util.List;
import javax.swing.DefaultComboBoxModel;

/**
 * <strong>Controller</strong> responsible for user-related operations and UI data binding.
 *
 * <p>Acts as the intermediary between user services and the GUI. It handles the
 * provision of data models for user selection in filters and user profile management.</p>
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public class UserController {

    // ------------------------------------------------------------
    //                   Attributes
    // ------------------------------------------------------------
    private final UserService userService;
    private final QuizTestService quizTestService;
    private final PanelManager panelManager;


    // ------------------------------------------------------------
    //                   Constructor
    // ------------------------------------------------------------
    /**
     * Constructs a new UserController with its required dependencies.
     *
     * <p>Injects QuizTestService to allow retrieval of creators who have
     * published content, ensuring accurate filtering in the UI.</p>
     *
     * @param userService     the service for general user profile and account operations.
     * @param quizTestService the service used to identify active content creators.
     * @param panelManager  the helper utility used for switching between game-related views.
     */
    public UserController(UserService userService, QuizTestService quizTestService, PanelManager panelManager) {
        this.userService = userService;
        this.quizTestService = quizTestService;
        this.panelManager = panelManager;
    }

    // ------------------------------------------------------------
    //                      Data Retrieval & UI Models
    // ------------------------------------------------------------
    /**
     * Fetches all registered users from the service layer for administrative purposes.
     * @return a list of all user entities
     */
    public List<User> findAllUsers() {
        // Delegates directly to the business logic layer
        return userService.findAll();
    }

    /**
     * Creates and populates a ComboBox model with the names of all active creators.
     *
     * <p>This method uses {@code quizTestService.findAllCreatorsOfPublishedTests()}
     * to ensure the list only contains active creators (as Strings). It adds an
     * internationalized placeholder at the first position.</p>
     *
     * @return a {@link DefaultComboBoxModel} containing creator names.
     */
    public DefaultComboBoxModel<String> getCreatorsComboBoxModel() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

        // 1. Add internationalized placeholder
        model.addElement(I18nUtil.getMessage("filter.all_creators"));

        // 2. Retrieve creator names from the service
        List<String> creatorNames = quizTestService.findAllCreatorsOfPublishedTests();

        // 3. Populate model
        if (creatorNames != null) {
            for (String name : creatorNames) {
                model.addElement(name);
            }
        }

        return model;
    }

    // ------------------------------------------------------------
    //                    Administrative Operations
    // ------------------------------------------------------------
    /**
     * Updates the profile information of a user.
     *
     * @param user the user entity to update.
     */
    public void updateProfile(User user) {
        userService.update(user);
    }

    /**
     * Updates the role of a specific user.
     */
    public void updateUserRole(Integer userId, Role newRole) {
        userService.updateRole(userId, newRole);
    }

    /**
     * Forces a password reset for a user bypassing current password checks.
     */
    public void forcePasswordReset(Integer userId, String newPassword) {
        userService.forcePasswordReset(userId, newPassword);
    }

    /**
     * Permanently deletes a user from the database after running anonymization.
     *
     * @return true if deletion completed successfully, false otherwise.
     */
    public boolean deleteUser(Integer userId) {
        return userService.deleteById(userId);
    }

    // ------------------------------------------------------------
    //                      Navigation Methods
    // ------------------------------------------------------------
    /**
     * Navigates the UI to the administration panel.
     */
    public void navigateToAdminPanel() {
        panelManager.showPanel(PanelManager.ADMIN);
    }

    /**
     * Navigates the UI to the registration screen.
     */
    public void navigateToMainMenu() {
        panelManager.showPanel(PanelManager.MAIN_MENU);
    }
}