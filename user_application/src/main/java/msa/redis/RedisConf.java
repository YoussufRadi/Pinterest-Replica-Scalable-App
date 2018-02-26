package msa.redis;

import msa.pojo.User;
import msa.postgresql.DatabaseController;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RLiveObjectService;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class RedisConf {

    private RedissonClient client;
    public RedissonClient getClient() {
        return client;
    }

    public void setClient(RedissonClient client) {
        this.client = client;
    }


    public RedisConf() throws IOException {
       client = Redisson.create();
        Config config = Config.fromJSON(new File("/home/souidan/Desktop/PintrestUser/user_application/src/main/java/msa/redis/singleNodeConfig.json"));


    }

    public static void main(String[] argv) throws IOException {

        RedisConf conf = new RedisConf();
        DatabaseController cont = new DatabaseController();
        RLiveObjectService service = conf.client.getLiveObjectService();

        User u = new User("AhmedRedis",
                "ZaherRedis",
                "ahmedzher@gmail.com",
                "password",false
        );

        UUID id = cont.addUser(u);

        RBucket<User> bucket = conf.client.getBucket("user");
        bucket.set(u);
        System.out.println(bucket.get().getId());
    }
}
