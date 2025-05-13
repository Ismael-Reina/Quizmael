package com.quizmael.model;

import com.quizmael.model.enums.Language;
import com.quizmael.model.enums.State;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <strong>Test</strong> entity containing a set of questions.
 * <p>Associated with a creator, has a language, topic, difficulty, and moderation <code>state</code>.</p>
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
@Entity
@Table(name = "tests")
public class QuizTest {

    // ------------------------------------------------------------
    //                   Attributes / Fields
    // ------------------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    // Number of options per question (between 2 and 6)
    @Column(name = "options_count", nullable = false)
    private Integer optionsCount;

    // Time limit in seconds (between 0 and 3600)
    @Column(name = "time_limit", nullable = false)
    private Integer timeLimit;

    // Test creation date
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    // Date of last test update
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    // Test description
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    // Questions associated with this test
    @OneToMany(mappedBy = "test", fetch = FetchType.LAZY)
    private List<Question> questions;

    // Image associated with the test
    @Lob
    @Column(name = "image")
    private byte[] image;

    // Number of times the test has been played    @Column(name = "times_played", nullable = false)
    private Integer timesPlayed;

    // Average test score
    @Column(name = "average_score", nullable = false, precision = 4, scale = 2)
    private Double averageScore;

    // Average test user rating
    @Column(name = "average_rating", nullable = false, precision = 2, scale = 1)
    private Double averageRating;

    // Topics associated with this test (through TestTopic)
    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TestTopic> testTopics = new HashSet<>();

    // The user who reviewed (validated or rejected) the test
    @ManyToOne
    @JoinColumn(name = "moderated_by_id")
    private User moderatedBy;

    // The date and time when the test was reviewed
    @Column(name = "moderation_date")
    private Instant moderationDate;


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

    public Integer getOptionsCount() {
        return optionsCount;
    }

    public void setOptionsCount(Integer optionsCount) {
        this.optionsCount = optionsCount;
    }

    public Integer getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Integer getTimesPlayed() {
        return timesPlayed;
    }

    public void setTimesPlayed(Integer timesPlayed) {
        this.timesPlayed = timesPlayed;
    }

    public Double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Double averageScore) {
        this.averageScore = averageScore;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Set<TestTopic> getTestTopics() {
        return testTopics;
    }

    @Transient
    public Set<Topic> getTopics() {
        return testTopics.stream()
                .map(TestTopic::getTopic)
                .collect(Collectors.toSet());
    }

    public User getModeratedBy() {
        return moderatedBy;
    }

    public void setModeratedBy(User moderatedBy) {
        this.moderatedBy = moderatedBy;
    }

    public Instant getModerationDate() {
        return moderationDate;
    }

    public void setModerationDate(Instant moderationDate) {
        this.moderationDate = moderationDate;
    }

}
