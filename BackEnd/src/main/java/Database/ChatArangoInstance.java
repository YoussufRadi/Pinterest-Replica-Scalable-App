package Database;

import Config.Config;
import Models.ServerDBObject;
import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;

import java.util.Iterator;

public class ChatArangoInstance {


    private Config conf = Config.getInstance();

    private ArangoDB arangoDB;
    private String dbUserName = conf.getArangoUserName();
    private String dbPass = conf.getArangoQueuePass();

    private String dbName = conf.getArangoChatDbName();

    public ChatArangoInstance(int maxConnections){
        arangoDB = new ArangoDB.Builder().user(dbUserName).password(dbPass).maxConnections(maxConnections).build();
    }


    public void setMaxDBConnections(int maxDBConnections){
        arangoDB = new ArangoDB.Builder().user(dbUserName).password(dbPass).maxConnections(maxDBConnections).build();
    }

    public void initializeDB(){

        try{

            arangoDB.createDatabase("Chat");
            arangoDB.db(dbName).createCollection("Servers");
            System.out.println("Database created: " + dbName);
        } catch (ArangoDBException e) {
            System.err.println("Failed to create database: Post");
        }
    }

    public void dropDB(){

        try{
            arangoDB.db("Chat").drop();
            System.out.println("Database dropped: Post");
        } catch (ArangoDBException e) {
            System.err.println("Failed to drop database: Post");
        }
    }
    public void addServer(ServerDBObject serverDBObject){

        arangoDB.db("Chat").collection("Servers").insertDocument(serverDBObject);

    }
    public ServerDBObject getServer( ){
      int x= (int) (Math.random()* arangoDB.db("Chat").collection("Servers").count().getCount());
        ArangoCursor<ServerDBObject> cursor =arangoDB.db("Chat").query("For server In Servers Return server",
                null,null, ServerDBObject.class);
        Iterator myservers=cursor.iterator();
        int i=0;
        System.out.println(x);
        while(i<x+1){
            if(i==x) {
                return(ServerDBObject)myservers.next();
            }else{
                myservers.next();
            }
            i++;
        }
        return null;
    }
    public void deleteServer(String id){

        arangoDB.db("Chat").collection("Servers").deleteDocument(id);
    }


//    public static void main(String[] args){
//        ArangoInstance arango = new ArangoInstance("root","sinsin1234",15);

       // String s = "Post";
        //arango.arangoDB.db(s).createCollection("posts_tags");
       // System.out.println(arango.getPostsOfTagLimit(0,2,"cold"));
        //arango.initializeDB();
//        arango.dropDB();
//        arango.arangoDB.db("Chat").createCollection("test");

        //arango.addServer(new ServerDBObject("1","1"));
//        Arango.CategoryDBObject category = new Arango.CategoryDBObject("trial",new ArrayList<String>());
//       arango.insertNewCategory(category);
//        Arango.CategoryDBObject category =     arango.GetCategory("89047");
       // System.out.println(category);

        //Arango.PostDBObject post = new Arango.PostDBObject("1",null,null,"2");
        //System.out.println(arango.insertNewPost(post));


//        arango.RemovePostToCategory("89047","28839");
//        arango.DislikePost("3","16256");

     //   Arango.PostDBObject post =arango.GetPost("28839");

//        Arango.PostDBObject post = new Arango.PostDBObject("1",null,null,"1");
//        Arango.CommentDBObject comment = new Arango.CommentDBObject("1","Kareem");
//        Arango.CommentDBObject reply = new Arango.CommentDBObject("1","Kareem1");
//
//        arango.insertNewPost(post);
//        arango.insertNewComment(comment,post.getId());
//        arango.insertNewReply(reply,comment.getId());

        //   System.out.println(post);

//        Arango.TagDBObject tag = new Arango.TagDBObject("Kef7a", "28839");
//        arango.insertNewTag(tag);
//        System.out.println(arango.getPostsOfTagLimit(0,5,"Kef7a"));
//        System.out.println(arango.getTagsOfPostLimit(0,5,"28839"));

        //    ArrayList<Arango.PostDBObject> a =arango.getPostsLimit(0,2);
    //    System.out.println(a);

      //  ArrayList<Arango.PostDBObject> a =arango.getPostsLimit(0,2);
      //  System.out.println(a);

//        BoardDBObject board = new BoardDBObject("1","boardeeno" );
//        arango.insertNewBoard(board);
//        arango.InsertPostToBoard("192649","15");
//        arango.RemovePostFromBoard("192649","15");
//        System.out.println(arango.GetBoard("192649"));
//    }


}
