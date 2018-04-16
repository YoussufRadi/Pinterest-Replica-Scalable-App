package postCommands;

import Interface.ConcreteCommand;

public class InsertComment extends ConcreteCommand {

    @Override
    protected void doCommand() {
        ArangoInstance.insertNewComment(message.getComment_object(),message.getPost_id());
        String category = gson.toJson(message.getComment_object());
        responseJson = jsonParser.parse(category);
    }
}
