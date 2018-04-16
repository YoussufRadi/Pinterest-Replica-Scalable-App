package UserCommands;

import Interface.ConcreteCommand;

import java.util.UUID;

public class RemovePin extends ConcreteCommand {

    @Override
    protected void doCommand() {

        boolean respBool =
                UserCacheController.removePin(message.getPayload().getId(), UUID.fromString(message.getPinId()));

        String response = respBool + "";
        responseJson = jsonParser.parse(response);
        System.out.println(response);
    }
}
