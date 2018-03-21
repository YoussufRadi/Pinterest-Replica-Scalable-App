package PostService;

import java.util.ArrayList;

public class CommentDBObject extends com.arangodb.entity.DocumentEntity {

    private String user_id;
    private ArrayList<String> replies_id;
    private String body;

    public String getUser_id() {
        return user_id;
    }


    public CommentDBObject(){

    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public ArrayList<String> getReplies_id() {

        return replies_id;
    }

    public void setReplies_id(ArrayList<String> replies_id) {
        this.replies_id = replies_id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public CommentDBObject(String user_id, String body) {
        this.user_id = user_id;
        this.body = body;
        replies_id = new ArrayList<String>();

    }


    public String getId() {

        return getKey();
    }

    public void addReply(String comment_id){
        replies_id.add(comment_id);
    }
}
