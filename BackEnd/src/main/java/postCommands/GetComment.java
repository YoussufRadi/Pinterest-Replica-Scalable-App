package postCommands;

import Database.ArangoInstance;
import Interface.ConcreteCommand;
import Models.CommentDBObject;

public class GetComment extends ConcreteCommand {

    @Override
    protected void doCommand() {
        String comment = gson.toJson(getComment(message.getComment_id(), ArangoInstance));
        if(comment != null)
            responseJson = jsonParser.parse(comment);
    }

    private CommentDBObject getComment(String comment_id , ArangoInstance arangoInstance){
            return arangoInstance.getComment(comment_id);
    }

}
