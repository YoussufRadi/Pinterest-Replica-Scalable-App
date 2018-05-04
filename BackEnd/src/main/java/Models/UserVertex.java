package Models;
import com.arangodb.entity.DocumentField;
import com.arangodb.entity.DocumentField.Type;

public class UserVertex {
    @DocumentField(Type.ID)
    private String id;

    public UserVertex(String id){
        this.id = id;
    }
}
