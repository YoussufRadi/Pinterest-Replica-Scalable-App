package userservice;

import java.util.HashMap;

public abstract class Command implements Runnable {

    protected HashMap<String, Object> data;

    final public void init(HashMap<String, Object> parameters) {
        this.data = parameters;
    }

    protected abstract void execute();

    final public void run() {
        this.execute();
    }

}

