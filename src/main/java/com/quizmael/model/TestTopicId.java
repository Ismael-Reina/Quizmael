package com.quizmael.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

/**
 * <strong>Composite primary key</strong> for the {@link TestTopic} entity.
 *
 * <p>Consists of the test ID and the topic ID.</p>
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
@Embeddable
public class TestTopicId implements Serializable {

    private static final long serialVersionUID = -8310419411800728082L;

    // ------------------------------------------------------------
    //                   Attributes / Fields
    // ------------------------------------------------------------
    @Column(name = "test_id", nullable = false)
    private Integer testId;

    @Column(name = "topic_id", nullable = false)
    private Integer topicId;

    // ------------------------------------------------------------
    //                   Getters & Setters
    // ------------------------------------------------------------
    public Integer getTestId() {
        return testId;
    }

    public void setTestId(Integer testId) {
        this.testId = testId;
    }

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TestTopicId entity = (TestTopicId) o;
        return Objects.equals(this.topicId, entity.topicId) &&
                Objects.equals(this.testId, entity.testId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topicId, testId);
    }

}