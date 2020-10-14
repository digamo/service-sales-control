package br.com.digamo.salescontrol.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

import br.com.digamo.salescontrol.model.repository.UserRepository;
import br.com.digamo.salescontrol.service.UserService;
import br.com.digamo.salescontrol.service.AuthenticationService;

//Habilitar o módulo de securança na aplicação
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private UserService autenticacaoService;

	@Autowired
	private AuthenticationService tokenService;
	
	@Autowired
	private UserRepository userRepository;

	
	//Gerenciamento de usuários
	@Override
	@Bean
	public AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
		 
	}

	//Gerenciamento de autenticacao da aplicação.
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		//BCryptPasswordEncoder é o algorítimo de hash que está sendo usado para encriptar o password
		auth.userDetailsService(autenticacaoService).passwordEncoder(new BCryptPasswordEncoder());
	}

	//Configuracao de autorizacao de request
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	
		http
			.authorizeRequests()
				.antMatchers(
						"/api/users",
						"/api/auth").permitAll()
				.antMatchers(
						"/api/customers/**",
						"/api/service-provided/**").authenticated().
			
		anyRequest().denyAll().and() // Impede que qualquer outro path não mapeado seja acessado
			.csrf().disable() // Desabilitado validação do csrf
			.cors() //Habilita o Cors
		
		.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Não irá manter a sessão pois será controlado pelo Token.
		.and().addFilterBefore(new AuthenticationTokenFilter(tokenService, userRepository), UsernamePasswordAuthenticationFilter.class);
	
	}

	//Configuration of statistical resources (js, css, imagens, etc)
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
        .antMatchers("/**.html", "/v2/api-docs", "/webjars/**", "/configuration/**", "/swagger-resources/**");
	}
}
