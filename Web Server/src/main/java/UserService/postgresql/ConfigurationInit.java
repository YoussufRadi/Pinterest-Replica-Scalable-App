package UserService.postgresql;

import UserService.pojo.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class ConfigurationInit {

    public static SessionFactory getFactory() {
        return factory;
    }

    private static  SessionFactory factory;

    public ConfigurationInit(){
        try {

            factory = new org.hibernate.cfg.Configuration().
                    configure().
                    addAnnotatedClass(User.class).
                    buildSessionFactory();

        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() throws HibernateException {
        return factory.openSession();
    }

}
