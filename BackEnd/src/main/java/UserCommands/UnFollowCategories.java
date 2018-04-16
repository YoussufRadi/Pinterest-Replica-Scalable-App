package UserCommands;

import Interface.ConcreteCommand;

import java.util.UUID;

public class UnFollowCategories extends ConcreteCommand {

    @Override
    protected void doCommand() {
        boolean respBool =
                UserCacheController.unfollowCategories(message.getPayload().getId(), UUID.fromString(message.getCategoryId()));

        String response = respBool + "";
        responseJson = jsonParser.parse(response);
        System.out.println(response);
    }
}
