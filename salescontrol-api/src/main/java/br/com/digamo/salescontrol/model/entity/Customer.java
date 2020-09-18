package br.com.digamo.salescontrol.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.br.CPF;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer extends Auditable<String>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 150)
	@NotEmpty(message = "{required.name.field}")
	private String name;
	
	@Column(nullable = false, length = 11)
	@NotEmpty(message = "{required.cpf.field}")
	@CPF(message = "{invalid.cpf.field}")
	private String cpf;
	
//	@Column(name="date_register")
//	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//	private LocalDateTime dateRegister;
//	
//	@PrePersist
//	public void prePersist() {
//		setDateRegister(LocalDateTime.now());
//	}

}
