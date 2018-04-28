package PostCommands;

import Interface.ConcreteCommand;
import org.json.JSONObject;


public class InsertCategory extends ConcreteCommand {

    @Override
    protected void doCommand() {
        String id = ArangoInstance.insertNewCategory(message.getCategory_object());
        JSONObject response = new JSONObject();
        response.put("id",id);
        responseJson = jsonParser.parse(response.toString());
    }
}