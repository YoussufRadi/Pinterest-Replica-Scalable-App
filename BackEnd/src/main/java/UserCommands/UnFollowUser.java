package UserCommands;

import Interface.ConcreteCommand;
import java.util.UUID;

public class UnFollowUser extends ConcreteCommand {

    @Override
    protected void doCommand() {
        boolean respBool =
                UserCacheController.unfollowUser(message.getPayload().getId(), UUID.fromString(message.getOtherUserId()));

        String response = respBool + "";
        responseJson = jsonParser.parse(response);
        System.out.println(response);
    }
}
