package br.com.digamo.salescontrol.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 
 * @author digamo
 *
 */
public class BCriptUtilTest {

	@Test
	public void shouldReturnStringPasswordEncrypted() {
		
		// scenario
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String password = "digamo@123";
		
		// action
		String encryptedPassword = BCriptUtil.encript(password);

		// verification
		assertThat(encryptedPassword).isNotNull();
		assertThat(encoder.matches(password, encryptedPassword)).isTrue();
	}

	@Test
	public void shouldThrowExceptionWhenTriesEncriptEmptyOrNullValue() {
		
		// scenario
		String emptyPassword = "";
		String nullPassword = "";
		
		// action / verification
	    assertThrows(
	    		IllegalArgumentException.class, () -> {
	    			BCriptUtil.encript(emptyPassword);
	            }
	    );
	    
	    assertThrows(
	    		IllegalArgumentException.class, () -> {
	    			BCriptUtil.encript(nullPassword);
	            }
	    );
	}

	@Test
	public void shouldReturnTrueWhenPasswordsMatch() {
		
		// scenario
		String password = "digamo@123";
		String encryptedPassword = BCriptUtil.encript(password);
		
		// action
		boolean result = BCriptUtil.match(encryptedPassword, password);

		// verification
		assertThat(result).isTrue();
	}

	@Test
	public void shouldReturnFalseWhenPasswordsNotMatch() {
		
		// scenario
		String password = "digamo@123";
		
		// action
		boolean result = BCriptUtil.match("", password);

		// verification
		assertThat(result).isFalse();
	}

	@Test
	public void shouldThrowExceptionWhenTriesMatchPasswordWithNullValues() {
		
		// scenario
		String nullEncodedPassword = null;
		String nullPassword = null;
		
		// action / verification
	    assertThrows(
	    		IllegalArgumentException.class, () -> {
	    			BCriptUtil.match(nullEncodedPassword, "");
	            }
	    );
	    
	    assertThrows(
	    		IllegalArgumentException.class, () -> {
	    			BCriptUtil.match("", nullPassword);
	            }
	    );
	}
}
