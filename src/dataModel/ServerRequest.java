package dataModel;

import java.io.Serializable;

/**
 * Created by krzysiek on 18.01.2017.
 */
public class ServerRequest implements Serializable {
    public String requestName;
    public Object object;

    public ServerRequest(String requestName, Object object) {
        this.requestName = requestName;
        this.object = object;
    }

    @Override
    public String toString() {
        return "ServerRequest{" +
                "requestName='" + requestName + '\'' +
                ", object=" + object +
                '}';
    }
}
