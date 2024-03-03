package application;

import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Order {

	private Customer customer;
	private Brand brand;
	private Car car;
	private Date OrderDate;
	private String OrderStatus;
	private String formattedDate;
	private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");// formatter for the date

	public Order(Customer customer, Brand brand, Car car, Date orderDate, String orderStatus) {
		this.customer = customer;
		this.brand = brand;
		this.car = car;
		OrderDate = orderDate;
		OrderStatus = orderStatus;
		formattedDate = formatter.format(OrderDate);

	}

	// getters and setters
	public String getFormattedDate() {
		return formattedDate;
	}

	public void setFormattedDate(String formattedDate) {
		this.formattedDate = formattedDate;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public Date getOrderDate() {
		return OrderDate;
	}

	public void setOrderDate(Date orderDate) {
		OrderDate = orderDate;
	}

	public String getOrderStatus() {
		return OrderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		OrderStatus = orderStatus;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (obj == null)
			return false;

		Order o = (Order) obj;

		return (this.customer.equals(o.customer) && this.brand.equals(o.brand) && this.car.equals(o.car)
				&& this.formattedDate.equals(o.formattedDate) && this.OrderStatus.equals(o.OrderStatus));
	}

	@Override
	public String toString() {
		return customer.toString() + ", " + brand.getBrandName() + ", " + car.toString() + ", " + formattedDate + ", "
				+ OrderStatus;
	}

}
