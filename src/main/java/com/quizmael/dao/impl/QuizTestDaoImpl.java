package com.quizmael.dao.impl;

import com.quizmael.dao.QuizTestDao;
import com.quizmael.model.QuizTest;
import com.quizmael.model.enums.State;
import com.quizmael.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

/**
 * Hibernate implementation of {@link QuizTestDao}.
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public class QuizTestDaoImpl implements QuizTestDao {

    @Override
    public void save(QuizTest test) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(test);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public void update(QuizTest test) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(test);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public void delete(int testId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            QuizTest test = session.get(QuizTest.class, testId);
            if (test != null) {
                session.remove(test);
            }
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) transaction.rollback();
            throw ex;
        }
    }

    @Override
    public Optional<QuizTest> findById(int testId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(QuizTest.class, testId));
        }
    }

    @Override
    public List<QuizTest> findByCreatorId(int userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM QuizTest WHERE creator.id = :userId", QuizTest.class)
                    .setParameter("userId", userId)
                    .list();
        }
    }

    @Override
    public List<QuizTest> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM QuizTest", QuizTest.class).list();
        }
    }

    @Override
    public List<QuizTest> findAllPublic() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM QuizTest WHERE state = 'VALIDATED'", QuizTest.class
            ).list();
        }
    }

    @Override
    public List<QuizTest> findByTopic(String topicName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT qt FROM QuizTest qt JOIN qt.testTopics tt " +
                                    "WHERE qt.state = 'VALIDATED' AND tt.topic.name = :topicName", QuizTest.class
                    ).setParameter("topicName", topicName)
                    .list();
        }
    }

    @Override
    public List<QuizTest> findByTitleContaining(String title) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM QuizTest WHERE state = 'VALIDATED' AND LOWER(title) LIKE :title", QuizTest.class
                    ).setParameter("title", "%" + title.toLowerCase() + "%")
                    .list();
        }
    }

    @Override
    public List<QuizTest> findByState(State state) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM QuizTest q WHERE q.state = :state", QuizTest.class)
                    .setParameter("state", state)
                    .list();
        }
    }

}