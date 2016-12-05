package com.andreiolar.abms.client.exception;

import com.google.gwt.user.client.rpc.IsSerializable;

public class InvalidCredentialsException extends Exception implements IsSerializable {

	private static final long serialVersionUID = 5892090060982062423L;

	public InvalidCredentialsException() {
		super();
	}

	public InvalidCredentialsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidCredentialsException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidCredentialsException(String message) {
		super(message);
	}

	public InvalidCredentialsException(Throwable cause) {
		super(cause);
	}

}
