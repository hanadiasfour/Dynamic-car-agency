package application;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Brand implements Comparable<Brand> {

	private String brandName;
	private SingleLinkedList carsList = new SingleLinkedList();// SLL containing the cars of this brand
	private ObservableList<Car> data = FXCollections.observableArrayList();// observable list of martyrs
	private ObservableList<Car> sortedData = data.sorted();
	private ObservableList<String> modelsList, colorsList, priceList, yearList;

	public Brand(String brandName) {// constructor
		this.brandName = brandName;
		modelsList = FXCollections.observableArrayList();
		colorsList = FXCollections.observableArrayList();
		priceList = FXCollections.observableArrayList();
		yearList = FXCollections.observableArrayList();
	}
	// setters and getters

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public SingleLinkedList getCarsList() {
		return carsList;
	}

	public ObservableList<Car> getData() {
		return data;
	}

	public ObservableList<Car> getSortedData() {
		return sortedData;
	}

	public ObservableList<String> getModelsList() {
		return modelsList;
	}

	public ObservableList<String> getColorsList() {
		return colorsList;
	}

	public ObservableList<String> getPriceList() {
		return priceList;
	}

	public ObservableList<String> getYearList() {
		return yearList;
	}

	@Override
	public String toString() {
		return brandName;
	}

	@Override
	public int compareTo(Brand o) {

		if (Character.toLowerCase(this.brandName.charAt(0)) < Character.toLowerCase(o.brandName.charAt(0)))
			return -1;
		else if (Character.toLowerCase(this.brandName.charAt(0)) > Character.toLowerCase(o.brandName.charAt(0)))
			return 1;
		else
			return 0;

	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (obj == null)
			return false;

		Brand otherBrand = (Brand) obj;
		return brandName.equalsIgnoreCase(otherBrand.brandName);
	}

}
