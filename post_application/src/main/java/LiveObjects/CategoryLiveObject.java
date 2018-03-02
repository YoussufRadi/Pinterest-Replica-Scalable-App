package LiveObjects;


import org.redisson.api.annotation.*;
import java.util.List;

@REntity
public class CategoryLiveObject {
    @RId
    private String key;
    private String id;
    private String title;
    private List<String> posts_id;


    public CategoryLiveObject(){ }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getPosts_id() {
        return posts_id;
    }

    public void setPosts_id(List<String> posts_id) {
        this.posts_id = posts_id;
    }

    @Override
    public String toString() {
        return "\"CategoryDBObject{" +
                "title: '"+getTitle() +'\''+
                ", post_ids: '"+getPosts_id()+'\''+"}";
    }
}
