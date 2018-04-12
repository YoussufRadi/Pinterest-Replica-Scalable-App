package Cache;

import org.redisson.Redisson;
import org.redisson.api.RLiveObjectService;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.io.File;
import java.io.IOException;

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
                ("/home/souidan/Desktop/PintrestUser/user_application/src/main/java/msa/Cache/singleNodeConfig.json"));
        service = client.getLiveObjectService();

    }


}
