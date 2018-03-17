package Arango;

import java.util.ArrayList;

public class CategoryDBObject  extends com.arangodb.entity.DocumentEntity {
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getPosts_id() {
        return posts_id;
    }

    public void setPosts_id(ArrayList<String> posts_id) {
        this.posts_id = posts_id;
    }

    private ArrayList<String> posts_id;
    public String toString() {
       return "Arango.CategoryDBObject{" +
               "id='" + getId() ;
//                ", likes_id=" + likes_id +
//                ", dislikes_id=" + dislikes_id +
//                ", comments_id=" + comments_id +
//                ", categories_id=" + categories_id +
//                ", tags_id=" + tags_id +
//                ", image_id='" + image_id + '\'' +
//                ", created_at='" + created_at + '\'' +
    //             '}';
    }
    public CategoryDBObject(){

    }
    public CategoryDBObject(String title,ArrayList<String> posts_id){
        super();
        this.title=title;

        this.posts_id  =posts_id;
    }

    public String getId() {

        return getKey();
    }

}

