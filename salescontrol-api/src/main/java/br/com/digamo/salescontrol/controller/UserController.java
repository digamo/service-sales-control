package br.com.digamo.salescontrol.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.digamo.salescontrol.controller.dto.UserDto;
import br.com.digamo.salescontrol.exception.RegisteredUserException;
import br.com.digamo.salescontrol.model.entity.Roles;
import br.com.digamo.salescontrol.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private UserService service;

	public UserController(UserService service) {
		this.service = service;
	}

	/**
	 * Receives JSON with not null DTO to register a new User
	 * This DTO parameter has the function of shielding the API so that the entity is not directly accessed
	 * @param user
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void save(@RequestBody @Valid UserDto userDto) {
		
		try {
			service.save(userDto, Roles.USER);
			
		} catch (RegisteredUserException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		
	}
	
}
