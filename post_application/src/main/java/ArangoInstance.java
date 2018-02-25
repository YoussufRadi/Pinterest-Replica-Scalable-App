
import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDB;
import com.arangodb.entity.DocumentCreateEntity;
import com.arangodb.ArangoDBException;

import java.util.ArrayList;

public class ArangoInstance {


    public ArangoDB arangoDB;


    public ArangoInstance(String user, String password){
        String rootPath = "src/main/resources/";
//
//        try {
//            InputStream in = new FileInputStream(rootPath + "arango.properties");
//        }catch (FileNotFoundException e){
//            System.err.println("arongo properties file not found "+e.getMessage());
//        }

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
        ArangoCursor<PostDBObject> cursor =arangoDB.db("Post").query("For post In posts Limit "+skip+", "+limit+" Return post",
                null,null,PostDBObject.class);
        return new ArrayList<PostDBObject>(cursor.asListRemaining());
    }

    public static void main(String[] args){
        ArangoInstance arango = new ArangoInstance("root","pass");
//        arango.initializeDB();

//        PostDBObject post = new PostDBObject();
//        post.setUser_id("mama");
//        arango.insertNewPost(post);

//        PostDBObject post =arango.getPost("757");

//        System.out.println(post);

        ArrayList<PostDBObject> a =arango.getPostsLimit(1,2);
        System.out.println(a);
    }


















}
