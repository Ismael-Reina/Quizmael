package com.quizmael.dao.impl;

import com.quizmael.dao.QuestionDao;
import com.quizmael.model.Question;
import com.quizmael.model.QuizTest;
import com.quizmael.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

/**
 * Hibernate implementation of {@link QuestionDao}.
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public class QuestionDaoImpl implements QuestionDao {

    @Override
    public void save(Question question) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(question);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public void update(Question question) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(question);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public void delete(Question question) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.remove(question);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public Optional<Question> findById(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Question.class, id));
        }
    }

    public List<Question> findByTests(List<QuizTest> tests) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Question q WHERE q.test IN :tests", Question.class)
                    .setParameter("tests", tests)
                    .list();
        }
    }

    @Override
    public List<Question> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Question", Question.class).list();
        }
    }

}
