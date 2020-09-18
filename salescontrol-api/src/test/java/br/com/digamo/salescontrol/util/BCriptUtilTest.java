package br.com.digamo.salescontrol.util;

import org.junit.Assert;
import org.junit.Test;

public class BCriptUtilTest {

	@Test
	public void shouldThrowExceptionWhenPasswordIsNull() {
		try {
			String passwordNull = null;
			BCriptUtil.encript(passwordNull);
			Assert.fail("It shouldn't get to that point!");
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("rawPassword cannot be null", e.getMessage());
		}

		
		
	}
}
