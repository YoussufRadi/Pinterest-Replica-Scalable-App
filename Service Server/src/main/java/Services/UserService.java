package Services;


import Cache.UserCacheController;
import Interface.ControlService;

import java.io.IOException;

public class UserService extends ControlService {


    public UserService(String host, int port, int threadsNo, int maxDBConnections) {
        super(host,port,threadsNo, maxDBConnections, "User");
    }

    @Override
    public void init() {
        try {
            userCacheController = new UserCacheController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setMaxDBConnections(int connections) {
        // TODO @soudian
    }


    public static void main(String[] argv) {
        new UserService("localhost",5672,15,15);
    }

}



