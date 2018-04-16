package UserCommands;

import Interface.ConcreteCommand;

import java.util.UUID;

public class AddBoard extends ConcreteCommand {

    @Override
    protected void doCommand() {
        boolean respBool =
                UserCacheController.addBoard(message.getPayload().getId(), UUID.fromString(message.getBoardId()));

        String response = respBool + "";

        responseJson = jsonParser.parse(response);
        System.out.println(response +"Responsee");
    }
}
