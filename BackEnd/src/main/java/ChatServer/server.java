package ChatServer;

public class server {
    String ip;
    int port;

    public server(String ip,int port){
        this.ip=ip;
        this.port=port;

    }
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    String UUID;
}
