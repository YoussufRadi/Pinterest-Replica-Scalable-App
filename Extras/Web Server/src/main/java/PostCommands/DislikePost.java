package PostCommands;

import Database.ArangoInstance;
import Interface.ConcreteCommand;
import Models.PostDBObject;
import Models.PostLiveObject;
import org.redisson.api.RLiveObjectService;


public class DislikePost extends ConcreteCommand {

    @Override
    protected void doCommand() {
        String post = gson.toJson(dislikePost(message.getPost_id(),message.getUser_id(), ArangoInstance, RLiveObjectService));
        responseJson = jsonParser.parse(post);
    }

    public PostDBObject dislikePost(String post_id, String user_id, ArangoInstance arangoInstance, RLiveObjectService liveObjectService){
        arangoInstance.dislikePost(user_id,post_id);
        PostLiveObject postLiveObject = liveObjectService.get(PostLiveObject.class,post_id);
        if (postLiveObject!= null){
            postLiveObject.setLikes_id(arangoInstance.getPost(post_id).getLikes_id());
            postLiveObject.setDislikes_id(arangoInstance.getPost(post_id).getDislikes_id());
        }
        return arangoInstance.getPost(post_id);
    }
}