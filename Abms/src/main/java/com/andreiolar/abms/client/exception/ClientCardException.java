package com.andreiolar.abms.client.exception;

public class ClientCardException extends Exception {

	private static final long serialVersionUID = -2878096699126739906L;

	public ClientCardException() {
		super();
	}

	public ClientCardException(String message, Throwable cause) {
		super(message, cause);
	}

	public ClientCardException(String message) {
		super(message);
	}

	public ClientCardException(Throwable cause) {
		super(cause);
	}
}
