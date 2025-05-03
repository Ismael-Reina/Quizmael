package com.quizmael.service.enums;

/**
 * <strong>ChangePasswordResult</strong> represents the possible outcomes
 * when attempting to change a user's password.
 *
 * <p>Used by the {@code changePassword} method in the user service layer to
 * indicate whether the password change operation succeeded or failed,
 * and the reason for failure if applicable.</p>
 *
 * <ul>
 * <li><code>SUCCESS</code> — The password was successfully updated.</li>
 * <li><code>USER_NOT_FOUND</code> — The specified user does not exist in the system.</li>
 * <li><code>OLD_PASSWORD_INCORRECT</code> — The current password entered does not match the stored password.</li>
 * </ul>
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public enum ChangePasswordResult {
    SUCCESS,
    USER_NOT_FOUND,
    OLD_PASSWORD_INCORRECT
}