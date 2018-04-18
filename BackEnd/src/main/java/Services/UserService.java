package Services;


import Cache.UserCacheController;
import Interface.ControlService;

import java.io.IOException;

public class UserService extends ControlService {

    @Override
    public void init() {
        RPC_QUEUE_NAME = conf.getServiceUserQueue();
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
        new UserService();
    }

}



