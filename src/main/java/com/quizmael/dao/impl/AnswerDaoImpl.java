package com.quizmael.dao.impl;

import com.quizmael.dao.AnswerDao;
import com.quizmael.model.Answer;
import com.quizmael.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the {@link AnswerDao} interface.
 * <p>Handles CRUD operations for the {@link Answer} entity using Hibernate.</p>
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public class AnswerDaoImpl implements AnswerDao {

    @Override
    public void save(Answer answer) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(answer);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    @Override
    public void update(Answer answer) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(answer);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    @Override
    public void delete(Answer answer) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(answer);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    @Override
    public Optional<Answer> findById(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Answer answer = session.get(Answer.class, id);
            return Optional.ofNullable(answer);
        }
    }

    @Override
    public List<Answer> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Answer", Answer.class).list();
        }
    }
}
