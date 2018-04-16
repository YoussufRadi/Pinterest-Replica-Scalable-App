package postCommands;

import Interface.ConcreteCommand;


public class InsertCategory extends ConcreteCommand {

    @Override
    protected void doCommand() {
        ArangoInstance.insertNewCategory(message.getCategory_object());
        String category = gson.toJson(message.getCategory_object());
        responseJson = jsonParser.parse(category);
    }
}