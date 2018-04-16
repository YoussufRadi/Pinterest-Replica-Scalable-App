package UserCommands;

import Interface.ConcreteCommand;

import java.util.UUID;

public class RemoveBoard extends ConcreteCommand {

    @Override
    protected void doCommand() {
        boolean respBool =
                UserCacheController.removeBoard(message.getPayload().getId(), UUID.fromString(message.getBoardId()));


        String response = respBool + "";
        responseJson = jsonParser.parse(response);
        System.out.println(response);
    }
}
