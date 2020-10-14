package br.com.digamo.salescontrol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import br.com.digamo.salescontrol.config.SpringSecurityAuditorAware;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@SpringBootApplication
@EnableSwagger2
public class SalesControlApplication {

	public static void main(String[] args) {

		SpringApplication.run(SalesControlApplication.class, args);
		
	}

	@Bean
	public AuditorAware<String> auditorAware() {
	    return new SpringSecurityAuditorAware();
	}	
	
	
}
