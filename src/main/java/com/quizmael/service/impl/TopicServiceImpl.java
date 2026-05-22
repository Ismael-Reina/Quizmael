package com.quizmael.service.impl;

import com.quizmael.dao.TopicDao;
import com.quizmael.model.Topic;
import com.quizmael.service.TopicService;
import com.quizmael.util.LoggerUtil;

import java.util.List;
import java.util.Optional;
/**
 * Implementation of the TopicService interface.
 * Handles business logic for topic management, including validation and logging.
 */
public class TopicServiceImpl implements TopicService {

    private final TopicDao topicDao;

    /**
     * Constructs the TopicServiceImpl with the required topic data access object.
     *
     * @param topicDao the DAO responsible for category and topic data management.
     */
    public TopicServiceImpl(TopicDao topicDao) {
        this.topicDao = topicDao;
    }

    @Override
    public List<Topic> findAll() {
        LoggerUtil.debug(TopicServiceImpl.class, "Fetching all topics.");
        return topicDao.findAll();
    }

    @Override
    public Optional<Topic> findById(int id) {
        LoggerUtil.debug(TopicServiceImpl.class, "Fetching topic by ID: " + id);
        return topicDao.findById(id);
    }

    @Override
    public Optional<Topic> findByName(String name) {
        LoggerUtil.debug(TopicServiceImpl.class, "Fetching topic by name: " + name);
        return topicDao.findByName(name);
    }

    @Override
    public void createTopic(Topic topic) {
        LoggerUtil.info(TopicServiceImpl.class, "Attempting to create topic: " +
                (topic != null ? topic.getName() : "null"));

        validateTopic(topic);

        // Check for uniqueness (case-insensitive check is recommended in DAO)
        if (topicDao.findByName(topic.getName().trim()).isPresent()) {
            LoggerUtil.warn(TopicServiceImpl.class, "Topic creation failed: Name already exists.");
            throw new IllegalArgumentException("error.topic.already_exists");
        }

        try {
            topic.setName(topic.getName().trim());
            topicDao.save(topic);
            LoggerUtil.info(TopicServiceImpl.class, "Topic created successfully.");
        } catch (Exception e) {
            LoggerUtil.error(TopicServiceImpl.class, "Database error creating topic.", e);
            throw new RuntimeException("error.database.internal", e);
        }
    }

    @Override
    public void updateTopic(Topic topic) {
        LoggerUtil.info(TopicServiceImpl.class, "Updating topic ID: " + topic.getId());

        validateTopic(topic);

        try {
            topic.setName(topic.getName().trim());
            topicDao.update(topic);
            LoggerUtil.info(TopicServiceImpl.class, "Topic updated successfully.");
        } catch (Exception e) {
            LoggerUtil.error(TopicServiceImpl.class, "Database error updating topic.", e);
            throw new RuntimeException("error.database.internal", e);
        }
    }

    @Override
    public boolean deleteTopic(int id) {
        LoggerUtil.info(TopicServiceImpl.class, "Attempting to delete topic ID: " + id);

        Optional<Topic> topicOpt = topicDao.findById(id);

        if (topicOpt.isPresent()) {
            try {
                topicDao.delete(topicOpt.get()); // Homogéneo con el resto
                LoggerUtil.info(TopicServiceImpl.class, "Topic deleted successfully.");
                return true;
            } catch (Exception e) {
                LoggerUtil.error(TopicServiceImpl.class, "Error deleting topic ID: " + id, e);
                return false;
            }
        }
        return false;
    }

    // ------------------------------------------------------------
    //                      Private Methods
    // ------------------------------------------------------------

    /**
     * Validates that the topic and its mandatory fields are correct.
     */
    private void validateTopic(Topic topic) {
        if (topic == null) {
            throw new IllegalArgumentException("error.validation.topic.null");
        }
        if (topic.getName() == null || topic.getName().trim().isEmpty()) {
            LoggerUtil.warn(TopicServiceImpl.class, "Validation failed: Topic name is empty.");
            throw new IllegalArgumentException("error.validation.topic.name_empty");
        }
    }
}
