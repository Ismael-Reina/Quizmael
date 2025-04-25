package com.quizmael.model;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

/**
 * <strong>Game</strong> entity representing a play session by a user.
 * <p>Stores the <code>user</code> who played and the <code>playedAt</code> timestamp.</p>
 */
@Entity
@Table(name = "games")
public class Game {
    // ------------------------------------------------------------
    //                   Attributes / Fields
    // ------------------------------------------------------------
    @Id
    @Column(name = "game_id", nullable = false)
    private Integer id;

    // The user who played this game
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // The timestamp when the game was played
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

    public Instant getPlayedAt() {
        return playedAt;
    }

    public void setPlayedAt(Instant playedAt) {
        this.playedAt = playedAt;
    }

}