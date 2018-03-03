import Arango.ArangoInstance;
import Arango.CategoryDBObject;
import Arango.PostDBObject;
import LiveObjects.CategoryLiveObject;
import LiveObjects.PostLiveObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import org.redisson.api.RLiveObjectService;
//import org.redisson.api.RLiveObjectService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;;
import java.util.concurrent.*;


public class QHandler {

    private ArangoInstance arangoInstance;
    private RedisConf redisConf;
    private RLiveObjectService liveObjectService;
    ExecutorService executorService;

    public QHandler() throws IOException {
        arangoInstance = new ArangoInstance("root","pass");
        redisConf = new RedisConf();
        liveObjectService = redisConf.getService();
        executorService = Executors.newFixedThreadPool(15);

    }

    public void addPost(String user_id, ArrayList<String> categories_id, ArrayList<String> tags_id, String image_id) {
        PostDBObject postDBObject = new PostDBObject(user_id,categories_id,tags_id,image_id);
        arangoInstance.insertNewPost(postDBObject);
    }

    public PostLiveObject getPost(String post_id){
        PostLiveObject postLiveObject = liveObjectService.get(PostLiveObject.class,post_id);
        if(postLiveObject==null){
            PostDBObject postDBObject= arangoInstance.getPost(post_id);
            String message = new Gson().toJson(postDBObject);
            Gson gson = new GsonBuilder().create();
            postLiveObject = gson.fromJson(message, PostLiveObject.class);
            postLiveObject = liveObjectService.merge(postLiveObject);
            System.out.println("sda:"+postLiveObject);
            return postLiveObject;
        }
        return postLiveObject;

    }

    public CategoryLiveObject getCategory(String category_id ){
        CategoryLiveObject categoryLiveObject = liveObjectService.get(CategoryLiveObject.class,category_id);
        System.out.println(categoryLiveObject);
        if(categoryLiveObject==null){
            CategoryDBObject categoryDBObject= arangoInstance.getCategory(category_id);
            String message = new Gson().toJson(categoryDBObject);
            Gson gson = new GsonBuilder().create();
            categoryLiveObject = gson.fromJson(message, CategoryLiveObject.class);
            categoryLiveObject = liveObjectService.merge(categoryLiveObject);
            return categoryLiveObject;
        }
        return categoryLiveObject;
    }


}