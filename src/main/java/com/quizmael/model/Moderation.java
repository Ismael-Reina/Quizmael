package com.quizmael.model;

import com.quizmael.model.enums.State;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

/**
 * <strong>Moderation</strong> entity representing the review of a test.
 * <p>Includes the moderator's decision, date of review, and optional rejection reason.</p>
 */
@Entity
@Table(name = "moderations")
public class Moderation {

    // ------------------------------------------------------------
    //                   Attributes / Fields
    // ------------------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "moderation_id", nullable = false)
    private Integer id;

    // The test being moderated
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "test_id", nullable = false)
    private Test test;

    // The moderator who reviewed the test
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "moderator_id", nullable = false)
    private User moderator;

    // The state assigned to the test after moderation (PUBLISHED, REJECTED)
    @Enumerated(EnumType.STRING)
    @Column(name = "assigned_state", nullable = false)
    private State assignedState;

    // The timestamp when the moderation was performed
    @ColumnDefault("current_timestamp()")
    @Column(name = "moderated_at", nullable = false)
    private Instant moderatedAt;

    // The reason for rejection, if applicable
    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    // ------------------------------------------------------------
    //                   Getters & Setters
    // ------------------------------------------------------------
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public User getModerator() {
        return moderator;
    }

    public void setModerator(User moderator) {
        this.moderator = moderator;
    }

    public State getAssignedState() {
        return assignedState;
    }

    public void setAssignedState(State assignedState) {
        this.assignedState = assignedState;
    }

    public Instant getModeratedAt() {
        return moderatedAt;
    }

    public void setModeratedAt(Instant moderatedAt) {
        this.moderatedAt = moderatedAt;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

}