package br.com.digamo.salescontrol.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import br.com.digamo.salescontrol.controller.dto.CustomerDto;
import br.com.digamo.salescontrol.controller.dto.UserDto;
import br.com.digamo.salescontrol.exception.AuthenticationException;
import br.com.digamo.salescontrol.exception.CustomerException;
import br.com.digamo.salescontrol.model.entity.Customer;
import br.com.digamo.salescontrol.model.entity.Role;
import br.com.digamo.salescontrol.model.entity.Roles;
import br.com.digamo.salescontrol.model.repository.RoleRepository;
import br.com.digamo.salescontrol.service.AuthenticationService;
import br.com.digamo.salescontrol.service.CustomerService;
import br.com.digamo.salescontrol.service.UserService;
import br.com.digamo.salescontrol.util.StringUtils;

/**
 * 
 * @author digam
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CustomerControllerTest {

	@Autowired
    private MockMvc mvc;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private UserService userService;

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private AuthenticationService authService;
	
	@Autowired
	private MessageSource messageSource;
	
	private static final String REQUIRED_NAME_FIELD = "required.name.field";
	private static final String REQUIRED_CPF_FIELD = "required.cpf.field";
	private static final String INVALID_CPF_FIELD = "invalid.cpf.field";

	private static final String URI = "/api/customers";
	
	private static final String NAME_A = "Digamo A";
	private static final String CPF_A = "15552680057"; //fake number
	
	private String token = "";
    
	@BeforeEach
	private void setup() throws AuthenticationException {
		
		// scenario	
		String username = "admin";
		String password = "admin@123";
		UserDto userDto = new UserDto(username,password);

		roleRepository.save(new Role(Roles.ADMIN.toString()));
		userService.save(userDto, Roles.ADMIN);
    	 
    	 // action
        token = authService.generateToken(userDto);

	}

    @AfterEach
    private void end() throws CustomerException {
    	customerService.deleteAll();
		userService.deleteAll();
    }

    @Test
    public void shouldSaveCustomerAndReturn201WhenValidDtoInput() throws Exception  {

		// scenario
		CustomerDto customerDto = new CustomerDto(null, NAME_A, CPF_A) ; 

		// action / verification
        mvc.perform(MockMvcRequestBuilders.post(URI)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(StringUtils.asJsonString(customerDto))
        		.header("Authorization", "Bearer " + token))
        		.andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void shouldThrowExceptionWhenTriesToSaveCustomerAndNameIsNullOrEmpty() throws Exception  {

		// scenario
		CustomerDto customerDto1 = new CustomerDto(null, null, CPF_A) ; 

		CustomerDto customerDto2 = new CustomerDto(null, "", CPF_A) ;
		
		// action / verification
        mvc.perform(MockMvcRequestBuilders.post(URI)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(StringUtils.asJsonString(customerDto1))
        		.header("Authorization", "Bearer " + token))
        		.andExpect(MockMvcResultMatchers.status().isBadRequest())
        		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        		.andExpect(jsonPath("$[0].message").value(
        				messageSource.getMessage(REQUIRED_NAME_FIELD, null, Locale.getDefault())));
        
        
        
        mvc.perform(MockMvcRequestBuilders.post(URI)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(StringUtils.asJsonString(customerDto2))
        		.header("Authorization", "Bearer " + token))
        		.andExpect(MockMvcResultMatchers.status().isBadRequest())
        		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        		.andExpect(jsonPath("$[0].message").value(
        				messageSource.getMessage(REQUIRED_NAME_FIELD, null, Locale.getDefault())));


    }

    @Test
    public void shouldThrowExceptionWhenTriesToSaveCustomerAndCpfIsNull() throws Exception  {

		// scenario
		CustomerDto customerDto1 = new CustomerDto(null, NAME_A, null) ; 

		// action / verification
        mvc.perform(MockMvcRequestBuilders.post(URI)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(StringUtils.asJsonString(customerDto1))
        		.header("Authorization", "Bearer " + token))
        		.andExpect(MockMvcResultMatchers.status().isBadRequest())
        		.andDo(print())
        		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        		.andExpect(jsonPath("$[0].message").value(
        				messageSource.getMessage(REQUIRED_CPF_FIELD, null, Locale.getDefault())));
        
    }

    @Test
    public void shouldThrowExceptionWhenTriesToSaveCustomerAndCpfIsInvalid() throws Exception  {

		// scenario
		CustomerDto customerDto = new CustomerDto(null, NAME_A, "21212121212") ; // fake and invalid CPF
		
        mvc.perform(MockMvcRequestBuilders.post(URI)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(StringUtils.asJsonString(customerDto))
        		.header("Authorization", "Bearer " + token))
        		.andExpect(MockMvcResultMatchers.status().isBadRequest())
        		.andDo(print())
        		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        		.andExpect(jsonPath("$[0].message").value(
        				messageSource.getMessage(INVALID_CPF_FIELD, null, Locale.getDefault())));

    }

    @Test
    public void shouldThrowExceptionWhenTriesToAccessSaveCustomerWithoutAuthorization() throws Exception  {

		// scenario
		CustomerDto customerDto = new CustomerDto(null, NAME_A, CPF_A) ; 

		// action / verification
        mvc.perform(MockMvcRequestBuilders.post(URI)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(StringUtils.asJsonString(customerDto)))
        		.andExpect(MockMvcResultMatchers.status().isForbidden());
    }
    
    @Test
    public void shouldFindCustomerByPathVariableIdAndReturnStatus200AndNotEmptyObjectCustomer() throws Exception  {

		// scenario
		CustomerDto customerDto = new CustomerDto(null, NAME_A, CPF_A); 
		Customer customer = customerService.save(customerDto);
		
		// action / verification
        mvc.perform(MockMvcRequestBuilders.get(URI + "/" + customer.getId())
        		.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization", "Bearer " + token))
        		.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void shouldThrowExceptionWhenTriesToFindCustomerByIdAndCanNot() throws Exception  {

		// scenario
		Long idCustomerNotPersisted = 1L;
		
		// action / verification
        mvc.perform(MockMvcRequestBuilders.get(URI + "/" + idCustomerNotPersisted)
        		.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization", "Bearer " + token))
        		.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    
    @Test
    public void shouldThrowExceptionWhenTriesToAccessFindCustomerByIdWithoutAuthorization() throws Exception  {

		// scenario
		Long idCustomerNotPersisted = 1L;
		
		// action / verification
        mvc.perform(MockMvcRequestBuilders.get(URI + "/" + idCustomerNotPersisted)
        		.contentType(MediaType.APPLICATION_JSON))
        		.andExpect(MockMvcResultMatchers.status().isForbidden());
    }
    
    @Test
    public void shouldFindAllCustomerAndReturnStatus200AndNotEmptyListOfCustomer() throws Exception  {

		// scenario
		CustomerDto customerDto = new CustomerDto(null, NAME_A, CPF_A); 
		customerService.save(customerDto);

		// action / verification
        mvc.perform(MockMvcRequestBuilders.get(URI + "/all")
        		.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization", "Bearer " + token))
        		.andExpect(MockMvcResultMatchers.status().isOk())
        		.andExpect(jsonPath("$[0].name").value(NAME_A));

    }

    @Test
    public void shouldThrowExceptionWhenTriesToAccessFindAllCustomerWithoutAuthorization() throws Exception  {

		// scenario
		CustomerDto customerDto = new CustomerDto(null, NAME_A, CPF_A); 
		customerService.save(customerDto);

		// action / verification
        mvc.perform(MockMvcRequestBuilders.get(URI + "/all")
        		.contentType(MediaType.APPLICATION_JSON))
        		.andExpect(MockMvcResultMatchers.status().isForbidden());

    }
    
	@Test
	public void shoulFindNotEmptyListOfAllCustomersWithPageable() throws Exception {
		
		// scenario
		CustomerDto customerDto = new CustomerDto(null, NAME_A, CPF_A) ; 
		customerService.save(customerDto);

		// action / verification
        mvc.perform(MockMvcRequestBuilders.get(URI + "?page=0&size=1")
        		.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization", "Bearer " + token))
        		.andExpect(MockMvcResultMatchers.status().isOk())
        		.andExpect(jsonPath("$.content[0].name", is(NAME_A)));
        	
	}

	@Test
	public void shouldThrowExceptionWhenTriesToAccessFindListOfCustomerWithPageableWithoutAuthorization() throws Exception {
		
		// scenario
		CustomerDto customerDto = new CustomerDto(null, NAME_A, CPF_A) ; 
		customerService.save(customerDto);

		// action / verification
        mvc.perform(MockMvcRequestBuilders.get(URI + "?page=0&size=1")
        		.contentType(MediaType.APPLICATION_JSON))
        		.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

    @Test
    public void shouldDeleteCustomerAndReturnStatus204() throws Exception  {

		// scenario
		CustomerDto customerDto = new CustomerDto(null, NAME_A, CPF_A); 
		Customer customer = customerService.save(customerDto);

		// action / verification
        mvc.perform(MockMvcRequestBuilders.delete(URI + "/" + customer.getId())
        		.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization", "Bearer " + token))
        		.andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    public void shouldThrowExceptionWhenTriesToDeleteCustomerThatNotExists() throws Exception  {

		// scenario
		Long idCustomerNotPersisted = 1L;

		// action / verification
        mvc.perform(MockMvcRequestBuilders.delete(URI + "/" + idCustomerNotPersisted)
        		.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization", "Bearer " + token))
        		.andExpect(jsonPath("$[0].message").value(
        				messageSource.getMessage(CustomerService.CUSTOMER_NOT_FOUND, null, Locale.getDefault())));

    }

    @Test
    public void shouldThrowExceptionWhenTriesToAccessDeleteCustomerWithoutAuthorization() throws Exception  {

		// scenario
		CustomerDto customerDto = new CustomerDto(null, NAME_A, CPF_A); 
		Customer customer = customerService.save(customerDto);

		// action / verification
        mvc.perform(MockMvcRequestBuilders.delete(URI + "/" + customer.getId())
        		.contentType(MediaType.APPLICATION_JSON))
        		.andExpect(MockMvcResultMatchers.status().isForbidden());

    }

    @Test
    public void shouldUpdateCustomerAndReturnStatus204() throws Exception  {

		// scenario
		CustomerDto customerDto1 = new CustomerDto(null, NAME_A, CPF_A); 
		Customer customer = customerService.save(customerDto1);

		CustomerDto updatedCustomer = new CustomerDto(customer.getId(), "NAME_B", "95233857049");
		
		// action / verification
        mvc.perform(MockMvcRequestBuilders.put(URI)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(StringUtils.asJsonString(updatedCustomer))
        		.header("Authorization", "Bearer " + token))
				.andExpect(MockMvcResultMatchers.status().isNoContent());

    }
    
    
    @Test
    public void shouldThrowExceptionWhenTriesToAccessUpdateCustomerWithoutAuthorization() throws Exception  {

		// scenario
		CustomerDto customerDto1 = new CustomerDto(null, NAME_A, CPF_A); 
		Customer customer = customerService.save(customerDto1);

		CustomerDto customerDto2 = new CustomerDto(customer.getId(), "NAME_B", "95233857049");
		
		// action / verification
        mvc.perform(MockMvcRequestBuilders.put(URI)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(StringUtils.asJsonString(customerDto2)))
        		.andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void shouldThrowExceptionWhenTriesToUpdateCustomerThatNotExists() throws Exception  {

		// scenario
		CustomerDto customerDto1 = new CustomerDto(null, NAME_A, CPF_A); 
		Customer customer = customerService.save(customerDto1);
		
		CustomerDto customerDto2 = new CustomerDto(customer.getId() + 1L, "NAME_B", "95233857049");
		
		// action / verification
        mvc.perform(MockMvcRequestBuilders.put(URI)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(StringUtils.asJsonString(customerDto2))
        		.header("Authorization", "Bearer " + token))
        		.andExpect(jsonPath("$[0].message").value(
        				messageSource.getMessage(CustomerService.CUSTOMER_NOT_FOUND, null, Locale.getDefault())));

    }

}
