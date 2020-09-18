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

import com.fasterxml.jackson.annotation.JsonFormat;

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
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate dateService;

//	@Column(name="date_register")
//	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//	private LocalDateTime dateRegister;
//	
//	@PrePersist
//	public void prePersist() {
//		setDateRegister(LocalDateTime.now());
//	}

	
}
