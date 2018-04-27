package ChatCommands;

import Interface.ConcreteCommand;


public class AddServer extends ConcreteCommand {

    @Override
    protected void doCommand() {
        ChatArangoInstance.addServer(message.getServer_object());
        responseJson = jsonParser.parse("response");
    }
}