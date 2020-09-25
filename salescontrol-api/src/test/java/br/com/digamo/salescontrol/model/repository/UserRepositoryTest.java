package br.com.digamo.salescontrol.model.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.digamo.salescontrol.model.entity.User;

/**
 * 
 * @author digam
 *
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;
	
	private static final String USERNAME = "Digamo";
	private static final String PASSWORD = "12345";
	
	@Test
	public void shouldPersistUser() {

		// scenario	
		User user = new User(USERNAME, PASSWORD) ; 

		// action
		user = userRepository.save(user);
		
		// verification				
		Assertions.assertThat(user.getId()).isNotNull();
		Assertions.assertThat(user.getUsername()).isEqualTo(USERNAME);
		Assertions.assertThat(user.getPassword()).isEqualTo(PASSWORD);
	}

	@Test
	public void shouldUpdateUser() {

		// scenario	
		User user = new User(USERNAME, PASSWORD) ; 
		userRepository.save(user);
		
		user.setUsername("Digamo X");
		user.setPassword("67890");
		
		// action
		user= this.userRepository.save(user);
		
		// verification		
		Assertions.assertThat(user.getId()).isNotNull();
		Assertions.assertThat(user.getUsername()).isEqualTo("Digamo X");
		Assertions.assertThat(user.getPassword()).isEqualTo("67890"); 

	}
	
	@Test
	public void shouldRemoveUser() {
		
		// scenario	
		User user = new User(USERNAME, PASSWORD) ; 
		this.userRepository.save(user);
		
		// action
		this.userRepository.delete(user);
		
		// verification		
		Assertions.assertThat(this.userRepository.findById(user.getId()).isPresent()).isFalse();
	}

	@Test
	public void shouldFindUserByUsername() {
		
		// scenario	
		User user = new User(USERNAME, PASSWORD) ; 
		this.userRepository.save(user);
		
		// action
		Optional<User> userFoundByName = this.userRepository.findByUsername(USERNAME);
		
		// verification		
		Assertions.assertThat(userFoundByName.isPresent()).isTrue();
		Assertions.assertThat(userFoundByName.get().getUsername()).isEqualTo(USERNAME);
		Assertions.assertThat(userFoundByName.get().getPassword()).isEqualTo(PASSWORD);

	}

	@Test
	public void shouldVerifyIfUserExistsByUsername() {
		
		// scenario	
		User user = new User(USERNAME, PASSWORD) ; 
		this.userRepository.save(user);
		
		// action
		boolean userExists = this.userRepository.existsByUsername(USERNAME);
		
		// verification		
		Assertions.assertThat(userExists).isTrue();
	}

}
