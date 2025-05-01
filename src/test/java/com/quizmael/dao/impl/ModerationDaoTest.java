package com.quizmael.dao.impl;

import com.quizmael.dao.ModerationDao;
import com.quizmael.dao.QuizTestDao;
import com.quizmael.model.Moderation;
import com.quizmael.model.QuizTest;
import com.quizmael.model.User;
import com.quizmael.model.enums.Language;
import com.quizmael.model.enums.Role;
import com.quizmael.model.enums.State;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ModerationDaoImpl}.
 * Verifies CRUD operations on Moderation entities.
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
class ModerationDaoTest {

    private ModerationDao moderationDao;
    private QuizTest quizTest;

    @BeforeEach
    void setUp() {
        moderationDao = new ModerationDaoImpl();
        QuizTestDao quizTestDao = new QuizTestDaoImpl();

        // Creamos un test asociado a la moderación
        quizTest = new QuizTest();
        User creator = new User();
        creator.setName("Ismael");
        creator.setRole(Role.REGISTERED);
        quizTest.setCreator(creator);
        quizTest.setTitle("Test de programación en Java");
        quizTest.setLanguage(Language.ES);
        quizTest.setState(State.REJECTED);
        quizTest.setOptionsCount(4);
        quizTest.setTimeLimit(30);
        quizTestDao.save(quizTest);
    }

    // Verifies that a moderation can be saved and correctly retrieved by its ID.
    @Test
    void testSaveAndFindById() {
        Moderation moderation = new Moderation();
        moderation.setTest(quizTest);
        moderation.setRejectionReason("Respuestas 2, 3 y 5 incorrectas");
        moderation.setModeratedAt(Instant.now());

        moderationDao.save(moderation);
        Optional<Moderation> retrieved = moderationDao.findById(moderation.getId());

        assertTrue(retrieved.isPresent());
        assertEquals("Respuestas 2, 3 y 5 incorrectas", retrieved.get().getRejectionReason());
        assertEquals(quizTest.getId(), retrieved.get().getTest().getId());
    }

    // Verifies that the findAll method returns all stored moderations.
    @Test
    void testFindAll() {
        Moderation moderation1 = new Moderation();
        moderation1.setTest(quizTest);
        moderation1.setRejectionReason("Las preguntas no concuerdan con el tema");
        moderation1.setModeratedAt(Instant.now());
        moderationDao.save(moderation1);

        Moderation moderation2 = new Moderation();
        moderation2.setTest(quizTest);
        moderation2.setRejectionReason("Las retroalimentaciones contienen faltas de ortografía");
        moderation2.setModeratedAt(Instant.now());
        moderationDao.save(moderation2);

        List<Moderation> all = moderationDao.findAll();
        assertTrue(all.size() >= 2);
    }

    // Verifies that an existing moderation can be correctly updated.
    @Test
    void testUpdate() {
        Moderation moderation = new Moderation();
        moderation.setTest(quizTest);
        moderation.setRejectionReason("Todo está mal, investiga mejor");
        moderation.setModeratedAt(Instant.now());
        moderationDao.save(moderation);

        moderation.setRejectionReason("Todas las respuestas incorrectas, salvo la de la primera pregunta");
        moderationDao.update(moderation);

        Optional<Moderation> updated = moderationDao.findById(moderation.getId());
        assertTrue(updated.isPresent());
        assertEquals("Comentario actualizado", updated.get().getRejectionReason());
    }

    // Verifies that a moderation can be deleted and cannot be retrieved afterward.
    @Test
    void testDelete() {
        Moderation moderation = new Moderation();
        moderation.setTest(quizTest);
        moderation.setRejectionReason("Las retroalimentaciones son algo imprecisas");
        moderation.setModeratedAt(Instant.now());
        moderationDao.save(moderation);

        moderationDao.delete(moderation);
        Optional<Moderation> deleted = moderationDao.findById(moderation.getId());
        assertTrue(deleted.isEmpty());
    }

    // Verifies that searching for a non-existent moderation returns an empty Optional.
    @Test
    void testFindByIdNotFound() {
        Optional<Moderation> result = moderationDao.findById(9999);
        assertTrue(result.isEmpty());
    }
}
