package dataModel;

import java.io.Serializable;

public class ChangeStatus implements Serializable {
	private String id;
	private String status;
	public ChangeStatus(String id, String status) {
		super();
		this.id = id;
		this.status = status;
	}
	public String getId() {
		return id;
	}
	public String getStatus() {
		return status;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	

}
