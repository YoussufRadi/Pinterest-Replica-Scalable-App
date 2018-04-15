package Interface;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;

import java.io.IOException;
import java.util.concurrent.ThreadPoolExecutor;

public abstract class ControlService {


    public int threadsNo;
    public int maxDBConnections;
    public  ThreadPoolExecutor executor;
    protected Channel channel;
    protected String consumerTag;
    protected Consumer consumer;
    protected String RPC_QUEUE_NAME;

    public abstract void init(int thread, int connections);
    public abstract void start();

    public void setMaxThreadsSize(int threads){
        threadsNo =threads;
        executor.setMaximumPoolSize(threads);
    };
    //TODO
//    public abstract void setMaxDBConnections(int connections);
//    public abstract void add_command();
//    public abstract void delete_command();
//    public abstract void update_command();
//    public abstract void update_class();

    public void freeze() {
        try {
            channel.basicCancel(consumerTag);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        try {
            consumerTag = channel.basicConsume(RPC_QUEUE_NAME, false, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public abstract void set_error_reporting_level();

}
