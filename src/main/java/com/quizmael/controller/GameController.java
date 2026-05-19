package com.quizmael.controller;

import com.quizmael.dao.QuizTestDao;
import com.quizmael.gui.helpers.PanelManager;
import com.quizmael.model.*;
import com.quizmael.model.enums.Language;
import com.quizmael.service.GameService;
import com.quizmael.service.QuizTestService;
import com.quizmael.service.UserInteractionService;
import com.quizmael.session.SessionContext;
import com.quizmael.util.HibernateUtil;
import com.quizmael.util.LoggerUtil;
import org.hibernate.Hibernate;
import org.hibernate.Session;

import java.time.Instant;
import java.util.List;
import javax.swing.Timer;

/**
 * Controller responsible for managing the gameplay logic.
 * Handles timers, scoring, question sequencing, and result persistence.
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public class GameController {

    // ------------------------------------------------------------
    //                   Attributes
    // ------------------------------------------------------------
    private final GameService gameService;
    private final PanelManager panelManager;
    private final SessionContext sessionContext;

    // Game state
    private Game currentGame;
    private List<GameQuestion> gameQuestions;
    private int currentQuestionIndex;
    private int timeLeft;
    private Timer gameTimer;

    // ------------------------------------------------------------
    //                   Constructor
    // ------------------------------------------------------------
    /**
     * Constructs a new GameController to manage the quiz gameplay flow.
     * <p>Requires access to the panel manager to navigate to results or
     * return to selection once a game concludes.</p>
     *
     * @param gameService   the service handling the core gameplay and scoring logic.
     * @param panelManager  the helper utility used for switching between game-related views.
     */
    public GameController(GameService gameService, PanelManager panelManager) {
        this.gameService = gameService;
        this.panelManager = panelManager;
        this.sessionContext = SessionContext.getInstance();
    }

    // ------------------------------------------------------------
    //                      Navigation Methods
    // ------------------------------------------------------------
    /**
     * Navigates the UI to the quiz selection screen.
     */
    public void navigateToQuizSelection() {
        panelManager.showPanel(PanelManager.QUIZ_SELECTION);
    }

    /**
     * Navigates back to the main menu.
     */
    public void navigateToMainMenu() {
        panelManager.showPanel(PanelManager.MAIN_MENU);
    }

    // ------------------------------------------------------------
    //                      Game Flow Methods
    // ------------------------------------------------------------

    /**
     * Initializes and starts a new game session with the selected test.
     * @param test The QuizTest to be played.
     * @param questionCount The number of questions chosen by the user.
     */
    public void startNewGame(QuizTest test, int questionCount) {
        if (test == null || test.getQuestions().isEmpty()) {
            LoggerUtil.error(getClass(), "Cannot start game: Test is null or has no questions.", null);
            return;
        }

        User user = sessionContext.getCurrentUser();

        try {
            // 1. Initialize game in DB using the specific count
            // The service handles the random selection and shuffling internally.
            currentGame = gameService.startGame(user, test, questionCount);

            // 2. Retrieve the questions wrapped for this game session
            gameQuestions = gameService.getGameQuestions(currentGame);

            currentQuestionIndex = 0;

            // Set timer based on test settings
            timeLeft = test.getTimeLimit();

            LoggerUtil.info(getClass(), "Game started. ID: " + currentGame.getId() +
                    " with " + questionCount + " questions.");

            setupTimer();
            panelManager.showPanel(PanelManager.PLAY);

        } catch (Exception e) {
            LoggerUtil.error(getClass(), "Failed to start game session", e);
            // Here you could call view.showError("error.game.start_failed") if needed
        }
    }

    /**
     * Processes the selected answer and advances the game state.
     * @param selectedAnswer The answer chosen by the user.
     * @return true if there are more questions, false if the game is over.
     */
    public boolean processAnswer(Answer selectedAnswer) {
        try {
            // Persist the answer choice progressively
            GameQuestion currentGQ = gameQuestions.get(currentQuestionIndex);
            gameService.submitAnswer(currentGQ, selectedAnswer);

            LoggerUtil.debug(getClass(), "Answer submitted for question index: " + currentQuestionIndex);
        } catch (Exception e) {
            LoggerUtil.error(getClass(), "Error submitting answer", e);
        }

        currentQuestionIndex++;
        return currentQuestionIndex < gameQuestions.size();
    }

    /**
     * Finalizes the game session, stops active timers, updates final statistics,
     * and persists the records into the database.
     */
    public void finishGame() {
        // Stop the countdown timer immediately
        stopTimer();
        LoggerUtil.info(getClass(), "Game finished visual timer stopped.");

        if (currentGame != null && gameQuestions != null) {
            // Recount correct answers directly from the live list to bypass Hibernate cache mismatch
            int correct = 0;
            for (GameQuestion gq : gameQuestions) {
                if (gq.getIsCorrect()) {
                    correct++;
                }
            }
            currentGame.setCorrectAnswers(correct);

            // Log final analytical breakdown for tracking and debugging
            LoggerUtil.info(getClass(), String.format("Final score breakdown: %d/%d correct answers.",
                    correct, gameQuestions.size()));
        }

        try {
            // Service calculates score (out of 10) and updates end time
            gameService.finishGame(currentGame);
            LoggerUtil.info(getClass(), "Game session finished and updated in DB.");
        } catch (Exception e) {
            LoggerUtil.error(getClass(), "Error finalizing game session", e);
        }

        panelManager.showPanel(PanelManager.RESULTS);
    }

    /**
     * Forces the game to stop (e.g., user exits or time runs out).
     */
    public void abortGame() {
        stopTimer();
        LoggerUtil.info(getClass(), "Game aborted by user or system.");
        panelManager.showPanel(PanelManager.MAIN_MENU);
    }

    // ------------------------------------------------------------
    //                      Helper Methods
    // ------------------------------------------------------------

    private void setupTimer() {
        if (gameTimer != null) gameTimer.stop();

        gameTimer = new Timer(1000, e -> {
            timeLeft--;
            if (timeLeft <= 0) {
                LoggerUtil.warn(getClass(), "Time is up!");
                finishGame();
            }
        });
        gameTimer.start();
    }

    /**
     * Stops the game timer if it is currently running.
     * Used to release resources when the game ends or is aborted.
     */
    private void stopTimer() {
        if (gameTimer != null && gameTimer.isRunning()) {
            gameTimer.stop();
        }
    }

    // ------------------------------------------------------------
    //            Getters for View (UI synchronization)
    // ------------------------------------------------------------

    /**
     * Retrieves the current question object being played.
     * * @return The {@link Question} entity from the current game session.
     */
    public Question getCurrentQuestion() {
        return gameQuestions.get(currentQuestionIndex).getQuestion();
    }

    /**
     * Returns the list of questions assigned to the current game session.
     * @return List of GameQuestion objects.
     */
    public List<GameQuestion> getGameQuestions() {
        return gameQuestions;
    }

    /**
     * Returns the remaining time for the current test in seconds.
     * * @return The seconds left before the game ends.
     */
    public int getTimeLeft() {
        return timeLeft;
    }

    /**
     * Returns the 1-based index of the current question.
     * Useful for displaying "Question X of Y" in the UI.
     * * @return The current question number.
     */
    public int getCurrentQuestionNumber() {
        return currentQuestionIndex + 1;
    }

    /**
     * Returns the total number of questions for the current game session.
     * * @return The size of the question list.
     */
    public int getTotalQuestions() {
        return gameQuestions.size();
    }

    /**
     * Returns the current game session data.
     * @return the active Game entity.
     */
    public Game getCurrentGame() {
        return currentGame;
    }

    /**
     * Restarts the quiz session using the exact same test and question count.
     */
    public void restartCurrentGame() {
        if (currentGame != null) {
            startNewGame(currentGame.getQuizTest(), currentGame.getQuestionCount());
        }
    }

