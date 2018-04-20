package Interface;

import java.util.TreeMap;

public abstract class Command implements Runnable {

    protected TreeMap<String, Object> data;

    final public void init(TreeMap<String, Object> parameters) {
        this.data = parameters;
    }

    protected abstract void execute();

    final public void run() {
        this.execute();
    }

}

