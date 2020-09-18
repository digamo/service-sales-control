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
		
		String newValue = value.replace(".", "").replace(",", ".");
		
		return new BigDecimal(newValue);
	}

}
