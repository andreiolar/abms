package com.andreiolar.abms.client.exception;

public class OldPasswordNotCorrectException extends Exception {

	private static final long serialVersionUID = -4545546799268550658L;

	public OldPasswordNotCorrectException() {
		super();
	}

	public OldPasswordNotCorrectException(String message, Throwable cause) {
		super(message, cause);
	}

	public OldPasswordNotCorrectException(String message) {
		super(message);
	}

	public OldPasswordNotCorrectException(Throwable cause) {
		super(cause);
	}
}
