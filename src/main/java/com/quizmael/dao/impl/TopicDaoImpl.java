package com.quizmael.dao.impl;

import com.quizmael.dao.TopicDao;
import com.quizmael.model.Topic;
import com.quizmael.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link TopicDao} using Hibernate.
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public class TopicDaoImpl implements TopicDao {

    @Override
    public void save(Topic topic) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(topic);
            tx.commit();
        }
    }

    @Override
    public void update(Topic topic) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(topic);
            tx.commit();
        }
    }

    @Override
    public void delete(Topic topic) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.remove(topic);
            tx.commit();
        }
    }

    @Override
    public Optional<Topic> findById(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Topic.class, id));
        }
    }

    @Override
    public List<Topic> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Topic", Topic.class).list();
        }
    }
}
