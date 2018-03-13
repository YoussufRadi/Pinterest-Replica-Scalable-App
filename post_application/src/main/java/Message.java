import Arango.*;
import com.arangodb.entity.DocumentEntity;
import com.arangodb.velocypack.annotations.SerializedName;

public class Message {
    private String method;

    @SerializedName("post_object")
    private PostDBObject post_object;

    @SerializedName("post_id")
    private String post_id;

    @SerializedName("category_id")
    private String category_id;

    @SerializedName("category_object")
    private CategoryDBObject category_object;

    public CategoryDBObject getCategory_object() {
        return category_object;
    }

    public void setCategory_object(CategoryDBObject category_object) {
        this.category_object = category_object;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public PostDBObject getPost_object() {
        return post_object;
    }

    public void setPost_object(PostDBObject post_object) {
        this.post_object = post_object;
    }

    public Message() {
    }

    public Message(String method, PostDBObject post_object) {
        this.method = method;
        this.post_object = post_object;
    }
}
