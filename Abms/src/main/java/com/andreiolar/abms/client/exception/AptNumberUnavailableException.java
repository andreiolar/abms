package com.andreiolar.abms.client.exception;

public class AptNumberUnavailableException extends Exception {

	private static final long serialVersionUID = -1483215428237846528L;

	public AptNumberUnavailableException() {
		super();
	}

	public AptNumberUnavailableException(String message, Throwable cause) {
		super(message, cause);
	}

	public AptNumberUnavailableException(String message) {
		super(message);
	}

	public AptNumberUnavailableException(Throwable cause) {
		super(cause);
	}
}
