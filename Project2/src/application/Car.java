package application;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Car implements Comparable<Car> {
	private String model;
	private int year;
	private String color;
	private double price;
	private String fomrattedPrice;

	// constructor for price as double
	public Car(String model, int year, String color, double price) throws IllegalArgumentException {
		// checking that the values are correct
		if (model.isEmpty() || year > new GregorianCalendar().get(Calendar.YEAR) || year < 1800 || price < 0
				|| color.isEmpty())
			throw new IllegalArgumentException();
		else {
			this.model = model;
			this.year = year;
			this.color = color;
			this.price = price;
			setStringPrice();
		}

	}

	// constructor for price as string
	public Car(String model, int year, String color, String fomrattedPrice) throws IllegalArgumentException {
		// checking that the values are correct
		if (model.isEmpty() || year > new GregorianCalendar().get(Calendar.YEAR) || year < 1800
				|| fomrattedPrice.isEmpty() || color.isEmpty())
			throw new IllegalArgumentException();
		else {
			this.model = model;
			this.year = year;
			this.color = color;
			this.fomrattedPrice = fomrattedPrice;
			this.price = getPriceFromFormat(this.fomrattedPrice);

		}

	}

	private void setStringPrice() {// converts the price to a string value

		String symbol = "";
		double dummyPrice = this.price;

		if (price >= 1000000) {// price is in millions(m)
			dummyPrice = price / 1000000.0;
			symbol = "M";
		} else if (price >= 1000) {// price is in thousands(k)
			dummyPrice = price / 1000.0;
			symbol = "k";
		}

		DecimalFormat decimalFormat = new DecimalFormat("0.##");
		fomrattedPrice = decimalFormat.format(dummyPrice) + symbol;

	}

	// converts the formated price to a double value
	private double getPriceFromFormat(String fomrattedPrice) throws IllegalArgumentException {

		String symbol = fomrattedPrice.substring(fomrattedPrice.length() - 1);// getting the symbol at the end

		if (symbol != null && Character.isLetter(symbol.charAt(0))) {
			String num = fomrattedPrice.substring(0, fomrattedPrice.length() - 1);

			if (symbol.equalsIgnoreCase("k")) {// thousands
				return Double.parseDouble(num) * 1000;

			} else if (symbol.equalsIgnoreCase("m"))// millions
				return Double.parseDouble(num) * 1000000;
			else// not million or thousand
				throw new IllegalArgumentException();
		}

		// no symbol at the end(return same value as the string)
		return Double.parseDouble(fomrattedPrice);
	}

	Car() {// default constructor
	}

	// setters And getters
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
		setStringPrice();
	}

	public String getFormattedPrice() {
		return fomrattedPrice;

	}

	public void setFormattedPrice(String formattedPrice) {
		this.fomrattedPrice = formattedPrice;
		this.price = getPriceFromFormat(this.fomrattedPrice);

	}

	@Override
	public String toString() {
		return model + ", " + year + ", " + color + ", " + fomrattedPrice;
	}

	@Override
	public int compareTo(Car o) {
		if (this.year < o.year)
			return 1;
		else if (this.year > o.year)
			return -1;
		else
			return 0;

	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (obj == null)
			return false;

		Car c = (Car) obj;
		return (model.equals(c.model) && color.equals(c.color) && year == c.year && price == c.price);
	}

}
