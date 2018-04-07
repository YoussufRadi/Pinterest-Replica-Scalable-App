import java.util.ArrayList;

public class ChatDBObject {

    private String key;
    private String uuid1;
    private String uuid2;
    private ArrayList<ChatMessage> messages;

    public ChatDBObject(){
        super();
    }
    public ChatDBObject(String uuid1,String uuid2){
        this.uuid1=uuid1;
        this.uuid2=uuid2;

        this.messages = new ArrayList<ChatMessage>();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUuid1() {
        return uuid1;
    }
    public String getUuid2() {
        return uuid2;
    }

    public String setUuid1() {
        return uuid1;
    }
    public String setUuid2() {
        return uuid2;
    }



    public ArrayList<ChatMessage> getMessages() {
        return messages;
    }
    public void setMessages(ArrayList<ChatMessage> messages) {
        this.messages = messages;
    }


}


