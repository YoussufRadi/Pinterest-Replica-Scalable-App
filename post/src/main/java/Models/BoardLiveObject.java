package Models;

import org.redisson.api.annotation.REntity;
import org.redisson.api.annotation.RId;

import java.util.ArrayList;

@REntity
public class BoardLiveObject{

    @RId
    private String key;
    private String id;
    private  String user_id;
    private String name;
    private ArrayList<String> posts_id;

    public BoardLiveObject(){}

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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
}
