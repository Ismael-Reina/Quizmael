package com.quizmael.controller;

/**
 * Controller responsible for managing the game flow and logic,
 * including loading quizzes, starting a game, and processing results.
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public class GameController {

    private final AppController appController;

    public GameController(AppController appController) {
        this.appController = appController;
    }

    /**
     * Displays the quiz selection panel where the user can browse and select a test to play.
     */
    public void showQuizSelection() {
        appController.showQuizSelectionPanel();
    }

}
