package com.quizmael.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * <strong>Entity</strong> representing the association between a test and a topic.
 *
 * <p>Defines a many-to-one relationship with {@link Topic}, using a composite primary key.</p>
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
@Entity
@Table(name = "test_topics")
public class TestTopic {

    // ------------------------------------------------------------
    //                   Attributes / Fields
    // ------------------------------------------------------------
    @EmbeddedId
    private TestTopicId id;

    // Test entity associated with the topic
    @MapsId("testId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "test_id", nullable = false)
    private Test test;

    // Topic entity associated with the test
    @MapsId("topicId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    // ------------------------------------------------------------
    //                   Getters & Setters
    // ------------------------------------------------------------
    public TestTopicId getId() {
        return id;
    }

    public void setId(TestTopicId id) {
        this.id = id;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

}