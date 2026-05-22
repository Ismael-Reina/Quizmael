package com.quizmael.service;

import com.quizmael.model.Topic;

import java.util.List;
import java.util.Optional;

/**
 * Interface for managing topics (categories) within the quiz application.
 * Provides methods for administrative CRUD operations and retrieval for filtering.
 *
 *  @author Ismael Reina Muñoz
 *  @version 1.0
 */
public interface TopicService {

    /**
     * Retrieves all topics available in the system.
     * @return a list of all topics.
     */
    List<Topic> findAll();

    /**
     * Finds a topic by its unique ID.
     * @param id the topic ID.
     * @return an Optional containing the topic, if found.
     */
    Optional<Topic> findById(int id);

    /**
     * Finds a topic by its name.
     * @param name the name of the topic.
     * @return an Optional containing the topic, if found.
     */
    Optional<Topic> findByName(String name);

    /**
     * Creates a new topic in the system.
     * @param topic the topic to create.
     * @throws IllegalArgumentException if validation fails or name already exists.
     */
    void createTopic(Topic topic);

    /**
     * Updates an existing topic.
     * @param topic the topic with updated information.
     * @throws IllegalArgumentException if validation fails.
     */
    void updateTopic(Topic topic);

    /**
     * Deletes a topic by its ID.
     * @param id the ID of the topic to delete.
     * @return true if deleted successfully, false otherwise.
     */
    boolean deleteTopic(int id);
}
