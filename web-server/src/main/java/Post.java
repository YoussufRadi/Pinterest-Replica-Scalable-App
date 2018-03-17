import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Date;

public class Post {
    String user_id;
    ArrayList<String> likes_id;
    ArrayList<String> dislikes_id;
    ArrayList<String> comments_id;
    ArrayList<String> categories_id;
    ArrayList<String> tags_id;
    String image_id;
    String created_at;

    @JsonCreator
    public Post(@JsonProperty("user_id") String user_id,
                @JsonProperty("categories_id")ArrayList<String> categories_id,
                @JsonProperty("tags_id")ArrayList<String> tags_id,
                @JsonProperty("image_id")String image_id) {
        super();
        this.user_id = user_id;
        this.categories_id = categories_id;
        this.tags_id = tags_id;
        this.image_id = image_id;
    }

}
