package Models;
import com.arangodb.entity.DocumentField;
import com.arangodb.entity.DocumentField.Type;
public class FollowEdge {
    @DocumentField(Type.FROM)
    String from;
    @DocumentField(Type.TO)
    String to;

    public FollowEdge(String from,String to){
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
