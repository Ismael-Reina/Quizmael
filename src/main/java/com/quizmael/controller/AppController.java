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

        // Create the main application window
        MainWindow mainWindow = new MainWindow();

        // Obtain the panel manager from the main window
        panelManager = mainWindow.getPanelManager();

        // Register all view panels to the panel manager
        registerPanels();

        // Show the initial panel (Login)
        showLoginPanel();

        // Display the main window
        mainWindow.setVisible(true);
    }

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
    //                      Private Methods
    // ------------------------------------------------------------

    /**
     * Registers all application panels with the panel manager.
     */
    private void registerPanels() {
        authService = new AuthServiceImpl();
        authController = new AuthController(authService, this);

        panelManager.addPanel("login", new LoginPanel(authController));
        panelManager.addPanel("register", new RegisterPanel(this));
        panelManager.addPanel("mainMenu", new MainMenuPanel(this));
        panelManager.addPanel("admin", new AdminPanel(this));
        panelManager.addPanel("play", new PlayPanel(this));
        panelManager.addPanel("quizSelection", new QuizSelectionPanel(this));
        panelManager.addPanel("results", new ResultsPanel(this));
        panelManager.addPanel("appSettings", new AppSettingsPanel(this));
    }

}