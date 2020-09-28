package br.com.digamo.salescontrol.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.digamo.salescontrol.controller.dto.UserDto;
import br.com.digamo.salescontrol.model.entity.Role;
import br.com.digamo.salescontrol.model.entity.Roles;
import br.com.digamo.salescontrol.model.repository.RoleRepository;
import br.com.digamo.salescontrol.service.AuthenticationService;
import br.com.digamo.salescontrol.service.UserService;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

	@Autowired
    private MockMvc mvc;

	@Autowired
	private AuthenticationService authService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private RoleRepository roleRepository;

	
    @Test
    public void shouldNotAllowAccessToUnauthenticatedUsers() throws Exception {

		// scenario	
		String username = "admin_x";
		String password = "admin@123x";
		UserDto userDto = new UserDto(username,password);
   	 	String token = ""; //empty token
		
		// action / verification
    	mvc.perform(MockMvcRequestBuilders.post("/api/auth")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(userDto))
        		.header("Authorization", token))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldGenerateAuthToken() throws Exception {
    
		// scenario	
		String username = "admin";
		String password = "admin@123";
		UserDto userDto = new UserDto(username,password);

		roleRepository.save(new Role(Roles.ADMIN.toString()));
		userService.save(userDto, Roles.ADMIN);
    	 
    	 // action
        String token = authService.generateToken(userDto);

        // verification	
        Assertions.assertThat(token).isNotNull();
        mvc.perform(MockMvcRequestBuilders.post("/api/auth")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(userDto))
        		.header("Authorization", token))
        		.andExpect(MockMvcResultMatchers.status().isOk());
    }


    /**
     * 
     * @param obj
     * @return
     */
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}


