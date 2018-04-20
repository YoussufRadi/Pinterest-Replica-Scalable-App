package Services;


import Cache.UserCacheController;
import ClientService.Client;
import Interface.ControlService;
import Models.ErrorLog;
import io.netty.handler.logging.LogLevel;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class UserService extends ControlService {

    @Override
    public void init() {
        RPC_QUEUE_NAME = conf.getMqInstanceUserQueue();
        try {
            userCacheController = new UserCacheController();
        } catch (IOException e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            Client.channel.writeAndFlush(new ErrorLog(LogLevel.ERROR, errors.toString()));
            e.printStackTrace();
        }
    }

    @Override
    public void setDBConnections(int connections) {
        // TODO @soudian
    }


//    public static void main(String[] argv) {
//        new UserService();
//    }

}



