package br.com.digamo.salescontrol.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import br.com.digamo.salescontrol.model.entity.Customer;
import br.com.digamo.salescontrol.model.repository.CustomerRepository;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

	@Autowired
	private CustomerRepository customerRepository;

	/**
	 * Receives JSON with not null fields to register a new customer
	 * Returns JSON with the new customer created and HttpStatus.CREATED (201)
	 * @param customer
	 * @return  
	 */
	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public Customer save(@RequestBody @Valid Customer customer ) {
		return customerRepository.save(customer);
	}

	/**
	 * Receives @PathVariable id to find a customer
	 * Returns JSON with the customer found and HttpStatus.OK (200). If not, returns HttpStatus.NOT_FOUND (404)
	 * @param id
	 * @return 
	 */
	@GetMapping("{id}")
	public Customer findById(@PathVariable Long id ) {
		return customerRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	/**
	 * Returns JSON with the list of customer with Pageable and HttpStatus.OK (200). If not, returns HttpStatus.NOT_FOUND (404)
	 * @param page
	 * @param size
	 * @return Returns JSON with a customer list with Pageable found and HttpStatus.OK (200). If not, a empty list
	 */
	@GetMapping
	public Page<Customer> listWithPageable(@RequestParam(
			required = false, defaultValue = "0") Integer page, 
			@RequestParam(required = false, defaultValue = "10") Integer size ){

		Pageable pagination =  PageRequest.of(page, size);
		
		Page<Customer> customers = customerRepository.findAll(pagination);
		
		return customers;

	}

	/**
	 * Returns JSON with the list of customer and HttpStatus.OK (200). If not, returns HttpStatus.NOT_FOUND (404)
	 * @return Returns JSON with a customer list found and HttpStatus.OK (200). If not, a empty list
	 */
	@GetMapping("/all")
	public List<Customer> listAll(){

		return customerRepository.findAll();

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
		
		customerRepository.findById(id)
			.map( customer -> {
				customerRepository.delete(customer);
				return Void.TYPE;
				
			})
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
		
	}

	/**
	 * Receives @PathVariable id and the Customer Object in @RequestBody to be updated
	 * Returns HttpStatus.NO_CONTENT (204) if update was success. If not, returns HttpStatus.NOT_FOUND (404)
	 * @param id
	 * @return 
	 */
	@PutMapping("{id}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void update(@PathVariable Long id, @Valid @RequestBody Customer updatedCustomer ) {
		
		customerRepository.findById(id)
			.map( customer -> {
				
				customer.setCpf(updatedCustomer.getCpf());
				customer.setName(updatedCustomer.getName());

				customerRepository.save(customer);
				return Void.TYPE;
				
			})
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
		
	}
	
	
}
