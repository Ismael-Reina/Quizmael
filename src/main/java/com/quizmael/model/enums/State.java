package com.quizmael.model.enums;

/**
 * <strong>Test approval status</strong>.
 * <p>Defines the current state of a test within the moderation workflow.</p>
 *
 * <ul>
 *     <li><code>DRAFT</code> — Incomplete or not yet submitted for review.</li>
 *     <li><code>PENDING</code> — Submitted and waiting for moderator review.</li>
 *     <li><code>PUBLISHED</code> — Approved and visible to all users.</li>
 *     <li><code>REJECTED</code> — Rejected by a moderator.</li>
 * </ul>
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public enum State {
    DRAFT,
    PENDING,
    PUBLISHED,
    REJECTED
}