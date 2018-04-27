package Models;

import com.arangodb.velocypack.annotations.SerializedName;

public class Message {
    private String method;
    private String command;



    @SerializedName("server_object")
    private ServerDBObject server_object;

    @SerializedName("server_id")
    private String server_id;






    public String getCommand() {
        return command;
    }

    public ServerDBObject getServer_object() {
        return server_object;
    }

    public void setServer_object(ServerDBObject server_object) {
        this.server_object = server_object;
    }

    public String getServer_id() {
        return server_id;
    }

    public Message() {
    }

    public Message(String method, ServerDBObject server_object) {
        this.method = method;
        this.server_object = server_object;
    }
}
