package PostCommands;

import Interface.ConcreteCommand;
import org.json.JSONObject;

public class InsertBoard extends ConcreteCommand {

    @Override
    protected void doCommand() {
        System.out.println(message.getBoard_object());
        String id = ArangoInstance.insertNewBoard(message.getBoard_object());
        JSONObject response = new JSONObject();
        response.put("id",id);
        responseJson = jsonParser.parse(response.toString());
    }
}
