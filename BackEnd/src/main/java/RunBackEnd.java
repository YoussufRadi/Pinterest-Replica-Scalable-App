import ClientService.Client;
import LoadBalancer.LoadBalancer;
import LoadBalancer.MQinstance;
import Controller.Controller;
import Models.ServicesType;
import WebServer.WebServer;

public class RunBackEnd {

    private static ServicesType type;

    public static void main(String[] args) throws InterruptedException {

        run("server");
        Thread.sleep(200);
        run("controller");
        Thread.sleep(200);
        run("loadBalancer");
        Thread.sleep(200);
        run("mQinstance");
        Thread.sleep(200);
        type = ServicesType.post;
        run("client");
        Thread.sleep(200);

    }

    private static void run(String instance){
        Thread t = new Thread(() -> {
            switch (instance.toLowerCase()){
                case "server":
                    WebServer s = new WebServer();
                    s.start();
                    break;
                case "controller":
                    Controller controller = new Controller();
                    controller.start();
                    break;
                case "loadbalancer":
                    LoadBalancer loadBalancer = new LoadBalancer();
                    loadBalancer.start();
                    break;
                case "mqinstance":
                    MQinstance mQinstance = new MQinstance();
                    mQinstance.start();
                    break;
                case "client":
                    Client client = new Client();
                    client.initService(RunBackEnd.type);
                    client.startService();
                    client.start();
                    break;
            }
        });
        t.start();
    }
}
