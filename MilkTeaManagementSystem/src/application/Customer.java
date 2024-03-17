package application;

import java.util.Date;

public class Customer {

	private int id;
	private int phone;
	private int times;
	private Date date;
	
	public Customer(int id, int phone, int times, Date date) {
		this.id = id;
		this.phone = phone;
		this.times = times;
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPhone() {
		return phone;
	}

	public void setPhone(int phone) {
		this.phone = phone;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
}
