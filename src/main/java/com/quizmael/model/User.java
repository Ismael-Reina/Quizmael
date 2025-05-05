package com.quizmael.model;

import com.quizmael.model.enums.Role;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user of the application, which may have different roles and permissions.
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
@Entity
@Table(name = "users")
public class User {

    // ------------------------------------------------------------
    //                   Attributes / Fields
    // ------------------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer id;

    // Username of the user
    @Column(name = "name", nullable = false, unique = true, length = 20)
    private String name;

    // The email address of the user
    @Column(name = "email", length = 40)
    private String email;

    // Encrypted password of the user
    @Column(name = "password", length = 20)
    private String password;

    // Hint to aid the user in recalling their password
    @Column(name = "password_hint", length = 100)
    private String passwordHint;

    // Secret question for password recovery.
    @Column(name = "secret_question", length = 100)
    private String secretQuestion;

    // Encrypted answer to the secret question
    @Column(name = "secret_answer", length = 20)
    private String secretAnswer;

    // Fecha de nacimiento del usuario
    @Column(name = "birth_date")
    private LocalDate birthDate;

    // Imagen de perfil del usuario
    @Lob
    @Column(name = "profile_picture")
    private byte[] profilePicture;

    // The role of the user (ANONYMOUS, REGISTERED, MODERATOR, ADMINISTRATOR)
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    // Tests marked as favorite by the user
    @ManyToMany
    @JoinTable(
            name = "user_favorite_tests",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "test_id")
    )
    private List<QuizTest> favoriteTests = new ArrayList<>();

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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<QuizTest> getFavoriteTests() {
        return favoriteTests;
    }

}
