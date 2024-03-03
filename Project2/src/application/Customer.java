package application;

public class Customer {

	private String name;
	private String mobile;

	public Customer() {

	}

	public Customer(String name, String mobile) {// constructor
		this.name = name;
		this.mobile = mobile;
	}

	// getters and setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Override
	public String toString() {
		return name + ", " + mobile;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (obj == null)
			return false;

		Customer c = (Customer) obj;
		return (this.name.equals(c.name) && this.mobile.equals(c.mobile));
	}

}
