package com.quizmael.dao.impl;

import com.quizmael.dao.GameQuestionDao;
import com.quizmael.model.Game;
import com.quizmael.model.GameQuestion;
import com.quizmael.model.GameQuestionId;
import com.quizmael.model.Question;
import com.quizmael.util.HibernateUtil;
import com.quizmael.util.LoggerUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link GameQuestionDao} using Hibernate.
 * Provides standard CRUD operations.
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public class GameQuestionDaoImpl implements GameQuestionDao {

/*    @Override
    public void save(GameQuestion gameQuestion) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            System.out.println("Saving GameQuestion: " + gameQuestion);             // TODO: remove debug lines
            System.out.println("GameQuestion ID: " + gameQuestion.getId());
            System.out.println("GameQuestionId: gameId=" + gameQuestion.getId().getGameId() + ", questionId=" + gameQuestion.getId().getQuestionId());


            System.out.println("Game: " + gameQuestion.getGame());
            System.out.println("Test: " + gameQuestion.getGame().getQuizTest());
            System.out.println("User: " + gameQuestion.getGame().getUser());
            System.out.println("Game ID: " + gameQuestion.getGame().getId());

            System.out.println("Question: " + gameQuestion.getQuestion());
            System.out.println("Question ID: " + gameQuestion.getQuestion().getId());

            GameQuestion existing = session.get(GameQuestion.class, gameQuestion.getId());
            if (existing != null) {
                System.out.println("GameQuestion ya existe: " + gameQuestion.getId());
                return;
            }

            session.persist(gameQuestion);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            throw e; // Re-lanzar la excepción para identificar el problema
        }
    }*/

    public void save(GameQuestion gameQuestion) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            // Validar que los objetos relacionados no sean nulos
            if (gameQuestion == null || gameQuestion.getId() == null) {
                throw new IllegalArgumentException("GameQuestion o su ID no pueden ser nulos");
            }
            if (gameQuestion.getGame() == null || gameQuestion.getGame().getId() == null) {
                throw new IllegalArgumentException("El Game asociado no puede ser nulo y debe tener un ID válido");
            }
            if (gameQuestion.getQuestion() == null || gameQuestion.getQuestion().getId() == null) {
                throw new IllegalArgumentException("La Question asociada no puede ser nula y debe tener un ID válido");
            }

            // Reanexar las entidades relacionadas al contexto de persistencia
            Game managedGame = session.get(Game.class, gameQuestion.getGame().getId());
            if (managedGame == null) {
                throw new IllegalStateException("El Game con ID " + gameQuestion.getGame().getId() + " no existe en la base de datos");
            }
            gameQuestion.setGame(managedGame);

            Question managedQuestion = session.get(Question.class, gameQuestion.getQuestion().getId());
            if (managedQuestion == null) {
                throw new IllegalStateException("La Question con ID " + gameQuestion.getQuestion().getId() + " no existe en la base de datos");
            }
            gameQuestion.setQuestion(managedQuestion);

            // Persistir el objeto
            session.persist(gameQuestion);
            tx.commit();
        } catch (Exception e) {
            LoggerUtil.error(getClass(), "CRITICAL DATABASE ERROR: " + e.getMessage(), e);
            if (tx != null) {
                try {
                    tx.rollback();
                } catch (Exception rollbackEx) {
                    LoggerUtil.warn(getClass(), "Rollback failed (Normal if connection closed): " + rollbackEx.getMessage());
                }
            }
            throw e;
        }
    }

    @Override
    public void update(GameQuestion gameQuestion) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(gameQuestion);
            tx.commit();
        } catch (Exception e) {
            LoggerUtil.error(getClass(), "CRITICAL DATABASE ERROR: " + e.getMessage(), e);
            if (tx != null) {
                try {
                    tx.rollback();
                } catch (Exception rollbackEx) {
                    LoggerUtil.warn(getClass(), "Rollback failed (Normal if connection closed): " + rollbackEx.getMessage());
                }
            }
            throw e;
        }
    }

    @Override
    public void delete(GameQuestion gameQuestion) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.remove(gameQuestion);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public Optional<GameQuestion> findById(GameQuestionId id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(GameQuestion.class, id));
        }
    }

    @Override
    public List<GameQuestion> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM GameQuestion", GameQuestion.class).list();
        }
    }

    @Override
    public List<GameQuestion> findByGame(Game game) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT DISTINCT gq FROM GameQuestion gq " +
                                    "JOIN FETCH gq.question q " +
                                    "JOIN FETCH q.answers " +
                                    "WHERE gq.game = :game", GameQuestion.class)
                    .setParameter("game", game)
                    .getResultList();
        }
    }

}
