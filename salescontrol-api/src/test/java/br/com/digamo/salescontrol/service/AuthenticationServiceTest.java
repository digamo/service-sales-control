package br.com.digamo.salescontrol.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Locale;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.digamo.salescontrol.controller.dto.UserDto;
import br.com.digamo.salescontrol.exception.AuthenticationException;
import br.com.digamo.salescontrol.exception.CustomerException;
import br.com.digamo.salescontrol.model.entity.Role;
import br.com.digamo.salescontrol.model.entity.Roles;
import br.com.digamo.salescontrol.model.entity.User;
import br.com.digamo.salescontrol.model.repository.RoleRepository;

/**
 * 
 * @author digamo
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthenticationServiceTest {


	@Autowired
	private AuthenticationService authService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
    private MessageSource messageSource;
    
    @AfterEach
    private void end() throws CustomerException {
    	userService.deleteAll();
    	roleRepository.deleteAll();
    }
	
    @Test
	public void shouldGenerateAValidTokenWithSuccess() throws AuthenticationException {

		// scenario	
		String username = "admin";
		String password = "admin@123";
		UserDto userDto = new UserDto(username,password);

		roleRepository.save(new Role(Roles.ADMIN.toString()));
		userService.save(userDto, Roles.ADMIN);
    	 
    	 // action
        String token = authService.generateToken(userDto);

        // verification	
        Assertions.assertThat(token).isNotNull();

	}

    @Test
	public void shouldThrowExptionWhenTheTokenGeneratedIsNotValid() throws AuthenticationException {

		// scenario	
		String username = "x";
		String password = "x";
		UserDto userDto = new UserDto(username,password);
   	 	
		// action
	    Throwable exception = assertThrows(
	    		AuthenticationException.class, () -> {
	            	authService.generateToken(userDto);
	            }
	    );
	 
	    // verification		
	    assertEquals(
	    		messageSource.getMessage(AuthenticationService.INVALID_USER_PASSWORD, null, Locale.getDefault())
	    		, exception.getMessage());

	}

    @Test
	public void shouldVerifyAValidToken() throws AuthenticationException {

		// scenario	
		String username = "admin";
		String password = "admin@123";
		UserDto userDto = new UserDto(username,password);

		roleRepository.save(new Role(Roles.ADMIN.toString()));
		userService.save(userDto, Roles.ADMIN);

        String token = authService.generateToken(userDto);

        // action
        boolean isValidToken = authService.isValidToken(token);

        // verification	
        Assertions.assertThat(isValidToken).isTrue();

	}

    @Test
	public void shouldVerifyAInvalidToken() throws AuthenticationException {

		// scenario	
        String token = "";

        // action
        boolean isValidToken = authService.isValidToken(token);

        // verification	
        Assertions.assertThat(isValidToken).isFalse();

	}

	public void shouldGetTheAuthenticatedUserId() throws AuthenticationException {

		// scenario	
		String username = "admin";
		String password = "admin@123";
		UserDto userDto = new UserDto(username,password);

		roleRepository.save(new Role(Roles.ADMIN.toString()));
		User user = userService.save(userDto, Roles.ADMIN);
    	 
        String token = authService.generateToken(userDto);

        // action
        Long idUserAuth = authService.getUserId(token);
        
        // verification	
        Assertions.assertThat(idUserAuth).isEqualTo(user.getId());
	}

}
