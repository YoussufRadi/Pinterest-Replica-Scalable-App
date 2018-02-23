package msa.postgresql;

import msa.pojo.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class DatabaseController {
    private static SessionFactory factory;

    public DatabaseController() {
        ConfigurationInit conf = new ConfigurationInit();
        this.factory = conf.getFactory();

    }


    /* Method to CREATE an user in the database */
    public UUID addUser(String fname, String lname,
                            String email, String password,
                            boolean gender){
        Session session = factory.openSession();
        Transaction tx = null;
        UUID userID = null;

        try {
            tx = session.beginTransaction();
            User user = new User(fname, lname, email,password,gender);
            userID = (UUID) session.save(user);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return userID;
    }

    public UUID addUser(User user){
        Session session = factory.openSession();
        Transaction tx = null;
        UUID userID = null;

        try {
            tx = session.beginTransaction();

            userID = (UUID) session.save(user);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return userID;
    }

    /* Method to  READ all the users */
    public void listUsers( ){
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            List userss = session.createQuery("FROM User").list();
            for (Iterator iterator = userss.iterator(); iterator.hasNext();){
                User user = (User) iterator.next();
                System.out.print("First Name: " + user.getFirstName());
                System.out.print("  Last Name: " + user.getLastName());
                System.out.println("  ID: " + user.getId());
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    /* Method to UPDATE firstName for an employee */
    public void updateUserFirstName(UUID userID, String firstName ){
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            User user = (User)session.get(User.class, userID);
            user.setFirstName( firstName );
            session.update(user);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    /* Method to DELETE a user from the records */
   /* public void deleteUser(UUID UserID){
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            User user = (User)session.get(User.class, UserID);
            session.delete(user);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }*/
}

