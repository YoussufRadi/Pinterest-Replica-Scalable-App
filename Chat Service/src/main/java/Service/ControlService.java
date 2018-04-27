package Service;

import java.util.concurrent.ThreadPoolExecutor;

public abstract class ControlService {


    public int threadsNo;
    public int maxDBConnections;
    public  ThreadPoolExecutor executor;
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
//    public abstract void freeze();
//    public abstract void resume();
//    public abstract void set_error_reporting_level();
}
