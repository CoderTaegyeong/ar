package entity;

import javax.swing.JCheckBox;

public class SeatDTO {
	private String airnum;
	private String seatNumber;
	private String seatGrade;
	private String reserved;
	private String depDate;
	private int price;
	public SeatDTO() {}
	public String getAirnum() {
		return airnum;
	}
	public void setAirnum(String airnum) {
		this.airnum = airnum;
	}
	public String getSeatNumber() {
		return seatNumber;
	}
	public void setSeatNumber(String seatNumber) {
		this.seatNumber = seatNumber;
	}
	public String getSeatGrade() {
		return seatGrade;
	}
	public void setSeatGrade(String seatGrade) {
		this.seatGrade = seatGrade;
	}
	public String getReserved() {
		return reserved;
	}
	public void setReserved(String reserved) {
		this.reserved = reserved;
	}
	public String getDepDate() {
		return depDate;
	}
	public void setDepDate(String depDate) {
		this.depDate = depDate;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	@Override
	public String toString() {
		return "SeatDTO [airnum=" + airnum + ", seatNumber=" + seatNumber + ", seatGrade=" + seatGrade + ", reserved="
				+ reserved + ", depDate=" + depDate + ", price=" + price + "]";
	}
	
	
}