package br.com.digamo.salescontrol.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.digamo.salescontrol.controller.dto.ServiceProvidedDto;
import br.com.digamo.salescontrol.model.entity.Customer;
import br.com.digamo.salescontrol.model.entity.ServiceProvided;
import br.com.digamo.salescontrol.model.repository.CustomerRepository;
import br.com.digamo.salescontrol.model.repository.ServiceProvidedRepository;
import br.com.digamo.salescontrol.model.repository.custom.ServiceProvidedCustomRepository;
import br.com.digamo.salescontrol.util.BigDecimalConverter;

@RestController
@RequestMapping("/api/service-provided")
public class ServiceProvidedController {

	@Autowired
	private ServiceProvidedRepository repository;

	@Autowired
	private ServiceProvidedCustomRepository repositoryCustom;

	@Autowired
	private CustomerRepository customerRepository;
	
	/**
	 * Receives JSON with not null DTO to register a new ServiceProvided
	 * Returns JSON with the new ServiceProvided created and HttpStatus.CREATED (201)
	 * @param customer
	 * @return  
	 */
	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public ServiceProvided save(@RequestBody @Valid ServiceProvidedDto serviceProvidedDto) {
		
		LocalDate dateFormated = null;
		
		try {
			dateFormated = LocalDate.parse(serviceProvidedDto.getDateService(), DateTimeFormatter.ofPattern("dd/MM/yyyy")); 
		
		}catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato de data errado.");
		}
		
		Customer customer = customerRepository
				.findById(serviceProvidedDto.getIdCustomer())
				.orElseThrow( () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cliente inexistente."));
		
		ServiceProvided serviceProvided = new ServiceProvided();
		serviceProvided.setDescription(serviceProvidedDto.getDescription());
		serviceProvided.setDateService(dateFormated);
		serviceProvided.setCustomer(customer);
		serviceProvided.setValue(BigDecimalConverter.converter(serviceProvidedDto.getValue()));
		
		repository.save(serviceProvided);
		
		return serviceProvided;
		
	}

	/**
	 * Receives @RequestParam customer name and month of service to find a list of service
	 * Returns JSON with the customer; found and HttpStatus.OK (200). If not, returns HttpStatus.NOT_FOUND (404)
	 * @param id
	 * @return 
	 */
	@GetMapping
	public List<ServiceProvided> search(
			@RequestParam(value = "customerName", required = false) String customerName, 
			@RequestParam(value = "serviceMonth", required = false) Integer serviceMonth) {
		
		return repositoryCustom.findServiceByCustomerNameAndServiceMonth(customerName, serviceMonth);
	}

}
