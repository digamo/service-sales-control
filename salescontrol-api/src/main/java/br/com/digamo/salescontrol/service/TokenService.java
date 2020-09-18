package br.com.digamo.salescontrol.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.digamo.salescontrol.model.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {

	@Value("${app.jwt.expiration}")
	private String expiration;
	
	@Value("${app.jwt.secret}")
	private String secret;
	
	public String generateToken(Authentication authentication) {

		User user = (User) authentication.getPrincipal();
		Date today = new Date();
		
		return Jwts.builder()
				.setSubject(user.getId().toString())
				.claim("username", user.getUsername())
				.claim("role", authentication.getAuthorities())
				.setIssuedAt(today)
				.setExpiration(new Date(System.currentTimeMillis()+Long.parseLong(expiration)))
				.signWith(SignatureAlgorithm.HS256, secret)
				.compact();
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


