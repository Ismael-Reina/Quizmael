package com.quizmael.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility class for simplifying logging operations across the application.
 * <p>Example of usage:</p>
 * <pre>
 *      <code>LoggerUtil.error(HibernateUtil.class, "Failed to initialize SessionFactory");</code>
 * </pre>
 */
public class LoggerUtil {

    private LoggerUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Returns a Logger instance for the specified class.
     *
     * @param clazz the class for which the logger is created
     * @return Logger instance
     */
    public static Logger getLogger(Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }

    /**
     * Logs an informational message.
     *
     * @param clazz the class originating the log
     * @param message the message to log
     */
    public static void info(Class<?> clazz, String message) {
        getLogger(clazz).info(message);
    }

    /**
     * Logs a warning message.
     *
     * @param clazz the class originating the log
     * @param message the message to log
     */
    public static void warn(Class<?> clazz, String message) {
        getLogger(clazz).warn(message);
    }

    /**
     * Logs an error message.
     *
     * @param clazz the class originating the log
     * @param message the message to log
     */
    public static void error(Class<?> clazz, String message) {
        getLogger(clazz).error(message);
    }

    /**
     * Logs a debug message.
     *
     * @param clazz the class originating the log
     * @param message the message to log
     */
    public static void debug(Class<?> clazz, String message) {
        getLogger(clazz).debug(message);
    }

}
