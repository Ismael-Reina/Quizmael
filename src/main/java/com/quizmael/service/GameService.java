package com.quizmael.service;

import com.quizmael.model.Game;
import com.quizmael.model.QuizTest;
import com.quizmael.model.User;

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
     * @param gameId     the game session ID
     * @param questionId the question being answered
     * @param answerId   the selected answer ID
     */
    void submitAnswer(int gameId, int questionId, int answerId);

}
