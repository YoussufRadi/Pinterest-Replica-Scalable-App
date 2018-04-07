import java.sql.Timestamp;

public class ChatMessage {
    String from;

    String to;
    String message;

    public ChatMessage(String from,String to,String message){
        this.message = message;
        this.from = from;
        this.to=to;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    }
    public ChatMessage(){
        super();
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    Timestamp date;



}
