package Models;

import java.util.ArrayList;

public class BoardDBObject extends com.arangodb.entity.DocumentEntity {

    private  String user_id;
    private String name;
    private ArrayList<String> posts_id;

    public BoardDBObject(){}

    public BoardDBObject(String user_id, String name) {
        this.user_id = user_id;
        this.name = name;
        posts_id = new ArrayList<String>();
    }

    public String getId(){
        return getKey();
    }


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getPosts_id() {
        return posts_id;
    }

    public void setPosts_id(ArrayList<String> posts_id) {
        this.posts_id = posts_id;
    }

    public void addPost(String post_id){
        posts_id.add(post_id);
    }

    public void removePost(String post_id){
        posts_id.remove(post_id);
    }

    @Override
    public String toString() {
        return "BoardDBObject{" +
                "id='" + getId() + '\'' +
                "user_id='" + user_id + '\'' +
                ", name='" + name + '\'' +
                ", posts_id=" + posts_id +
                '}';
    }
}
