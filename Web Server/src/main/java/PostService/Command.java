package PostService;

import org.redisson.api.RLiveObjectService;

import java.io.IOException;
import java.util.HashMap;

public abstract class Command implements Runnable {

    protected HashMap<String, Object> data;
    protected ArangoInstance arangoInstance;
    protected RedisConf redisConf;
    protected RLiveObjectService liveObjectService;

    final public void init(HashMap<String, Object> parameters,ArangoInstance arangoInstance) throws IOException {
        this.data = parameters;
        redisConf = new RedisConf();
        liveObjectService = redisConf.getService();
        this.arangoInstance = arangoInstance;
    }


    protected abstract void execute();

    final public void run() {
        this.execute();
    }

}
