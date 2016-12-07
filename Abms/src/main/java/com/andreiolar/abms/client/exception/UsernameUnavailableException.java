package com.andreiolar.abms.client.exception;

public class UsernameUnavailableException extends Exception {

	private static final long serialVersionUID = -6247245139348004280L;

	public UsernameUnavailableException() {
		super();
	}

	public UsernameUnavailableException(String message, Throwable cause) {
		super(message, cause);
	}

	public UsernameUnavailableException(String message) {
		super(message);
	}

	public UsernameUnavailableException(Throwable cause) {
		super(cause);
	}

}
