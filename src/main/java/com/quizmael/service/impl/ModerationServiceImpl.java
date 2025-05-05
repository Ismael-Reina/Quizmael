package com.quizmael.service.impl;

import com.quizmael.dao.ModerationDao;
import com.quizmael.dao.QuizTestDao;
import com.quizmael.dao.UserDao;
import com.quizmael.model.Moderation;
import com.quizmael.model.QuizTest;
import com.quizmael.model.enums.State;
import com.quizmael.model.User;
import com.quizmael.service.ModerationService;

import java.time.Instant;
import java.util.List;

/**
 * Implementation of the ModerationService interface.
 * This class handles the moderation operations for quiz tests,
 * including finding pending tests and validating them.
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public class ModerationServiceImpl implements ModerationService {

    private final QuizTestDao quizTestDao;
    private final UserDao userDao;
    private final ModerationDao moderationDao;

    public ModerationServiceImpl(QuizTestDao quizTestDao, UserDao userDao, ModerationDao moderationDao) {
        this.quizTestDao = quizTestDao;
        this.userDao = userDao;
        this.moderationDao = moderationDao;
    }

    @Override
    public List<QuizTest> findPendingTests() {
        return quizTestDao.findByState(State.PENDING);
    }

    @Override
    public List<QuizTest> findRejectedTestsByUser(int userId) {
        return quizTestDao.findRejectedTestsByUser(userId);
    }

    @Override
    public List<QuizTest> findModeratedBy(int moderatorId) {
        return quizTestDao.findModeratedBy(moderatorId);
    }

    @Override
    public void validateTest(int moderatorId, int testId) {
        // Search for the test
        QuizTest test = quizTestDao.findById(testId)
                .orElseThrow(() -> new IllegalArgumentException("Test not found with ID: " + testId));

        // Find the moderator
        User moderator = userDao.findById(moderatorId)
                .orElseThrow(() -> new IllegalArgumentException("Moderator not found with ID: " + moderatorId));

        // Set the moderation fields in the test
        test.setState(State.PUBLISHED);
        test.setModeratedBy(moderator);
        test.setModerationDate(Instant.now());
        quizTestDao.update(test);

        // Create and save the moderation record
        Moderation moderation = new Moderation();
        moderation.setTest(test);
        moderation.setModerator(moderator);
        moderation.setAssignedState(State.PUBLISHED);
        moderation.setModeratedAt(Instant.now());
        moderation.setRejectionReason(null);  // No hay motivo de rechazo en este caso

        moderationDao.save(moderation);
    }

    @Override
    public void rejectTest(int moderatorId, int testId, String reason) {
        // Buscar el test
        QuizTest test = quizTestDao.findById(testId)
                .orElseThrow(() -> new IllegalArgumentException("Test not found with ID: " + testId));

        // Buscar el moderador
        User moderator = userDao.findById(moderatorId)
                .orElseThrow(() -> new IllegalArgumentException("Moderator not found with ID: " + moderatorId));

        // Establecer los campos de moderación en el test
        test.setState(State.REJECTED);
        test.setModeratedBy(moderator);
        test.setModerationDate(Instant.now());
        quizTestDao.update(test);

        // Crear y guardar el registro de moderación
        Moderation moderation = new Moderation();
        moderation.setTest(test);
        moderation.setModerator(moderator);
        moderation.setAssignedState(State.REJECTED);
        moderation.setModeratedAt(Instant.now());
        moderation.setRejectionReason(reason);

        moderationDao.save(moderation);
    }

}
