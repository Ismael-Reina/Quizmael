package com.quizmael.util;

import java.security.SecureRandom;

/**
 * Utility class for password-related operations such as verification and random generation.
 * Relies on {@link EncryptionUtil} for SHA-256 hashing.
 */
public class PasswordUtils {

    private static final int PASSWORD_LENGTH = 10;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private PasswordUtils() {
        // Prevent instantiation
    }

    /**
     * Encrypts a plaintext password using SHA-256 via EncryptionUtil.
     *
     * @param password the raw password
     * @return the hashed password
     */
    public static String hashPassword(String password) {
        return EncryptionUtil.encryptSHA256(password);
    }

    /**
     * Verifies that a raw password matches the hashed version.
     *
     * @param rawPassword the user-entered password
     * @param hashedPassword the stored SHA-256 hash
     * @return true if the passwords match
     */
    public static boolean checkPassword(String rawPassword, String hashedPassword) {
        return hashPassword(rawPassword).equals(hashedPassword);
    }

    /**
     * Generates a secure random alphanumeric password.
     *
     * @return a new password string
     */
    public static String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}