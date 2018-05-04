package UserCommands;

import Interface.ConcreteCommand;

import java.util.UUID;

public class FollowUser extends ConcreteCommand {

    @Override
    protected void doCommand() {
        boolean respBool =
                UserCacheController.followUser(message.getPayload().getId(), UUID.fromString(message.getOtherUserId()));

        String response = respBool + "";
        if(respBool){

        }
        responseJson = jsonParser.parse(response);
        System.out.println(response);
    }
}
