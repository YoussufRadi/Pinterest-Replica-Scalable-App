
import com.arangodb.velocypack.annotations.SerializedName;

public class Message {
    private String method;

    @SerializedName("payload")
    private PostDBObject payload;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public PostDBObject getPayload() {
        return payload;
    }

    public void setPayload(PostDBObject payload) {
        this.payload = payload;
    }

    public Message() {
    }

    public Message(String method, PostDBObject payload) {
        this.method = method;
        this.payload = payload;
    }
}
