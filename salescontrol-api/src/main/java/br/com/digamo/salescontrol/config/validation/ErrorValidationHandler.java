package br.com.digamo.salescontrol.config.validation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

/**
 * Class that will be called every time an exception occurs in any method
 * from any controller. Spring will call this class, as an interceptor,
 * where appropriate error handling will be done.
 * 
 * @author digamo
 *
 */
@RestControllerAdvice
public class ErrorValidationHandler {

	@Autowired
	private MessageSource messageSource; 
	
	/**
	* Method that will handle the error of each request
	* @ExceptionHandler is an annotation that tells Spring that this method should be called when an exception is seen in a controller
	* MethodArgumentNotValidException.class is the type of exception that can occur within any controller. In this case, it is a form validation exception.
	* The return status of the request with error will be: HttpStatus.BAD_REQUEST
	* @param methodArgumentNotValidException (where it contains the thrown errors)
	* @return
	*/
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public List<Error> handleValidationException( MethodArgumentNotValidException methodArgumentNotValidException ) {
		
		List<Error> dtoErrors = new ArrayList<>();
		List<FieldError> fieldErrors = methodArgumentNotValidException.getBindingResult().getFieldErrors();
		
		fieldErrors.forEach(e -> {
			String msg = messageSource.getMessage(e, LocaleContextHolder.getLocale());
			Error error = new Error(e.getField(), msg); 
			
			dtoErrors.add(error);
		});
		
		return dtoErrors;
	}
	
	/**
	* Method that will handle the error of each request
	* @ExceptionHandler is an annotation that tells Spring that this method should be called when an exception is seen in a controller
	* MethodArgumentNotValidException.class is the type of exception that can occur within any controller. In this case, it is a form validation exception.
	* The return status of the request with error will be: HttpStatus.BAD_REQUEST
	* @param methodArgumentNotValidException (where it contains the thrown errors)
	* @return
	*/
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ResponseStatusException.class)
	public List<Error> handleResponseStatusException( ResponseStatusException responseStatusException ) {
		
		List<Error> dtoErrors = new ArrayList<>();
		
		dtoErrors.add(new Error("", responseStatusException.getReason()));
		
		return dtoErrors;
	}
	
}
