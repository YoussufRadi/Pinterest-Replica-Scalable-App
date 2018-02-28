import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
//import org.redisson.api.RLiveObjectService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class QHandler {

    private ArangoInstance arangoInstance;
    //private RedisConf redisConf;
    //private RLiveObjectService service;


    public QHandler() throws IOException {
       arangoInstance = new ArangoInstance("root","pass");


    }

    public void addPost(String user_id, ArrayList<String> categories_id, ArrayList<String> tags_id, String image_id) {
        PostDBObject postDBObject = new PostDBObject(user_id,categories_id,tags_id,image_id);
        arangoInstance.insertNewPost(postDBObject);
    }

    public PostDBObject getPost(String post_id){
        return arangoInstance.getPost(post_id);
    }
}