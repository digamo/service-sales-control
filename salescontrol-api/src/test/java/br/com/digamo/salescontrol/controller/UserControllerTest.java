package br.com.digamo.salescontrol.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
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

import br.com.digamo.salescontrol.controller.dto.UserDto;
import br.com.digamo.salescontrol.exception.CustomerException;
import br.com.digamo.salescontrol.model.entity.Role;
import br.com.digamo.salescontrol.model.entity.Roles;
import br.com.digamo.salescontrol.model.repository.RoleRepository;
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
public class UserControllerTest {

	@Autowired
    private MockMvc mvc;

	@Autowired
	private UserService userService;

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private MessageSource messageSource;
	
	private static final String REQUIRED_USERNAME_FIELD = "required.user.username.field";
	private static final String REQUIRED_PASSWORD_FIELD = "required.user.passowrd.field";

	private static final String URI = "/api/users";
	
    @AfterEach
    private void end() throws CustomerException {
		userService.deleteAll();
		roleRepository.deleteAll();
    }

    @Test
    public void shouldSaveUserAndReturn201WhenValidDtoInput() throws Exception  {

		// scenario
		String username = "digamo";
		String password = "d123";
		UserDto userDto = new UserDto(username,password);
		roleRepository.save(new Role(Roles.USER.toString()));
		
		// action / verification
        mvc.perform(MockMvcRequestBuilders.post(URI)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(StringUtils.asJsonString(userDto)))
        		.andExpect(MockMvcResultMatchers.status().isCreated());
    }

    
    @Test
    public void shouldThrowExceptionWhenTriesToSaveUserAndUsernameIsNullOrEmpty() throws Exception  {

		// scenario
		String username = null;
		String password = "d123";
		UserDto userDto = new UserDto(username,password);
		roleRepository.save(new Role(Roles.USER.toString()));
		
		// action / verification
        mvc.perform(MockMvcRequestBuilders.post(URI)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(StringUtils.asJsonString(userDto)))
        		.andExpect(MockMvcResultMatchers.status().isBadRequest())
        		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        		.andExpect(jsonPath("$[0].message").value(
        				messageSource.getMessage(REQUIRED_USERNAME_FIELD, null, Locale.getDefault())));

    }

    @Test
    public void shouldThrowExceptionWhenTriesToSaveUserAndPasswordIsNullOrEmpty() throws Exception  {

		// scenario
		String username = "digamo";
		String password = null;
		UserDto userDto = new UserDto(username,password);
		roleRepository.save(new Role(Roles.USER.toString()));
		
		// action / verification
        mvc.perform(MockMvcRequestBuilders.post(URI)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(StringUtils.asJsonString(userDto)))
        		.andExpect(MockMvcResultMatchers.status().isBadRequest())
        		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        		.andExpect(jsonPath("$[0].message").value(
        				messageSource.getMessage(REQUIRED_PASSWORD_FIELD, null, Locale.getDefault())));

    }
}
