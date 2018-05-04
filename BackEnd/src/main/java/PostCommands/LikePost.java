package PostCommands;

import Database.ArangoInstance;
import Interface.ConcreteCommand;
import Models.NotificationDBObject;
import Models.PostDBObject;
import Models.PostLiveObject;
import org.redisson.api.RLiveObjectService;


public class LikePost extends ConcreteCommand {

    @Override
    protected void doCommand() {
        String post = gson.toJson(likePost(message.getPost_id(),message.getUser_id(), ArangoInstance, RLiveObjectService));
        responseJson = jsonParser.parse(post);
    }

    private PostDBObject likePost(String post_id, String user_id, ArangoInstance arangoInstance, RLiveObjectService liveObjectService){
        arangoInstance.likePost(user_id,post_id);
        PostLiveObject postLiveObject = liveObjectService.get(PostLiveObject.class,post_id);
        if (postLiveObject!= null){
            postLiveObject.setLikes_id(arangoInstance.getPost(post_id).getLikes_id());
            postLiveObject.setDislikes_id(arangoInstance.getPost(post_id).getDislikes_id());
        }
        PostDBObject postDBObject = arangoInstance.getPost(post_id);
        String user_temp = UserCacheController.getUserNameByID(user_id);
        NotificationDBObject notificationDBObject = new NotificationDBObject(postDBObject.getUser_id(),user_temp,user_temp+" likes your post");
        return arangoInstance.getPost(post_id);
    }
}