package com.quizmael.service.enums;

/**
 * <strong>PasswordResetStatus</strong> represents the result of attempting
 * to reset a user's password via email.
 *
 * <p>Used by the {@code resetPasswordWithEmail} method in the user service layer
 * to determine the result of the reset process and provide appropriate feedback.</p>
 *
 * <ul>
 * <li><code>USER_NOT_FOUND</code> — No user with the given username exists in the database.</li>
 * <li><code>EMAIL_NOT_SET</code> — The user exists but does not have an email associated.</li>
 * <li><code>RESET_EMAIL_SENT</code> — A temporary password was generated and sent to the user's email (simulated or real).</li>
 * </ul>
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public enum PasswordResetStatus {
    USER_NOT_FOUND,
    EMAIL_NOT_SET,
    RESET_EMAIL_SENT
}