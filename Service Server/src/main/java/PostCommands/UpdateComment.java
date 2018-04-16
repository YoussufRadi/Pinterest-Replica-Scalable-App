package PostCommands;

import Database.ArangoInstance;
import Interface.ConcreteCommand;
import Models.CommentDBObject;
import org.json.JSONObject;

public class UpdateComment extends ConcreteCommand {

    @Override
    protected void doCommand() {
        String s  = "";
        if(message.getComment_id() == null){
            s+= "Comment_id is missing";
        }
        if(message.getComment_object()==null){
            s+= "Comment_object is missing";
        }
        if(s.isEmpty()) {
            String comment = gson.toJson(update_comment(message.getComment_id(), message.getComment_object(), ArangoInstance));
            responseJson = jsonParser.parse(comment);
        }else {
            System.out.println(s);
            JSONObject err = new JSONObject();
            err.put("error",s);
            responseJson = jsonParser.parse(err.toString());
        }
    }

    private CommentDBObject update_comment(String comment_id, CommentDBObject commentDBObject, ArangoInstance arangoInstance){
        arangoInstance.updateComment(comment_id,commentDBObject);
        return arangoInstance.getComment(comment_id);
    }
}
