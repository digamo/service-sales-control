package br.com.digamo.salescontrol.controller.dto;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.br.CPF;

import br.com.digamo.salescontrol.model.entity.Customer;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerDto {

	private Long id;
	
	@NotEmpty(message = "{required.name.field}")
	private String name;
	
	@NotEmpty(message = "{required.cpf.field}")
	@CPF(message = "{invalid.cpf.field}")
	private String cpf;
	
	public Customer convertToEntity () {
		return new Customer (this.id, this.name, this.cpf );
	}
}
