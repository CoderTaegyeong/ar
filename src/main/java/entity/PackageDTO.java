package entity;

import java.awt.Image;

//CREATE TABLE TRAVEL_PACKAGE 
//(
//  ID NUMBER(10) 
//, IMAGEPATH VARCHAR2(255) 
//, TITLE VARCHAR2(255) 
//, travelloc VARCHAR2(255) 
//, traveldays NUMBER(10) 
//, price VARCHAR2(20) 
//, detailtext VARCHAR2(255) 
//);
//

public class PackageDTO {
	private int id;
	private String imagePath;
	private String title;
	private String travelLoc;
	private int travelDays; 
	private int price;
	private String detailText;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTravelLoc() {
		return travelLoc;
	}
	public void setTravelLoc(String travelLoc) {
		this.travelLoc = travelLoc;
	}
	public int getTravelDays() {
		return travelDays;
	}
	public void setTravelDays(int travelDays) {
		this.travelDays = travelDays;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getDetailText() {
		return detailText;
	}
	public void setDetailText(String detailText) {
		this.detailText = detailText;
	}
	@Override
	public String toString() {
		return "PackageDTO [id=" + id + ", imagePath=" + imagePath + ", title=" + title + ", travelLoc=" + travelLoc
				+ ", travelDays=" + travelDays + ", price=" + price + ", detailText=" + detailText + "]";
	}
}
