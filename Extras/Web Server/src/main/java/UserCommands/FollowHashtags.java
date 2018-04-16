package UserCommands;

import Interface.ConcreteCommand;

import java.util.UUID;

public class FollowHashtags extends ConcreteCommand {

    @Override
    protected void doCommand() {
        boolean respBool =
                UserCacheController.followHashtags(message.getPayload().getId(), UUID.fromString(message.getHashtagId()));

        String response = respBool + "";
        responseJson = jsonParser.parse(response);

        System.out.println(response);
    }
}
