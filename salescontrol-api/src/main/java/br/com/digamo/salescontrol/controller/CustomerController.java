package br.com.digamo.salescontrol.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.digamo.salescontrol.controller.dto.CustomerDto;
import br.com.digamo.salescontrol.exception.CustomerException;
import br.com.digamo.salescontrol.model.entity.Customer;
import br.com.digamo.salescontrol.service.CustomerService;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

	private final CustomerService customerService;

	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}

	/**
	 * Receives JSON with not null fields to register a new customerDto
	 * This DTO parameter has the function of shielding the API so that the entity is not directly accessed
	 * Returns JSON with the new customer created and HttpStatus.CREATED (201)
	 * @param customerDto
	 * @return  
	 */
	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public Customer save(@RequestBody @Valid CustomerDto customerDto ) {

		Customer customerSaved = null;
		
		try {
			customerSaved = customerService.save(customerDto);
			
		} catch (CustomerException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
		
		return customerSaved;
	}

	/**
	 * Receives @PathVariable id to find a customer
	 * Returns JSON with the customer found and HttpStatus.OK (200). If not, returns HttpStatus.NOT_FOUND (404)
	 * @param id
	 * @return 
	 */
	@GetMapping("{id}")
	public Customer findById(@PathVariable Long id ) {
		
		Customer customer = null;
		
		try {
			customer = customerService.findById(id);
					
		} catch (CustomerException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
		
		return customer;

	}

	/**
	 * Returns JSON with the list of customer with Pageable and HttpStatus.OK (200). If not, a empty list
	 * @param page
	 * @param size
	 * @return 
	 */
	@GetMapping
	public Page<Customer> listWithPageable(
			@RequestParam(required = false, defaultValue = "0") Integer page, 
			@RequestParam(required = false, defaultValue = "10") Integer size ){

		Pageable pagination =  PageRequest.of(page, size);
		
		Page<Customer> customers = customerService.findAll(pagination);
		
		return customers;

	}

	/**
	 * Returns JSON with a customer list found and HttpStatus.OK (200). If not, a empty list 
	 * @return
	 */
	@GetMapping("/all")
	public List<Customer> listAll(){
		return customerService.findAll();
	}

	/**
	 * Receives @PathVariable id to delete a customer
	 * Returns HttpStatus.NO_CONTENT (204) if delete was success. If not, returns HttpStatus.NOT_FOUND (404)
	 * @param id
	 * @return 
	 */
	@DeleteMapping("{id}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id ) {
		
		try {
			customerService.delete(id);
			
		} catch (CustomerException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	/**
	 * Receives CustomerDto Object in @RequestBody to be updated
	 * This DTO parameter has the function of shielding the API so that the entity is not directly accessed
	 * Returns HttpStatus.NO_CONTENT (204) if update was success. If not, returns HttpStatus.NOT_FOUND (404)
	 * @param id
	 * @return 
	 */
	@PutMapping
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void update(@Valid @RequestBody CustomerDto updatedCustomer ) {

		try {
			customerService.update(updatedCustomer);
			
		} catch (CustomerException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}
	
}
