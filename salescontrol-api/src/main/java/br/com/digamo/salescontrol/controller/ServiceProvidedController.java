package br.com.digamo.salescontrol.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import br.com.digamo.salescontrol.exception.CustomerException;
import br.com.digamo.salescontrol.exception.ServiceProvidedException;
import br.com.digamo.salescontrol.model.entity.ServiceProvided;
import br.com.digamo.salescontrol.service.ServiceProvidedService;

@RestController
@RequestMapping("/api/service-provided")
public class ServiceProvidedController {

	private final ServiceProvidedService service;

	public ServiceProvidedController(ServiceProvidedService service) {
		this.service = service;
	}

	/**
	 * Receives JSON with not null DTO to register a new ServiceProvided
	 * This DTO parameter has the function of shielding the API so that the entity is not directly accessed
	 * Returns JSON with the new ServiceProvided created and HttpStatus.CREATED (201)
	 * @param customer
	 * @return  
	 * @throws CustomerException 
	 */
	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public ServiceProvided save(@RequestBody @Valid ServiceProvidedDto serviceProvidedDto) {
		
		ServiceProvided serviceSaved = null;
		
		try {
			serviceSaved = service.save(serviceProvidedDto);
		
		} catch (CustomerException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
			
		} catch (ServiceProvidedException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
		
		return serviceSaved;
	}

	/**
	 * Receives @RequestParam customer name and month of service to find a list of service
	 * Returns JSON with the customer; found and HttpStatus.OK (200). If not, returns HttpStatus.NOT_FOUND (404)
	 * @param id
	 * @return 
	 */
	@GetMapping
	public Page<ServiceProvided> search(
			@RequestParam(required = false, defaultValue = "1") Integer page, 
			@RequestParam(required = false, defaultValue = "4") Integer size,
			@RequestParam(value = "customerName", required = false) String customerName, 
			@RequestParam(value = "serviceMonth", required = false) Integer serviceMonth) {

		Pageable pageable =  PageRequest.of(page, size);
		
		return service.findServiceByCustomerNameAndServiceMonth(customerName, serviceMonth, pageable);
	}

}
