package Arango;

import org.redisson.api.annotation.REntity;

import java.util.ArrayList;


public class TagDBObject extends com.arangodb.entity.DocumentEntity   {

    private String tag_name;
    private String post_id;


    public String getTag() {
        return tag_name;
    }

    public void setTag(String tag) {
        this.tag_name = tag;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public TagDBObject(String tag, String post_id) {

        this.tag_name = tag;
        this.post_id = post_id;
    }

    public TagDBObject(){

    }

    public String getId() {

        return getKey();
    }

    @Override
    public String toString() {
        return "Arango.TagDBObject{" +
                "tag_name='" + tag_name + '\'' +
                ", post_id='" + post_id + '\'' +
                '}';
    }
}
