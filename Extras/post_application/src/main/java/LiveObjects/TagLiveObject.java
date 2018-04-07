package LiveObjects;


import org.redisson.api.annotation.*;

@REntity
public class TagLiveObject {
    @RId
    private String key;
    private String id;
    private String tag_name;
    private String post_id;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public String getTag_name() {
        return tag_name;
    }

    public String getPost_id() {
        return post_id;
    }
    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }
    public String toString() {
        return "\"TagLiveObject{" +
                "tag: '"+getTag_name() +'\''+
                ", post_id: '"+getPost_id()+'\''+"}";
    }

}
