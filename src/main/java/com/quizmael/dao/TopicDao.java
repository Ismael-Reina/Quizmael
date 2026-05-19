package com.quizmael.dao;

import com.quizmael.model.Topic;
import java.util.List;
import java.util.Optional;

/**
 * DAO interface for {@link Topic} entity.
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public interface TopicDao {

    /**
     * Saves a new topic to the database.
     *
     * @param topic the topic to save
     */
    void save(Topic topic);

    /**
     * Updates an existing topic in the database.
     *
     * @param topic the topic to update
     */
    void update(Topic topic);

    /**
     * Deletes a topic from the database.
     *
     * @param topic the topic to delete
     */
    void delete(Topic topic);

    /**
     * Finds a topic by its ID.
     *
     * @param id the ID of the topic to find
     * @return an Optional containing the found topic, or empty if not found
     */
    Optional<Topic> findById(Integer id);

    /**
     * Finds a topic by its name (case-insensitive recommended).
     *
     * @param name the name of the topic
     * @return an Optional containing the found topic, or empty if not found
     */
    Optional<Topic> findByName(String name);

    /**
     * Finds all topics in the database.
     *
     * @return a list of all topics
     */
    List<Topic> findAll();

}
