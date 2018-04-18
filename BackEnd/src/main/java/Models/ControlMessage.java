package Models;

import java.io.Serializable;
import java.util.Objects;

public class ControlMessage implements Serializable {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ControlMessage that = (ControlMessage) o;
        return Objects.equals(controlCommand, that.controlCommand) &&
                Objects.equals(param, that.param) &&
                Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(controlCommand, param, path);
    }

    private ControlCommand controlCommand;
    private String param;
    private String path;

    public ControlMessage(ControlCommand controlCommand){
        this.controlCommand = controlCommand;
    }

    public ControlMessage(ControlCommand controlCommand, String param){
        this.controlCommand = controlCommand;
        this.param = param;
    }

    public ControlMessage(ControlCommand controlCommand, String param, String path) {
        this.controlCommand = controlCommand;
        this.param = param;
        this.path = path;
    }

    public ControlCommand getControlCommand() {
        return controlCommand;
    }

    public String getParam() {
        return param;
    }

    public String getPath() {
        return path;
    }
}
