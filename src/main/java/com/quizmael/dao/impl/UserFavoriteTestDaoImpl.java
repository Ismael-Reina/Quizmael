package com.quizmael.dao.impl;

import com.quizmael.dao.UserFavoriteTestDao;
import com.quizmael.model.UserFavoriteTest;
import com.quizmael.model.UserFavoriteTestId;
import com.quizmael.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link UserFavoriteTestDao} using Hibernate.
 * <p>Provides CRUD operations for {@link UserFavoriteTest} entities.</p>
 * <p>Uses Hibernate sessions and manages transactions explicitly.</p>
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public class UserFavoriteTestDaoImpl implements UserFavoriteTestDao {

    @Override
    public void save(UserFavoriteTest entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void delete(UserFavoriteTest entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public Optional<UserFavoriteTest> findById(UserFavoriteTestId id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(UserFavoriteTest.class, id));
        }
    }

    @Override
    public List<UserFavoriteTest> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM UserFavoriteTest", UserFavoriteTest.class).list();
        }
    }
}
