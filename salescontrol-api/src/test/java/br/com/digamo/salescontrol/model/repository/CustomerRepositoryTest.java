package br.com.digamo.salescontrol.model.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.digamo.salescontrol.model.entity.Customer;

/**
 * 
 * @author digam
 *
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class CustomerRepositoryTest {

	private static final String NAME_A = "Digamo A";
	private static final String CPF_A = "15552680057"; //fake number
	
	private static final String NAME_B = "Digamo B";
	private static final String CPF_B = "95233857049"; //fake number
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Test
	public void shouldPersistCustomer() {

		// scenario
		Customer customer = new Customer(NAME_A, CPF_A) ; 
		
		// action
		customer = customerRepository.save(customer);

		// verification		
		Assertions.assertThat(customer.getId()).isNotNull();
		Assertions.assertThat(customer.getName()).isEqualTo(NAME_A);
		Assertions.assertThat(customer.getCpf()).isEqualTo(CPF_A);
	}
	
	@Test
	public void shouldUpdateCustomer() {

		// scenario	
		Customer customer = new Customer(NAME_A, CPF_A) ; 
		customerRepository.save(customer);
		
		customer.setName(NAME_B);
		customer.setCpf(CPF_B); 

		// action
		customer = this.customerRepository.save(customer);
		
		// verification		
		Assertions.assertThat(customer.getId()).isNotNull();
		Assertions.assertThat(customer.getName()).isEqualTo(NAME_B);
		Assertions.assertThat(customer.getCpf()).isEqualTo(CPF_B); 
	}

	@Test
	public void shouldRemoveCustomer() {
		
		// scenario	
		Customer customer = new Customer(NAME_A, CPF_A) ; 
		this.customerRepository.save(customer);
		
		// action
		this.customerRepository.delete(customer);
		
		// verification		
		Assertions.assertThat(this.customerRepository.findById(customer.getId()).isPresent()).isFalse();
	}
	
	@Test
	public void shouldFindCustomerWithCustomerName() {
		
		// scenario	
		Customer customer = new Customer(NAME_A, CPF_A) ; 
		customerRepository.save(customer);
		
		// action
		Optional<Customer> customerFoundByName = this.customerRepository.findByNameOrCpf(NAME_A, null);
		
		// verification		
		Assertions.assertThat(customerFoundByName.isPresent()).isTrue();
		Assertions.assertThat(customerFoundByName.get().getName()).isEqualTo(NAME_A);
		Assertions.assertThat(customerFoundByName.get().getCpf()).isEqualTo(CPF_A);

	}

	@Test
	public void shouldFindCustomerWithCustomerCpf() {

		// scenario	
		Customer customer = new Customer(NAME_A, CPF_A) ; 
		customerRepository.save(customer);
		
		// action
		Optional<Customer> customerFoundByCpf = this.customerRepository.findByNameOrCpf(null, CPF_A);
		
		// verification		
		Assertions.assertThat(customerFoundByCpf.isPresent()).isTrue();
		Assertions.assertThat(customerFoundByCpf.get().getName()).isEqualTo(NAME_A);
		Assertions.assertThat(customerFoundByCpf.get().getCpf()).isEqualTo(CPF_A);
	}

	@Test
	public void shouldFindCustomerWithCustomerNameAndCustomerCpf() {
		
		// scenario	
		Customer customer = new Customer(NAME_A, CPF_A) ; 
		customerRepository.save(customer);
		
		// action
		Optional<Customer> customerFoundByNameAndCpf = this.customerRepository.findByNameOrCpf(NAME_A, CPF_A);
		
		// verification		
		Assertions.assertThat(customerFoundByNameAndCpf.isPresent()).isTrue();
		Assertions.assertThat(customerFoundByNameAndCpf.get().getName()).isEqualTo(NAME_A);
		Assertions.assertThat(customerFoundByNameAndCpf.get().getCpf()).isEqualTo(CPF_A);
		
	}

	@Test
	public void shouldFindMoreThenOneCustomerNameWithDifferentId() {
		
		// scenario	
		Customer customerA = new Customer(NAME_A, CPF_A) ; 
		customerRepository.save(customerA);

		Customer customerB = new Customer(NAME_B, CPF_B) ; 
		customerRepository.save(customerB);
		
		//In an attempt to update a customer with a name that already exists
		customerB.setName(NAME_A);
		
		// action
		Optional<Customer> customerFoundByNameAndDifferentId = 
				this.customerRepository.
				findMoreThenOneNameOrCpfWithDifferentId(customerB.getName(), customerB.getName(), customerB.getId());
		
		// verification		
		Assertions.assertThat(customerFoundByNameAndDifferentId.isPresent()).isTrue();
		Assertions.assertThat(customerFoundByNameAndDifferentId.get().getName()).isEqualTo(customerA.getName());
		Assertions.assertThat(customerFoundByNameAndDifferentId.get().getCpf()).isEqualTo(customerA.getCpf());
		
	}

	@Test
	public void shouldFindMoreThenOneCustomerCpfWithDifferentId() {
		
		// scenario	
		Customer customerA = new Customer(NAME_A, CPF_A) ; 
		customerRepository.save(customerA);

		Customer customerB = new Customer(NAME_B, CPF_B) ; 
		customerRepository.save(customerB);
		
		//In an attempt to update a customer with a cpf that already exists
		customerB.setCpf(CPF_A);
		
		// action
		Optional<Customer> customerFoundByCpfAndDifferentId = 
				this.customerRepository.
				findMoreThenOneNameOrCpfWithDifferentId(customerB.getName(), customerB.getCpf(), customerB.getId());
		
		// verification		
		Assertions.assertThat(customerFoundByCpfAndDifferentId.isPresent()).isTrue();
		Assertions.assertThat(customerFoundByCpfAndDifferentId.get().getName()).isEqualTo(customerA.getName());
		Assertions.assertThat(customerFoundByCpfAndDifferentId.get().getCpf()).isEqualTo(customerA.getCpf());
		
	}
	
}



