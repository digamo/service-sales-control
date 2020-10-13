package br.com.digamo.salescontrol.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

/**
 * 
 * @author digamo
 *
 */
public class BigDecimalUtilTest {

	
	@Test
	public void shouldConverterStringToBigDecimalWithComma() {
		
		// scenario
		String bigDecimalAsStringWithComma = "100,2";
		
		// action
		BigDecimal convertedValue = BigDecimalUtil.converter(bigDecimalAsStringWithComma);

		// verification
		assertThat(convertedValue).isNotNull();
		assertThat(convertedValue).isEqualTo(new BigDecimal("100.2"));
	}

	@Test
	public void shouldConverterStringToBigDecimalWithDot() {
		
		// scenario
		String bigDecimalAsStringWithDot = "100.2";
		
		// action
		BigDecimal convertedValue = BigDecimalUtil.converter(bigDecimalAsStringWithDot);

		// verification
		assertThat(convertedValue).isNotNull();
		assertThat(convertedValue).isEqualTo(new BigDecimal("100.2"));
	}

	@Test
	public void shouldReturnNullWhenTriesConverterNullToBigDecimal() {
		
		// scenario
		String nullValue = null;
		
		// action
		BigDecimal convertedValue = BigDecimalUtil.converter(nullValue);

		// verification
		assertThat(convertedValue).isNull();
	}

	@Test
	public void shouldThrowExceptionWhenTriesConverterUnexpectedValueToBigDecimal() {
		
		// scenario
		String unexpectedValue = "teste";
		
		// action / verification
	    assertThrows(
	    		NumberFormatException.class, () -> {
	    			BigDecimalUtil.converter(unexpectedValue);
	            }
	    );
	    
	}

}
