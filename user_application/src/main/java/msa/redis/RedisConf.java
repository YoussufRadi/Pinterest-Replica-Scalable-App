package msa.redis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import msa.pojo.User;
import msa.pojo.UserLiveObject;
import msa.postgresql.DatabaseController;
import org.redisson.Redisson;
import org.redisson.api.RLiveObjectService;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class RedisConf {

    private RedissonClient client;



    private  RLiveObjectService service;

    public RedissonClient getClient() {
        return client;
    }


    public RLiveObjectService getService() {
        return service;
    }




    public RedisConf() throws IOException {
       client = Redisson.create();
        Config config = Config.fromJSON(new File
                ("/home/souidan/Desktop/PintrestUser/user_application/src/main/java/msa/redis/singleNodeConfig.json"));
        service = client.getLiveObjectService();

    }


}
