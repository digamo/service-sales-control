package br.com.digamo.salescontrol.model.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class Role  extends Auditable<String> implements GrantedAuthority{

	private static final long serialVersionUID = 1L;
	
	@Id
	private String name;

	@Override
	public String getAuthority() {
		return this.name;
	}
	
}
