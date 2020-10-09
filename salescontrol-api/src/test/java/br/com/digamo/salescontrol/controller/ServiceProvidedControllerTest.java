package br.com.digamo.salescontrol.controller;

import static org.hamcrest.CoreMatchers.is;
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
import br.com.digamo.salescontrol.controller.dto.ServiceProvidedDto;
import br.com.digamo.salescontrol.controller.dto.UserDto;
import br.com.digamo.salescontrol.exception.AuthenticationException;
import br.com.digamo.salescontrol.exception.CustomerException;
import br.com.digamo.salescontrol.model.entity.Customer;
import br.com.digamo.salescontrol.model.entity.Role;
import br.com.digamo.salescontrol.model.entity.Roles;
import br.com.digamo.salescontrol.model.repository.RoleRepository;
import br.com.digamo.salescontrol.service.AuthenticationService;
import br.com.digamo.salescontrol.service.CustomerService;
import br.com.digamo.salescontrol.service.ServiceProvidedService;
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
public class ServiceProvidedControllerTest {

	@Autowired
    private MockMvc mvc;

	@Autowired
	private ServiceProvidedService service;

	@Autowired
	private UserService userService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private AuthenticationService authService;
	

	@Autowired
	private MessageSource messageSource;
	
	private static final String REQUIRED_DESCRIPTION_FIELD = "required.description.field";
	private static final String REQUIRED_VALUE_FIELD = "required.value.field";
	public final static String INCORRECT_DATE_FORMAT = "incorrect.date.format";
	private static final String REQUIRED_DATE_FIELD = "required.date.field";
	private static final String CUSTOMER_NOT_FOUND = "customer.not.found.message";
	private static final String REQUIRED_CUSTOMER_FIELD = "required.customer.field";
	
