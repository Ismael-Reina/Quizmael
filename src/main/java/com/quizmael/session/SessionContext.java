package com.quizmael.session;

import com.quizmael.model.User;

/**
 * Singleton class that stores the current user session across the application.
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public class SessionContext {

    // Static instance to ensure only one session context exists (Singleton pattern)
    private static SessionContext instance;

    // The currently logged-in user
    private User currentUser;

    /**
     * Private constructor to prevent external instantiation.
     */
    private SessionContext() {
    }

    /**
     * Returns the singleton instance of SessionContext.
     * Creates the instance if it does not exist yet.
     *
     * @return SessionContext instance
     */
    public static SessionContext getInstance() {
        if (instance == null) {
            instance = new SessionContext();
        }
        return instance;
    }

    /**
     * Sets the current logged-in user.
     *
     * @param user the user to set as current
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    /**
     * Returns the currently logged-in user.
     *
     * @return current user or null if not logged in
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Clears the current session by removing the user.
     */
    public void clearSession() {
        currentUser = null;
    }
}