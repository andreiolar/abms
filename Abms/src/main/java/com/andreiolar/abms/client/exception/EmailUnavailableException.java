package com.andreiolar.abms.client.exception;

public class EmailUnavailableException extends Exception {

	private static final long serialVersionUID = -4729576902811872857L;

	public EmailUnavailableException() {
		super();
	}

	public EmailUnavailableException(String message, Throwable cause) {
		super(message, cause);
	}

	public EmailUnavailableException(String message) {
		super(message);
	}

	public EmailUnavailableException(Throwable cause) {
		super(cause);
	}
}
