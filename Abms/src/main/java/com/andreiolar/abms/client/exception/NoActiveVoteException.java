package com.andreiolar.abms.client.exception;

public class NoActiveVoteException extends Exception {

	private static final long serialVersionUID = -5204082699855045713L;

	public NoActiveVoteException() {
		super();
	}

	public NoActiveVoteException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoActiveVoteException(String message) {
		super(message);
	}

	public NoActiveVoteException(Throwable cause) {
		super(cause);
	}
}
