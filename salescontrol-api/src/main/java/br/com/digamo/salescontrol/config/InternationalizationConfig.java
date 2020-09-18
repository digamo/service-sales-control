package br.com.digamo.salescontrol.config;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class InternationalizationConfig {

	/**
	 * Message file is defined
	 * @return
	 */
	@Bean
	public MessageSource messageSource() {
		
		ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource(); 
		
		ms.setBasename("classpath:messages");
		ms.setDefaultEncoding("ISO-8859-1");
		ms.setDefaultLocale(Locale.getDefault());
		
		return ms;
	}
	
	/**
	 * Bean that will be used as a validation bean 
	 * @return
	 */
	@Bean
	public LocalValidatorFactoryBean validatorFactoryBean() {
		
		LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
		factoryBean.setValidationMessageSource(messageSource());
		
		return factoryBean;
	}

}
