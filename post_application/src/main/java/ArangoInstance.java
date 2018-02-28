
import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;

import java.util.ArrayList;
import java.util.HashMap;

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
            arangoDB.db(dbName).createCollection("posts_tags");
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
    public void insertNewCategory(CategoryDBObject categoryDBObject){
        arangoDB.db("Post").collection("categories").insertDocument(categoryDBObject);

    }
    public CategoryDBObject getCategory(String id){
       // System.out.println(arangoDB.db("Post").collection("categories").getDocument(id,CategoryDBObject.class));
        CategoryDBObject category =arangoDB.db("Post").collection("categories").getDocument(id, CategoryDBObject.class);
        return category;
    }
    public void updateCategory(String id,CategoryDBObject category){
        arangoDB.db("Post").collection("categories").updateDocument(id,category);
    }
    public void addNewPostToCategory(String id,String postid){
        PostDBObject post= getPost(postid);
        if(post!=null){
            CategoryDBObject category =arangoDB.db("Post").collection("categories").getDocument(id, CategoryDBObject.class);
            ArrayList<String> posts=new ArrayList<String>();
           // System.out.println(category.getTitle());
            for(int i=0;i<category.getPosts_id().size();i++){
                posts.add(category.getPosts_id().get(i));
            }
            posts.add(postid);
            category.setPosts_id(posts);
            arangoDB.db("Post").collection("categories").updateDocument(id,category);

        }
    }
    public void RemovePostToCategory(String id,String postid){
        PostDBObject post= getPost(postid);
        if(post!=null){
            CategoryDBObject category =arangoDB.db("Post").collection("categories").getDocument(id, CategoryDBObject.class);
            ArrayList<String> posts=new ArrayList<String>();
            // System.out.println(category.getTitle());
            for(int i=0;i<category.getPosts_id().size();i++){
                if(!category.getPosts_id().get(i).equals(postid))
                posts.add(category.getPosts_id().get(i));
            }
            category.setPosts_id(posts);
            arangoDB.db("Post").collection("categories").updateDocument(id,category);

        }
    }
    public void deleteCategory(String id){
        arangoDB.db("Post").collection("categories").deleteDocument(id);
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




    public CommentDBObject getComment(String id){
        CommentDBObject comment =arangoDB.db("Post").collection("comments").getDocument(id, CommentDBObject.class);
        return comment;
    }

    public void deleteComment(String id){
        CommentDBObject comment = getComment(id);
        for (int i =0; i<comment.getReplies_id().size();i++){
            deleteComment(comment.getReplies_id().get(i));
        }
        arangoDB.db("Post").collection("comments").deleteDocument(id);


    }

    public void insertNewComment(CommentDBObject commentDBObject, String post_id){
        arangoDB.db("Post").collection("comments").insertDocument(commentDBObject);
        PostDBObject post = getPost(post_id);
        post.addComment(commentDBObject.getId());
        updatePost(post.getId(),post);
    }

    public void insertNewReply(CommentDBObject commentDBObject, String comment_id){
        arangoDB.db("Post").collection("comments").insertDocument(commentDBObject);
        CommentDBObject comment = getComment(comment_id);
        comment.addReply(commentDBObject.getId());
        updateComment(comment.getId(),comment);
    }

    public void updateComment(String id,CommentDBObject comment){
        arangoDB.db("Post").collection("comments").updateDocument(id,comment);
    }

    public void insertNewTag(TagDBObject tagDBObject) {
        arangoDB.db("Post").collection("posts_tags").insertDocument(tagDBObject);
    }

    public ArrayList<TagDBObject> getPostsOfTagLimit(int skip,int limit, String tag_name){
        HashMap<String, Object> bindVars = new HashMap<String,Object>();
        bindVars.put("skip",skip);
        bindVars.put("limit",limit);
        bindVars.put("tag_name",tag_name);
        ArangoCursor<TagDBObject> cursor =arangoDB.db("Post").query("For pt In posts_tags For p in posts Filter pt.tag_name == @tag_name "+
                        "Filter p._key==pt.post_id Limit @skip,@limit Return {pt}",
                bindVars,null,TagDBObject.class);
        return new ArrayList<TagDBObject>(cursor.asListRemaining());
    }

    public static void main(String[] args){
        ArangoInstance arango = new ArangoInstance("root","pass");
        //arango.initializeDB();
//        arango.dropDB();
    //arango.deleteCategory("87839");
//        CategoryDBObject category = new CategoryDBObject("trial",new ArrayList<String>());
//       arango.insertNewCategory(category);
//        CategoryDBObject category =     arango.getCategory("89047");
       // System.out.println(category);

        //PostDBObject post = new PostDBObject("1",null,null,"2");
        //System.out.println(arango.insertNewPost(post));


//        arango.RemovePostToCategory("89047","28839");
//        arango.dislikePost("3","16256");

     //   PostDBObject post =arango.getPost("28839");

//        PostDBObject post = new PostDBObject("1",null,null,"1");
//        CommentDBObject comment = new CommentDBObject("1","Kareem");
//        CommentDBObject reply = new CommentDBObject("1","Kareem1");
//
//        arango.insertNewPost(post);
//        arango.insertNewComment(comment,post.getId());
//        arango.insertNewReply(reply,comment.getId());



//        TagDBObject tag = new TagDBObject("Kef7a", "14228");
//        arango.insertNewTag(tag);
        System.out.println(arango.getPostsOfTagLimit(0,5,"Kef7a"));
     //   System.out.println(post);

    //    ArrayList<PostDBObject> a =arango.getPostsLimit(0,2);
    //    System.out.println(a);

      //  ArrayList<PostDBObject> a =arango.getPostsLimit(0,2);
      //  System.out.println(a);
    }


}
