package application;

import java.util.Comparator;

public class comparing implements Comparator<String> {

	@Override
	public int compare(String o1, String o2) {
		// TODO Auto-generated method stub
		if (getPriceFromFormat(o1) > getPriceFromFormat(o2))
			return 1;
		else if (getPriceFromFormat(o1) < getPriceFromFormat(o2))
			return -1;
		else
			return 0;
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

}
