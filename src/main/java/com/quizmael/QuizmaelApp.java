package com.quizmael;

import com.quizmael.controller.AppController;
import com.quizmael.util.HibernateUtil;
import com.quizmael.util.LoggerUtil;

import javax.swing.UIManager;
import javax.swing.SwingUtilities;

/**
 * Main application entry point for the Quizmael gaming platform.
 * Centralizes UI bootstrapping, asynchronous database warm-up, and graceful shutdown.
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public class QuizmaelApp {

    public static void main(String[] args) {
        // 1. SAFETY NET: Global uncaught exception handler to prevent silent failures in Swing threads
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            LoggerUtil.error(QuizmaelApp.class, "Uncaught critical exception in thread: " + thread.getName(), throwable);
        });

        // 2. LIFELINE: Graceful shutdown hook to safely close Hibernate resources when the JVM exits
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LoggerUtil.info(QuizmaelApp.class, "Application shutdown detected. Closing Hibernate resources...");
            try {
                if (HibernateUtil.getSessionFactory() != null && !HibernateUtil.getSessionFactory().isClosed()) {
                    HibernateUtil.getSessionFactory().close();
                    LoggerUtil.info(QuizmaelApp.class, "Database connection pool closed successfully. Data is safe.");
                }
            } catch (Exception e) {
                System.err.println("Critical error closing Hibernate during shutdown: " + e.getMessage());
            }
        }));

        // 3. PERFORMANCE: Asynchronous database warm-up in a background thread to prevent login lag
        new Thread(() -> {
            try {
                LoggerUtil.info(QuizmaelApp.class, "Starting asynchronous Hibernate preloading (Database Warm-up)...");
                // Forces Hibernate to compile metadata and initialize the pool early
                HibernateUtil.getSessionFactory();
                LoggerUtil.info(QuizmaelApp.class, "Persistence layer preloaded successfully in the background! System ready.");
            } catch (Exception e) {
                LoggerUtil.error(QuizmaelApp.class, "Critical error during asynchronous database warm-up", e);
            }
        }).start();

        // 4. GRAPHICAL USER INTERFACE: Safe application bootstrap on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            try {
                LoggerUtil.info(QuizmaelApp.class, "Applying native system Look and Feel...");
                // Replaces the retro default theme with the modern host OS visual style
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                LoggerUtil.warn(QuizmaelApp.class, "Failed to apply native Look and Feel. Falling back to default.");
            }

            LoggerUtil.info(QuizmaelApp.class, "Starting graphical user interface...");
            // Main application orchestrator takes control of the window flow
            AppController appController = new AppController();
        });

        /*
        // Launches GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            LoggerUtil.info(QuizmaelApp.class, "Starting graphical user interface...");
            AppController appController = new AppController();
        });

        // Preloading Hibernate in a background thread (Asynchronous)
        // This starts in parallel without freezing the login window
        new Thread(() -> {
            try {
                LoggerUtil.info(QuizmaelApp.class, "Iniciando precarga de Hibernate (Database Warm-up)...");

                // Forzamos a Hibernate a construir la factoría de sesiones e inicializar las tablas ahora
                HibernateUtil.getSessionFactory();

                LoggerUtil.info(QuizmaelApp.class, "¡Persistencia precargada con éxito en segundo plano! El juego está listo.");
            } catch (Exception e) {
                LoggerUtil.error(QuizmaelApp.class, "Error crítico durante la precarga asíncrona de la base de datos", e);
            }
        }).start();

         */
    }
}