	private static final String URI = "/api/service-provided";

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
    	service.deleteAll();
    	customerService.deleteAll();
		userService.deleteAll();
    }

    @Test
    public void shouldSaveServiceProvidedAndReturn201WhenValidDtoInput() throws Exception  {

		// scenario
		CustomerDto customerDto = new CustomerDto(null, NAME_A, CPF_A) ;  
		Customer customer = customerService.save(customerDto);
		
		ServiceProvidedDto serviceProvidedDto = new ServiceProvidedDto();
		serviceProvidedDto.setDescription("Description Test");
		serviceProvidedDto.setIdCustomer(customer.getId());
		serviceProvidedDto.setDateService("2020-01-01");
		serviceProvidedDto.setValue("100.0");
		
		// action / verification
        mvc.perform(MockMvcRequestBuilders.post(URI)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(StringUtils.asJsonString(serviceProvidedDto))
        		.header("Authorization", "Bearer " + token))
        		.andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void shouldThrowExceptionWhenTriesToAccessSaveServiceProvidedWithoutAuthorization() throws Exception  {

		// scenario
		CustomerDto customerDto = new CustomerDto(null, NAME_A, CPF_A) ;  
		Customer customer = customerService.save(customerDto);
		
		ServiceProvidedDto serviceProvidedDto = new ServiceProvidedDto();
		serviceProvidedDto.setDescription("Description Test");
		serviceProvidedDto.setIdCustomer(customer.getId());
		serviceProvidedDto.setDateService("2020-01-01");
		serviceProvidedDto.setValue("100.0");
		
		// action / verification
        mvc.perform(MockMvcRequestBuilders.post(URI)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(StringUtils.asJsonString(serviceProvidedDto)))
        		.andExpect(MockMvcResultMatchers.status().isForbidden());
    }
    
    @Test
    public void shouldThrowExceptionWhenTriesToSaveServiceProvidedAndDescriptionIsNullOrEmpty() throws Exception  {

		// scenario
		CustomerDto customerDto = new CustomerDto(null, NAME_A, CPF_A);  
		Customer customer = customerService.save(customerDto);
		
		ServiceProvidedDto serviceProvidedDto = new ServiceProvidedDto();
		serviceProvidedDto.setDescription(null);
		serviceProvidedDto.setIdCustomer(customer.getId());
		serviceProvidedDto.setDateService("2020-01-01");
		serviceProvidedDto.setValue("100.0");
		
		// action / verification
        mvc.perform(MockMvcRequestBuilders.post(URI)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(StringUtils.asJsonString(serviceProvidedDto))
        		.header("Authorization", "Bearer " + token))
        		.andExpect(MockMvcResultMatchers.status().isBadRequest())
        		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        		.andExpect(jsonPath("$[0].message").value(
        				messageSource.getMessage(REQUIRED_DESCRIPTION_FIELD, null, Locale.getDefault())));

    }

    @Test
    public void shouldThrowExceptionWhenTriesToSaveServiceProvidedAndValueIsNull() throws Exception  {

		// scenario
		CustomerDto customerDto = new CustomerDto(null, NAME_A, CPF_A);  
		Customer customer = customerService.save(customerDto);
		
		ServiceProvidedDto serviceProvidedDto = new ServiceProvidedDto();
		serviceProvidedDto.setDescription("Description Test");
		serviceProvidedDto.setIdCustomer(customer.getId());
		serviceProvidedDto.setDateService("2020-01-01");
		serviceProvidedDto.setValue(null);
		
		// action / verification
        mvc.perform(MockMvcRequestBuilders.post(URI)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(StringUtils.asJsonString(serviceProvidedDto))
        		.header("Authorization", "Bearer " + token))
        		.andExpect(MockMvcResultMatchers.status().isBadRequest())
        		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        		.andExpect(jsonPath("$[0].message").value(
        				messageSource.getMessage(REQUIRED_VALUE_FIELD, null, Locale.getDefault())));

    }
    
    @Test
    public void shouldThrowExceptionWhenTriesToSaveServiceProvidedAndDateIsNullOrInvalid() throws Exception  {

		// scenario
		CustomerDto customerDto = new CustomerDto(null, NAME_A, CPF_A);  
		Customer customer = customerService.save(customerDto);
		
		ServiceProvidedDto serviceProvidedDto = new ServiceProvidedDto();
		serviceProvidedDto.setDescription("Description Test");
		serviceProvidedDto.setIdCustomer(customer.getId());
		serviceProvidedDto.setDateService("2020");
		serviceProvidedDto.setValue("100.0");
		
		// action / verification
        mvc.perform(MockMvcRequestBuilders.post(URI)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(StringUtils.asJsonString(serviceProvidedDto))
        		.header("Authorization", "Bearer " + token))
        		.andExpect(MockMvcResultMatchers.status().isBadRequest())
        		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        		.andExpect(jsonPath("$[0].message").value(
        				messageSource.getMessage(INCORRECT_DATE_FORMAT, null, Locale.getDefault())));
        
        serviceProvidedDto.setDateService(null);
        
		// action / verification
        mvc.perform(MockMvcRequestBuilders.post(URI)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(StringUtils.asJsonString(serviceProvidedDto))
        		.header("Authorization", "Bearer " + token))
        		.andExpect(MockMvcResultMatchers.status().isBadRequest())
        		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        		.andExpect(jsonPath("$[0].message").value(
        				messageSource.getMessage(REQUIRED_DATE_FIELD, null, Locale.getDefault())));

        
    }

    @Test
    public void shouldThrowExceptionWhenTriesToSaveServiceProvidedAndIdCustomerIsNull() throws Exception  {

		// scenario
		ServiceProvidedDto serviceProvidedDto = new ServiceProvidedDto();
		serviceProvidedDto.setDescription("Description Test");
		serviceProvidedDto.setIdCustomer(null);
		serviceProvidedDto.setDateService("2020-01-01");
		serviceProvidedDto.setValue("100.0");
		
		// action / verification
        mvc.perform(MockMvcRequestBuilders.post(URI)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(StringUtils.asJsonString(serviceProvidedDto))
        		.header("Authorization", "Bearer " + token))
        		.andExpect(MockMvcResultMatchers.status().isBadRequest())
        		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        		.andExpect(jsonPath("$[0].message").value(
        				messageSource.getMessage(REQUIRED_CUSTOMER_FIELD, null, Locale.getDefault())));
        
    }

    @Test
    public void shouldThrowExceptionWhenTriesToSaveServiceProvidedAndIdCustomerIsNotFound() throws Exception  {

		// scenario
    	Long idCustomerNotPersisted = 1L;
		
		ServiceProvidedDto serviceProvidedDto = new ServiceProvidedDto();
		serviceProvidedDto.setDescription("Description Test");
		serviceProvidedDto.setIdCustomer(idCustomerNotPersisted);
		serviceProvidedDto.setDateService("2020-01-01");
		serviceProvidedDto.setValue("100.0");
		
		// action / verification
        mvc.perform(MockMvcRequestBuilders.post(URI)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(StringUtils.asJsonString(serviceProvidedDto))
        		.header("Authorization", "Bearer " + token))
        		.andExpect(MockMvcResultMatchers.status().isBadRequest())
        		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        		.andExpect(jsonPath("$[0].message").value(
        				messageSource.getMessage(CUSTOMER_NOT_FOUND, null, Locale.getDefault())));
        
    }

	@Test
	public void shoulFindNotEmptyListOfAllServiceProvidedWithValidParametersAndPageable() throws Exception {
		
		// scenario
		CustomerDto customerDto = new CustomerDto(null, NAME_A, CPF_A) ;  
		Customer customer = customerService.save(customerDto);
		
		ServiceProvidedDto serviceProvidedDto = new ServiceProvidedDto();
		serviceProvidedDto.setDescription("Description Test");
		serviceProvidedDto.setIdCustomer(customer.getId());
		serviceProvidedDto.setDateService("2020-01-01");
		serviceProvidedDto.setValue("100.0");
		service.save(serviceProvidedDto);
		
		// action / verification
        mvc.perform(MockMvcRequestBuilders.get(URI + "?page=0&size=1&customerName=A&serviceMonth=1")
        		.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization", "Bearer " + token))
        		.andExpect(MockMvcResultMatchers.status().isOk())
        		.andExpect(jsonPath("$.content[0].dateService", is("2020-01-01")))
        		.andExpect(jsonPath("$.content[0].customer.name", is(NAME_A)));
        	
	}
	
	@Test
	public void shouldThrowExceptionWhenTriesToAccessFindListOfServiceProvidedWithPageableWithoutAuthorization() throws Exception {
		
		// scenario
		CustomerDto customerDto = new CustomerDto(null, NAME_A, CPF_A) ;  
		Customer customer = customerService.save(customerDto);
		
		ServiceProvidedDto serviceProvidedDto = new ServiceProvidedDto();
		serviceProvidedDto.setDescription("Description Test");
		serviceProvidedDto.setIdCustomer(customer.getId());
		serviceProvidedDto.setDateService("2020-01-01");
		serviceProvidedDto.setValue("100.0");
		service.save(serviceProvidedDto);
		
		// action / verification
        mvc.perform(MockMvcRequestBuilders.get(URI + "?page=0&size=1&customerName=A&serviceMonth=1")
        		.contentType(MediaType.APPLICATION_JSON))
        		.andExpect(MockMvcResultMatchers.status().isForbidden());
        	
	}

}
