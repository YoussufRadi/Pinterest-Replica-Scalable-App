package postCommands;


import Database.ArangoInstance;
import Interface.ConcreteCommand;
import Models.PostLiveObject;
import org.redisson.api.RLiveObjectService;


public class DeletePost extends ConcreteCommand {

    @Override
    protected void doCommand() {
        deletePost(message.getPost_id(), ArangoInstance, RLiveObjectService);
    }

    private void deletePost(String post_id, ArangoInstance arangoInstance, RLiveObjectService liveObjectService){

        arangoInstance.deletePost(post_id);
        PostLiveObject postLiveObject = liveObjectService.get(PostLiveObject.class,post_id);
        if(postLiveObject!=null){
            liveObjectService.delete(PostLiveObject.class,post_id);
        }
    }
}