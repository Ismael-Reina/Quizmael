package com.quizmael.service.impl;

import com.quizmael.dao.GameDao;
import com.quizmael.dao.GameQuestionDao;
import com.quizmael.dao.QuestionDao;
import com.quizmael.model.*;
import com.quizmael.service.GameService;
import com.quizmael.util.LoggerUtil;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of the {@link GameService} interface.
 * Handles the core gameplay logic, including session initialization,
 * random question selection, answer submission, and score calculation.
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public class GameServiceImpl implements GameService {

    private final GameDao gameDao;
    private final GameQuestionDao gameQuestionDao;
    private final QuestionDao questionDao;

    /**
     * Constructs the GameServiceImpl with the necessary data access objects for gameplay logic.
     *
     * @param gameDao the DAO for persisting game session data.
     * @param gameQuestionDao the DAO for managing the association between games and questions.
     * @param questionDao the DAO for retrieving question content from the database.
     */
    public GameServiceImpl(GameDao gameDao, GameQuestionDao gameQuestionDao, QuestionDao questionDao) {
        this.gameDao = gameDao;
        this.gameQuestionDao = gameQuestionDao;
        this.questionDao = questionDao;
    }

    // ------------------------------------------------------------
    //                   Game Lifecycle Methods
    // ------------------------------------------------------------

    @Override
    public Game startGame(User user, QuizTest currentQuizTest, int questionCount) {
        LoggerUtil.info(GameServiceImpl.class, "Starting new game for user: " +
                (user != null ? user.getName() : "null") + " on test ID: " +
                (currentQuizTest != null ? currentQuizTest.getId() : "null"));

        // 1. Validate inputs using i18n key
        if (user == null || currentQuizTest == null) {
            LoggerUtil.warn(GameServiceImpl.class, "Failed to start game: User or Test is null.");
            throw new IllegalArgumentException("error.validation.game.null_entities");
        }

        try {
            // 2. Create the game session
            Game game = new Game();
            game.setUser(user);
            game.setQuizTest(currentQuizTest);
            game.setStartTime(Instant.now());
            game.setScore(0.0);
            game.setQuestionCount(questionCount);
            game.setCorrectAnswers(0);

            gameDao.save(game);
            LoggerUtil.debug(GameServiceImpl.class, "Game session saved with ID: " + game.getId());

            // 3. Get all test questions and mix them
            List<Question> availableQuestions = questionDao.findByQuizTest(currentQuizTest);
            Collections.shuffle(availableQuestions);

            // Defensive programming: avoid IndexOutOfBounds if test has fewer questions than requested
            int actualCount = Math.min(availableQuestions.size(), questionCount);
            List<Question> selectedQuestions = availableQuestions.subList(0, actualCount);

            // Update game question count if it was adjusted
            if (actualCount != questionCount) {
                game.setQuestionCount(actualCount);
                gameDao.update(game);
            }

            // 4. Create and save the GameQuestion relations
            for (Question question : selectedQuestions) {
                GameQuestion gameQuestion = new GameQuestion();
                gameQuestion.setGame(game);
                gameQuestion.setQuestion(question);
                gameQuestion.setId(new GameQuestionId(game.getId(), question.getId()));

                // Initially, answers are not correct until submitted
                gameQuestion.setIsCorrect(false);

                gameQuestionDao.save(gameQuestion);
            }

            LoggerUtil.info(GameServiceImpl.class, "Game started successfully with " + actualCount + " questions.");
            return game;

        } catch (Exception e) {
            LoggerUtil.error(GameServiceImpl.class, "Internal error while starting the game.", e);
            throw new RuntimeException("error.database.internal", e);
        }
    }

    @Override
    public void submitAnswer(GameQuestion gameQuestion, Answer selectedAnswer) {
        LoggerUtil.debug(GameServiceImpl.class, "Submitting answer for GameQuestion ID: " +
                gameQuestion.getId().getQuestionId());

        try {
            // 1. Check if the selected answer is correct
            boolean isCorrect = selectedAnswer.isCorrect();

            // 2. Update the GameQuestion entity
            gameQuestion.setIsCorrect(isCorrect);
            gameQuestionDao.update(gameQuestion);

            // 3. If correct, update the global Game correct answers counter
            if (isCorrect) {
                Game game = gameQuestion.getGame();
                game.setCorrectAnswers(game.getCorrectAnswers() + 1);
                gameDao.update(game);
                LoggerUtil.debug(GameServiceImpl.class, "Answer was correct. Total correct: " + game.getCorrectAnswers());
            }

        } catch (Exception e) {
            LoggerUtil.error(GameServiceImpl.class, "Error submitting answer.", e);
            throw new RuntimeException("error.database.internal", e);
        }
    }

    @Override
    public void finishGame(Game game) {
        LoggerUtil.info(GameServiceImpl.class, "Finishing game ID: " + game.getId());
        try {
            // Calculate final statistics
            game.setEndTime(Instant.now());

            // Calculate score out of 10
            if (game.getQuestionCount() > 0) {
                double score = ((double) game.getCorrectAnswers() / game.getQuestionCount()) * 10.0;
                game.setScore(Math.round(score * 100.0) / 100.0); // Round to 2 decimals
            } else {
                game.setScore(0.0);
            }

            gameDao.update(game);
            LoggerUtil.info(GameServiceImpl.class, "Game finished. Final score: " + game.getScore());
        } catch (Exception e) {
            LoggerUtil.error(GameServiceImpl.class, "Error finishing game ID: " + game.getId(), e);
            throw new RuntimeException("error.database.internal", e);
        }
    }

    // ------------------------------------------------------------
    //                   Lookup Methods
    // ------------------------------------------------------------

    @Override
    public List<GameQuestion> getGameQuestions(Game game) {
        LoggerUtil.debug(GameServiceImpl.class, "Fetching questions for game ID: " + game.getId());
        return gameQuestionDao.findByGame(game);
    }

}
