package com.quizmael.util;

import com.quizmael.util.LoggerUtil;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * <strong>Hibernate Utility</strong> class to manage the <code>SessionFactory</code>.
 * <p>Initializes and provides access to Hibernate sessions using the configuration file.</p>
 * <p>Logs errors during setup and ensures a single session factory instance is reused.</p>
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Exception ex) {
            LoggerUtil.error(HibernateUtil.class, "Initial SessionFactory creation failed.", ex);
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
