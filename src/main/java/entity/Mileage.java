package entity;

import java.util.Date;

public class Mileage {
	private String id, detail;
	int num;
	int mileChange;
	private Date timestamp;
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getMileChange() {
		return mileChange;
	}
	public void setMileChange(int mileChange) {
		this.mileChange = mileChange;
	}
	@Override
	public String toString() {
		return "Mileage [id=" + id + ", detail=" + detail + ", num=" + num + ", mileChange=" + mileChange
				+ ", timestamp=" + timestamp + "]";
	}
}
