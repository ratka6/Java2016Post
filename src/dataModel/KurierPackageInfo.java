package dataModel;

import java.io.Serializable;

/**
 * Created by krzysiek on 19.01.2017.
 */
public class KurierPackageInfo implements Serializable {
    private PackageStatus status;
    private User user;

    public KurierPackageInfo(PackageStatus status, User user) {
        this.status = status;
        this.user = user;
    }

    public PackageStatus getPackageStatus() {
        return status;
    }

    public User getUser() {
        return user;
    }

    public String getId() {
        return status.getId();
    }

    public String getStatus() {
        return status.getStatuses();
    }

    public String getSource() {
        return status.getPack().getSourceAddress();
    }

    public String getDestination() {
        return status.getPack().getDestinationAddres();
    }

	@Override
	public String toString() {
		return "KurierPackageInfo [status=" + status + ", user=" + user + "]";
	}
    
    
}
