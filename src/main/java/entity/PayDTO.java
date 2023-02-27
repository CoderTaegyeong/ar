package entity;

import java.util.Date;

public class PayDTO {
	private int id;
	private int price;
	private Date payDate;
	private String item;
	private String status;
	private String payWith;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public Date getPayDate() {
		return payDate;
	}
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPayWith() {
		return payWith;
	}
	public void setPayWith(String payWith) {
		this.payWith = payWith;
	}
	@Override
	public String toString() {
		return "PayDTO [id=" + id + ", price=" + price + ", payDate=" + payDate + ", item=" + item + ", status="
				+ status + ", payWith=" + payWith + "]";
	}
}
