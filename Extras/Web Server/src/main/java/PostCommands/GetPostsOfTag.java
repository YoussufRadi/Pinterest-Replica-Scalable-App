package PostCommands;

import Interface.ConcreteCommand;


public class GetPostsOfTag extends ConcreteCommand {

    @Override
    protected void doCommand() {
        String posts = gson.toJson(ArangoInstance.getPostsOfTagLimit(message.getSkip(),message.getLimit(),message.getTag_name()));
        responseJson = jsonParser.parse(posts);
    }

}
