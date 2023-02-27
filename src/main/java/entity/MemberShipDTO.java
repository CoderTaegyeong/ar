package entity;

public class MemberShipDTO {
	private String id;
	private Integer mileage;
	private String inDate;
	private String endDate;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getMileage() {
		return mileage;
	}
	public void setMileage(Integer mileage) {
		this.mileage = mileage;
	}
	public String getInDate() {
		return inDate;
	}
	public void setInDate(String inDate) {
		this.inDate = inDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	@Override
	public String toString() {
		return "MemberShipDTO [id=" + id + ", mileage=" + mileage + ", inDate=" + inDate + ", endDate=" + endDate + "]";
	}
	
}
