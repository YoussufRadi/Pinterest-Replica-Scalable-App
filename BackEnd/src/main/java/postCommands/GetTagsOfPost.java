package postCommands;

import Interface.ConcreteCommand;


public class GetTagsOfPost extends ConcreteCommand {

    @Override
    protected void doCommand() {
        String Tags = gson.toJson(ArangoInstance.getTagsOfPostLimit(message.getSkip(),message.getLimit(),message.getPost_id()));
        responseJson = jsonParser.parse(Tags);
    }

}
