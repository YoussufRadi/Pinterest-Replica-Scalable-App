package Config;

import java.io.*;
import java.util.Properties;

public class Config {

    private static Config instance = new Config();

    private final Properties arangoConfig = new Properties();
    private final Properties controllerConfig = new Properties();
    private final Properties loadBalancerConfig = new Properties();
    private final Properties mediaServerConfig = new Properties();
    private final Properties mqInstanceConfig = new Properties();
    private final Properties serviceConfig = new Properties();
    private final Properties webServerConfig = new Properties();

    private final String arangoPath = "./src/main/resources/arango.conf";
    private final String controllerPath = "./src/main/resources/controller.conf";
    private final String loadBalancerPath = "./src/main/resources/load.balancer.conf";
    private final String mediaServerPath = "./src/main/resources/media.server.conf";
    private final String mqInstancePath = "./src/main/resources/mq.instance.conf";
    private final String servicePath = "./src/main/resources/service.conf";
    private final String webServerpath = "./src/main/resources/web.server.conf";

    private Config() {
        loadConfig(arangoConfig, arangoPath);
        loadConfig(controllerConfig, controllerPath);
        loadConfig(loadBalancerConfig, loadBalancerPath);
        loadConfig(mediaServerConfig, mediaServerPath);
        loadConfig(mqInstanceConfig, mqInstancePath);
        loadConfig(serviceConfig, servicePath);
        loadConfig(webServerConfig, webServerpath);
    }

    private void loadConfig(Properties config, String path){
        try {
            FileInputStream file = new FileInputStream(path);
            config.load(file);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Config getInstance(){
        return instance;
    }

    public void setProperty(ConfigTypes config, String key, String val){
        Properties props = null;
        String path = null;
        switch (config){
            case Arango:
                props = arangoConfig;
                path = arangoPath;
                break;
            case Controller:
                props = controllerConfig;
                path = controllerPath;
                break;
            case LoadBalancer:
                props = loadBalancerConfig;
                path = loadBalancerPath;
                break;
            case MediaServer:
                props = mediaServerConfig;
                path = mediaServerPath;
                break;
            case MqInstance:
                props = mqInstanceConfig;
                path = mqInstancePath;
                break;
            case Service:
                props = serviceConfig;
                path = servicePath;
                break;
            case WebServer:
                props = webServerConfig;
                path = webServerpath;
                break;
        }
        props.setProperty(key,val);
        writeConfig(props, path);
    }

    private void writeConfig(Properties config, String path){
        OutputStream out;
        try {
            out = new FileOutputStream(path, false);
            config.store(out, "");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Server Configs

    public int getWebServerPort() {
        return Integer.parseInt(webServerConfig.getProperty("server.port"));
    }

    public String getServerQueueHost() {
        return webServerConfig.getProperty("server.rabbitmq.host");
    }

    public int getServerQueuePort() {
        return Integer.parseInt(webServerConfig.getProperty("server.rabbitmq.port"));
    }

    public String getServerQueueUserName() {
        return webServerConfig.getProperty("server.rabbitmq.username");
    }

    public String getServerQueuePass() {
        return webServerConfig.getProperty("server.rabbitmq.password");
    }

    public String getServerQueueName() {
        return webServerConfig.getProperty("server.rabbitmq.queue");
    }


    //LoadBalancer Configs

    public String getLoadBalancerQueueHost() {
        return loadBalancerConfig.getProperty("load.balancer.rabbitmq.host");
    }

    public int getLoadBalancerQueuePort() {
        return Integer.parseInt(loadBalancerConfig.getProperty("load.balancer.rabbitmq.port"));
    }

    public String getLoadBalancerQueueUserName() {
        return loadBalancerConfig.getProperty("load.balancer.rabbitmq.username");
    }

    public String getLoadBalancerQueuePass() {
        return loadBalancerConfig.getProperty("load.balancer.rabbitmq.password");
    }

    public String getLoadBalancerQueueName() {
        return loadBalancerConfig.getProperty("load.balancer.rabbitmq.queue");
    }


    //MediaServer Configs

    public String getMediaServerPath() {
        return mediaServerConfig.getProperty("media.server.file.path");
    }
}
