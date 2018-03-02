package msa;

import msa.messagequeue.QHandler;
import msa.pojo.UserLiveObject;
import msa.postgresql.DatabaseController;
import msa.redis.RedisConf;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import javax.jws.soap.SOAPBinding;
import java.io.IOException;
import java.util.UUID;

public class Main {

    public static void main (String[]argv) throws IOException {

        DatabaseController dbcontroller = new DatabaseController();
        RedisConf conf = new RedisConf();
        QHandler q =new QHandler();

        //UserLiveObject userLiveObjec=q.signIn("ahmedsouidan@gmail.com","password");
        //System.out.println(userLiveObjec.getId());
        UUID id = q.addUser("zaher","tarek",
                "jojihiqh","jojo@gmail.com",
                "password",true,22);

        System.out.println(id);
        //UUID uid = UUID.fromString("85c05c80-564a-425e-9a5b-aec519a9f655");
        //UUID uid2 = UUID.fromString("85c05c80-564a-425e-9a5b-aec519a9f778");
        //UUID uid3 = UUID.fromString("882bbd03-915b-4eda-88cd-a7f0dd04a612");

        /*dbcontroller.addUser("Ahmed","Mohamed",
                "souidanUsername","ahmedsouidan@gmail.com","password",

        true,15);*/


        /*UserLiveObject userID3 = new UserLiveObject(uid3.toString(),"ahmed", "souidan",
                "usersouidan","ahmedsouidan@gmail.com","password",
                true,25
                );

        userID3 = conf.getService().persist(userID3);

        UserLiveObject user = conf.getService().get(UserLiveObject.class, uid3.toString());

        UserLiveObject userID4 = new UserLiveObject(uid3.toString(),"khaled", "souidan",
                "userkhaled","ahmedsouidan@gmail.com","password",
                true,25
        );

       userID4 =conf.getService().merge(userID4);

        UserLiveObject user3 = conf.getService().get(UserLiveObject.class, uid3.toString());
*/

        //System.out.println(user.getFirstName());
/*
        RedissonClient redisson = Redisson.create();
        RBucket<String> bucket3 = redisson.getBucket("5");

        RBucket<String> bucket = redisson.getBucket("1");
        System.out.println(bucket3.get());
        RBucket<String> bucket2 = redisson.getBucket("2");

        bucket.set("123");
        bucket.set("456");


        String ledger = bucket.get();

        System.out.println(ledger);
        boolean isUpdated = bucket.compareAndSet("123", "4934");
        String prevObject = bucket.getAndSet("321");
        boolean isSet = bucket.trySet("901");
        long objectSize = bucket.size();*/







    }
}
