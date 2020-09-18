package br.com.digamo.salescontrol.service;

import java.util.Arrays;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.digamo.salescontrol.exception.RegisteredUserException;
import br.com.digamo.salescontrol.model.entity.Role;
import br.com.digamo.salescontrol.model.entity.Roles;
import br.com.digamo.salescontrol.model.entity.User;
import br.com.digamo.salescontrol.model.repository.UserRepository;
import br.com.digamo.salescontrol.util.BCriptUtil;

//Class acionada pelo spring para validar o username
@Service
public class UserService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MessageSource messageSource; 
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User userAtentication = userRepository
				.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Invalid credential."));
	
		return userAtentication;
	}

	public User save(User user, Roles roles) {

		boolean exists = userRepository.existsByUsername(user.getUsername()); 
		
		if(exists)
			throw new RegisteredUserException(messageSource.
					getMessage("user.username.already.registered", 
						new Object[] {user.getUsername()}, Locale.getDefault()));
		
		user.setPassword(BCriptUtil.encript(user.getPassword()));
		user.setRoles(Arrays.asList(new Role(roles.toString())));
		
		return userRepository.save(user);
	}

	
}
