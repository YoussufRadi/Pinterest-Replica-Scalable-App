package msa;

import msa.messagequeue.QHandler;
import msa.postgresql.DatabaseController;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.io.IOException;
import java.util.UUID;

public class Main {

    public static void main (String[]argv) throws IOException {

       /* DatabaseController dbcontroller = new DatabaseController();
        QHandler q =new QHandler();
        UUID uid = UUID.fromString("85c05c80-564a-425e-9a5b-aec519a9f655");
        UUID uid2 = UUID.fromString("85c05c80-564a-425e-9a5b-aec519a9f778");

       *//* UUID userID1 = q.
                addUser(
                        "mostafaKHALEDEGE","MOE","username",
                        "alison@gmail.com","b8=M3Y",
                        true,25);
        q.updateUser(userID1,"ahmed","khaled",
                "passwrd","username",13,true);*//*

        q.addBoard(UUID.fromString("778cadfa-8033-42f0-b33c-41f260a8f1a7"),uid);*/

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
        long objectSize = bucket.size();







    }
}
