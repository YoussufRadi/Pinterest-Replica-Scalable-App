package msa.messagequeue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import msa.pojo.User;
import msa.pojo.UserLiveObject;
import msa.postgresql.DatabaseController;
import msa.redis.RedisConf;
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

        //make a live object which is explained in the UserLiveObject.class
        UserLiveObject userLive = new UserLiveObject(id.toString(),
                fname,lname,username,email,password,gender,age);
        //save it in redis cache
        userLive = service.persist(userLive);

        //save email - id pair in redis also
        //by creating a bucket which is a class that wraps the string object
        //to store it in redis
        // the email in redisConf.getClient().getBucket(email); is the id of the redis object
        RBucket<String> bucket = redisConf.getClient().getBucket(email);
        // set the value to the user id
        bucket.set(id.toString());
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
    }

        public void addBoard (UUID userID, UUID boardID ){

        // add a board in the db by using the normal method
        dbcont.addBoard(userID,boardID);


        // if the user is in the cache then update his boards also !!
            if (service.get(UserLiveObject.class, userID.toString()) != null) {

                //same as the above
                UserLiveObject user = service.get(UserLiveObject.class, userID.toString());
                //get the boards
                Set boards = user.getBoards();
                //add a board to it
                boards.add(boardID);



            }

            System.out.println(service.get(UserLiveObject.class,
                    userID.toString()).getBoards().toString());


    }


    public UserLiveObject signIn (String email, String password ){

        //sign in in the database
        User user = dbcont.signIn(email,password);

        //convert the user to userliveobject by using GSON
        // we convert the user to a json string using gson
        //then we convert the string back to a live object

        String message = new Gson().toJson(user);
        Gson gson = new GsonBuilder().create();

        UserLiveObject userLive = gson.fromJson(message, UserLiveObject.class);

        //save email -id pair in redis
        RBucket<String> bucket = redisConf.getClient().getBucket(email);
        bucket.set(userLive.toString());

        //save the signed in guy in redis
        userLive = service.persist(userLive);

        return userLive;


    }

    public UserLiveObject getUserByEmail (String email ){

        RBucket<String> bucket = redisConf.getClient().getBucket(email);


        //check if the email - id pair are in redis
        if(bucket.get()!=null){

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
