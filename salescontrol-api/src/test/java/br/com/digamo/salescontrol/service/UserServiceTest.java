package br.com.digamo.salescontrol.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Locale;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.digamo.salescontrol.controller.dto.UserDto;
import br.com.digamo.salescontrol.exception.CustomerException;
import br.com.digamo.salescontrol.exception.RegisteredUserException;
import br.com.digamo.salescontrol.exception.ServiceProvidedException;
import br.com.digamo.salescontrol.model.entity.Role;
import br.com.digamo.salescontrol.model.entity.Roles;
import br.com.digamo.salescontrol.model.entity.User;
import br.com.digamo.salescontrol.model.repository.RoleRepository;
import br.com.digamo.salescontrol.util.BCriptUtil;

/**
 * 
 * @author digamo
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleRepository roleRepository;

    @Autowired
    private MessageSource messageSource;

    @AfterEach
    private void end() throws CustomerException {
    	userService.deleteAll();
    }

    
	@Test
	public void shouldReturnUserObjectByUsername() {

		// scenario
		String username = "admin";
		String password = "admin@123";
		UserDto userDto = new UserDto(username,password);

		roleRepository.save(new Role(Roles.ADMIN.toString()));
		userService.save(userDto, Roles.ADMIN);
		
		// action
		User user = (User) userService.loadUserByUsername(username);

		// verification		
		Assertions.assertThat(user.getId()).isNotNull();
		Assertions.assertThat(user.getUsername()).isEqualTo(username);
	}

	@Test
	public void shouldThrowExptionWhenUsernameDoNotExist() throws CustomerException {

		// scenario
		String username = "admin"; //username that was not persisted
		
		// action
	    Throwable exception = assertThrows(
	    		UsernameNotFoundException.class, () -> {
	            	userService.loadUserByUsername(username);
	            }
	    );
	 
	    // verification		
	    assertEquals(
	    		messageSource.getMessage(UserService.INVALID_CREDENTIAL, null, Locale.getDefault())
	    		, exception.getMessage());
	    
	}

	@Test
	public void shouldSaveNewUserWithSuccess() throws CustomerException, ServiceProvidedException {
		
		// scenario
		String username = "admin";
		String password = "admin@123";
		UserDto userDto = new UserDto(username,password);

		roleRepository.save(new Role(Roles.ADMIN.toString()));
		
		// action
		User user = userService.save(userDto, Roles.ADMIN);
		
		// verification
		Assertions.assertThat(user.getId()).isNotNull();
		Assertions.assertThat(user.getUsername()).isEqualTo(username);
		Assertions.assertThat(BCriptUtil.match(user.getPassword(), password)).isTrue();
	}

	@Test
	public void shouldThrowExptionWhenUsernameAlreadyExists() throws CustomerException {

		// scenario
		String username = "admin";
		String password = "admin@123";
		UserDto userDto1 = new UserDto(username,password);

		roleRepository.save(new Role(Roles.ADMIN.toString()));
		userService.save(userDto1, Roles.ADMIN);
		
		UserDto userDto2 = new UserDto(username,password);
		roleRepository.save(new Role(Roles.ADMIN.toString()));

		// action
	    Throwable exception = assertThrows(
	    		RegisteredUserException.class, () -> {
	    			userService.save(userDto2, Roles.ADMIN);
	            }
	    );
	 
	    // verification		
	    assertEquals(
	    		messageSource.getMessage(UserService.USERNAME_ALREADY_REGISTRED, new Object[] {username}, Locale.getDefault())
	    		, exception.getMessage());
	    
	}

	@Test
	public void shoulDeleteAllOfUsers() throws CustomerException, ServiceProvidedException {
		
		// scenario
		String username = "admin";
		String password = "admin@123";
		UserDto userDto = new UserDto(username,password);

		roleRepository.save(new Role(Roles.ADMIN.toString()));
		userService.save(userDto, Roles.ADMIN);
		
		// action
		userService.deleteAll();

	    // verification		
		Assertions.assertThat(userService.findAll().size()).isEqualTo(0);
	}

	@Test
	public void shoulFindAllOfUsers() throws CustomerException, ServiceProvidedException {
		
		// scenario
		String username = "admin";
		String password = "admin@123";
		UserDto userDto = new UserDto(username,password);

		roleRepository.save(new Role(Roles.ADMIN.toString()));
		userService.save(userDto, Roles.ADMIN);

		// action
		List<User> listOfUsers = userService.findAll();

	    // verification		
		Assertions.assertThat(listOfUsers.size()).isEqualTo(1);
	}

}
