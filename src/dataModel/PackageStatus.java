package dataModel;

import java.io.Serializable;

/**
 * Created by krzysiek on 07.01.2017.
 */
public class PackageStatus implements Serializable {
    private String id;
    private String status;
    private Package pack;

    public void setPack(Package pack) {
        this.pack = pack;
    }

    public Package getPack() {
        return pack;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public PackageStatus(String id, String status) {
        this.id = id;
        this.status = status;
    }

    @Override
    public String toString() {
        return "PackageStatus{" +
                "id='" + id + '\'' +
                ", status='" + status + '\'' +
                ", pack=" + pack +
                '}';
    }
}
