package com.quizmael.model;

import com.quizmael.model.enums.Role;
import jakarta.persistence.*;

/**
 * Represents a user of the application, which may have different roles and permissions.
 */
@Entity
@Table(name = "users")
public class User {
    // ------------------------------------------------------------
    //                   Attributes / Fields
    // ------------------------------------------------------------
    @Id
    @Column(name = "user_id", nullable = false)
    private Integer id;

    // Username of the user
    @Column(name = "name", nullable = false, length = 20)
    private String name;

    // The email address of the user
    @Column(name = "email", length = 40)
    private String email;

    // Encrypted password of the user
    @Column(name = "password", length = 20)
    private String password;

    // Hint to help recover the password
    @Column(name = "password_hint", length = 100)
    private String passwordHint;

    // Secret question for password recovery.
    @Column(name = "secret_question", length = 100)
    private String secretQuestion;

    // Encrypted answer to the secret question
    @Column(name = "secret_answer", length = 20)
    private String secretAnswer;

    // The role of the user (ANONYMOUS, REGISTERED, MODERATOR, ADMIN)
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    // ------------------------------------------------------------
    //                   Getters & Setters
    // ------------------------------------------------------------
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordHint() {
        return passwordHint;
    }

    public void setPasswordHint(String passwordHint) {
        this.passwordHint = passwordHint;
    }

    public String getSecretQuestion() {
        return secretQuestion;
    }

    public void setSecretQuestion(String secretQuestion) {
        this.secretQuestion = secretQuestion;
    }

    public String getSecretAnswer() {
        return secretAnswer;
    }

    public void setSecretAnswer(String secretAnswer) {
        this.secretAnswer = secretAnswer;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}