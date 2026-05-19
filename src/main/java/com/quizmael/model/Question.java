package com.quizmael.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

/**
 * <strong>Question</strong> entity belonging to a test.
 * <p>Can contain multiple possible answers, one or more of which may be correct.</p>
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
@Entity
@Table(name = "questions")
public class Question {

    // ------------------------------------------------------------
    //                   Attributes / Fields
    // ------------------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id", nullable = false)
    private Integer id;

    // The test to which this question belongs
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "test_id", nullable = false)
    private QuizTest test;

    // The possible answers to this question
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    // The textual content of the question
    @Column(name = "text", nullable = false)
    private String text;

    // Optional feedback shown after answering the question
    @Column(name = "feedback", length = 500)
    private String feedback;

    // ------------------------------------------------------------
    //                   Getters & Setters
    // ------------------------------------------------------------
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public QuizTest getTest() {
        return test;
    }

    public void setTest(QuizTest test) {
        this.test = test;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

}