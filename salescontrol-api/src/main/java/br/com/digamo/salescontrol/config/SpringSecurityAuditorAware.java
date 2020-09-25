package br.com.digamo.salescontrol.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

public class SpringSecurityAuditorAware implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		
		String username = null;
				
		try {

			//During the tests the SecurityContextHolder.getContext() is empty
			username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		}catch (Exception e) {}
		
		return Optional.ofNullable(username);
	}
 
}