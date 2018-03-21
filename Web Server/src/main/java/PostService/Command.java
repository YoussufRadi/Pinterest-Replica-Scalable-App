package PostService;

import org.redisson.api.RLiveObjectService;

import java.io.IOException;
import java.util.HashMap;

public abstract class Command implements Runnable {

    protected HashMap<String, Object> data;
    protected ArangoInstance arangoInstance;
    protected RedisConf redisConf;
    protected RLiveObjectService liveObjectService;

    final public void init(HashMap<String, Object> parameters) throws IOException {
        this.data = parameters;
        arangoInstance = new ArangoInstance("root","pass");
        redisConf = new RedisConf();
        liveObjectService = redisConf.getService();
    }

    protected abstract void execute();

    final public void run() {
        this.execute();
    }

}
