import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDB;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ArangoInstance {


    ArangoDB arangoDB;


    public ArangoInstance(String user, String password){
        String rootPath = "src/main/resources/";

        try {
            InputStream in = new FileInputStream(rootPath + "arango.properties");
        }catch (FileNotFoundException e){
            System.err.println("arongo properties file not found "+e.getMessage());
        }

        arangoDB = new ArangoDB.Builder().user(user).password(password).build();
    }


    public void initializeDB(){

        String dbName = "Post";
        arangoDB.createDatabase("Post");
        arangoDB.db(dbName).createCollection("posts");
        arangoDB.db(dbName).createCollection("comments");
        arangoDB.db(dbName).createCollection("categories");
    }

    public void dropDB(){

        arangoDB.db("Post").drop();
    }

    public void insertNewPost(PostDBObject postDBObject){
        arangoDB.db("Post").collection("posts").insertDocument(postDBObject);
    }

    public PostDBObject getPost(String uuid1,String uuid2){
        //type of hashmap is sketchy
        Map<String, Object> bindVars = new HashMap<String,Object>();
//        bindVars.put("uuid1",uuid1);
//        bindVars.put("uuid2",uuid2);
//        String query= "FOR chat IN chats FILTER chat.uuid1 == @uuid1 && chat.uuid2 == @uuid2  RETURN chat";
        ArangoCursor<PostDBObject> cursor = arangoDB.db("Post").query(query,bindVars, null, PostDBObject.class);

        while(cursor.hasNext()) {
            PostDBObject post = cursor.next();
            return post;
        }

        bindVars.clear();
//        bindVars.put("uuid1",uuid2);
//        bindVars.put("uuid2",uuid1);
//        query= "FOR chat IN chats FILTER chat.uuid1 == @uuid1 && chat.uuid2 == @uuid2  RETURN chat";
        cursor = arangoDB.db("Post").query(query,bindVars, null, PostDBObject.class);

        for(; cursor.hasNext-+();) {
            PostDBObject post = cursor.next();
            return post;
        }
        return null;
    }

//    public void sendMessage(String message,String from,String to){
//        //getChat to getPost
//        PostDBObject chat = getChat(from,to);
//        if(chat == null){
//            chat = new ChatDBObject(from,to);
//            insertNewPost(post);
//        }
//    }


    public static void main(String[] args){
        ArangoInstance arangoInstance = new ArangoInstance("root","122195");
       // arangoInstance.initializeChatsDB();
        //arangoInstance.dropChatsDB();
        //PostDBObject postDBObject = new PostDBObject("Zaher","Souidan");

       // arangoInstance.arangoDB.db("Chat").collection("chats").insertDocument(chatDBObject);
        arangoInstance.sendMessage("Hi","Ahmed","Souidan");
        //System.out.println(arangoInstance.getChat("Zaher","Souidan").getUuid1());
    }


















}
