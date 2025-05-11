package com.quizmael.controller;

import com.quizmael.gui.windows.MainWindow;

/**
 * Central controller for managing the application's state and navigation.
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public class AppController {

    private MainWindow mainWindow;

    /**
     * Initializes the application: creates the main window.
     */
    public void initApp() {
        mainWindow = new MainWindow();
        mainWindow.setVisible(true);
    }

    public MainWindow getMainWindow() {
        return mainWindow;
    }
}