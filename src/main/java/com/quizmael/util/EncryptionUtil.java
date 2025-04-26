package com.quizmael.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class for encrypting data using SHA-256 hashing.
 */
public class EncryptionUtil {

    private EncryptionUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Encrypts the given input text using SHA-256 algorithm.
     *
     * @param input the text to encrypt
     * @return the encrypted text in hexadecimal format
     * @throws RuntimeException if the SHA-256 algorithm is not available
     */
    public static String encryptSHA256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes());
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    /**
     * Converts an array of bytes into a hexadecimal string.
     *
     * @param bytes the array of bytes to convert
     * @return a hexadecimal string representation of the bytes
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            // Convert each byte to two hexadecimal characters
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
