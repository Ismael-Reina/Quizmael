package com.quizmael.controller;

import com.quizmael.gui.helpers.PanelManager;
import com.quizmael.gui.views.admin.AdminPanel;
import com.quizmael.gui.views.auth.LoginPanel;
import com.quizmael.gui.views.auth.RegisterPanel;
import com.quizmael.gui.views.game.PlayPanel;
import com.quizmael.gui.views.game.QuizSelectionPanel;
import com.quizmael.gui.views.game.ResultsPanel;
import com.quizmael.gui.views.mainmenu.MainMenuPanel;
import com.quizmael.gui.views.profile.AppSettingsPanel;
import com.quizmael.gui.windows.MainWindow;
import com.quizmael.service.AuthService;
import com.quizmael.service.impl.AuthServiceImpl;
import com.quizmael.session.SessionContext;

/**
 * Central controller for managing the application's state and navigation.
 * It initializes the main window, registers all view panels, and handles navigation between them.
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public class AppController {

    // ------------------------------------------------------------
    //                      Attributes
    // ------------------------------------------------------------

    // Singleton instance of the session context
    private final SessionContext sessionContext = SessionContext.getInstance();

    // Manages switching between different view panels
    private final PanelManager panelManager;

    private AuthService authService;
    private AuthController authController;


    // ------------------------------------------------------------
    //                      Public Methods
    // ------------------------------------------------------------

    /**
     * Constructor that initializes the main application window and registers all view panels.
     */
    public AppController() {

        MainWindow mainWindow = new MainWindow();    // Create the main application window
        panelManager = mainWindow.getPanelManager(); // Obtain the panel manager from the main window
        registerPanels();                            // Register all view panels to the panel manager
        showLoginPanel();                            // Show the initial panel (Login)
        mainWindow.setVisible(true);                 // Display the main window
    }

    // ------------------------------------------------------------
    //                 Navigation Between Panels
    // ------------------------------------------------------------

    /**
     * Displays the login panel.
     */
    public void showLoginPanel() {
        panelManager.showPanel("login");
    }

    /**
     * Displays the register panel.
     */
    public void showRegisterPanel() {
        panelManager.showPanel("register");
    }

    /**
     * Displays the main menu panel.
     */
    public void showMainMenuPanel() {
        panelManager.showPanel("mainMenu");
    }

    /**
     * Displays the admin panel.
     */
    public void showAdminPanel() {
        panelManager.showPanel("admin");
    }

    /**
     * Displays the play panel.
     */
    public void showPlayPanel() {
        panelManager.showPanel("play");
    }

    /**
     * Displays the quiz selection panel.
     */
    public void showQuizSelectionPanel() {
        panelManager.showPanel("quizSelection");
    }

    /**
     * Displays the results panel.
     */
    public void showResultsPanel() {
        panelManager.showPanel("results");
    }

    /**
     * Displays the application settings panel.
     */
    public void showAppSettingsPanel() {
        panelManager.showPanel("appSettings");
    }

    // ------------------------------------------------------------
    //                        Getters
    // ------------------------------------------------------------

    /**
     * Returns the current session context.
     *
     * @return the session context
     */
    public SessionContext getSessionContext() {
        return sessionContext;
    }

    /**
     * Returns the AuthController for user authentication and registration.
     *
     * @return the AuthController instance
     */
    public AuthController getAuthController() {
        return authController;
    }

    // ------------------------------------------------------------
    //                      Private Methods
    // ------------------------------------------------------------

    /**
     * Registers all application panels with the panel manager.
     */
    private void registerPanels() {
        authService = new AuthServiceImpl();
        authController = new AuthController(authService, this);

        panelManager.addPanel("login", new LoginPanel(this));
        panelManager.addPanel("register", new RegisterPanel(this));
        panelManager.addPanel("mainMenu", new MainMenuPanel(this));
        panelManager.addPanel("admin", new AdminPanel(this));
        panelManager.addPanel("play", new PlayPanel(this));
        panelManager.addPanel("quizSelection", new QuizSelectionPanel(this));
        panelManager.addPanel("results", new ResultsPanel(this));
        panelManager.addPanel("appSettings", new AppSettingsPanel(this));
    }
}