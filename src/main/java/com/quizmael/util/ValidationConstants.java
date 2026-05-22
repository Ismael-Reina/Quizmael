package com.quizmael.util;

/**
 * Centralized regex patterns for strict data validation across layers.
 * @author Ismael Reina Muñoz
 */
public class ValidationConstants {

    // Alphanumeric and underscores, 3-20 characters
    public static final String USERNAME_REGEX = "^[a-zA-Z0-9_]{3,20}$";

    // Strict email match criteria
    public static final String EMAIL_REGEX = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";

    // Password strength containing: Uppercase, Lowercase, Number and Special Character (6-20 chars)
    public static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#._\\-])[A-Za-z\\d@$!%*?&#._\\-]{6,20}$";
}