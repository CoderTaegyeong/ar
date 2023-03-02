package entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import app.PayDialog;

public class PayDTO {
	private int num;
	private String id;
	private int price, pay, mile;
	private Date payDate;
	private String item;
	private String status;
	private String payWith;

	@Override
	public String toString() {
		return "PayDTO [num=" + num + ", id=" + id + ", price=" + price + ", pay=" + pay + ", mile=" + mile
				+ ", payDate=" + payDate + ", item=" + item + ", status=" + status + ", payWith=" + payWith + "]";
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getPay() {
		return pay;
	}
	public void setPay(int pay) {
		this.pay = pay;
	}
	public int getMile() {
		return mile;
	}
	public void setMile(int mile) {
		this.mile = mile;
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
	public String payInfo() {
		return  "일시 : " + new SimpleDateFormat("YYYY-MM-dd EEE HH:mm:ss").format(payDate) +
				"\n아이디 : " + id +
				"\n상품명 : " + item +
				"\n가격 : " + String.format("%,d 원", price) +
				"\n결제금액 : " + String.format("%,d 원", pay) +
				(mile > 0 ? "\n마일리지 사용 : " + String.format("%,d 마일", mile) : "")+
				"\n결제상태 : " + payWith + " -- "+ status;
	}
	public boolean ok() {
		return status != null && status.equals(PayDialog.PAY_OK);
	}
}
