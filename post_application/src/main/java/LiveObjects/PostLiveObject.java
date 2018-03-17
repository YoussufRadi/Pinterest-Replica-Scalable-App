package LiveObjects;


import org.redisson.api.annotation.*;

import java.util.ArrayList;
import java.util.List;

@REntity

public class PostLiveObject {
    @RId
    private String key;
    private String id;
    private String user_id;
    private List<String> likes_id;
    private List<String> dislikes_id;
    private List<String> comments_id;
    private List<String> categories_id;
    private List<String> tags_id;
    private String image_id;
    private String created_at;

    public String getCreated_at() {
        return created_at;
    }

    public PostLiveObject() {
    }

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

    public List<String> getLikes_id() {
        return likes_id;
    }

    public void setLikes_id(List<String> likes_id) {
        this.likes_id = likes_id;
    }

    public List<String> getDislikes_id() {
        return dislikes_id;
    }

    public void setDislikes_id(List<String> dislikes_id) {
        this.dislikes_id = dislikes_id;
    }

    public List<String> getComments_id() {
        return comments_id;
    }

    public void setComments_id(List<String> comments_id) {
        this.comments_id = comments_id;
    }

    public List<String> getCategories_id() {
        return categories_id;
    }

    public void setCategories_id(List<String> categories_id) {
        this.categories_id = categories_id;
    }

    public List<String> getTags_id() {
        return tags_id;
    }

    public void setTags_id(List<String> tags_id) {
        this.tags_id = tags_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }


    public String getImage_id() {
        return image_id;
    }

    public String toString() {
        return "PostLiveObject{" +
                "user_id='" + this.getUser_id() + '\'' +
                ", likes_id=" + this.getLikes_id() +
                ", dislikes_id=" + this.getDislikes_id()+
                ", comments_id=" + this.getComments_id() +
                ", categories_id=" + this.getCategories_id() +
                ", tags_id=" + this.getTags_id()+
                ", image_id='" + this.getImage_id()+ '\'' +
                ", created_at='" + this.getCreated_at() + '\'' +
                '}';
    }





}
