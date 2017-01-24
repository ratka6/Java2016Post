package dataModel;

import java.io.Serializable;

/**
 * Created by krzysiek on 07.01.2017.
 */
public class PackageStatus implements Serializable {
    private String id;
    private Statuses statuses;
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
   
     

    public String getStatuses() {
		return statuses.toString();
	}

	public void setStatuses(Statuses statuses) {
		this.statuses = statuses;
	}

	public PackageStatus(String id, Statuses statuses){
    	this.id =id;
    	this.statuses = statuses;
    }

	public PackageStatus(String string, Statuses statuses, Package package1) {
		this.id = string;
		this.statuses = statuses;
		this.pack = package1;
	}

	public PackageStatus(String id, String status, Package package1) {
		this.id = id;
		System.out.println(status);
		this.statuses = statuses.getStatusByString("U kuriera");
		System.out.println(statuses);
		this.pack = package1;
	}

	@Override
	public String toString() {
		return "PackageStatus [id=" + id + ", statuses=" + statuses + ", pack=" + pack + "]";
	}



}
