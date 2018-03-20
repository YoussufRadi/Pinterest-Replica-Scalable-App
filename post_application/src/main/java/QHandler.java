import Arango.ArangoInstance;
import Arango.CategoryDBObject;
import Arango.PostDBObject;
//import RedisConf;
import LiveObjects.CategoryLiveObject;
import LiveObjects.PostLiveObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.redisson.api.RLiveObjectService;
//import org.redisson.api.RLiveObjectService;

import java.io.IOException;
;
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



    ////Posts ///////////////////////////////////////

    public PostDBObject insert_post(PostDBObject postDBObject) {
        arangoInstance.insertNewPost(postDBObject);
        return postDBObject;
    }



    public PostLiveObject getPost(String post_id){
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
                //System.out.println("sda:" + postLiveObject);
                //return postLiveObject;
            }
        }
        return postLiveObject;

    }
    public void deletePost(String post_id){

        arangoInstance.deletePost(post_id);
        PostLiveObject postLiveObject = liveObjectService.get(PostLiveObject.class,post_id);
        if(postLiveObject!=null){
            liveObjectService.delete(PostLiveObject.class,post_id);
        }
    }


    public PostDBObject update_post(String post_id,PostDBObject postDBObject){
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





    ///////////Categories///////////////////////////////////////////


    public CategoryDBObject insert_category(CategoryDBObject categoryDBObject){
          arangoInstance.insertNewCategory(categoryDBObject);
          return categoryDBObject;
    }

    public CategoryLiveObject getCategory(String category_id ){
        CategoryLiveObject categoryLiveObject = liveObjectService.get(CategoryLiveObject.class,category_id);
        System.out.println(categoryLiveObject);
        if(categoryLiveObject==null){
            CategoryDBObject categoryDBObject= arangoInstance.getCategory(category_id);
            if(categoryDBObject!=null) {
                String message = new Gson().toJson(categoryDBObject);
                Gson gson = new GsonBuilder().create();
                categoryLiveObject = gson.fromJson(message, CategoryLiveObject.class);
                categoryLiveObject = liveObjectService.merge(categoryLiveObject);
                //return categoryLiveObject;
            }
        }
        return categoryLiveObject;
    }

    public void deleteCategory(String category_id){
        arangoInstance.deleteCategory(category_id);
        CategoryLiveObject categoryLiveObject = liveObjectService.get(CategoryLiveObject.class,category_id);
        if(categoryLiveObject!=null){
            liveObjectService.delete(CategoryLiveObject.class,category_id);
        }
    }

    public CategoryDBObject updateCategoty(String category_id,CategoryDBObject categoryDBObject){
        arangoInstance.updateCategory(category_id,categoryDBObject);
        CategoryLiveObject categoryLiveObject = liveObjectService.get(CategoryLiveObject.class,category_id);
        if(categoryLiveObject != null){
            categoryLiveObject.setTitle(categoryDBObject.getTitle());
            categoryLiveObject.setPosts_id(categoryDBObject.getPosts_id());
        }
        return arangoInstance.getCategory(category_id);
    }



}