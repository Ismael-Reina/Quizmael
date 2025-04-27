package com.quizmael.exceptions;

/**
 * Custom exception for encryption-related errors in the application.
 * <p>
 * This exception is thrown when an unexpected error occurs during
 * encryption processes, such as missing encryption algorithms.
 * </p>
 *
 * @author Ismael Reina Muñoz
 */
public class EncryptionException extends RuntimeException {

    /**
     * Constructs a new EncryptionException with the specified detail message and cause.
     *
     * @param message the detail message explaining the reason for the exception
     * @param cause the underlying cause of the exception
     */
    public EncryptionException(String message, Throwable cause) {
        super(message, cause);
    }
}
