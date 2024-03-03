package application;

public class Statistics {

	private String brandName, highestPrice, lowestPrice, highestModel, lowestModel;

	Statistics(Brand b) {// constructor
		brandName = b.getBrandName();
		
		if (!b.getCarsList().isEmpty())
			doStat(b);
		else {//for when there are no cars to develop a report on
			highestPrice = "N/A";
			lowestPrice = "N/A";
			highestModel = "N/A";
			lowestModel = "N/A";

		}
	}

	private void doStat(Brand b) {// finds the cars with the highest and lowest price in the brand given O(n)

		SNode currentCar = b.getCarsList().getFirstNode();// first car in cars list

		// initializing values
		Car maxCar = (Car) currentCar.getElement();
		Car minCar = (Car) currentCar.getElement();
		double max = 0;
		double min = Double.MAX_VALUE;
		double currPrice = 0;

		// looping until end of Cars list in the given brand
		for (int i = 0; i < b.getCarsList().getSize(); i++) {
			currPrice = ((Car) currentCar.getElement()).getPrice();

			if (currPrice > max) {// current is max in price
				max = currPrice;
				maxCar = (Car) currentCar.getElement();
			}

			if (currPrice < min) {// current is min in price
				min = currPrice;
				minCar = (Car) currentCar.getElement();
			}

			currentCar = currentCar.getNext();// next car in list

		}

		// setting the values
		highestPrice = maxCar.getFormattedPrice();
		lowestPrice = minCar.getFormattedPrice();
		highestModel = maxCar.getModel();
		lowestModel = minCar.getModel();

	}

	// setters and gettersS

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getHighestPrice() {
		return highestPrice;
	}

	public void setHighestPrice(String highestPrice) {
		this.highestPrice = highestPrice;
	}

	public String getLowestPrice() {
		return lowestPrice;
	}

	public void setLowestPrice(String lowestPrice) {
		this.lowestPrice = lowestPrice;
	}

	public String getHighestModel() {
		return highestModel;
	}

	public void setHighestModel(String highestModel) {
		this.highestModel = highestModel;
	}

	public String getLowestModel() {
		return lowestModel;
	}

	public void setLowestModel(String lowestModel) {
		this.lowestModel = lowestModel;
	}

}
