package ChatCommands;

import Interface.ConcreteCommand;


public class GetServer extends ConcreteCommand{

    @Override
    protected void doCommand() {
        String res = gson.toJson(ChatArangoInstance.getServer());
        responseJson = jsonParser.parse(res);
    }
}