package br.com.digamo.salescontrol.service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.digamo.salescontrol.controller.dto.CustomerDto;
import br.com.digamo.salescontrol.exception.CustomerException;
import br.com.digamo.salescontrol.model.entity.Customer;
import br.com.digamo.salescontrol.model.repository.CustomerRepository;

@Service
public class CustomerService {


	public final static String CUSTOMER_NOT_FOUND = "customer.not.found.message";
	public final static String CUSTOMER_NAME_ALREADY_EXISTS = "customer.name.already.exists";
	public final static String CUSTOMER_CPF_ALREADY_EXISTS = "customer.cpf.already.exists";

	private final MessageSource messageSource;
	private final CustomerRepository customerRepository; 
	
	public CustomerService(CustomerRepository customerRepository, MessageSource messageSource) {
		this.customerRepository = customerRepository;
		this.messageSource = messageSource;
	}

	/**
	 * Saves a new service according to the parameter customerDto passed
	 * @param customer
	 * @return
	 * @throws CustomerException
	 */
	public Customer save(CustomerDto customerDto) throws CustomerException {

		Optional<Customer> customerFound = 
				customerRepository.findByNameOrCpf(customerDto.getName(), customerDto.getCpf());

		if(customerFound.isPresent()) {
		
			if(customerDto.getCpf().equals(customerFound.get().getCpf()))
				throw new CustomerException(
						messageSource.getMessage(CUSTOMER_CPF_ALREADY_EXISTS, null, Locale.getDefault()));
			
			else if(customerDto.getName().equals(customerFound.get().getName())) 
				throw new CustomerException(
						messageSource.getMessage(CUSTOMER_NAME_ALREADY_EXISTS, null, Locale.getDefault()));
		
		}

		return customerRepository.save(customerDto.convertToEntity());
		
	}


	/**
	 * 
	 * @param id
	 * @return
	 * @throws CustomerException
	 */
	public Customer findById(Long id) throws CustomerException {
		
		Optional<Customer> customer = customerRepository.findById(id);
		
		customer.orElseThrow(() -> 
			new CustomerException(
				messageSource.getMessage(CUSTOMER_NOT_FOUND, null, Locale.getDefault())));

		return customer.get();
	}

	/**
	 * 
	 * @param pagination
	 * @return
	 */
	public Page<Customer> findAll(Pageable pagination) {
		return customerRepository.findAll(pagination);
	}

	/**
	 * 
	 * @return
	 */
	public List<Customer> findAll() {
		return customerRepository.findAll();
	}

	/**
	 * 
	 * @param id
	 * @throws CustomerException
	 */
	public void delete(Long id) throws CustomerException {
		customerRepository.findById(id)
		.map( customer -> {
			customerRepository.delete(customer);
			return Void.TYPE;
		})
		.orElseThrow(() -> 
		new CustomerException(
				messageSource.getMessage(CUSTOMER_NOT_FOUND, null, Locale.getDefault())));
		
	}

	/**
	 * 
	 * @throws CustomerException
	 */
	public void deleteAll() throws CustomerException {

		customerRepository.deleteAll();
	}

	/**
	 * 
	 * @param id
	 * @param updatedCustomer
	 * @throws CustomerException
	 */
	public void update(CustomerDto customerDto ) throws CustomerException {

		Optional<Customer> customerFound = 
				customerRepository.findMoreThenOneNameOrCpfWithDifferentId(customerDto.getName(), customerDto.getCpf(), customerDto.getId());

		if(customerFound.isPresent()) {
		
			if(customerDto.getCpf().equals(customerFound.get().getCpf()))
				throw new CustomerException(
						messageSource.getMessage(CUSTOMER_CPF_ALREADY_EXISTS, null, Locale.getDefault()));

			else if(customerDto.getName().equals(customerFound.get().getName())) 
				throw new CustomerException(
						messageSource.getMessage(CUSTOMER_NAME_ALREADY_EXISTS, null, Locale.getDefault()));
		
		}
		
		customerRepository.findById(customerDto.getId())
		.map( customer -> {
			
			customer.setCpf(customerDto.getCpf());
			customer.setName(customerDto.getName());

			customerRepository.save(customer);
			return Void.TYPE;
			
		})
		.orElseThrow(() -> 
			new CustomerException(
					messageSource.getMessage(CUSTOMER_NOT_FOUND, null, Locale.getDefault())));
	}

}
