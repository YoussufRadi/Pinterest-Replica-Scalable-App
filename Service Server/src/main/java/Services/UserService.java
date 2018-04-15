package Services;


import java.io.IOException;
import Cache.UserCacheController;
import Interface.ControlService;

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


    public static void main(String[] argv) {
        new UserService("localhost",5672,15,15);
    }

}



