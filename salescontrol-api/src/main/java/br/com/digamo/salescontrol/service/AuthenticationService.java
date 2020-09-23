package br.com.digamo.salescontrol.service;


import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.digamo.salescontrol.controller.dto.UserDto;
import br.com.digamo.salescontrol.exception.AuthenticationException;
import br.com.digamo.salescontrol.model.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class AuthenticationService {

	@Value("${app.jwt.expiration}")
	private String expiration;

	@Value("${app.jwt.secret}")
	private String secret;

	public static final String INVALID_USER_PASSWORD = "invalid.user.password";

	private final MessageSource messageSource;
	private final AuthenticationManager authManager; 
	
	public AuthenticationService(MessageSource messageSource, AuthenticationManager authManager) {
		this.messageSource = messageSource;
		this.authManager = authManager;
	}

	public String generateToken(UserDto userDto) throws AuthenticationException  {

		String token = null;

		try {
			
			UsernamePasswordAuthenticationToken login = userDto.convert();

			Authentication authentication = authManager.authenticate(login);
			
			User user = (User) authentication.getPrincipal();
			Date today = new Date();

			token = Jwts.builder().setSubject(user.getId().toString()).claim("username", user.getUsername())
					.claim("role", authentication.getAuthorities()).setIssuedAt(today)
					.setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(expiration)))
					.signWith(SignatureAlgorithm.HS256, secret).compact();

		} catch (Exception e) {
			throw new AuthenticationException(messageSource.getMessage(INVALID_USER_PASSWORD, null, Locale.getDefault()));

		}

		return token;
	}

	public boolean isValidToken(String token) {

		try {
			Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
			return true;

		} catch (Exception e) {
			return false;
		}
	}

	public Long getUserId(String token) {

		Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
		return Long.parseLong(claims.getSubject());
	}

}
