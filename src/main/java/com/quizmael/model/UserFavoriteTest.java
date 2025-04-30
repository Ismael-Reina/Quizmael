package com.quizmael.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * <strong>Entity</strong> representing a test that a user has marked as favorite.
 *
 * <p>Uses a composite primary key linking a user and a test.</p>
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
@Entity
@Table(name = "user_favorite_tests")
public class UserFavoriteTest {

    // ------------------------------------------------------------
    //                   Attributes / Fields
    // ------------------------------------------------------------
    @EmbeddedId
    private UserFavoriteTestId id;

    // The user who marked the test as favorite
    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // The test that was marked as favorite
    @MapsId("testId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "test_id", nullable = false)
    private QuizTest test;

    // ------------------------------------------------------------
    //                   Getters & Setters
    // ------------------------------------------------------------
    public UserFavoriteTestId getId() {
        return id;
    }

    public void setId(UserFavoriteTestId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public QuizTest getTest() {
        return test;
    }

    public void setTest(QuizTest test) {
        this.test = test;
    }

}