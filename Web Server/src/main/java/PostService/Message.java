package PostService;

import com.arangodb.velocypack.annotations.SerializedName;

public class Message {
    private String method;
    private String command;



    @SerializedName("post_object")
    private PostDBObject post_object;

    @SerializedName("post_id")
    private String post_id;

    @SerializedName("category_id")
    private String category_id;

    @SerializedName("category_object")
    private CategoryDBObject category_object;

    @SerializedName("board_object")
    private BoardDBObject board_object;

    @SerializedName("board_id")
    private String board_id;

    @SerializedName("user_id")
    private String user_id;




    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public BoardDBObject getBoard_object() {
        return board_object;
    }

    public void setBoard_object(BoardDBObject board_object) {
        this.board_object = board_object;
    }

    public String getBoard_id() {
        return board_id;
    }

    public void setBoard_id(String board_id) {
        this.board_id = board_id;
    }

    public Message() {
    }

    public Message(String method, PostDBObject post_object) {
        this.method = method;
        this.post_object = post_object;
    }
}
