package br.com.digamo.salescontrol.controller.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ServiceProvidedDto {

	@NotEmpty(message = "{required.description.field}")
	private String description;
	
	@NotEmpty(message = "{required.value.field}")
	private String value;
	
	@NotEmpty(message = "{required.date.field}")
	private String dateService;
	
	@NotNull(message = "{required.customer.field}")
	private Long idCustomer;
	
	
}
