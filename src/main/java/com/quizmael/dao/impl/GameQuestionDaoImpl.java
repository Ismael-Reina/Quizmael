package com.quizmael.dao.impl;

import com.quizmael.dao.GameQuestionDao;
import com.quizmael.model.GameQuestion;
import com.quizmael.model.GameQuestionId;
import com.quizmael.util.HibernateUtil;
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

    @Override
    public void save(GameQuestion gameQuestion) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(gameQuestion);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
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
            if (tx != null) tx.rollback();
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
}
