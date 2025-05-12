package com.quizmael.controller;

import com.quizmael.model.User;
import com.quizmael.service.AuthService;
import com.quizmael.session.SessionContext;

import javax.swing.*;
import java.util.Optional;

/**
 * Controller responsible for handling authentication actions such as login and registration.
 * It delegates logic to the AuthService and coordinates navigation via the AppController.
 * Displays basic user feedback through dialogs.
 *
 * Note: This controller assumes a simple interaction with the view using JOptionPane,
 * and is not fully decoupled from the UI layer.
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public class AuthController {

    private final AuthService authService;
    private final AppController appController;

    public AuthController(AuthService authService, AppController appController) {
        this.authService = authService;
        this.appController = appController;
    }

    /**
     * Attempts to log in the user with the provided username and password.
     * If successful, updates the session context and navigates to the main menu.
     * Shows appropriate feedback dialogs for validation errors or failed login.
     *
     * @param username the username entered by the user
     * @param password the password entered by the user
     */
    public void login(String username, String password) {
        if (username.isBlank() || password.isBlank()) {
            JOptionPane.showMessageDialog(null, "Username and password are required.", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Optional<User> user = authService.login(username, password);
        if (user.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Invalid credentials.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        } else {
            SessionContext.getInstance().setCurrentUser(user.orElse(null));
            JOptionPane.showMessageDialog(null, "Welcome, " + user.get().getName() + "!", "Login Successful", JOptionPane.INFORMATION_MESSAGE);
            appController.showMainMenuPanel();
        }
    }

    /**
     * Placeholder for user registration logic.
     * Currently displays a message indicating the feature is not yet implemented.
     *
     * @param username the username for the new account
     * @param password the password for the new account
     */
    public void register(String username, String password) {
        // TODO: implementar un registro simple
        JOptionPane.showMessageDialog(null, "Register feature not implemented yet.");
    }
}
