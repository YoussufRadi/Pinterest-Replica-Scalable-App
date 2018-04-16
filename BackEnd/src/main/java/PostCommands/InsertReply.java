package PostCommands;

import Interface.ConcreteCommand;

public class InsertReply extends ConcreteCommand {

    @Override
    protected void doCommand() {
        String comment = gson.toJson(message.getComment_object());
        ArangoInstance.insertNewReply(message.getComment_object(),message.getComment_id());
        responseJson = jsonParser.parse(comment);
    }
}
