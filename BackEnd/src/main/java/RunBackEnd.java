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
        run("controller");
        run("loadBalancer");
        run("mQinstance");
        type = ServicesType.post;
        run("client");

    }

    private static void run(String instance) throws InterruptedException {
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
                    Client c = new Client();
                    c.initService(ServicesType.post);
                    new Thread(() -> {
                        c.start();
                    }).start();
                    c.startService();
                    break;
            }
        });
        t.start();
        Thread.sleep(200);
    }
}
