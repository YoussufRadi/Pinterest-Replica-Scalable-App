import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDB;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ArangoInstance {


    ArangoDB arangoDB;


    public ArangoInstance(String user,String password){
        String rootPath = "src/main/resources/";

        try {
            InputStream in = new FileInputStream(rootPath + "arango.properties");
        }catch (FileNotFoundException e){
            System.err.println("arongo properties file not found "+e.getMessage());
        }

        arangoDB = new ArangoDB.Builder().user(user).password(password).build();
    }


    public void initializeChatsDB(){

        String dbName = "Chat";
        arangoDB.createDatabase(dbName);
        arangoDB.db(dbName).createCollection("chats");
    }

    public void dropChatsDB(){
        arangoDB.db("Chat").drop();
    }

    public void insertNewChat(ChatDBObject chatDBObject){
        arangoDB.db("Chat").collection("chats").insertDocument(chatDBObject);
    }

    public ChatDBObject getChat(String uuid1,String uuid2){
        Map<String, Object> bindVars = new HashMap<>();
        bindVars.put("uuid1",uuid1);
        bindVars.put("uuid2",uuid2);
        String query= "FOR chat IN chats FILTER chat.uuid1 == @uuid1 && chat.uuid2 == @uuid2  RETURN chat";
        ArangoCursor<ChatDBObject> cursor = arangoDB.db("Chat").query(query,bindVars, null, ChatDBObject.class);

        for(; cursor.hasNext();) {
            ChatDBObject chat = cursor.next();
           return chat;
        }

        bindVars.clear();
        bindVars.put("uuid1",uuid2);
        bindVars.put("uuid2",uuid1);
        query= "FOR chat IN chats FILTER chat.uuid1 == @uuid1 && chat.uuid2 == @uuid2  RETURN chat";
        cursor = arangoDB.db("Chat").query(query,bindVars, null, ChatDBObject.class);

        for(; cursor.hasNext();) {
            ChatDBObject chat = cursor.next();
            return chat;
        }
        return null;
    }

    public void sendMessage(String message,String from,String to){
        ChatDBObject chat = getChat(from,to);
        if(chat == null){
            chat = new ChatDBObject(from,to);
            insertNewChat(chat);
        }
    }


    public static void main(String[] args){
        ArangoInstance arangoInstance = new ArangoInstance("root","122195");
       // arangoInstance.initializeChatsDB();
        //arangoInstance.dropChatsDB();
        //ChatDBObject chatDBObject = new ChatDBObject("Zaher","Souidan");

       // arangoInstance.arangoDB.db("Chat").collection("chats").insertDocument(chatDBObject);
        arangoInstance.sendMessage("Hi","Ahmed","Souidan");
        //System.out.println(arangoInstance.getChat("Zaher","Souidan").getUuid1());
    }


















}
