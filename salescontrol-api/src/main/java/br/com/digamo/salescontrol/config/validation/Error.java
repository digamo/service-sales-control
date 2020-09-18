package br.com.digamo.salescontrol.config.validation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class that will represent the validation error
 * @author digamo
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Error {
	
	private String field;
	private String message;
	
}
