package com.quizmael.session;

import com.quizmael.model.User;
import com.quizmael.util.LoggerUtil;

/**
 * Singleton class that manages the global state of the current user session.
 * Provides a centralized point to access authenticated user data across the application.
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public class SessionContext {

    // ------------------------------------------------------------
    //                   Attributes
    // ------------------------------------------------------------

    // Static instance to ensure only one session context exists (Singleton pattern)
    private static SessionContext instance;

    // The currently logged-in user
    private User currentUser;

    // ------------------------------------------------------------
    //              Constructor, Getters & Setters
    // ------------------------------------------------------------

    /**
     * Private constructor to enforce Singleton pattern.
     */
    private SessionContext() {
        LoggerUtil.debug(SessionContext.class, "SessionContext initialized.");
    }

    /**
     * Retrieves the unique instance of the SessionContext.
     * @return the singleton instance.
     */
    public static synchronized SessionContext getInstance() {
        if (instance == null) {
            instance = new SessionContext();
        }
        return instance;
    }

    /**
     * Gets the currently authenticated user.
     * @return the current User or null if no session is active.
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Updates the current user in the session and logs the change.
     * @param currentUser the user to set as active.
     */
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        if (currentUser != null) {
            LoggerUtil.info(SessionContext.class, "Global session started for user: " + currentUser.getName());
        } else {
            LoggerUtil.info(SessionContext.class, "Global session cleared (logout).");
        }
    }

    // ------------------------------------------------------------
    //                      Utility Methods
    // ------------------------------------------------------------

    /**
     * Checks if a user is currently logged in.
     * @return true if there is an active session.
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Utility to check if the current user has administrative privileges.
     * Useful for UI conditional rendering (e.g., hiding admin buttons).
     * @return true if the user is an administrator.
     */
    public boolean isAdmin() {
        // We assume User entity has an isAdmin() method or a Role field
        return isLoggedIn() && currentUser.isAdmin();
    }
}