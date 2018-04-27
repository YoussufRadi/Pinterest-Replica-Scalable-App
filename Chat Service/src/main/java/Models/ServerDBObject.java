package Models;

import java.util.ArrayList;

public class ServerDBObject  extends com.arangodb.entity.DocumentEntity {
    private String ip;
    private String port;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }



    public String toString() {
        return "Arango.CategoryDBObject{" +
                "ip: "+getIp();
//                ", likes_id=" + likes_id +
//                ", dislikes_id=" + dislikes_id +
//                ", comments_id=" + comments_id +
//                ", categories_id=" + categories_id +
//                ", tags_id=" + tags_id +
//                ", image_id='" + image_id + '\'' +
//                ", created_at='" + created_at + '\'' +
        //             '}';
    }
    public ServerDBObject(){

    }
    public ServerDBObject(String ip,String port){
        super();
        this.ip=ip;

        this.port  =port;
    }

    public String getId() {

        return getKey();
    }

}