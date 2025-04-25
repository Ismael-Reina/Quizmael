package com.quizmael.model.enums;

/**
 * <strong>User role</strong> within the application.
 * <p>Determines the set of permissions and accessible features for the user.</p>
 *
 * <ul>
 *     <li><code>ANONYMOUS</code> — Temporary session-based user.</li>
 *     <li><code>REGISTERED</code> — Logged-in user with full test creation capabilities.</li>
 *     <li><code>MODERATOR</code> — Can moderate and approve/reject tests.</li>
 *     <li><code>ADMIN</code> — Full access, including user management.</li>
 * </ul>
 */
public enum Role {
    ANONYMOUS,
    REGISTERED,
    MODERATOR,
    ADMIN
}

