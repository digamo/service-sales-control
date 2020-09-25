package br.com.digamo.salescontrol.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProvided extends Auditable<String>{

	public ServiceProvided(String description, BigDecimal value, LocalDate dateService, Customer customer) {
		this.description = description;
		this.value = value;
		this.dateService = dateService;
		this.customer = customer;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 250)
	private String description;

	@Column
	private BigDecimal value;
	
	@ManyToOne
	@JoinColumn(name="id_customer")
	private Customer customer;

	@Column(name="date_service")
	private LocalDate dateService;

}
