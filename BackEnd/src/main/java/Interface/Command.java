package Interface;

import Models.Message;

import java.util.TreeMap;
import java.util.concurrent.Callable;

public abstract class Command implements Callable {

    protected TreeMap<String, Object> data;

    final public void init(TreeMap<String, Object> parameters) {
        this.data = parameters;
    }

    protected abstract String execute();
    public abstract void setMessage(Message message);
    public abstract Message getMessage();

    final public String call() {
       return this.execute();
    }

}

