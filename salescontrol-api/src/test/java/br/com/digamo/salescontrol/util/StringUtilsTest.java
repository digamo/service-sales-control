package br.com.digamo.salescontrol.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.digamo.salescontrol.controller.dto.UserDto;

/**
 * 
 * @author digamo
 *
 */
public class StringUtilsTest {

	
	@Test
	public void shouldReturnJsonWhenSomeObjectNotEmptyIsPassed() throws JsonProcessingException {
		
		// scenario
		UserDto userDto = new UserDto("userTest", "passwordTest");
		
		// action
		String json = StringUtils.asJsonString(userDto);

		// verification
		assertThat(json).isEqualTo("{\"username\":\"userTest\",\"password\":\"passwordTest\"}");
	}

	@Test
	public void shouldThrowExceptionWhenTriesConverterObjectNullToJson() {
		
		// scenario
		Object nullObject = null;
		
		// action / verification
	    assertThrows(
	    		IllegalArgumentException.class, () -> {
	    			StringUtils.asJsonString(nullObject);
	            }
	    );
	    
	}
}
