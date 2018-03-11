package msa.postgresql;

import msa.pojo.User;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;

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
                        String username, String email,String password,
                        boolean gender, int age){
        Session session = factory.openSession();
        Transaction tx = null;
        UUID userID = null;

        try {
            tx = session.beginTransaction();
            User user = new User(fname, lname,username, email,password,gender, age);
            userID = (UUID) session.save(user);
            tx.commit();
            return userID;
        } catch (HibernateException e) {
            if (tx!=null) {
                tx.rollback();
            }
            e.printStackTrace();
            System.out.println("EROOOOOORRR");
            return null;

        } catch (Exception e) {
            e.printStackTrace();

            System.out.println("EROOOOOORRR");
            return null;

        }finally
         {
            session.close();
             return userID;

        }
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




    public User signIn (String email, String password ){
        Session session = factory.openSession();
        Transaction tx = null;

        System.out.println(email);
        System.out.println(password);

        User user = null;

        try {
            tx = session.beginTransaction();
            Criteria criteria = session.createCriteria(User.class);
            user = (User) criteria.add(Restrictions.eq("email",email)).uniqueResult();
            if (user==null){
                return null;
            }
            if(user.getPassword().equals(password)){


                session.update(user);
                tx.commit();


                return user;


            }

            session.update(user);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
            return null;

        } finally {
            session.close();

        }
        return user;
    }


    public User getUserByEmail (String email ){
        Session session = factory.openSession();
        Transaction tx = null;
        User user = null;

        try {
            tx = session.beginTransaction();
            Criteria criteria = session.createCriteria(User.class);
            user = (User) criteria.add(Restrictions.eq("email",email)).uniqueResult();
            if (user==null){
                return null;
            }
            session.update(user);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return user;
    }


    public User getUser(UUID user_id){
        Session session = factory.openSession();
        Transaction tx = null;
        User user =null;

        try {
            tx = session.beginTransaction();
            user = (User)session.get(User.class, user_id);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
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
    public void updateUser(UUID userID, String firstName, String lastname, String password, String username, int age, boolean gender ){
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            User user = (User)session.get(User.class, userID);
            user.setFirstName( firstName );
            user.setLastName(lastname);
            user.setPassword(password);
            user.setAge(age);
            user.setGender(gender);
            user.setUsername(username);
            session.update(user);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void followCategories(UUID userID, UUID categoryID ){
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            User user = (User)session.get(User.class, userID);
            user.getUserCat().add(categoryID);
            session.update(user);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }



    public void addBoard (UUID userID, UUID boardID ){
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            User user = (User)session.get(User.class, userID);
            user.getBoards().add(boardID);
            session.update(user);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }


    public void removeBoard (UUID userID, UUID boardID ){
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            User user = (User)session.get(User.class, userID);
            user.getBoards().remove(boardID);
            session.update(user);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }


    public boolean addPin (UUID userID, UUID pinID ){
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            User user = (User)session.get(User.class, userID);
            user.getPinnedPosts().add(pinID);
            session.update(user);
            tx.commit();
            return true;
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    public boolean removePin (UUID userID, UUID pinID ){
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            User user = (User)session.get(User.class, userID);
            user.getPinnedPosts().remove(pinID);
            session.update(user);
            tx.commit();
            return true;
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }



    public void followHashtag (UUID userID, UUID hashtagID ){
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            User user = (User)session.get(User.class, userID);
            user.getHashtags().add(hashtagID);
            session.update(user);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void unfollowHashtag (UUID userID, UUID hashtagID ){
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            User user = (User)session.get(User.class, userID);
            user.getHashtags().remove(hashtagID);
            session.update(user);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }



    public boolean likePhotos(UUID userID, UUID likedPhotoID ){
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            User user = (User)session.get(User.class, userID);
            user.getUserLikedPhotos().add(likedPhotoID);
            session.update(user);
            tx.commit();
            return true;
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }


    public boolean unlikePhotos(UUID userID, UUID unlikedPhotoID ){
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            User user = (User)session.get(User.class, userID);
            user.getUserLikedPhotos().remove(unlikedPhotoID);
            session.update(user);
            tx.commit();
            return true;
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }


    public boolean dislikePhotos(UUID userID, UUID dislikedPhotoID ){
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            User user = (User)session.get(User.class, userID);
            user.getUserDislikedPhotos().add(dislikedPhotoID);
            session.update(user);
            tx.commit();
            return true;
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }

    }

    public boolean undislikePhotos(UUID userID, UUID undislikedPhotoID ){
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            User user = (User)session.get(User.class, userID);
            user.getUserDislikedPhotos().remove(undislikedPhotoID);
            session.update(user);
            tx.commit();
            return true;
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }











    public User getUserWithProfileById(UUID userId)
    {
        Session session = factory.openSession();
        Transaction tx = null;
        User user = null;
        try {
            tx = session.beginTransaction();

            user = (User) session.createCriteria(User.class).add(Restrictions.idEq(userId)).uniqueResult();

            Hibernate.initialize(user.getUserCat());
            Hibernate.initialize(user.getBlock());
            Hibernate.initialize(user.getBlockedBy());
            Hibernate.initialize(user.getFollowedBy());
            Hibernate.initialize(user.getFollow());
            Hibernate.initialize(user.getFollowers());
            Hibernate.initialize(user.getFollowing());





            tx.commit();

    } catch (HibernateException e) {
        if (tx!=null) tx.rollback();
        e.printStackTrace();
    } finally {
        session.close();
    }

        return user;
    }

    public boolean blockUser(UUID userID, UUID blocked ){
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            User user = (User)session.get(User.class, userID);
            User block = (User)session.get(User.class, blocked);

            user.getBlock().add(block);

            session.update(user);
            tx.commit();
            return true;
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    public void unblockUser(UUID userID, UUID unblocked ){
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            User user = (User)session.get(User.class, userID);
            User unblock = (User)session.get(User.class, unblocked);

            user.getBlock().remove(unblock);

            session.update(user);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }


    public boolean followUser(UUID userID, UUID followingId ){
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            User user = (User)session.get(User.class, userID);
            User following = (User)session.get(User.class,followingId );

            following.getFollowedBy().add(user);
            user.getFollow().add(following);
            session.update(user);
            tx.commit();
            return true;
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }


    public boolean unfollowUser(UUID userID, UUID followingId ){
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            User user = (User)session.get(User.class, userID);
            User following = (User)session.get(User.class,followingId );
            following.getFollowedBy().remove(user);
            user.getFollow().remove(following);
            session.update(user);
            tx.commit();
            return true;
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
            return false;
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

