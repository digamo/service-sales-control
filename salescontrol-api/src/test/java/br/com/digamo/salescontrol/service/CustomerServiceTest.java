package br.com.digamo.salescontrol.service;

import static org.assertj.core.api.Assertions.assertThat;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.digamo.salescontrol.controller.dto.CustomerDto;
import br.com.digamo.salescontrol.exception.CustomerException;
import br.com.digamo.salescontrol.model.entity.Customer;

/**
 * 
 * @author digamo
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerServiceTest {

	private static final String NAME_A = "Digamo A";
	private static final String CPF_A = "15552680057"; //fake number
	
	private static final String NAME_B = "Digamo B";
	private static final String CPF_B = "95233857049"; //fake number
	
	@Autowired
	private CustomerService customerService;

    @Autowired
    private MessageSource messageSource;

    @AfterEach
    private void end() throws CustomerException {
    	customerService.deleteAll();
    }
    
	@Test
	public void shouldSaveNewCustomerWithSuccess() throws CustomerException {

		// scenario
		CustomerDto customerDto = new CustomerDto(null, NAME_A, CPF_A) ; 
		
		// action
		Customer customer = customerService.save(customerDto);

		// verification		
		Assertions.assertThat(customer.getId()).isNotNull();
		Assertions.assertThat(customer.getName()).isEqualTo(NAME_A);
		Assertions.assertThat(customer.getCpf()).isEqualTo(CPF_A);
	}

	@Test
	public void shouldThrowExptionWhenCustomerAlreadyExistWithSameName() throws CustomerException {

		// scenario
		CustomerDto customerDto = new CustomerDto(null, NAME_A, CPF_A) ; 
		customerService.save(customerDto);
		CustomerDto customerDto2 = new CustomerDto(null, NAME_A, CPF_B) ; 
		
		// action
	    Throwable exception = assertThrows(
	            CustomerException.class, () -> {
	            	customerService.save(customerDto2);
	            }
	    );
	 
	    // verification		
	    assertEquals(
	    		messageSource.getMessage(CustomerService.CUSTOMER_NAME_ALREADY_EXISTS, null, Locale.getDefault())
	    		, exception.getMessage());
	    
	}

	@Test
	public void shouldThrowExptionWhenCustomerAlreadyExistWithSameCpf() throws CustomerException {

		// scenario
		CustomerDto customerDto = new CustomerDto(null, NAME_A, CPF_A) ; 
		customerService.save(customerDto);
		CustomerDto customerDto2 = new CustomerDto(null, NAME_B, CPF_A) ; 
		
		// action
	    Throwable exception = assertThrows(
	            CustomerException.class, () -> {
	            	customerService.save(customerDto2);
	            }
	    );
	 
	    // verification		
	    assertEquals(
	    		messageSource.getMessage(CustomerService.CUSTOMER_CPF_ALREADY_EXISTS, null, Locale.getDefault())
	    		, exception.getMessage());
	    
	}

	@Test
	public void shouldFindCustomerById() throws CustomerException {
		
		// scenario
		CustomerDto customerDto = new CustomerDto(null, NAME_A, CPF_A) ; 
		Customer customer = customerService.save(customerDto);
		
		// action
		Customer customerFound = customerService.findById(customer.getId());
		
	    // verification		
		Assertions.assertThat(customerFound.getId()).isNotNull();
		Assertions.assertThat(customerFound.getName()).isEqualTo(NAME_A);
		Assertions.assertThat(customerFound.getCpf()).isEqualTo(CPF_A);
	    
	}

	@Test
	public void shouldThrowExptionWhenCustomerIsNotFindById() throws CustomerException {
		
		// scenario
		Customer customer = new Customer();
		customer.setId(1L);//client id that was not persisted
		
		// action
	    Throwable exception = assertThrows(
	            CustomerException.class, () -> {
	            	customerService.findById(customer.getId());
	            }
	    );
	 
	    // verification		
	    assertEquals(
	    		messageSource.getMessage(CustomerService.CUSTOMER_NOT_FOUND, null, Locale.getDefault())
	    		, exception.getMessage());
	    
	}

	@Test
	public void shoulFindANotEmptyListOfAllCustomers() throws CustomerException {
		// scenario
		CustomerDto customerDto = new CustomerDto(null, NAME_A, CPF_A) ; 
		customerService.save(customerDto);

		CustomerDto customerDto2 = new CustomerDto(null, NAME_B, CPF_B) ; 
		customerService.save(customerDto2);
		
		// action
		List<Customer> customers = customerService.findAll();
		
	    // verification		
		assertThat(customers.size()).isEqualTo(2);
	    
	}

	@Test
	public void shoulFindAEmptyListOfCustomers() throws CustomerException {
		
		// no scenario
		
		// action
		List<Customer> customers = customerService.findAll();
		
	    // verification		
		assertThat(customers.size()).isEqualTo(0);
	    
	}

	
	@Test
	public void shoulFindANotEmptyListOfAllCustomersWithPageable() throws CustomerException {
		
		// scenario
		CustomerDto customerDto = new CustomerDto(null, NAME_A, CPF_A) ; 
		customerService.save(customerDto);

		CustomerDto customerDto2 = new CustomerDto(null, NAME_B, CPF_B) ; 
		customerService.save(customerDto2);

		Pageable pageable =  PageRequest.of(0, 1);
		
		// action
		Page<Customer> pagesCustomer = this.customerService.
				findAll(pageable);

	    // verification		
		Assertions.assertThat(pagesCustomer.getContent().size()).isEqualTo(1);
		Assertions.assertThat(pagesCustomer.getContent().get(0).getName()).isEqualTo(NAME_A);
	}

	@Test
	public void shoulFindAEmptyListOfAllCustomersWithPageable() throws CustomerException {

		//scenario
		Pageable pageable =  PageRequest.of(0, 1);
		
		// action
		Page<Customer> pagesCustomer = this.customerService.
				findAll(pageable);

	    // verification		
		Assertions.assertThat(pagesCustomer.getContent().size()).isEqualTo(0);
	}

	@Test
	public void shoulDeleteCustomerByIdWithSuccess() throws CustomerException {

		// scenario
		CustomerDto customerDto = new CustomerDto(null, NAME_A, CPF_A) ; 
		Customer customer = customerService.save(customerDto);
		Long idCustomer = customer.getId();
		
		// action
		customerService.delete(idCustomer);
		
	    // verification
	    Throwable exception = assertThrows(
	            CustomerException.class, () -> {
	            	customerService.findById(idCustomer);
	            }
	    );

	    assertEquals(
	    		messageSource.getMessage(CustomerService.CUSTOMER_NOT_FOUND, null, Locale.getDefault())
	    		, exception.getMessage());

	}

	@Test
	public void shouldThrowExptionWhenCustomerIsNotFindByIdToBeDeleted() throws CustomerException {
		
		// scenario
		Customer customer = new Customer();
		customer.setId(1L); //id de cliente que não foi persistido
		
		// action
	    Throwable exception = assertThrows(
	            CustomerException.class, () -> {
	            	customerService.delete(customer.getId());
	            }
	    );
	 
	    // verification		
	    assertEquals(
	    		messageSource.getMessage(CustomerService.CUSTOMER_NOT_FOUND, null, Locale.getDefault())
	    		, exception.getMessage());
	    
	}

	@Test
	public void shoulDeleteAllOfCustomers() throws CustomerException {
		
		// scenario
		CustomerDto customerDto = new CustomerDto(null, NAME_A, CPF_A) ; 
		customerService.save(customerDto);

		CustomerDto customerDto2 = new CustomerDto(null, NAME_B, CPF_B) ; 
		customerService.save(customerDto2);

		// action
		this.customerService.deleteAll();

	    // verification		
		Assertions.assertThat(customerService.findAll().size()).isEqualTo(0);
	}

	@Test
	public void shouldUpdateCustomerWithSuccess() throws CustomerException {

		// scenario
		CustomerDto customerDto = new CustomerDto(null, NAME_A, CPF_A) ; 
		Customer customer = customerService.save(customerDto);
		
		CustomerDto newCustomerDto = new CustomerDto(null, NAME_B, CPF_B) ;
		newCustomerDto.setId(customer.getId());
		
		// action
		customerService.update(newCustomerDto);
		
		// verification
		Customer customerUpdated = customerService.findById(customer.getId());
		Assertions.assertThat(customerUpdated.getName()).isEqualTo(NAME_B);
		Assertions.assertThat(customerUpdated.getCpf()).isEqualTo(CPF_B); 
	}

	@Test              
	public void shouldThrowExceptionWhenTryingUpdateCustomerWithTheSameNameAsAnotherThatAlreadyExists() throws CustomerException {
		
		// scenario	
		CustomerDto customerDtoA = new CustomerDto(null, NAME_A, CPF_A) ; 
		customerService.save(customerDtoA);

		CustomerDto customerDtoB = new CustomerDto(null, NAME_B, CPF_B) ; 
		Customer customerB = customerService.save(customerDtoB);

		//In an attempt to update a customer with a name that already exists
		customerDtoB.setName(NAME_A);
		customerDtoB.setId(customerB.getId());
		
		// action
	    Throwable exception = assertThrows(
	            CustomerException.class, () -> {
	            	customerService.update(customerDtoB);
	            }
	    );
	 
	    // verification		
	    assertEquals(
	    		messageSource.getMessage(CustomerService.CUSTOMER_NAME_ALREADY_EXISTS, null, Locale.getDefault())
	    		, exception.getMessage());

	}

	@Test              
	public void shouldThrowExceptionWhenTryingUpdateCustomerWithTheSameCpfAsAnotherThatAlreadyExists() throws CustomerException {
		
		// scenario	
		CustomerDto customerDtoA = new CustomerDto(null, NAME_A, CPF_A) ; 
		customerService.save(customerDtoA);

		CustomerDto customerDtoB = new CustomerDto(null, NAME_B, CPF_B) ; 
		Customer customerB = customerService.save(customerDtoB);

		//In an attempt to update a customer with a name that already exists
		customerDtoB.setCpf(CPF_A);
		customerDtoB.setId(customerB.getId());

		// action
	    Throwable exception = assertThrows(
	            CustomerException.class, () -> {
	            	customerService.update(customerDtoB);
	            }
	    );
	 
	    // verification		
	    assertEquals(
	    		messageSource.getMessage(CustomerService.CUSTOMER_CPF_ALREADY_EXISTS, null, Locale.getDefault())
	    		, exception.getMessage());

	}

}
