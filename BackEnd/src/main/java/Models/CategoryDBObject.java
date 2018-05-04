package Models;

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
               "id='" + getId()+
               ", title=" + title+
               ", posts=" + posts_id;
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

