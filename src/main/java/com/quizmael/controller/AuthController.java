package com.quizmael.controller;

import com.quizmael.gui.helpers.PanelManager;
import com.quizmael.model.User;
import com.quizmael.model.enums.Role;
import com.quizmael.service.AuthService;
import com.quizmael.service.UserService;
import com.quizmael.session.SessionContext;
import com.quizmael.util.LoggerUtil;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

/**
 * Controller responsible for handling authentication-related actions.
 * Acts as a bridge between Auth views and the {@link AuthService}.
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public class AuthController {

    private final AuthService authService;
    private final PanelManager panelManager;
    private final SessionContext sessionContext;

    /**
     * Constructs a new AuthController with the required service and panel manager.
     * <p>Initializes the session context to manage the current user state
     * throughout the application flow.</p>
     *
     * @param authService   the service handling authentication and registration logic.
     * @param panelManager  the helper utility used to manage navigation between panels.
     */
    public AuthController(AuthService authService, PanelManager panelManager) {
        this.authService = authService;
        this.panelManager = panelManager;
        this.sessionContext = SessionContext.getInstance();
    }

    // ------------------------------------------------------------
    //                      Public Methods
    // ------------------------------------------------------------

    /**
     * Handles the user login process.
     *
     * @param username The entered username.
     * @param password The entered password.
     * @return true if login was successful, false otherwise.
     */
    public boolean handleLogin(String username, String password) {
        LoggerUtil.info(AuthController.class, "Attempting login for user: " + username);

        try {
            // 1. Call the service
            Optional<User> userOpt = authService.login(username, password);

            if (userOpt.isPresent()) {
                User user = userOpt.get();

                // 2. Update the session global state
                sessionContext.setCurrentUser(user);
                LoggerUtil.info(AuthController.class, "Login successful. Session started for: " + username);

                // 3. Coordinate navigation
                navigateToMainMenu();
                return true;
            } else {
                LoggerUtil.warn(AuthController.class, "Login failed for: " + username + ". Invalid credentials.");
                return false;
            }
        } catch (Exception e) {
            LoggerUtil.error(AuthController.class, "Critical error during login process.", e);
            throw e; // Let the view handle the error message via BasePanel
        }
    }

    /**
     * Attempts to log in using a default guest account.
     */
    public void loginAsGuest() {
        handleLogin("Anonymous", null);
    }

    /**
     * Handles the user registration process.
     * * @param user The new user entity to be registered.
     * @return true if registration was successful.
     */
    public boolean handleRegister(User user) {
        LoggerUtil.info(AuthController.class, "Attempting to register new user: " + user.getName());

        try {
            User registeredUser = authService.register(user);
            LoggerUtil.info(AuthController.class, "User registered successfully: " + user.getName());

            // After registration, we send them to the Main Menu
            sessionContext.setCurrentUser(registeredUser);
            navigateToMainMenu();
            return true;
        } catch (Exception e) {
            LoggerUtil.error(AuthController.class, "Registration failed for user: " + user.getName(), e);
            throw e; // The view will show the specific i18n error
        }
    }

    /**
     * Clears the current session and returns to the login screen.
     */
    public void handleLogout() {
        String lastUser = sessionContext.getCurrentUser() != null ?
                sessionContext.getCurrentUser().getName() : "Guest";

        LoggerUtil.info(AuthController.class, "Logging out user: " + lastUser);

        sessionContext.setCurrentUser(null); // Clear the singleton session
        navigateToLogin();
    }

    /**
     * Navigates the UI back to the login screen.
     */
    public void navigateToLogin() {
        panelManager.showPanel(PanelManager.LOGIN);
    }

    /**
     * Navigates the UI to the registration screen.
     */
    public void navigateToRegister() {
        panelManager.showPanel(PanelManager.REGISTER);
    }

    /**
     * Navigates the UI to the registration screen.
     */
    public void navigateToMainMenu() {
        panelManager.showPanel(PanelManager.MAIN_MENU);
    }

