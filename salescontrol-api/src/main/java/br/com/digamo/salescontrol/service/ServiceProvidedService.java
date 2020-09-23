package br.com.digamo.salescontrol.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.digamo.salescontrol.controller.dto.ServiceProvidedDto;
import br.com.digamo.salescontrol.exception.CustomerException;
import br.com.digamo.salescontrol.exception.ServiceProvidedException;
import br.com.digamo.salescontrol.model.entity.Customer;
import br.com.digamo.salescontrol.model.entity.ServiceProvided;
import br.com.digamo.salescontrol.model.repository.ServiceProvidedRepository;
import br.com.digamo.salescontrol.model.repository.ServiceProvidedRepositoryCustom;
import br.com.digamo.salescontrol.util.BigDecimalConverter;

@Service
public class ServiceProvidedService {

	private final static String INCORRECT_DATE_FORMAT = "incorrect.date.format";
	private final static String INCORRECT_VALUE_FORMAT = "incorrect.value.format";
	
	private final MessageSource messageSource;
	private final ServiceProvidedRepository repository; 
	private final CustomerService customerService;
	
	public ServiceProvidedService(MessageSource messageSource, ServiceProvidedRepository repository,
			CustomerService customerService) {
		this.messageSource = messageSource;
		this.repository = repository;
		this.customerService = customerService;
	}

	/**
	 * Saves a new service according to the parameter serviceProvidedDto passed
	 * @param serviceProvidedDto
	 * @return
	 * @throws CustomerException
	 * @throws ServiceProvidedException
	 */
	public ServiceProvided save(ServiceProvidedDto serviceProvidedDto) throws CustomerException, ServiceProvidedException {
		
		//Search the customer for the selected customer's id
		Customer customer = customerService
				.findById(serviceProvidedDto.getIdCustomer());

		ServiceProvided serviceProvided = new ServiceProvided();
		serviceProvided.setDescription(serviceProvidedDto.getDescription());
		serviceProvided.setCustomer(customer);

		try {
			//Formats the String DateService, expected in the format "yyyy-MM-dd", for LocalDate type
			LocalDate formattedDate = LocalDate.parse(serviceProvidedDto.getDateService(), DateTimeFormatter.ofPattern("yyyy-MM-dd")); 
			serviceProvided.setDateService(formattedDate);
		
		}catch (Exception e) {
			throw new ServiceProvidedException(
					messageSource.getMessage(INCORRECT_DATE_FORMAT, null, Locale.getDefault()));
		}
		
		try {
			//Formats the String Value, expected in Number format, for the BigDecimal type
			BigDecimal formattedValue = BigDecimalConverter.converter(serviceProvidedDto.getValue());  
			serviceProvided.setValue(formattedValue);
		
		}catch (Exception e) {
			throw new ServiceProvidedException(
					messageSource.getMessage(INCORRECT_VALUE_FORMAT, null, Locale.getDefault()));			
		}
		
		repository.save(serviceProvided);
		
		return serviceProvided;
		
	}

	/**
	 * Search for the service through the customerName and serviceMonth filter parameters
	 * The filter are not required
	 * @param customerName
	 * @param serviceMonth
	 * @return
	 */
	public Page<ServiceProvided> findServiceByCustomerNameAndServiceMonth(String customerName, Integer serviceMonth, int page, int size) {

		Pageable pageable =  PageRequest.of(page, size);
		
		return repository.findServiceByCustomerNameAndServiceMonth(customerName, serviceMonth, pageable);

	}
}
