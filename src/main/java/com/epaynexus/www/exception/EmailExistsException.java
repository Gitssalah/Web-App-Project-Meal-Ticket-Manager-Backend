package com.epaynexus.www.exception;

public class EmailExistsException extends RuntimeException {
	public EmailExistsException() {
		super();
	}

	public EmailExistsException(String message) {
		super(message);
	}
}