//    private final AuthService authService;
//    private final UserService userService; // TODO: 04/05/2026 - agrego este atributo para hacer findAll() más abajo. También lo agrego e inicializo al constructor
//    private final AppController appController;
//
//    public AuthController(AuthService authService, UserService userService, AppController appController) {
//        this.authService = authService;
//        this.userService = userService;
//        this.appController = appController;
//    }
//
//    /**
//     * Attempts to log in the user with the provided username and password.
//     * If successful, updates the session context and navigates to the main menu.
//     * Shows appropriate feedback dialogs for validation errors or failed login.
//     *
//     * @param username the username entered by the user
//     * @param password the password entered by the user
//     */
//    public void login(String username, String password) {
//        if (username.isBlank() || password.isBlank()) {
//            // JOptionPane.showMessageDialog(null, "Username and password are required.", "Login Error", JOptionPane.ERROR_MESSAGE); // TODO: Move this literal to a resource bundle
//            JOptionPane.showMessageDialog(null, "El nombre de usuario y la contraseña son obligatorios.", "Error de inicio de sesión", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        Optional<User> user = authService.login(username, password);
//        if (user.isEmpty()) {
//            // JOptionPane.showMessageDialog(null, "Invalid credentials.", "Login Failed", JOptionPane.ERROR_MESSAGE); // TODO: Move this literal to a resource bundle
//            JOptionPane.showMessageDialog(null, "Credenciales inválidas.", "Inicio de sesión fallido", JOptionPane.ERROR_MESSAGE);
//        } else {
//            User loggedInUser = user.get();
//            SessionContext.getInstance().setCurrentUser(loggedInUser);
//            // JOptionPane.showMessageDialog(null, "Welcome, " + loggedInUser.getName() + "!", "Login Successful", JOptionPane.INFORMATION_MESSAGE); // TODO: Move this literal to a resource bundle. Use showTimedMessage to show this message
//
//            // Verificar si el usuario es administrador
//            boolean isAdmin = loggedInUser.getRole() == Role.ADMINISTRATOR;
//            appController.getMainMenuPanel().enableAdminPanelButton(isAdmin);
//
//            appController.getMainMenuPanel().refreshUserName(SessionContext.getInstance().getCurrentUser().getName()); // Refresh the username in the main menu
//            appController.showMainMenuPanel();
//        }
//
//    }
//
//
//    /**
//     * Logs in the user as an anonymous guest.
//     * <p>
//     * Retrieves the predefined guest user (e.g., with ID = 1) from the database and sets it as the current session user.
//     * Displays appropriate dialogs based on whether the guest user could be loaded.
//     * </p>
//     *
//     * @see com.quizmael.service.AuthService#loginAsGuest()
//     */
//    public void loginAsGuest() {
//        Optional<User> guest = authService.loginAsGuest();
//
//        if (guest.isEmpty()) {
//            // JOptionPane.showMessageDialog(null, "Unable to load guest account.", "Error", JOptionPane.ERROR_MESSAGE); // TODO: Move this literal to a resource bundle
//            JOptionPane.showMessageDialog(null, "No se pudo cargar la cuenta de invitado.", "Error", JOptionPane.ERROR_MESSAGE);
//        } else {
//            SessionContext.getInstance().setCurrentUser(guest.get());
//            // JOptionPane.showMessageDialog(null, "Logged in as guest.", "Guest Login", JOptionPane.INFORMATION_MESSAGE); // TODO: Move this literal to a resource bundle. Use showTimedMessage to show this message
//            appController.getMainMenuPanel().refreshUserName(SessionContext.getInstance().getCurrentUser().getName()); // Refresh the username in the main menu
//            appController.showMainMenuPanel();
//        }
//    }
//
//    // TODO: modificado el 04/05/2026. elimino getAllUsers de AuthService. Uso findAll() de userService. Renombrar método a findAll también. De todos modos, plantear si esto debe hacerlo AuthController (mejor UserController)
//    public List<User> getAllUsers() {
//        return userService.findAll().stream()
//                .filter(user -> user.getId() > 9) // Do not show system users
//                .toList();
//    }
//
//    /**
//     * Handles the registration of a new user.
//     * Validates that the passwords match and that the birth date is in a valid format.
//     * If all validations pass, delegates the registration to the AuthService.
//     *
//     * @param username        the desired username for the new account
//     * @param email           the email address of the user
//     * @param password        the password for the new account
//     * @param passwordRepeat  the repeated password for confirmation
//     * @param passwordHint    a hint to help recover the password
//     * @param secretQuestion  the user's secret question for recovery
//     * @param secretAnswer    the answer to the secret question
//     * @param birthDateStr    the birth date in ISO format (yyyy-MM-dd)
//     * @throws IllegalArgumentException if passwords do not match or the birth date is invalid
//     */
//    public void registerUser(String username, String email, String password, String passwordRepeat,
//                            String passwordHint, String secretQuestion, String secretAnswer, String birthDateStr) {
//        if (!password.equals(passwordRepeat)) {
//            // throw new IllegalArgumentException("Passwords do not match."); // TODO: Move this literal to a resource bundle
//            throw new IllegalArgumentException("Las contraseñas no coinciden.");
//
//        }
//
//        LocalDate birthDate;
//        try {
//            birthDate = birthDateStr.isEmpty() ? null : LocalDate.parse(birthDateStr); // ISO format (yyyy-MM-dd)
//        } catch (DateTimeParseException e) {
//            // throw new IllegalArgumentException("Invalid birth date format."); // TODO: Move this literal to a resource bundle
//            throw new IllegalArgumentException("Formato de fecha de nacimiento inválido.");
//        }
//
//        try {
//            authService.register(username, email, password, passwordHint, secretQuestion, secretAnswer, birthDate);
//        } catch (IllegalArgumentException e) {
//            // JOptionPane.showMessageDialog(null, e.getMessage(), "Registration Error", JOptionPane.ERROR_MESSAGE); // TODO: Move this literal to a resource bundle
//            JOptionPane.showMessageDialog(null, e.getMessage(), "Error de registro", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        // Automatic login after registration
//        Optional<User> loggedIn = authService.login(username, password);
//        if (loggedIn.isPresent()) {
//            SessionContext.getInstance().setCurrentUser(loggedIn.get());
//            // JOptionPane.showMessageDialog(null, "Registration successful! Welcome, " + loggedIn.get().getName() + "!",
//            //        "Success", JOptionPane.INFORMATION_MESSAGE); // TODO: Move this literal to a resource bundle. Use showTimedMessage to show this message
//
//            appController.getMainMenuPanel().refreshUserName(SessionContext.getInstance().getCurrentUser().getName()); // Refresh the username in the main menu
//            appController.showMainMenuPanel();
//        } else {
//            // JOptionPane.showMessageDialog(null, "Registration succeeded, but automatic login failed.", // TODO: Move this literal to a resource bundle
//            //        "Warning", JOptionPane.WARNING_MESSAGE);
//            JOptionPane.showMessageDialog(null, "El registro fue exitoso, pero no se pudo iniciar sesión automáticamente.", "Advertencia", JOptionPane.WARNING_MESSAGE);
//        }
//    }
//
//    /**
//     * Navigates back to the login screen.
//     */
//    public void goBackToLogin() {
//        appController.showLoginPanel();
//    }

}
