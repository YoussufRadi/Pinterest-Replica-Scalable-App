package Config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

    private static Config instance;

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

    public static void init(){
        if(instance == null)
            instance = new Config();
    }

    public static Config getInstance(){
        init();
        return instance;
    }


    public int getWebServerPort() {
        return Integer.parseInt(webServerConfig.getProperty("server.port"));
    }
}
