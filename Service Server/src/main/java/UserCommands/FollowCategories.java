package UserCommands;

import Interface.ConcreteCommand;

import java.util.UUID;

public class FollowCategories extends ConcreteCommand {

    @Override
    protected void doCommand() {
        boolean respBool =
                UserCacheController.followCategories(message.getPayload().getId(), UUID.fromString(message.getCategoryId()));

        String response = respBool + "";
        responseJson = jsonParser.parse(response);

        System.out.println(response);
    }
}
