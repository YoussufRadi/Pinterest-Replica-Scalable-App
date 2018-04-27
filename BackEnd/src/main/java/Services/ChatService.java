package Services;

import Database.ChatArangoInstance;
import Interface.ControlService;

public class ChatService extends ControlService {

    @Override
    public void init() {
        RPC_QUEUE_NAME = conf.getMqInstanceChatQueue();
        ChatArangoInstance = new ChatArangoInstance(maxDBConnections);
    }

    @Override
    public void setDBConnections(int connections){
        this.maxDBConnections = connections;
        ChatArangoInstance.setMaxDBConnections(maxDBConnections);
    }

}
