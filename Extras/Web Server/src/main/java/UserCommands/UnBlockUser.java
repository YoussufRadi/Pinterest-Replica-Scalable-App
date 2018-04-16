package UserCommands;

import Interface.ConcreteCommand;

import java.util.UUID;

public class UnBlockUser extends ConcreteCommand {

    @Override
    protected void doCommand() {
        boolean respBool =
                UserCacheController.UnblockUser(message.getPayload().getId(), UUID.fromString(message.getOtherUserId()));

        String response = respBool + "";
        responseJson = jsonParser.parse(response);
        System.out.println(response);
    }
}
