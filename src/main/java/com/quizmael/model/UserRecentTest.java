package com.quizmael.model;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

/**
 * <strong>Entity</strong> representing a test that a user has recently played.
 *
 * <p>Uses a composite primary key linking a user and a test, along with the timestamp
 * of when the test was played.</p>
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
@Entity
@Table(name = "user_recent_tests")
public class UserRecentTest {

    // ------------------------------------------------------------
    //                   Attributes / Fields
    // ------------------------------------------------------------
    @EmbeddedId
    private UserRecentTestId id;

    // The user who played the test
    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // The test that was played
    @MapsId("testId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "test_id", nullable = false)
    private Test test;

    // Timestamp when the test was played
    @ColumnDefault("current_timestamp()")
    @Column(name = "played_at", nullable = false)
    private Instant playedAt;

    // ------------------------------------------------------------
    //                   Getters & Setters
    // ------------------------------------------------------------
    public UserRecentTestId getId() {
        return id;
    }

    public void setId(UserRecentTestId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public Instant getPlayedAt() {
        return playedAt;
    }

    public void setPlayedAt(Instant playedAt) {
        this.playedAt = playedAt;
    }

}