package com.quizmael.dao.impl;

import com.quizmael.dao.QuizTestDao;
import com.quizmael.model.QuizTest;
import com.quizmael.model.enums.Language;
import com.quizmael.model.enums.State;
import com.quizmael.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    @Override
    public List<QuizTest> findRejectedTestsByUser(int userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM QuizTest q WHERE q.state = :state AND q.creator.id = :userId", QuizTest.class)
                    .setParameter("state", State.REJECTED)
                    .setParameter("userId", userId)
                    .list();
        }
    }

    @Override
    public List<QuizTest> findModeratedBy(int moderatorId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM QuizTest q WHERE q.moderatedBy.id = :moderatorId", QuizTest.class)
                    .setParameter("moderatorId", moderatorId)
                    .list();
        }
    }

    @Override
    public List<String> findDistinctTopicsInPublishedTests() {
        List<String> topics = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql =
                    "SELECT DISTINCT t.topic.name " +
                            "FROM TestTopic t " +
                            "WHERE t.test.state = :state";
            topics = session.createQuery(hql, String.class)
                    .setParameter("state", State.PUBLISHED)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace(); // TODO: cambiar a logger: logger.error("Error loading creators from published tests", e);
        }
        return topics;
    }


    @Override
    public List<String> findAllCreatorsOfPublishedTests() {
        List<String> creators = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT DISTINCT q.creator.name FROM QuizTest q WHERE q.state = :state";
            creators = session.createQuery(hql, String.class)
                    .setParameter("state", State.PUBLISHED)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace(); // TODO: cambiar a logger: logger.error("Error loading creators from published tests", e);
        }
        return creators;
    }

    @Override
    public List<QuizTest> findPublicByFilters(String topicName, String creatorName, Integer difficulty, Set<Language> languages) {
        List<QuizTest> results = new ArrayList<>();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StringBuilder hql = new StringBuilder(
                    "SELECT DISTINCT q " +
                            "FROM QuizTest q " +
                            "LEFT JOIN FETCH q.testTopics tt " +
                            "LEFT JOIN FETCH tt.topic t " +
                            "LEFT JOIN FETCH q.questions " +
                            "LEFT JOIN FETCH q.creator " +
                            "WHERE q.state = :state AND q.language IN (:languages)");

            if (topicName != null) {
                hql.append(" AND tt.topic.name = :topicName");
            }
            if (creatorName != null) {
                hql.append(" AND q.creator.name = :creatorName");
            }
            if (difficulty != null) {
                hql.append(" AND q.difficulty = :difficulty");
            }

            Query<QuizTest> query = session.createQuery(hql.toString(), QuizTest.class);
            query.setParameter("state", State.PUBLISHED);
            query.setParameterList("languages", languages);

            if (topicName != null) {
                query.setParameter("topicName", topicName);
            }
            if (creatorName != null) {
                query.setParameter("creatorName", creatorName);
            }
            if (difficulty != null) {
                query.setParameter("difficulty", difficulty);
            }

            results = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace(); // TODO: cambiar a logger: logger.error("Error loading creators from published tests", e);
        }

        return results;
    }

}