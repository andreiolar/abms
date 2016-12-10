package com.andreiolar.abms.client.exception;

public class InvalidCodeException extends Exception {

	private static final long serialVersionUID = -2163960708405043721L;

	public InvalidCodeException() {
		super();
	}

	public InvalidCodeException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidCodeException(String message) {
		super(message);
	}

	public InvalidCodeException(Throwable cause) {
		super(cause);
	}

}
