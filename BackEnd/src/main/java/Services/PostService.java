package Services;

import Cache.RedisConf;
import Database.ArangoInstance;
import Interface.ControlService;

import java.io.IOException;

public class PostService extends ControlService {

    private static RedisConf redisConf;
    private static String dbUserName;
    private static String dbPass;

    public PostService(String host, int port, int threadsNo, int maxDBConnections) {
        super(host,port,threadsNo, maxDBConnections, "post");
    }

    @Override
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
