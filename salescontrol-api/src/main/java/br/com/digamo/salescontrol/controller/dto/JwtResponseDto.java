package br.com.digamo.salescontrol.controller.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponseDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String token;
	
}