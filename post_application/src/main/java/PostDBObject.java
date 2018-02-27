import java.util.ArrayList;
import java.util.Date;

public class PostDBObject extends com.arangodb.entity.DocumentEntity {

    private String user_id;
    private ArrayList<String> likes_id;
    private ArrayList<String> dislikes_id;
    private ArrayList<String> comments_id;
    private ArrayList<String> categories_id;
    private ArrayList<String> tags_id;
    private String image_id;
    private String created_at;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public PostDBObject(){}

    @Override
    public String toString() {
        return "PostDBObject{" +
                "user_id='" + user_id + '\'' +
                ", likes_id=" + likes_id +
                ", dislikes_id=" + dislikes_id +
                ", comments_id=" + comments_id +
                ", categories_id=" + categories_id +
                ", tags_id=" + tags_id +
                ", image_id='" + image_id + '\'' +
                ", created_at='" + created_at + '\'' +
                '}';
    }

    public PostDBObject(String user_id, ArrayList<String> categories_id, ArrayList<String> tags_id, String image_id) {
        super();
        this.user_id = user_id;
        this.categories_id = categories_id;
        this.tags_id = tags_id;
        this.image_id = image_id;

        this.likes_id = new ArrayList<String>();
        this.dislikes_id = new ArrayList<String>();
        this.comments_id = new ArrayList<String>();
        this.created_at = new Date().toString();

    }

    public String getId() {

        return getKey();
    }

    public String getUser_id() {

        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public ArrayList<String> getLikes_id() {
        return likes_id;
    }

    public void setLikes_id(ArrayList<String> likes_id) {
        this.likes_id = likes_id;
    }

    public ArrayList<String> getDislikes_id() {
        return dislikes_id;
    }

    public void setDislikes_id(ArrayList<String> dislikes_id) {
        this.dislikes_id = dislikes_id;
    }

    public ArrayList<String> getComments_id() {
        return comments_id;
    }

    public void setComments_id(ArrayList<String> comments_id) {
        this.comments_id = comments_id;
    }

    public ArrayList<String> getCategories_id() {
        return categories_id;
    }

    public void setCategories_id(ArrayList<String> categories_id) {
        this.categories_id = categories_id;
    }

    public ArrayList<String> getTags_id() {
        return tags_id;
    }

    public void setTags_id(ArrayList<String> tags_id) {
        this.tags_id = tags_id;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public void addLike(String user_like_id){
        likes_id.add(user_like_id);
    }

    public void addDisike(String user_dislike_id){
        likes_id.add(user_dislike_id);
    }

    public void addComment(String comment_id){
        likes_id.add(comment_id);
    }

}
