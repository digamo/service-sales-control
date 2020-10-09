package br.com.digamo.salescontrol.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import br.com.digamo.salescontrol.controller.dto.ServiceProvidedDto;
import br.com.digamo.salescontrol.exception.CustomerException;
import br.com.digamo.salescontrol.exception.ServiceProvidedException;
import br.com.digamo.salescontrol.model.entity.Customer;
import br.com.digamo.salescontrol.model.entity.ServiceProvided;
import br.com.digamo.salescontrol.util.BigDecimalConverter;

/**
 * 
 * @author digamo
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceProvidedServiceTest {

	@Autowired
	private ServiceProvidedService service;

	@Autowired
	private CustomerService customerService;

    @Autowired
    private MessageSource messageSource;

	private static final String NAME_A = "Digamo A";
	private static final String CPF_A = "15552680057"; //fake number

    @AfterEach
    private void end() throws CustomerException {
    	service.deleteAll();
    	customerService.deleteAll();
    }

	@Test
	public void shouldSaveNewServiceProvidedWithSuccess() throws CustomerException, ServiceProvidedException {
		
		// scenario
		CustomerDto customerDto = new CustomerDto(null, NAME_A, CPF_A) ; //fake number 
		Customer customer = customerService.save(customerDto);
		
		ServiceProvidedDto serviceProvidedDto = new ServiceProvidedDto();
		serviceProvidedDto.setDescription("Description Test");
		serviceProvidedDto.setIdCustomer(customer.getId());
		serviceProvidedDto.setDateService("2020-01-01");
		serviceProvidedDto.setValue("100.0");
		
		// action
		ServiceProvided serviceProvided = service.save(serviceProvidedDto);
		
		// verification
		Assertions.assertThat(serviceProvided.getId()).isNotNull();
		Assertions.assertThat(serviceProvided.getCustomer().getId()).isEqualTo(customer.getId());
		Assertions.assertThat(serviceProvided.getDescription()).isEqualTo(serviceProvidedDto.getDescription());
		Assertions.assertThat(serviceProvided.getValue()).isEqualTo(BigDecimalConverter.converter(serviceProvidedDto.getValue()));
		Assertions.assertThat(serviceProvided.getDateService()).isEqualTo(LocalDate.parse(serviceProvidedDto.getDateService(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
	}
	
	@Test
	public void shouldThrowExptionWhenServiceProvidedReceveDateServiceInWrongFormat() throws CustomerException, ServiceProvidedException {

		// scenario
		CustomerDto customerDto = new CustomerDto(null, NAME_A, CPF_A) ; //fake number 
		Customer customer = customerService.save(customerDto);
		
		ServiceProvidedDto serviceProvidedDto = new ServiceProvidedDto();
		serviceProvidedDto.setDescription("Description Test");
		serviceProvidedDto.setIdCustomer(customer.getId());
		serviceProvidedDto.setDateService("01/01/2020"); // date with wrong format
		serviceProvidedDto.setValue("100.0");
		
		// action
	    Throwable exception = assertThrows(
	    		ServiceProvidedException.class, () -> {
	            	service.save(serviceProvidedDto);
	            }
	    );
	 
	    // verification		
	    assertEquals(
	    		messageSource.getMessage(ServiceProvidedService.INCORRECT_DATE_FORMAT, null, Locale.getDefault())
	    		, exception.getMessage());
	    
	}

	@Test
	public void shouldThrowExptionWhenServiceProvidedReceveValueServiceInWrongFormat() throws CustomerException, ServiceProvidedException {

		// scenario
		CustomerDto customerDto = new CustomerDto(null, NAME_A, CPF_A) ; //fake number 
		Customer customer = customerService.save(customerDto);
		
		ServiceProvidedDto serviceProvidedDto = new ServiceProvidedDto();
		serviceProvidedDto.setDescription("Description Test");
		serviceProvidedDto.setIdCustomer(customer.getId());
		serviceProvidedDto.setDateService("2020-01-01");
		serviceProvidedDto.setValue("N/A");  // value with wrong value
		
		// action
	    Throwable exception = assertThrows(
	    		ServiceProvidedException.class, () -> {
	            	service.save(serviceProvidedDto);
	            }
	    );
	 
	    // verification		
	    assertEquals(
	    		messageSource.getMessage(ServiceProvidedService.INCORRECT_VALUE_FORMAT, null, Locale.getDefault())
	    		, exception.getMessage());
	    
	}

	
	@Test
	public void shouldThrowExptionWhenServiceProvidedReceveIdCustomerThatIsNotFound() throws CustomerException {

		// scenario
		Customer customer = new Customer();
		customer.setId(1L);//client id that was not persisted
		
		ServiceProvidedDto serviceProvidedDto = new ServiceProvidedDto();
		serviceProvidedDto.setDescription("Description Test");
		serviceProvidedDto.setIdCustomer(customer.getId());
		serviceProvidedDto.setDateService("2020-01-01");
		serviceProvidedDto.setValue("100.0");
		
		// action
	    Throwable exception = assertThrows(
	            CustomerException.class, () -> {
	            	service.save(serviceProvidedDto);
	            }
	    );
	 
	    // verification		
	    assertEquals(
	    		messageSource.getMessage(CustomerService.CUSTOMER_NOT_FOUND, null, Locale.getDefault())
	    		, exception.getMessage());
	    
	}

	
	@Test
	public void shoulDeleteAllOfServicesProvided() throws CustomerException, ServiceProvidedException {
		
		// scenario
		CustomerDto customerDto = new CustomerDto(null, NAME_A, CPF_A) ; //fake number 
		Customer customer = customerService.save(customerDto);
		
		ServiceProvidedDto serviceProvidedDto = new ServiceProvidedDto();
		serviceProvidedDto.setDescription("Description Test");
		serviceProvidedDto.setIdCustomer(customer.getId());
		serviceProvidedDto.setDateService("2020-01-01");
		serviceProvidedDto.setValue("100.0");
		
		service.save(serviceProvidedDto);

		// action
		service.deleteAll();

	    // verification		
		Assertions.assertThat(service.findAll().size()).isEqualTo(0);
	}

	@Test
	public void shoulFindAllOfServicesProvided() throws CustomerException, ServiceProvidedException {
		
		// scenario
		CustomerDto customerDto = new CustomerDto(null, NAME_A, CPF_A) ; //fake number 
		Customer customer = customerService.save(customerDto);
		
		ServiceProvidedDto serviceProvidedDto = new ServiceProvidedDto();
		serviceProvidedDto.setDescription("Description Test");
		serviceProvidedDto.setIdCustomer(customer.getId());
		serviceProvidedDto.setDateService("2020-01-01");
		serviceProvidedDto.setValue("100.0");
		
		service.save(serviceProvidedDto);

		// action
		List<ServiceProvided> listOfServicesProvided = service.findAll();

	    // verification		
		Assertions.assertThat(listOfServicesProvided.size()).isEqualTo(1);
	}

	@Test
	public void shoulFindAListOfAllServicesProvidedWithPageableComparingByCustomerNameCriteria() throws CustomerException, ServiceProvidedException {
		
		// scenario
		CustomerDto customerDto = new CustomerDto(null, NAME_A, CPF_A) ; //fake number 
		Customer customer = customerService.save(customerDto);
		
		ServiceProvidedDto serviceProvidedDto1 = new ServiceProvidedDto();
		serviceProvidedDto1.setDescription("Description Test 1");
		serviceProvidedDto1.setIdCustomer(customer.getId());
		serviceProvidedDto1.setDateService("2020-01-01");
		serviceProvidedDto1.setValue("100.0");
		
		service.save(serviceProvidedDto1);

		ServiceProvidedDto serviceProvidedDto2 = new ServiceProvidedDto();
		serviceProvidedDto2.setDescription("Description Test 2");
		serviceProvidedDto2.setIdCustomer(customer.getId());
		serviceProvidedDto2.setDateService("2020-02-02");
		serviceProvidedDto2.setValue("200.0");
		
		service.save(serviceProvidedDto2);

		Pageable pageable =  PageRequest.of(0, 1);
		String customerName = "Digamo";
		Integer serviceMonth = null;
		
		// action
		Page<ServiceProvided> pagesCustomer = service.
				findServiceByCustomerNameAndServiceMonth(customerName, serviceMonth, pageable);

	    // verification		
		Assertions.assertThat(pagesCustomer.getContent().size()).isEqualTo(1);
		Assertions.assertThat(pagesCustomer.getContent().get(0).getCustomer().getName()).isEqualTo(customer.getName());
	}

	@Test
	public void shoulFindAListOfAllServicesProvidedWithPageableComparingByServiceMonthCriteria() throws CustomerException, ServiceProvidedException {
		
		// scenario
		CustomerDto customerDto = new CustomerDto(null, NAME_A, CPF_A) ; //fake number 
		Customer customer = customerService.save(customerDto);
		
		ServiceProvidedDto serviceProvidedDto1 = new ServiceProvidedDto();
		serviceProvidedDto1.setDescription("Description Test 1");
		serviceProvidedDto1.setIdCustomer(customer.getId());
		serviceProvidedDto1.setDateService("2020-01-01");
		serviceProvidedDto1.setValue("100.0");
		
		service.save(serviceProvidedDto1);

		ServiceProvidedDto serviceProvidedDto2 = new ServiceProvidedDto();
		serviceProvidedDto2.setDescription("Description Test 2");
		serviceProvidedDto2.setIdCustomer(customer.getId());
		serviceProvidedDto2.setDateService("2020-02-02");
		serviceProvidedDto2.setValue("200.0");
		
		ServiceProvided serviceProvided2 = service.save(serviceProvidedDto2);

		Pageable pageable =  PageRequest.of(0, 1);
		String customerName = null;
		Integer serviceMonth = 2;
		
		// action
		Page<ServiceProvided> pagesCustomer = service.
				findServiceByCustomerNameAndServiceMonth(customerName, serviceMonth, pageable);

	    // verification		
		Assertions.assertThat(pagesCustomer.getContent().size()).isEqualTo(1);
		Assertions.assertThat(pagesCustomer.getContent().get(0).getDateService().getMonthValue()).isEqualTo(serviceProvided2.getDateService().getMonthValue());
	}

	@Test
	public void shoulFindAListOfAllServicesProvidedWithPageableComparingByServiceMonthAndCustomerNameCriteria() throws CustomerException, ServiceProvidedException {
		
		// scenario
		CustomerDto customerDto = new CustomerDto(null, NAME_A, CPF_A) ; //fake number 
		Customer customer = customerService.save(customerDto);
		
		ServiceProvidedDto serviceProvidedDto1 = new ServiceProvidedDto();
		serviceProvidedDto1.setDescription("Description Test 1");
		serviceProvidedDto1.setIdCustomer(customer.getId());
		serviceProvidedDto1.setDateService("2020-01-01");
		serviceProvidedDto1.setValue("100.0");
		
		service.save(serviceProvidedDto1);

		ServiceProvidedDto serviceProvidedDto2 = new ServiceProvidedDto();
		serviceProvidedDto2.setDescription("Description Test 2");
		serviceProvidedDto2.setIdCustomer(customer.getId());
		serviceProvidedDto2.setDateService("2020-02-02");
		serviceProvidedDto2.setValue("200.0");
		
		ServiceProvided serviceProvided2 = service.save(serviceProvidedDto2);

		Pageable pageable =  PageRequest.of(0, 1);
		String customerName = "Digamo";
		Integer serviceMonth = 2;
		
		// action
		Page<ServiceProvided> pagesCustomer = service.
				findServiceByCustomerNameAndServiceMonth(customerName, serviceMonth, pageable);

	    // verification		
		Assertions.assertThat(pagesCustomer.getContent().size()).isEqualTo(1);
		Assertions.assertThat(pagesCustomer.getContent().get(0).getDateService().getMonthValue()).isEqualTo(serviceProvided2.getDateService().getMonthValue());
		Assertions.assertThat(pagesCustomer.getContent().get(0).getCustomer().getName()).isEqualTo(customer.getName());
	}

}
