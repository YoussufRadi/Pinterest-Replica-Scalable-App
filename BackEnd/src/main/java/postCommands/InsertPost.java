package postCommands;

import Interface.ConcreteCommand;


public class InsertPost extends ConcreteCommand {

    @Override
    protected void doCommand() {
        String post = gson.toJson(message.getPost_object());
//            System.out.println(post);
        ArangoInstance.insertNewPost(message.getPost_object());
        responseJson = jsonParser.parse(post);
    }
}