package br.com.digamo.salescontrol.controller.dto;

import javax.validation.constraints.NotEmpty;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import br.com.digamo.salescontrol.model.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
public class UserDto {

	@NotEmpty(message = "{required.user.username.field}")
	private String username;
	
	@NotEmpty(message = "{required.user.passowrd.field}")
	private String password;
	
	public User convertToEntity () {
		
		return new User ( this.username, this.password ); 
		
	}
	
	public UsernamePasswordAuthenticationToken convert() {
		return new UsernamePasswordAuthenticationToken(this.username, this.password);
	}


}
