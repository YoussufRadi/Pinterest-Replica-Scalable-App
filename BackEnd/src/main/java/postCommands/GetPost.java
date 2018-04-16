package postCommands;

import Database.ArangoInstance;
import Interface.ConcreteCommand;
import Models.PostDBObject;
import Models.PostLiveObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.redisson.api.RLiveObjectService;


public class GetPost extends ConcreteCommand {

    @Override
    protected void doCommand() {
        String post = gson.toJson(getPost(message.getPost_id(), ArangoInstance, RLiveObjectService));
        if(post!=null)
            responseJson = jsonParser.parse(post);
    }

    private PostLiveObject getPost(String post_id, ArangoInstance arangoInstance, RLiveObjectService liveObjectService){
        PostLiveObject postLiveObject = liveObjectService.get(PostLiveObject.class,post_id);
        System.out.println(postLiveObject);
        if(postLiveObject==null){
            PostDBObject postDBObject= arangoInstance.getPost(post_id);
            if(postDBObject!=null) {
                String message = new Gson().toJson(postDBObject);
                Gson gson = new GsonBuilder().create();
                System.out.println(message);
                postLiveObject = gson.fromJson(message, PostLiveObject.class);
                postLiveObject = liveObjectService.merge(postLiveObject);

            }
        }
        return postLiveObject;

    }

}
