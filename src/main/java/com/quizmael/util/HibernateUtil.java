package com.quizmael.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Exception ex) {
            // TODO: agregar log de error logger.error("Initial SessionFactory creation failed.", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private HibernateUtil() {
        // Prevent instantiation
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
