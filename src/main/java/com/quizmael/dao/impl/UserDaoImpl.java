package com.quizmael.dao.impl;

import com.quizmael.dao.UserDao;
import com.quizmael.model.QuizTest;
import com.quizmael.model.User;
import com.quizmael.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link UserDao} interface using Hibernate.
 * <p>Provides CRUD operations for {@link User} entities.</p>
 * <p>Uses Hibernate sessions and manages transactions explicitly.</p>
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public class UserDaoImpl implements UserDao {

    @Override
    public User save(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
            return user;
        } catch (Exception ex) {
            if (transaction != null) transaction.rollback();
            throw ex;
        }
    }

    @Override
    public void update(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) transaction.rollback();
            throw ex;
        }
    }

    @Override
    public void delete(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(user);
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) transaction.rollback();
            throw ex;
        }
    }

    @Override
    public Optional<User> findById(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(User.class, id));
        }
    }

    @Override
    public Optional<User> findByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM User WHERE name = :name";
            User user = session.createQuery(hql, User.class)
                    .setParameter("name", name)
                    .uniqueResult();
            return Optional.ofNullable(user);
        }
    }

    @Override
    public List<User> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User", User.class).list();
        }
    }

    public List<QuizTest> getFavoriteTests(int userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT t FROM User u JOIN u.favoriteTests t WHERE u.id = :userId", QuizTest.class)
                    .setParameter("userId", userId)
                    .list();
        }
    }

}
