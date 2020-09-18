package br.com.digamo.salescontrol.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

public class SpringSecurityAuditorAware implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		return Optional.ofNullable(username);
	}
 
}