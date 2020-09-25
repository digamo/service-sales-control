package br.com.digamo.salescontrol.model.repository;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.digamo.salescontrol.model.entity.Customer;
import br.com.digamo.salescontrol.model.entity.ServiceProvided;

/**
 * 
 * @author digam
 *
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class ServiceProvidedRepositoryTest {

	@Autowired
	private ServiceProvidedRepository serviceProvidedRepository;

	@Autowired
	private CustomerRepository customerRepository;
	
	private static final String DESCRIPTION = "TEST SERVICE PROVIDED";
	private static final BigDecimal VALUE = new BigDecimal(100.0);
	private static final LocalDate DATE_SERVICE = LocalDate.now();

	private static final String DESCRIPTION_2 = "TEST SERVICE PROVIDED 2";
	private static final BigDecimal VALUE_2 = new BigDecimal(200.0);
	private static final LocalDate DATE_SERVICE_2 = LocalDate.now().minusMonths(1L);

	private static final String NAME_CUSTOMER = "Digamo A";
	private static final String CPF_CUSTOMER = "15552680057"; //fake number
	
	private static final String NAME_CUSTOMER_2 = "Digamo B";
	private static final String CPF_CUSTOMER_2 = "95233857049"; //fake number
	
	@Test
	public void shouldPersistServiceProvided() {

		// scenario		
		Customer customer = customerRepository.save(new Customer(NAME_CUSTOMER, CPF_CUSTOMER));
		ServiceProvided serviceProvided = new ServiceProvided(
				DESCRIPTION,
				VALUE,
				DATE_SERVICE,
				customer);

		// action
		serviceProvided = serviceProvidedRepository.save(serviceProvided);
		
		// verification		
		Assertions.assertThat(serviceProvided.getId()).isNotNull();
		Assertions.assertThat(serviceProvided.getDescription()).isEqualTo(DESCRIPTION);
		Assertions.assertThat(serviceProvided.getValue()).isEqualTo(VALUE);
		Assertions.assertThat(serviceProvided.getDateService()).isEqualTo(DATE_SERVICE);
		Assertions.assertThat(serviceProvided.getCustomer().getId()).isEqualTo(customer.getId());
	}
	
	@Test
	public void shouldUpdateServiceProvided() {
		
		// scenario		
		//First persist
		Customer customer = customerRepository.save(new Customer(NAME_CUSTOMER, CPF_CUSTOMER));
		ServiceProvided serviceProvided = new ServiceProvided(	DESCRIPTION,
																VALUE,
																DATE_SERVICE,
																customer);
		
		serviceProvided = serviceProvidedRepository.save(serviceProvided);

		//Second persistence changing data
		Customer customer_2 = customerRepository.save(new Customer(NAME_CUSTOMER_2, CPF_CUSTOMER_2));
		serviceProvided.setDescription(DESCRIPTION_2);
		serviceProvided.setValue(VALUE_2);
		serviceProvided.setDateService(DATE_SERVICE_2);
		serviceProvided.setCustomer(customer_2);
		
		// action
		serviceProvided = serviceProvidedRepository.save(serviceProvided);
		
		// verification		
		Assertions.assertThat(serviceProvided.getId()).isNotNull();
		Assertions.assertThat(serviceProvided.getDescription()).isEqualTo(DESCRIPTION_2);
		Assertions.assertThat(serviceProvided.getValue()).isEqualTo(VALUE_2);
		Assertions.assertThat(serviceProvided.getDateService()).isEqualTo(DATE_SERVICE_2);
		Assertions.assertThat(serviceProvided.getCustomer().getId()).isEqualTo(customer_2.getId());
	}


	@Test
	public void shouldRemoveServiceProvided() {
		
		// scenario		
		Customer customer = customerRepository.save(new Customer(NAME_CUSTOMER, CPF_CUSTOMER));
		ServiceProvided serviceProvided = new ServiceProvided(	DESCRIPTION,
																VALUE,
																DATE_SERVICE,
																customer);
		this.serviceProvidedRepository.save(serviceProvided);
		
		// action
		this.serviceProvidedRepository.delete(serviceProvided);
		
		// verification		
		Assertions.assertThat(this.serviceProvidedRepository
				.findById(serviceProvided.getId()).isPresent()).isFalse();
	}

	@Test
	public void shouldFindServiceProvidedByCustomerNameWithLikeCommand() {
		
		// scenario		
		Customer customer = customerRepository.save(new Customer(NAME_CUSTOMER, CPF_CUSTOMER));
		ServiceProvided serviceProvided = new ServiceProvided(	DESCRIPTION,
																VALUE,
																DATE_SERVICE,
																customer);
		this.serviceProvidedRepository.save(serviceProvided);
		String partOfCustomerName = NAME_CUSTOMER.substring(0, 2);
		Pageable pageable =  PageRequest.of(0, 1);
		
		// action
		Page<ServiceProvided> pagesService = this.serviceProvidedRepository.
				findServiceByCustomerNameAndServiceMonth(	partOfCustomerName, 
															null, 
															pageable);
		
		// verification		
		Assertions.assertThat(pagesService.getContent().size()).isEqualTo(1);
		Assertions.assertThat(pagesService.getContent().get(0).getCustomer().getName()).isEqualTo(NAME_CUSTOMER);
	}
	
	@Test
	public void shouldFindServiceProvidedByMonthOfDateService() {
		
		// scenario		
		Customer customer = customerRepository.save(new Customer(NAME_CUSTOMER, CPF_CUSTOMER));
		ServiceProvided serviceProvided = new ServiceProvided(	DESCRIPTION,
																VALUE,
																DATE_SERVICE,
																customer);
		this.serviceProvidedRepository.save(serviceProvided);
		int monthOfDateService = DATE_SERVICE.getMonthValue();
		Pageable pageable =  PageRequest.of(0, 1);
		
		// action
		Page<ServiceProvided> pagesService = this.serviceProvidedRepository.
				findServiceByCustomerNameAndServiceMonth(	null, 
															monthOfDateService, 
															pageable);
		
		// verification		
		Assertions.assertThat(pagesService.getContent().size()).isEqualTo(1);
		Assertions.assertThat(pagesService.getContent().get(0).getDateService().getMonthValue()).isEqualTo(monthOfDateService);
	}
}
