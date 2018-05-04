package PostCommands;

import Interface.ConcreteCommand;

public class getUserNotifications extends ConcreteCommand {
    @Override
    protected void doCommand() {
        System.out.println(message.getUser_id());
        String notifications = gson.toJson(ArangoInstance.getUserNotifications(message.getUser_id()));
        responseJson = jsonParser.parse(notifications);
    }
}
