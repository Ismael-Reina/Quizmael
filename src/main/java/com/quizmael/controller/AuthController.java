package com.quizmael.controller;

import com.quizmael.model.User;
import com.quizmael.service.AuthService;
import com.quizmael.session.SessionContext;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
            User loggedInUser = user.get();
            SessionContext.getInstance().setCurrentUser(loggedInUser);
            JOptionPane.showMessageDialog(null, "Welcome, " + loggedInUser.getName() + "!", "Login Successful", JOptionPane.INFORMATION_MESSAGE);
            appController.showMainMenuPanel();
        }

    }


    /**
     * Logs in the user as an anonymous guest.
     * <p>
     * Retrieves the predefined guest user (e.g., with ID = 1) from the database and sets it as the current session user.
     * Displays appropriate dialogs based on whether the guest user could be loaded.
     * </p>
     *
     * @see com.quizmael.service.AuthService#loginAsGuest()
     */
    public void loginAsGuest() {
        Optional<User> guest = authService.loginAsGuest();

        if (guest.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Unable to load guest account.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            SessionContext.getInstance().setCurrentUser(guest.get());
            JOptionPane.showMessageDialog(null, "Logged in as guest.", "Guest Login", JOptionPane.INFORMATION_MESSAGE);
            appController.showMainMenuPanel();
        }
    }

    /**
     * Handles the registration of a new user.
     * Validates that the passwords match and that the birth date is in a valid format.
     * If all validations pass, delegates the registration to the AuthService.
     *
     * @param username        the desired username for the new account
     * @param email           the email address of the user
     * @param password        the password for the new account
     * @param passwordRepeat  the repeated password for confirmation
     * @param passwordHint    a hint to help recover the password
     * @param secretQuestion  the user's secret question for recovery
     * @param secretAnswer    the answer to the secret question
     * @param birthDateStr    the birth date in ISO format (yyyy-MM-dd)
     * @throws IllegalArgumentException if passwords do not match or the birth date is invalid
     */
    public void registerUser(String username, String email, String password, String passwordRepeat,
                             String passwordHint, String secretQuestion, String secretAnswer, String birthDateStr) {
        if (!password.equals(passwordRepeat)) {
            throw new IllegalArgumentException("Passwords do not match.");
        }

        LocalDate birthDate;
        try {
            birthDate = LocalDate.parse(birthDateStr); // ISO format (yyyy-MM-dd)
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid birth date format.");
        }

        authService.register(username, email, password, passwordHint, secretQuestion, secretAnswer, birthDate);
    }

}
