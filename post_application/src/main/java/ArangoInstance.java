
import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import com.arangodb.entity.DocumentCreateEntity;

import java.util.ArrayList;

public class ArangoInstance {


    public ArangoDB arangoDB;


    public ArangoInstance(String user, String password){
        String rootPath = "src/main/resources/";
        arangoDB = new ArangoDB.Builder().user(user).password(password).build();

    }


    public void initializeDB(){

        try{

            String dbName = "Post";
            arangoDB.createDatabase("Post");
            arangoDB.db(dbName).createCollection("posts");
            arangoDB.db(dbName).createCollection("comments");
            arangoDB.db(dbName).createCollection("categories");
            System.out.println("Database created: " + dbName);
        } catch (ArangoDBException e) {
            System.err.println("Failed to create database: Post");
        }
    }

    public void dropDB(){

        try{
            arangoDB.db("Post").drop();
            System.out.println("Database dropped: Post");
        } catch (ArangoDBException e) {
            System.err.println("Failed to drop database: Post");
        }
    }

    public void insertNewPost(PostDBObject postDBObject){
        arangoDB.db("Post").collection("posts").insertDocument(postDBObject);

    }

    public PostDBObject getPost(String id){
        PostDBObject post =arangoDB.db("Post").collection("posts").getDocument(id, PostDBObject.class);
        return post;
    }

    public ArrayList<PostDBObject> getPostsLimit(int skip,int limit){
        ArangoCursor<PostDBObject> cursor =arangoDB.db("Post").query("For post In posts Sort post.created_at Limit" +
                        " "+skip+", "+limit+" Return post",
                null,null,PostDBObject.class);
        return new ArrayList<PostDBObject>(cursor.asListRemaining());
    }

    public void updatePost(String id,PostDBObject post){
        arangoDB.db("Post").collection("posts").updateDocument(id,post);
    }

    public void deletePost(String id){
        arangoDB.db("Post").collection("posts").deleteDocument(id);
    }

    public void likePost(String user_id,String post_id){
        PostDBObject post = getPost(post_id);
        post.getLikes_id().add(user_id);
        updatePost(post_id,post);
    }

    public void dislikePost(String user_id,String post_id){
        PostDBObject post = getPost(post_id);
        post.getDislikes_id().add(user_id);
        updatePost(post_id,post);
    }

    public static void main(String[] args){
        ArangoInstance arango = new ArangoInstance("root","pass");
        //arango.initializeDB();
//        arango.dropDB();

        //PostDBObject post = new PostDBObject("1",null,null,"2");
        //System.out.println(arango.insertNewPost(post));

//        arango.dislikePost("3","16256");

//        PostDBObject post =arango.getPost("757");

//        System.out.println(post);

    //    ArrayList<PostDBObject> a =arango.getPostsLimit(0,2);
    //    System.out.println(a);
    }


}
