package PostCommands;

import Database.ArangoInstance;
import Interface.ConcreteCommand;

public class DeleteComment extends ConcreteCommand {

    @Override
    protected void doCommand() {
        deleteComment(message.getComment_id(), ArangoInstance);
    }

    private void deleteComment(String comment_id, ArangoInstance arangoInstance){

        arangoInstance.deleteComment(comment_id);
    }
}
