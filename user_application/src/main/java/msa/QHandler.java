package msa.messagequeue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import msa.pojo.User;
import msa.pojo.UserLiveObject;
import msa.postgresql.DatabaseController;
import msa.redis.RedisConf;
import org.hibernate.Hibernate;
import org.redisson.api.RBucket;
import org.redisson.api.RLiveObjectService;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class QHandler {

    private DatabaseController dbcont ;
    private RedisConf redisConf;
    private RLiveObjectService service ;




    public QHandler() throws IOException {

        //initializations no big issue
       dbcont = new DatabaseController();
       redisConf = new RedisConf();
       service = redisConf.getService();

    }

    public UUID addUser(String fname, String lname,
                        String username, String email,String password,
                        boolean gender, int age) {

        //call the method from the database
        UUID id = dbcont.addUser(fname,lname,username,email,password,gender,age);
        System.out.println(id +"         terfeirguigegutegtugqutgqgjgt");
        try {
            //make a live object which is explained in the UserLiveObject.class
            UserLiveObject userLive = new UserLiveObject(id.toString(),
                    fname, lname, username, email, password, gender, age);
            //save it in redis cache
            userLive = service.persist(userLive);

            //save email - id pair in redis also
            //by creating a bucket which is a class that wraps the string object
            //to store it in redis
            // the email in redisConf.getClient().getBucket(email); is the id of the redis object


            RBucket<String> bucket = redisConf.getClient().getBucket(email);
            // set the value to the user id
            bucket.set(id.toString());

        }catch (Exception e){

           e.printStackTrace();
            return null;

        }
        return id;

        //we store email -id pair on redis to ease for us searching in redis
        //using the email also not only the uuid this will be used in the sign in and getUserEmail
        //methods


    }

    public void updateUser(UUID userID,
                           String firstName, String lastname, String password,
                           String username, int age, boolean gender ) {

        //we update the user in the database easy
        dbcont.updateUser(userID, firstName, lastname, password, username, age, gender);

        //we check if the user is in the cache we update him
        try {
            if (service.get(UserLiveObject.class, userID.toString()) != null) {


                // we make a live object and update the fields no need to save anything
                //its a live object so it updates in a live matter ya salma w ya youmna
                UserLiveObject user = service.get(UserLiveObject.class, userID.toString());
                user.setFirstName(firstName);
                user.setLastName(lastname);
                user.setPassword(password);
                user.setUsername(username);
                user.setAge(age);
                user.setGender(gender);

            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

        public void addBoard (UUID userID, UUID boardID ){

        // add a board in the db by using the normal method
        dbcont.addBoard(userID,boardID);


        // if the user is in the cache then update his boards also !!
            try {
                if (service.get(UserLiveObject.class, userID.toString()) != null) {

                    //same as the above
                    UserLiveObject user = service.get(UserLiveObject.class, userID.toString());
                    //get the boards
                    Set boards = user.getBoards();
                    //add a board to it
                    boards.add(boardID);


                }
            }catch (Exception e){
                e.printStackTrace();
            }


    }

    public void removeBoard (UUID userID, UUID boardID ){

        // add a board in the db by using the normal method
        dbcont.removeBoard(userID,boardID);


        // if the user is in the cache then update his boards also !!
        try {
            if (service.get(UserLiveObject.class, userID.toString()) != null) {

                //same as the above
                UserLiveObject user = service.get(UserLiveObject.class, userID.toString());
                //get the boards
                Set boards = user.getBoards();
                //add a board to it
                boards.remove(boardID);


            }
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println(service.get(UserLiveObject.class,
                userID.toString()).getBoards().toString());


    }
    public void followCategories(UUID userID, UUID categoryID ){

        // add a board in the db by using the normal method
        dbcont.followCategories(userID,categoryID);

        try {
            // if the user is in the cache then update his boards also !!
            if (service.get(UserLiveObject.class, userID.toString()) != null) {

                //same as the above
                UserLiveObject user = service.get(UserLiveObject.class, userID.toString());
                //get the boards
                Set categories = user.getUserCat();
                //add a board to it
                categories.add(categoryID);


            }
        }catch (Exception e){
            e.printStackTrace();
        }



    }
    public void addPin (UUID userID, UUID pinID ){

        // add a board in the db by using the normal method
        dbcont.addPin(userID,pinID);

        try {
            // if the user is in the cache then update his boards also !!
            if (service.get(UserLiveObject.class, userID.toString()) != null) {

                //same as the above
                UserLiveObject user = service.get(UserLiveObject.class, userID.toString());
                //get the boards
                Set pins = user.getPinnedPosts();
                //add a board to it
                pins.add(pinID);


            }
        }catch (Exception e){
            e.printStackTrace();
        }



    }
    public void removePin (UUID userID, UUID pinID ){

        // add a board in the db by using the normal method
        dbcont.removePin(userID,pinID);



        // if the user is in the cache then update his boards also !!
        try {
            if (service.get(UserLiveObject.class, userID.toString()) != null) {

                //same as the above
                UserLiveObject user = service.get(UserLiveObject.class, userID.toString());
                //get the boards
                Set pins = user.getPinnedPosts();
                //add a board to it
                pins.remove(pinID);


            }
        }catch (Exception e){
            e.printStackTrace();
        }




    }
    public void followHashtags(UUID userID, UUID hashtagID ){

        // add a board in the db by using the normal method
        dbcont.followHashtag(userID,hashtagID);


        // if the user is in the cache then update his boards also !!
        try {
            if (service.get(UserLiveObject.class, userID.toString()) != null) {

                //same as the above
                UserLiveObject user = service.get(UserLiveObject.class, userID.toString());
                //get the boards
                Set hashtags = user.getHashtags();
                //add a board to it
                hashtags.add(hashtagID);


            }
        }catch (Exception e){
            e.printStackTrace();
        }

      ;


    }
    public void unfollowHashtags (UUID userID, UUID hashtagID ){

        // add a board in the db by using the normal method
        dbcont.unfollowHashtag(userID,hashtagID);


        // if the user is in the cache then update his boards also !!
        try {
            if (service.get(UserLiveObject.class, userID.toString()) != null) {

                //same as the above
                UserLiveObject user = service.get(UserLiveObject.class, userID.toString());
                //get the boards
                Set hashtags = user.getHashtags();
                //add a board to it
                hashtags.remove(hashtagID);


            }
        }catch (Exception e){
            e.printStackTrace();
        }




    }
    public void likePhotos(UUID userID, UUID likedPhotoID ){

        // add a board in the db by using the normal method
        dbcont.likePhotos(userID,likedPhotoID);


        // if the user is in the cache then update his boards also !!
        try {
            if (service.get(UserLiveObject.class, userID.toString()) != null) {

                //same as the above
                UserLiveObject user = service.get(UserLiveObject.class, userID.toString());
                //get the boards
                Set likesPhotos = user.getUserLikedPhotos();
                //add a board to it
                likesPhotos.add(likedPhotoID);


            }
        }catch (Exception e){
            e.printStackTrace();
        }




    }
    public void UnlikePhotos(UUID userID, UUID unlikedPhotoID ){

        // add a board in the db by using the normal method
        dbcont.unlikePhotos(userID,unlikedPhotoID);


        // if the user is in the cache then update his boards also !!
        try {
            if (service.get(UserLiveObject.class, userID.toString()) != null) {

                //same as the above
                UserLiveObject user = service.get(UserLiveObject.class, userID.toString());
                //get the boards
                Set unlikedPhotos = user.getUserLikedPhotos();
                //add a board to it
                unlikedPhotos.remove(unlikedPhotoID);


            }
        }catch (Exception e){
            e.printStackTrace();
        }




    }
    public void dislikePhotos(UUID userID, UUID dislikedPhotoID ){

        // add a board in the db by using the normal method
        dbcont.likePhotos(userID,dislikedPhotoID);


        // if the user is in the cache then update his boards also !!
        try {
            if (service.get(UserLiveObject.class, userID.toString()) != null) {

                //same as the above
                UserLiveObject user = service.get(UserLiveObject.class, userID.toString());
                //get the boards
                Set dislikedPhotos = user.getUserDislikedPhotos();
                //add a board to it
                dislikedPhotos.add(dislikedPhotoID);


            }
        }catch (Exception e){
            e.printStackTrace();
        }




    }
    public void UndislikePhotos(UUID userID, UUID UndislikedPhotoID ){

        // add a board in the db by using the normal method
        dbcont.undislikePhotos(userID,UndislikedPhotoID);


        // if the user is in the cache then update his boards also !!
        try {
            if (service.get(UserLiveObject.class, userID.toString()) != null) {

                //same as the above
                UserLiveObject user = service.get(UserLiveObject.class, userID.toString());
                //get the boards
                Set undislikedPhotos = user.getUserDislikedPhotos();
                //add a board to it
                undislikedPhotos.remove(UndislikedPhotoID);


            }
        }catch (Exception e){
            e.printStackTrace();
        }




    }
    public void blockUser(UUID userID, UUID blocked ){

        // add a board in the db by using the normal method
        dbcont.blockUser(userID,blocked);


        // if the user is in the cache then update his boards also !!
        try {
            if (service.get(UserLiveObject.class, userID.toString()) != null) {

                //same as the above
                UserLiveObject user = service.get(UserLiveObject.class, userID.toString());
                //get the boards
                Set blockedUsers = user.getBlock();
                //add a board to it
                blockedUsers.add(blocked);


            }
        }catch (Exception e){
            e.printStackTrace();
        }




    }

    public void UnblockUser(UUID userID, UUID unBlocked ){

        // add a board in the db by using the normal method
        dbcont.blockUser(userID,unBlocked);


        // if the user is in the cache then update his boards also !!
        try {
            if (service.get(UserLiveObject.class, userID.toString()) != null) {

                //same as the above
                UserLiveObject user = service.get(UserLiveObject.class, userID.toString());
                //get the boards
                Set blockedUsers = user.getBlock();
                //add a board to it
                blockedUsers.remove(unBlocked);


            }
        }catch (Exception e){
            e.printStackTrace();
        }




    }
    public void followUser(UUID userID, UUID followingId ){

        // add a board in the db by using the normal method
        dbcont.followUser(userID,followingId);


        // if the user is in the cache then update his boards also !!
        try {
            if (service.get(UserLiveObject.class, userID.toString()) != null) {

                //same as the above
                UserLiveObject user = service.get(UserLiveObject.class, userID.toString());
                //get the boards
                Set followingUsers = user.getFollowing();
                //add a board to it
                followingUsers.add(followingId);


            }
        }catch (Exception e){
            e.printStackTrace();
        }




    }
    public void unfollowUser(UUID userID, UUID followingId ){

        // add a board in the db by using the normal method
        dbcont.unfollowUser(userID,followingId);


        // if the user is in the cache then update his boards also !!
        try {
            if (service.get(UserLiveObject.class, userID.toString()) != null) {

                //same as the above
                UserLiveObject user = service.get(UserLiveObject.class, userID.toString());
                //get the boards
                Set unfollowingUsers = user.getFollowing();
                //add a board to it
                unfollowingUsers.remove(followingId);


            }
        }catch (Exception e){
            e.printStackTrace();
        }




    }

    public UserLiveObject signIn (String email, String password ) {


        try {

        RBucket<String> bucket = redisConf.getClient().getBucket(email);
        if (bucket.get() != null) {

            if (service.get(UserLiveObject.class, bucket.get()) != null) {
                UserLiveObject userLiveObject = service.get(UserLiveObject.class, bucket.get());
                if (email.equals(userLiveObject.getEmail()) &&
                        password.equals(userLiveObject.getPassword())) {

                    System.out.println("hereeeeeeeee");



                    return userLiveObject;
                }
            } else {
                User user = dbcont.signIn(email, password);
                if (user == null)
                    return null;

                String message = new Gson().toJson(user);
                Gson gson = new GsonBuilder().create();
                UserLiveObject userLive = gson.fromJson(message, UserLiveObject.class);
                userLive = service.persist(userLive);

            }

        } else {

            User user = dbcont.signIn(email, password);

            if (user == null)
                return null;

            if (service.get(UserLiveObject.class, user.getId().toString()) != null) {

                RBucket<String> bucket2 = redisConf.getClient().getBucket(email);
                bucket2.set(user.getId().toString());

                String message = new Gson().toJson(user);
                Gson gson = new GsonBuilder().create();
                UserLiveObject userLive = gson.fromJson(message, UserLiveObject.class);

                return userLive;
            } else {

                String message = new Gson().toJson(user);
                System.out.println(message);
                Gson gson = new GsonBuilder().create();
                UserLiveObject userLive = gson.fromJson(message, UserLiveObject.class);
                userLive = service.persist(userLive);
                System.out.println(userLive.getId() + "elsaya");

                RBucket<String> bucket2 = redisConf.getClient().getBucket(email);
                bucket2.set(user.getId().toString());
                return userLive;

            }

        }
    }catch (Exception e){
        e.printStackTrace();
    }
        return null;
    }










   /* public UserLiveObject getUserByEmail (String email ){

        RBucket<String> bucket = redisConf.getClient().getBucket(email);

        boolean emailIdInRedis = bucket.get()!=null;
        boolean objectInRedis = service.get(UserLiveObject.class, userId.toString())!=null;



        //check if the email - id pair are in redis
        if(emailIdInRedis){

            //get the id from the email -id pair
            String userId = bucket.get();

            //check if the user is in redis
            if(service.get(UserLiveObject.class, userId.toString())!=null) {
                return service.get(UserLiveObject.class, userId.toString());
            }
            //not in redis
            //get him and puut him in redis
            User user = dbcont.getUserByEmail(email);
            String message = new Gson().toJson(user);
            Gson gson = new GsonBuilder().create();
            UserLiveObject userLive = gson.fromJson(message, UserLiveObject.class);
            userLive = service.persist(userLive);
            return userLive;


        }

        // you didnt find the email - id pair then
        //add the user to redis
        //add the email - id pair to redis
        //easy

        User user = dbcont.getUserByEmail(email);
        String message = new Gson().toJson(user);
        Gson gson = new GsonBuilder().create();
        UserLiveObject userLive = gson.fromJson(message, UserLiveObject.class);
        userLive = service.persist(userLive);
        bucket.set(userLive.getId());
        return userLive;

    }

*/




        // gets the user with all collections (Sets)

        public UserLiveObject getUserWithProfileById(UUID userId)
    {

        if(service.get(UserLiveObject.class, userId.toString())!=null) {
            return service.get(UserLiveObject.class, userId.toString());
        }

        User user = dbcont.getUserWithProfileById(userId);
        String message = new Gson().toJson(user);
        System.out.println("not in cache");
        Gson gson = new GsonBuilder().create();
        UserLiveObject userLive = gson.fromJson(message, UserLiveObject.class);

        userLive = service.persist(userLive);

        return userLive;


    }

}