// TODO: eliminar todo lo que hay aquí debajo, es código antiguo que ya no se usa, pero lo dejo comentado por si acaso
//    // ------------------------------------------------------------
//    //                   Attributes
//    // ------------------------------------------------------------
//
//    private final AppController appController;       // Main application controller
//    private QuizTestDao quizTestDao;                 // DAO for accessing quiz test data
//    private final GameService gameService;           // Service for game-related operations
//    private final QuizTestService quizTestService;
//
//    private Game currentGame;                        // The current game instance
//    private List<Question> selectedQuestions;        // List of questions selected for the quiz
//    private Map<Question, Answer> selectedAnswers;   // Map of selected answers for each question
//    private List<GameQuestion> currentGameQuestions; // List of GameQuestion objects associated with the current game
//
//    private int currentQuestionIndex;                // Index of the current question in the quiz
//    private int correctAnswers;                      // Number of correct answers given by the user
//
//
//    // ------------------------------------------------------------
//    //                   Constructor
//    // ------------------------------------------------------------
//
//    public GameController(AppController appController, QuizTestDao quizTestDao, GameService gameService, QuizTestService quizTestService) {
//        this.appController = appController;
//        this.quizTestDao = quizTestDao;
//        this.gameService = gameService;
//        this.quizTestService = quizTestService;
//    }
//
//    // ------------------------------------------------------------
//    //                   Quiz Selection Methods
//    // ------------------------------------------------------------
//
//    /**
//     * Displays the quiz selection panel where the user can browse and select a test to play.
//     */
//    public void showQuizSelection() {
//        appController.showQuizSelectionPanel();
//    }
//
//    /**
//     * Loads a list of all topics available in public tests.
//     *
//     * @return list of topic names
//     */
//    public List<String> loadAvailableTopics() {
//        return quizTestDao.findDistinctTopicsInPublishedTests();
//    }
//
//    /**
//     * Retrieves a list of usernames who have created published tests.
//     *
//     * @return a list of unique creator usernames.
//     */
//    public List<String> loadAvailableCreators() {
//        return quizTestDao.findAllCreatorsOfPublishedTests();
//    }
//
//
//    public List<QuizTest> findFilteredPublicTests(String topicName, String creatorName, Integer difficulty, Set<Language> languages) {
//        return quizTestDao.findPublicByFilters(topicName, creatorName, difficulty, languages);
//    }
//
//    // ------------------------------------------------------------
//    //                       Play Quiz Methods
//    // ------------------------------------------------------------
//
//    /**
//     * Returns the current game being played.
//     * This game instance is created when the user starts a new quiz,
//     * and contains all information about the current quiz session.
//     *
//     * @return the current Game instance
//     */
//    public Game getCurrentGame() {
//        return currentGame;
//    }
//
//    /**
//     * Checks whether there is another question available in the current game session.
//     *
//     * @return {@code true} if there is at least one more question to answer, {@code false} otherwise.
//     */
//    public boolean hasNextQuestion() {
//        return currentQuestionIndex + 1 < selectedQuestions.size();
//    }
//
//    /**
//     * Starts a new quiz game with the given QuizTest and number of questions.
//     *
//     * @param currentQuizTest   the quiz test selected by the user
//     * @param numberOfQuestions the number of questions to play
//     */
///*    public void startQuiz(QuizTest currentQuizTest, int numberOfQuestions) {
//        List<QuizTest> tests = List.of(currentQuizTest);
//        User user = appController.getSessionContext().getCurrentUser();
//
//        currentGame = gameService.startGame(user, currentQuizTest, numberOfQuestions);
//        currentGameQuestions = gameService.getGameQuestions(currentGame);
//        currentQuestionIndex = 0;
//
//        // Initialize selectedQuestions with the game questions
//        selectedQuestions = new ArrayList<>();
//        for (GameQuestion gq : currentGameQuestions) {
//            selectedQuestions.add(gq.getQuestion());
//        }
//    }*/
//
//    public void startQuiz(QuizTest currentQuizTest, int numberOfQuestions) {
//        List<QuizTest> tests = List.of(currentQuizTest);
//        User user = appController.getSessionContext().getCurrentUser();
//
//        currentGame = gameService.startGame(user, currentQuizTest, numberOfQuestions);
//        currentGameQuestions = gameService.getGameQuestions(currentGame);
//        currentQuestionIndex = 0;
//
//        // Inicializar las preguntas y sus propiedades dentro de la sesión
//        selectedQuestions = new ArrayList<>();
//        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//            for (GameQuestion gq : currentGameQuestions) {
//                Question question = session.get(Question.class, gq.getQuestion().getId());
//                Hibernate.initialize(question.getText()); // Inicializar propiedad lazy
//                Hibernate.initialize(question.getAnswers()); // Inicializar respuestas asociadas
//                selectedQuestions.add(question);
//            }
//        }
//    }
//
//    /**
//     * Returns the currently active question in the quiz.
//     *
//     * @return the current Question object, or null if none
//     */
//    public Question getCurrentQuestion() {
//        if (currentQuestionIndex < selectedQuestions.size()) {
//            return selectedQuestions.get(currentQuestionIndex);
//        }
//        return null;
//    }
//
//    /**
//     * Evaluates the user's selected answer for the current question.
//     *
//     * @param selectedAnswer the answer selected by the user
//     * @return true if the answer is correct, false otherwise
//     */
//    public boolean answerCurrentQuestion(Answer selectedAnswer) {
//        GameQuestion gq = currentGameQuestions.get(currentQuestionIndex);
//
//        gameService.submitAnswer(gq, selectedAnswer);
//
//        return selectedAnswer.getCorrect();
//    }
//
//    /**
//     * Advances to the next question in the quiz.
//     *
//     * @return true if there is another question available, false if the quiz has ended
//     */
//    public boolean moveToNextQuestion() {
//        currentQuestionIndex++;
//        return currentQuestionIndex < selectedQuestions.size();
//    }
//
//    /**
//     * Finalizes the game, calculates the score and stores it.
//     */
//    public void finishGame() {
//        currentGame.setEndTime(Instant.now());
//
//        int totalQuestions = currentGame.getQuestionCount();
//        int correct = currentGame.getCorrectAnswers();
//        double score = (10.0 * correct) / totalQuestions;
//        currentGame.setScore(score);
//
//        gameService.finishGame(currentGame);
//    }
//
//
//    // Obtener todos los usuarios directamente desde AuthController
////    private List<String> getAllUsers() {
////        return appController.getAuthController().getAllUsernames();
////    }
//
//
//    public List<QuizTest> getAllTests() {
//        return quizTestService.getAllTests(); // Llama directamente al servicio
//    }
}
