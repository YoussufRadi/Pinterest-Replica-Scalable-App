import ClientService.Client;
import LoadBalancer.LoadBalancer;
import LoadBalancer.MQinstance;
import Controller.Controller;
import Models.ServicesType;
import WebServer.WebServer;

public class RunBackEnd {

    private static ServicesType type;

    public static void main(String[] args) throws InterruptedException {

//        run("server");
//        run("controller");
//        run("loadBalancer");
//        run("mQinstance");
//        type = ServicesType.user;
//        run("client");


        if(args.length > 1) {
            if (args[1].toLowerCase().equals("post"))
                type = ServicesType.post;
            if (args[1].toLowerCase().equals("user"))
                type = ServicesType.user;
            if (args[1].toLowerCase().equals("chat"))
                type = ServicesType.chat;
        }
        if(args.length > 0){
            System.out.println("Running from args : " + args[0]);
            run(args[0]);
        } else {
            run("server");
            run("controller");
            run("loadBalancer");
            run("mQinstance");
            type = ServicesType.user;
            run("client");
        }

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
                    c.initService(type);
                    new Thread(() -> {
                        c.start();
                    }).start();
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    c.initDB();
                    c.startService();
                    break;
            }
        });
        t.start();
        Thread.sleep(200);
    }
}
