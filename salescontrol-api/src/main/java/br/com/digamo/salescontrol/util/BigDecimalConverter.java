package br.com.digamo.salescontrol.util;

import java.math.BigDecimal;

public class BigDecimalConverter {

	/**
	 * Converts a String value to BigDecimal
	 * @param value
	 * @return
	 */
	public static BigDecimal converter(String value) {

		if(value == null)
			return null;

		BigDecimal convertedValue = null;
		
		try {
		
			String newValue = value.replace(".", "").replace(",", ".");
			convertedValue = new BigDecimal(newValue);
		
		}catch (NumberFormatException e) {
			throw new NumberFormatException(e.getMessage());
		}
		
		return convertedValue;
	}

}
