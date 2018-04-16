package PostCommands;


import Database.ArangoInstance;
import Interface.ConcreteCommand;
import Models.PostDBObject;
import Models.PostLiveObject;
import org.json.JSONObject;
import org.redisson.api.RLiveObjectService;


public class UpdatePost extends ConcreteCommand {

    @Override
    protected void doCommand() {
        String s  = "";
        if(message.getPost_id() == null){
            s+= "post_id is missing";
        }
        if(message.getPost_object()==null){
            s+= "post_object is missing";
        }
        if(s.isEmpty()) {
            String post = gson.toJson(update_post(message.getPost_id(), message.getPost_object(), ArangoInstance, RLiveObjectService));
            responseJson = jsonParser.parse(post);
        }else {
            System.out.println(s);
            JSONObject err = new JSONObject();
            err.put("error",s);
            responseJson = jsonParser.parse(err.toString());
        }
    }

    private PostDBObject update_post(String post_id, PostDBObject postDBObject, ArangoInstance arangoInstance, RLiveObjectService liveObjectService){
        arangoInstance.updatePost(post_id,postDBObject);
        PostLiveObject postLiveObject = liveObjectService.get(PostLiveObject.class,post_id);
        if (postLiveObject!= null){
            postLiveObject.setCategories_id(postDBObject.getCategories_id());
            postLiveObject.setComments_id(postDBObject.getComments_id());
            postLiveObject.setDislikes_id(postDBObject.getDislikes_id());
            postLiveObject.setImage_id(postDBObject.getImage_id());
            postLiveObject.setUser_id(postDBObject.getUser_id());
            postLiveObject.setTags_id(postDBObject.getTags_id());
        }
        return arangoInstance.getPost(post_id);
    }
}