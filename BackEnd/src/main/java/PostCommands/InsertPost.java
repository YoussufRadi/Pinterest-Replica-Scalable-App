package PostCommands;

import Interface.ConcreteCommand;
import org.json.JSONObject;


public class InsertPost extends ConcreteCommand {

    @Override
    protected void doCommand() {
        System.out.println("here");
        String id = ArangoInstance.insertNewPost(message.getPost_object());
        JSONObject response = new JSONObject();
        response.put("id",id);
        responseJson = jsonParser.parse(response.toString());
    }
}