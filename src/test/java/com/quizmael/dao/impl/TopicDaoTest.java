package com.quizmael.dao.impl;

import com.quizmael.dao.TopicDao;
import com.quizmael.model.Topic;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link TopicDaoImpl}.
 * Verifies CRUD operations on Topic entities.
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
class TopicDaoTest {

    private TopicDao topicDao;

    @BeforeEach
    void setUp() {
        topicDao = new TopicDaoImpl();
    }


    // Verifies that a topic can be saved and correctly retrieved by its ID.
    @Test
    void testSaveAndFindById() {
        Topic topic = new Topic();
        topic.setName("Java Fundamentals");
        topicDao.save(topic);

        Optional<Topic> retrieved = topicDao.findById(topic.getId());
        assertTrue(retrieved.isPresent());
        assertEquals("Java Fundamentals", retrieved.get().getName());
    }

    // Verifies that the findAll method returns all stored topics.
    @Test
    void testFindAll() {
        Topic topic1 = new Topic();
        topic1.setName("Databases");
        topicDao.save(topic1);

        Topic topic2 = new Topic();
        topic2.setName("Software Design");
        topicDao.save(topic2);

        List<Topic> all = topicDao.findAll();
        assertTrue(all.size() >= 2);
    }

    // Verifies that an existing topic can be correctly updated.
    @Test
    void testUpdate() {
        Topic topic = new Topic();
        topic.setName("Old Name");
        topicDao.save(topic);

        topic.setName("Updated Name");
        topicDao.update(topic);

        Optional<Topic> updated = topicDao.findById(topic.getId());
        assertTrue(updated.isPresent());
        assertEquals("Updated Name", updated.get().getName());
    }

    // Verifies that a topic can be deleted and cannot be retrieved afterward.
    @Test
    void testDelete() {
        Topic topic = new Topic();
        topic.setName("To be deleted");
        topicDao.save(topic);

        topicDao.delete(topic);
        Optional<Topic> deleted = topicDao.findById(topic.getId());
        assertTrue(deleted.isEmpty());
    }

    // Verifies that searching for a non-existent topic returns an empty Optional.
    @Test
    void testFindByIdNotFound() {
        Optional<Topic> result = topicDao.findById(9999);
        assertTrue(result.isEmpty());
    }

}