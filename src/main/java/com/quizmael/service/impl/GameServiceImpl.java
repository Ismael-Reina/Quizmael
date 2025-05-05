package com.quizmael.service.impl;

import com.quizmael.dao.GameDao;
import com.quizmael.dao.GameQuestionDao;
import com.quizmael.dao.QuestionDao;
import com.quizmael.dao.impl.AnswerDaoImpl;
import com.quizmael.model.*;
import com.quizmael.service.GameService;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class GameServiceImpl implements GameService {

    private final GameDao gameDao;
    private final GameQuestionDao gameQuestionDao;
    private final QuestionDao questionDao;

    public GameServiceImpl(GameDao gameDao, GameQuestionDao gameQuestionDao, QuestionDao questionDao) {
        this.gameDao = gameDao;
        this.gameQuestionDao = gameQuestionDao;
        this.questionDao = questionDao;
    }

    public Game startGame(User user, List<QuizTest> tests, int questionCount) {

        // TODO: add global transaction

        // 1. Create the game
        Game game = new Game();
        game.setUser(user);
        game.setStartTime(Instant.now());
        game.setScore(0.0);
        game.setQuestionCount(questionCount);
        game.setCorrectAnswers(0); // Ensures initialization

        gameDao.save(game); // Save the game in the database

        // 2. Get all test questions
        List<Question> availableQuestions = questionDao.findByTests(tests);

        // 3. Randomly select and mix the questions
        Collections.shuffle(availableQuestions);    // Mix the questions
        List<Question> selectedQuestions = availableQuestions.subList(0, questionCount);    // Select the first 'questionCount' questions

        // 4. Create and save the GameQuestions
        for (Question question : selectedQuestions) {
            GameQuestion gameQuestion = new GameQuestion();
            gameQuestion.setGame(game);
            gameQuestion.setQuestion(question);
            gameQuestionDao.save(gameQuestion);
        }

        return game;

    }

    public void submitAnswer(GameQuestion gameQuestion, Answer selectedAnswer) {

        // Check if the user's response is correct
        if (selectedAnswer.getCorrect()) {
            // Mark the answer as correct
            gameQuestion.setIsCorrect(true);

            // Increase the correct answers count
            Game game = gameQuestion.getGame();
            game.setCorrectAnswers(game.getCorrectAnswers() + 1);
            gameDao.update(game);

            // Save gameQuestion
            gameQuestionDao.update(gameQuestion);
        }

    }

}
