package br.com.digamo.salescontrol.exception;

import javassist.NotFoundException;

public class CustomerException extends NotFoundException {

	private static final long serialVersionUID = 1L;

	public CustomerException(String message) {
		super(message);
	}

}
