package msa.messagequeue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import msa.pojo.User;
import msa.pojo.UserLiveObject;
import msa.postgresql.DatabaseController;
import msa.redis.RedisConf;
import org.redisson.api.RLiveObjectService;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class QHandler {

    private DatabaseController dbcont ;
    private RedisConf redisConf;
    private RLiveObjectService service ;




    public QHandler() throws IOException {
       dbcont = new DatabaseController();
       redisConf = new RedisConf();
       service = redisConf.getService();

    }

    public UUID addUser(String fname, String lname,
                        String username, String email,String password,
                        boolean gender, int age) {
        UUID id = dbcont.addUser(fname,lname,username,email,password,gender,age);

        UserLiveObject userLive = new UserLiveObject(id.toString(),
                fname,lname,username,email,password,gender,age);
        userLive = service.persist(userLive);
        return id;


    }

    public void updateUser(UUID userID,
                           String firstName, String lastname, String password,
                           String username, int age, boolean gender ) {
        dbcont.updateUser(userID, firstName, lastname, password, username, age, gender);

        if (service.get(UserLiveObject.class, userID.toString()) != null) {

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

        dbcont.addBoard(userID,boardID);

            if (service.get(UserLiveObject.class, userID.toString()) != null) {

                UserLiveObject user = service.get(UserLiveObject.class, userID.toString());

                Set boards = user.getBoards();
                boards.add(boardID);

                user.setBoards(boards);


            }


    }







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
