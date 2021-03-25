package com.viettel.campaign.utils;
import com.viettel.security.PassTranformer;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.File;

/**
 * @author anhvd_itsol
 */
public class HibernateUtil {
    private static SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory()
    {
        try
        {
            if (sessionFactory == null)
            {
                Configuration configuration = new Configuration().configure(new File("etc/hibernate.cfg.xml"));
                PassTranformer.setInputKey(Config.SALT);
                configuration.setProperty("hibernate.connection.url", configuration.getProperty("connection.url"));
                configuration.setProperty("hibernate.connection.username", PassTranformer.decrypt(configuration.getProperty("connection.username")));
                configuration.setProperty("hibernate.connection.password", PassTranformer.decrypt(configuration.getProperty("connection.password")));
                StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder();
                serviceRegistryBuilder.applySettings(configuration.getProperties());
                ServiceRegistry serviceRegistry = serviceRegistryBuilder.build();
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            }
            return sessionFactory;
        } catch (Throwable ex)
        {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory()
    {
        return sessionFactory;
    }

    public static void shutdown()
    {
        getSessionFactory().close();
    }
}
