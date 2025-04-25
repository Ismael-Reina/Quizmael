package com.quizmael.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * <strong>Answer</strong> entity representing a possible answer to a question.
 * <p>Each answer is linked to a single question and can be correct or incorrect.</p>
 */
@Entity
@Table(name = "answers")
public class Answer {
    // ------------------------------------------------------------
    //                   Attributes / Fields
    // ------------------------------------------------------------
    @Id
    @Column(name = "answer_id", nullable = false)
    private Integer id;

    // The textual content of the answer
    @Column(name = "text", nullable = false)
    private String text;

    // Indicates whether the answer is correct
    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect = false;

    // The question to which this answer belongs
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    // ------------------------------------------------------------
    //                   Getters & Setters
    // ------------------------------------------------------------
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

}