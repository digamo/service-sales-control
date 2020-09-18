package br.com.digamo.salescontrol.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.digamo.salescontrol.controller.dto.JwtResponseDto;
import br.com.digamo.salescontrol.model.entity.User;
import br.com.digamo.salescontrol.service.TokenService;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authManager; 

	@Autowired
	private TokenService tokenService; 

	@PostMapping
	public ResponseEntity<?> authenticate(@RequestBody @Valid User user){
		
		UsernamePasswordAuthenticationToken login = user.convert();
		
		try {
			Authentication authentication = authManager.authenticate(login);
			String token = tokenService.generateToken(authentication);
			
			return ResponseEntity.ok().body(new JwtResponseDto(token));
			
		}catch (AuthenticationException e) {
			return ResponseEntity.badRequest().build();
		}
		
		
		
	}
}
