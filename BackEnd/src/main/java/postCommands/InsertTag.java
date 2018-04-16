package postCommands;

import Interface.ConcreteCommand;


public class InsertTag extends ConcreteCommand {

    @Override
    protected void doCommand() {
        String tag = gson.toJson(message.getTag_object());
        ArangoInstance.insertNewTag(message.getTag_object());
        responseJson = jsonParser.parse(tag);
    }
}