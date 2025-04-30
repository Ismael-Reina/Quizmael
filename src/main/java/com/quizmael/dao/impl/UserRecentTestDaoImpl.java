package com.quizmael.dao.impl;

import com.quizmael.dao.UserRecentTestDao;
import com.quizmael.model.UserRecentTest;
import com.quizmael.model.UserRecentTestId;
import com.quizmael.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) implementation for managing {@link UserRecentTest} entity.
 * Provides methods to perform CRUD operations on UserRecentTest entities.
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public class UserRecentTestDaoImpl implements UserRecentTestDao {

    @Override
    public void save(UserRecentTest entity) {
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
    public void delete(UserRecentTest entity) {
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
    public Optional<UserRecentTest> findById(UserRecentTestId id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(UserRecentTest.class, id));
        }
    }

    @Override
    public List<UserRecentTest> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM UserRecentTest", UserRecentTest.class).list();
        }
    }
}