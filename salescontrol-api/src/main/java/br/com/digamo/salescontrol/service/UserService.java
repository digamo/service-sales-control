package br.com.digamo.salescontrol.service;

import java.util.Arrays;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.digamo.salescontrol.controller.dto.UserDto;
import br.com.digamo.salescontrol.exception.CustomerException;
import br.com.digamo.salescontrol.exception.RegisteredUserException;
import br.com.digamo.salescontrol.model.entity.Role;
import br.com.digamo.salescontrol.model.entity.Roles;
import br.com.digamo.salescontrol.model.entity.User;
import br.com.digamo.salescontrol.model.repository.UserRepository;
import br.com.digamo.salescontrol.util.BCriptUtil;

//Class acionada pelo spring para validar o username
@Service
public class UserService implements UserDetailsService{

	private final static String USERNAME_ALREADY_REGISTRED = "user.username.already.registered";
	private final static String INVALID_CREDENTIAL = "invalid.credential";
	
	private final UserRepository userRepository;

	private final MessageSource messageSource; 

	public UserService(UserRepository userRepository, MessageSource messageSource) {
		this.userRepository = userRepository;
		this.messageSource = messageSource;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User userAtentication = userRepository
				.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(INVALID_CREDENTIAL));
	
		return userAtentication;
	}

	/**
	 * Saves a new service according to the parameters userDto and roles passed
	 * @param userDto
	 * @param roles
	 * @return
	 */
	public User save(UserDto userDto, Roles roles) {

		User newUser = userDto.convertToEntity();
		
		boolean exists = userRepository.existsByUsername(newUser.getUsername()); 

		//If the user already exists, an exception will be thrown
		if(exists)
			throw new RegisteredUserException(messageSource.
					getMessage(USERNAME_ALREADY_REGISTRED, 
						new Object[] {newUser.getUsername()}, Locale.getDefault()));
		
		//Register the user's password in encrypted form
		newUser.setPassword(BCriptUtil.encript(newUser.getPassword()));
		
		//Register user role
		newUser.setRoles(Arrays.asList(new Role(roles.toString())));
		
		return userRepository.save(newUser);
	}
	
	/**
	 * 
	 * @throws CustomerException
	 */
	public void deleteAll() throws CustomerException {

		userRepository.deleteAll();
	}


}
