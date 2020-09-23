package br.com.digamo.salescontrol.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.digamo.salescontrol.controller.dto.JwtResponseDto;
import br.com.digamo.salescontrol.controller.dto.UserDto;
import br.com.digamo.salescontrol.exception.AuthenticationException;
import br.com.digamo.salescontrol.service.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

	private final AuthenticationService authService; 

	public AuthenticationController(AuthenticationManager authManager, AuthenticationService authService) {
		this.authService = authService;
	}

	@PostMapping
	public ResponseEntity<?> authenticate(@RequestBody @Valid UserDto userDto) {
		
		try {
			
			String token = authService.generateToken(userDto);
			
			return ResponseEntity.ok().body(new JwtResponseDto(token));
			
		} catch (AuthenticationException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
		}
		
	}
}
