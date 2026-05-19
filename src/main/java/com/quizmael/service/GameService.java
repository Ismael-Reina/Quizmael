package com.quizmael.service;

import com.quizmael.model.*;

import java.util.List;

/**
 * Service interface for managing game sessions in the quiz application.
 * This includes starting a new game, submitting answers, and handling game-related logic.
 * <p>
 * The methods in this interface are designed to be used by the game controller
 * to manage the flow of the game and user interactions.
 * </p>
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public interface GameService {

    // ------------------------------------------------------------
    //                   Game Lifecycle Methods
    // ------------------------------------------------------------

    /**
     * Creates and starts a new game session for the specified user and test.
     *
     * @param user            the user who is playing the game
     * @param currentQuizTest the test associated with the game
     * @param questionCount   the number of questions to be included in the game
     * @return                the initialized Game instance with metadata but without user answers
     */
    Game startGame(User user, QuizTest currentQuizTest, int questionCount);

    /**
     * Submits an answer for a specific question within a game.
     *
     * @param gameQuestion   the GameQuestion object representing the question in the current game
     * @param selectedAnswer the selected answer
     */
    void submitAnswer(GameQuestion gameQuestion, Answer selectedAnswer);

    /**
     * Persists the completed game and its associated data.
     *
     * @param game the game object to be saved
     */
    void finishGame(Game game);

    // ------------------------------------------------------------
    //                   Lookup Methods
    // ------------------------------------------------------------

    /**
     * Retrieves the list of GameQuestion objects associated with a specific game.
     *
     * @param game the game for which to retrieve the questions
     * @return a list of GameQuestion objects
     */
    public List<GameQuestion> getGameQuestions(Game game);


}
