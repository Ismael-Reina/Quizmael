package com.quizmael.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

/**
 * <strong>Composite key</strong> for the <code>GameQuestion</code> entity.
 * <p>Combines <code>game_id</code> and <code>question_id</code> to uniquely identify an entry.</p>
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
@Embeddable
public class GameQuestionId implements Serializable {

    private static final long serialVersionUID = -4994674196001757508L;

    // ------------------------------------------------------------
    //                   Attributes / Fields
    // ------------------------------------------------------------
    @Column(name = "game_id", nullable = false)
    private Integer gameId;

    @Column(name = "question_id", nullable = false)
    private Integer questionId;



    // Constructor por defecto obligatorio (para Hibernate)                                     // TODO: 16/05
    public GameQuestionId() {}

    // Constructor con parámetros
    public GameQuestionId(Integer gameId, Integer questionId) {
        this.gameId = gameId;
        this.questionId = questionId;
    }




    // ------------------------------------------------------------
    //                   Getters & Setters
    // ------------------------------------------------------------
    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    // ------------------------------------------------------------
    //                  Identity Methods
    // ------------------------------------------------------------
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        GameQuestionId entity = (GameQuestionId) o;
        return Objects.equals(this.gameId, entity.gameId) &&
                Objects.equals(this.questionId, entity.questionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId, questionId);
    }

}