package PostCommands;

import Interface.ConcreteCommand;
import Models.PostDBObject;
import org.json.JSONObject;


public class InsertPost extends ConcreteCommand {

    @Override
    protected void doCommand() {
        System.out.println("Inside insert: "+message.getPost_object());
        PostDBObject postDBObject = message.getPost_object();
        if(message.getImageUrl()!=null)
            postDBObject.setImage_id(message.getImageUrl());
        String id = ArangoInstance.insertNewPost(postDBObject);
        JSONObject response = new JSONObject();
        response.put("id",id);
        responseJson = jsonParser.parse(response.toString());
    }
}