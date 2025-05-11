package com.quizmael;

import com.quizmael.controller.AppController;

import javax.swing.SwingUtilities;

/**
 * Main application entry point.
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public class QuizmaelApp {

    public static void main(String[] args) {
        // Launches GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            AppController appController = new AppController();
        });
    }
}
