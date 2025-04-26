package com.quizmael.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * <strong>GameQuestion</strong> entity representing a question answered during a game.
 * <p>Uses a composite key and stores whether the user's answer was correct.</p>
 */
@Entity
@Table(name = "game_questions")
public class GameQuestion {

    // ------------------------------------------------------------
    //                   Attributes / Fields
    // ------------------------------------------------------------
    @EmbeddedId
    private GameQuestionId id;

    // The game this question belongs to
    @MapsId("gameId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    // The question asked during the game
    @MapsId("questionId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    // Whether the question was answered correctly
    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect = false;

    // ------------------------------------------------------------
    //                   Getters & Setters
    // ------------------------------------------------------------
    public GameQuestionId getId() {
        return id;
    }

    public void setId(GameQuestionId id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

}