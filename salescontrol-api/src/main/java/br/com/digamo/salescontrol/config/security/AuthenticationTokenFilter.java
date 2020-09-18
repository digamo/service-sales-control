package br.com.digamo.salescontrol.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.digamo.salescontrol.model.entity.User;
import br.com.digamo.salescontrol.model.repository.UserRepository;
import br.com.digamo.salescontrol.service.TokenService;

public class AuthenticationTokenFilter extends OncePerRequestFilter{

	private TokenService tokenService;
	private UserRepository userRepository;

	public AuthenticationTokenFilter(TokenService tokenService, UserRepository userRepository) {
		this.tokenService = tokenService;
		this.userRepository = userRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = recuperaToken(request);

		boolean tokenValido = tokenService.isValidToken(token);
		
		if(tokenValido) {
			authenticateClient(token);
		}
		
		filterChain.doFilter(request, response);
	}

	private void authenticateClient(String token) {
		
		Long idUser = tokenService.getUserId(token);
		User user = userRepository.findById(idUser).get();
		
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	private String recuperaToken(HttpServletRequest request) {

		String token = request.getHeader("Authorization");
		
		if(token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
			return null;
		}
		
		return token.substring(7, token.length());
	}

	
}
