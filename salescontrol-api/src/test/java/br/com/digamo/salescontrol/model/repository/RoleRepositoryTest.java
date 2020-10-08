package br.com.digamo.salescontrol.model.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.digamo.salescontrol.model.entity.Role;

/**
 * 
 * @author digam
 *
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class RoleRepositoryTest {

	@Autowired
	private RoleRepository roleRepository;

	private static final String ROLE = "ADMIN";
	
	@Test
	public void shouldPersistRole() {

		// scenario	
		Role role = new Role(ROLE) ; 

		// action
		role = roleRepository.save(role);
		
		// verification				
		assertThat(role.getName()).isNotNull();
		assertThat(role.getName()).isEqualTo(ROLE);
	}

	@Test
	public void shouldUpdateRole() {

		// scenario	
		Role role = new Role(ROLE) ; 
		role = roleRepository.save(role);
		
		role.setName("ADMIN_2");
		
		// action
		role = this.roleRepository.save(role);
		
		// verification		
		assertThat(role.getName()).isNotNull();
		assertThat(role.getName()).isEqualTo("ADMIN_2");

	}
	
	@Test
	public void shouldRemoveRole() {
		
		// scenario	
		Role role = new Role(ROLE) ; 
		role = roleRepository.save(role);
		
		// action
		this.roleRepository.delete(role);
		
		// verification		
		assertThat(this.roleRepository.findByName(role.getName()).isPresent()).isFalse();
	}


}
