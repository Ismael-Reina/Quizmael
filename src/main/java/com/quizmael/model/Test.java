package com.quizmael.model;

import com.quizmael.model.enums.Language;
import com.quizmael.model.enums.State;
import jakarta.persistence.*;

/**
 * <strong>Test</strong> entity containing a set of questions.
 * <p>Associated with a creator, has a language, topic, difficulty, and moderation <code>state</code>.</p>
 */
@Entity
@Table(name = "tests")
public class Test {
    // ------------------------------------------------------------
    //                   Attributes / Fields
    // ------------------------------------------------------------
    @Id
    @Column(name = "test_id", nullable = false)
    private Integer id;

    // The title of the test
    @Column(name = "title", nullable = false, length = 100)
    private String title;

    // The user who created this test
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    // The state of the test (DRAFT, PUBLISHED, REJECTED, PENDING)
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private State state;

    // The language in which the test is written
    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false)
    private Language language;

    // ------------------------------------------------------------
    //                   Getters & Setters
    // ------------------------------------------------------------
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

}