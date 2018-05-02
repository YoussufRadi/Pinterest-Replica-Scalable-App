package Services;

import Cache.RedisConf;
import ClientService.Client;
import Database.ArangoInstance;
import Interface.ControlService;
import Models.ErrorLog;
import io.netty.handler.logging.LogLevel;

import java.io.*;

public class PostService extends ControlService {

    private RedisConf redisConf;

    @Override
    public void init() {
        RPC_QUEUE_NAME = conf.getMqInstancePostQueue();
        arangoInstance = new ArangoInstance(maxDBConnections);
        try {
            redisConf  = new RedisConf();
        } catch (IOException e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            Client.channel.writeAndFlush(new ErrorLog(LogLevel.ERROR, errors.toString()));
            e.printStackTrace();
        }
        liveObjectService = redisConf.getService();
    }

    @Override
    public void setDBConnections(int connections){
        this.maxDBConnections = connections;
        arangoInstance.setMaxDBConnections(maxDBConnections);
    }


//    public static void main(String[] args) {
//        new PostService();
//        //postService.add_command("GetKhara","/home/aboelenien/Desktop/GetKhara.txt");
//    }
}
