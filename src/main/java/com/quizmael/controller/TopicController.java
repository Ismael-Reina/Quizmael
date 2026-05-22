package com.quizmael.controller;

import com.quizmael.model.Topic;
import com.quizmael.service.TopicService;
import com.quizmael.util.I18nUtil;
import java.util.List;
import javax.swing.DefaultComboBoxModel;

/**
 * <strong>Controller</strong> responsible for managing topic-related operations.
 * <p>Acts as an intermediary between the {@link TopicService} and the UI components,
 * specifically providing models for category selection.</p>
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public class TopicController {

    private final TopicService topicService;

    /**
     * Constructs a new TopicController with the required service.
     * @param topicService the service used to handle business logic related to topics.
     */
    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    /**
     * Generates a model for a ComboBox containing all available topics.
     * * <p>The first element of the model is always a placeholder (e.g., "-- All Topics --")
     * retrieved from the internationalization utility.</p>
     * * @return a {@link DefaultComboBoxModel} populated with topic names.
     */
    public DefaultComboBoxModel<String> getTopicComboBoxModel() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

        // Add the "All topics" placeholder at index 0
        model.addElement(I18nUtil.getMessage("filter.all_topics"));

        // Retrieve and add all topic names from the service
        List<Topic> topics = topicService.findAll();
        for (Topic topic : topics) {
            model.addElement(topic.getName());
        }

        return model;
    }

    // Future CRUD methods for Phase 5 (Admin) can be added here
    // public void createTopic(String name) { ... }
}