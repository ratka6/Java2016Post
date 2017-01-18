package dataModel;

import java.io.Serializable;

/**
 * Created by krzysiek on 18.01.2017.
 */
public class ServerResponse implements Serializable {
    public String responseName;
    public String error;
    public Object object;

    public ServerResponse(String responseName, Object object) {
        this.responseName = responseName;
        this.object = object;
    }

    @Override
    public String toString() {
        return "ServerResponse{" +
                "responseName='" + responseName + '\'' +
                ", error='" + error + '\'' +
                ", object=" + object +
                '}';
    }
}

