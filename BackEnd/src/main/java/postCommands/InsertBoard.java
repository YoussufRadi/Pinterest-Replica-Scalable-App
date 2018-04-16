package postCommands;

import Interface.ConcreteCommand;

public class InsertBoard extends ConcreteCommand {

    @Override
    protected void doCommand() {
        System.out.println(message.getBoard_object());
        ArangoInstance.insertNewBoard(message.getBoard_object());
        System.out.println(message.getBoard_object());
        String board = gson.toJson(message.getBoard_object());
        responseJson = jsonParser.parse(board);
    }
}
