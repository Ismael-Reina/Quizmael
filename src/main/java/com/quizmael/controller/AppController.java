package com.quizmael.controller;

import com.quizmael.dao.impl.*;
import com.quizmael.gui.helpers.PanelManager;
import com.quizmael.gui.views.admin.AdminPanel2;
import com.quizmael.gui.views.auth.RegisterPanel;
import com.quizmael.gui.views.game.ResultsPanel;
import com.quizmael.gui.views.mainmenu.MainMenuPanel;
import com.quizmael.gui.windows.MainWindow;
import com.quizmael.gui.views.auth.LoginPanel;
import com.quizmael.gui.views.game.QuizSelectionPanel;
import com.quizmael.gui.views.game.PlayPanel;
import com.quizmael.service.impl.*;
import com.quizmael.util.I18nUtil;
import com.quizmael.util.LoggerUtil;

import javax.swing.SwingUtilities;

/**
 * <strong>AppController</strong> serves as the Composition Root and main orchestrator of the application.
 * <p>It is responsible for the instantiation and "wiring" of the entire application graph,
 * following a strict Dependency Injection pattern. It creates DAOs, Services, Controllers,
 * and Views in the correct order to ensure a decoupled and maintainable architecture.</p>
 * @author Ismael Reina Muñoz
 * @version 2.0
 */
public class AppController {

    // ------------------------------------------------------------
    //                      Controllers
    // ------------------------------------------------------------
    private AuthController authController;
    private GameController gameController;
    private TopicController topicController;
    private UserController userController;
    private QuizTestController quizTestController;

    // ------------------------------------------------------------
    //                      GUI Infrastructure
    // ------------------------------------------------------------
    private MainWindow mainWindow;
    private PanelManager panelManager;

    /**
     * Initializes the application by orchestrating the dependency graph.
     * * <p>The initialization follows a hierarchical order:</p>
     * <ol>
     * <li>Logging and Internationalization setup.</li>
     * <li>Data Access Objects (DAOs) instantiation.</li>
     * <li>Business Logic Services instantiation (injecting DAOs).</li>
     * <li>Controllers instantiation (injecting Services).</li>
     * <li>GUI framework setup (MainWindow and PanelManager).</li>
     * <li>Views instantiation (injecting Controllers) and registration.</li>
     * </ol>
     */
    public AppController() {
        try {
            LoggerUtil.info(AppController.class, "Initializing Quizmael Application...");
            // 1. DAOs (Persistence Layer)
            AnswerDaoImpl answerDao = new AnswerDaoImpl();
            GameDaoImpl gameDao = new GameDaoImpl();
            GameQuestionDaoImpl gameQuestionDao = new GameQuestionDaoImpl();
            QuestionDaoImpl questionDao = new QuestionDaoImpl();
            ModerationDaoImpl moderationDao = new ModerationDaoImpl();
            QuizTestDaoImpl quizTestDao = new QuizTestDaoImpl();
            TopicDaoImpl topicDao = new TopicDaoImpl();
            UserDaoImpl userDao = new UserDaoImpl();

            // 2. Services (Business Logic Layer)
            AuthServiceImpl authService = new AuthServiceImpl(userDao);
            GameServiceImpl gameService = new GameServiceImpl(gameDao,gameQuestionDao, questionDao);
            QuizTestServiceImpl quizTestService = new QuizTestServiceImpl(quizTestDao);
            ModerationServiceImpl moderationService = new ModerationServiceImpl(quizTestDao, userDao, moderationDao);
            TopicServiceImpl topicService = new TopicServiceImpl(topicDao);
            UserInteractionServiceImpl userInteractionService = new UserInteractionServiceImpl(userDao, quizTestDao);
            UserServiceImpl userService = new UserServiceImpl(userDao);

            // 4. GUI Infrastructure
            this.mainWindow = new MainWindow();
            this.panelManager = this.mainWindow.getPanelManager();

            // 3. Controllers (Orchestration Layer)
            this.authController = new AuthController(authService, this.panelManager);
            this.gameController = new GameController(gameService, this.panelManager);
            this.quizTestController = new QuizTestController(quizTestService);
            this.topicController = new TopicController(topicService);
            this.userController = new UserController(userService, quizTestService, this.panelManager);

            // 5. Views Registration
            registerViews();

            LoggerUtil.info(AppController.class, "Application wiring completed successfully.");

            // Start the flow
            startApp();

        } catch (Exception e) {
            LoggerUtil.error(AppController.class, "Critical failure during application startup: " + e.getMessage(), e);            // In a real scenario, you might show a localized error dialog here
        }
    }

    /**
     * Instantiates the GUI panels and registers them within the PanelManager.
     * * <p>Each view is provided with its specific controller, ensuring that views
     * only communicate with their designated logic handler.</p>
     */
    private void registerViews() {
        LoggerUtil.info(AppController.class, "Registering views in PanelManager...");

        panelManager.addPanel(PanelManager.LOGIN, new LoginPanel(this.authController));
        panelManager.addPanel(PanelManager.REGISTER, new RegisterPanel(this.authController));
        panelManager.addPanel(PanelManager.MAIN_MENU, new MainMenuPanel(this.authController, this.gameController, this.userController));
        panelManager.addPanel(PanelManager.QUIZ_SELECTION, new QuizSelectionPanel(this.quizTestController,
                this.topicController, this.userController, this.gameController));
        panelManager.addPanel(PanelManager.PLAY, new PlayPanel(this.gameController));
        panelManager.addPanel(PanelManager.RESULTS, new ResultsPanel(this.gameController));
        panelManager.addPanel(PanelManager.ADMIN, new AdminPanel2(this.quizTestController, this.userController)); //
    }

    /**
     * Finalizes the application startup by making the main window visible
     * and navigating to the initial screen (Login).
     */
    private void startApp() {
        SwingUtilities.invokeLater(() -> {
            mainWindow.setVisible(true);
            panelManager.showPanel(PanelManager.LOGIN);
            LoggerUtil.info(AppController.class, "Main window visible. Current Locale: " + I18nUtil.getLocale());
        });
    }

    // ------------------------------------------------------------
    //          Main Entry Point (Composition Root execution)
    // ------------------------------------------------------------
    public static void main(String[] args) {
        // Bootstrapping the application
        new AppController();
    }
}