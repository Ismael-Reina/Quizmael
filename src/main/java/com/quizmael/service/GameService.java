package com.quizmael.service;

import com.quizmael.model.*;

import java.util.List;

public interface GameService {

    /**
     * Creates and starts a new game session for the specified user and test.
     *
     * @param user          the user who is playing the game
     * @param tests         the tests associated with the game
     * @param questionCount the number of questions to be included in the game
     * @return              the initialized Game instance with metadata but without user answers
     */
    Game startGame(User user, List<QuizTest> tests, int questionCount);

    /**
     * Submits an answer for a specific question within a game.
     *
     * @param gameQuestion   the GameQuestion object representing the question in the current game
     * @param selectedAnswer the selected answer
     */
    void submitAnswer(GameQuestion gameQuestion, Answer selectedAnswer);

}
