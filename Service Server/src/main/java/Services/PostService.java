package Services;

import Interface.Command;
import Cache.RedisConf;
import Database.ArangoInstance;
import Interface.ControlService;
import com.rabbitmq.client.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.redisson.api.RLiveObjectService;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeoutException;

public class PostService extends ControlService {


    private static RedisConf redisConf;
    private static String dbUserName;
    private static String dbPass;

    public PostService(String host, int port, int threadsNo, int maxDBConnections) {
        super(host,port,threadsNo, maxDBConnections, "User");
    }

    public void setMaxDBConnections(int connections){
        this.maxDBConnections = connections;
        arangoInstance.setMaxDBConnections(maxDBConnections);
    }


    @Override
    public void init() {
        arangoInstance = new ArangoInstance(dbUserName,dbPass,maxDBConnections);
        try {
            redisConf  = new RedisConf();
        } catch (IOException e) {
            e.printStackTrace();
        }
        liveObjectService = redisConf.getService();
    }



    public static void main(String [] args) {
        dbUserName = "root";
        dbPass = "pass";
        new PostService("localhost",5672,15,15);
    }


}
