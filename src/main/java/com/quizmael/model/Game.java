package com.quizmael.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

/**
 * <strong>Game</strong> entity representing a play session by a user.
 * <p>Stores the <code>user</code> who played, the <code>score</code> achieved
 * and the <code>playedAt</code> timestamp.</p>
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
@Entity
@Table(name = "games")
public class Game {

    // ------------------------------------------------------------
    //                   Attributes / Fields
    // ------------------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id", nullable = false)
    private Integer id;

    // The user who played this game
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Total number of questions presented during the game
    @Column(name = "questionCount", nullable = false)
    @Min(1)
    @Max(100)
    private int questionCount;

    // Total number of correctly answered questions
    @Column(name = "correctAnswers", nullable = false)
    @Min(0)
    @Max(100)
    private int correctAnswers;

    // The score achieved in this game, between 0.00 and 10.00
    @Column(name = "score", nullable = false)
    private Double score;

    // The timestamp when the game started
    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    // The timestamp when the game finished
    @ColumnDefault("current_timestamp()")
    @Column(name = "played_at", nullable = false)
    private Instant playedAt;

    // ------------------------------------------------------------
    //                   Getters & Setters
    // ------------------------------------------------------------
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getPlayedAt() {
        return playedAt;
    }

    public void setPlayedAt(Instant playedAt) {
        this.playedAt = playedAt;
    }

}