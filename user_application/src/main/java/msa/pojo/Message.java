package msa.pojo;

import com.google.gson.annotations.SerializedName;

public class Message {
    private String method;

    @SerializedName("payload")
    private User payload;

    private String id;
    private String pinId;
    private String boardId;
    private String userId;



    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public User getPayload() {
        return payload;
    }

    public void setPayload(User payload) {
        this.payload = payload;
    }

    public Message() {
    }

    public Message(String method, User payload) {
        this.method = method;
        this.payload = payload;
    }
}
