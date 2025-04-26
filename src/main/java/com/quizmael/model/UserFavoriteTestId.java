package com.quizmael.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

/**
 * <strong>Composite primary key</strong> for the {@link UserFavoriteTest} entity.
 *
 * <p>Consists of the user ID and the test ID.</p>
 */
@Embeddable
public class UserFavoriteTestId implements Serializable {

    private static final long serialVersionUID = -9010380989633725700L;

    // ------------------------------------------------------------
    //                   Attributes / Fields
    // ------------------------------------------------------------
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "test_id", nullable = false)
    private Integer testId;

    // ------------------------------------------------------------
    //                   Getters & Setters
    // ------------------------------------------------------------
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTestId() {
        return testId;
    }

    public void setTestId(Integer testId) {
        this.testId = testId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserFavoriteTestId entity = (UserFavoriteTestId) o;
        return Objects.equals(this.testId, entity.testId) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(testId, userId);
    }

}