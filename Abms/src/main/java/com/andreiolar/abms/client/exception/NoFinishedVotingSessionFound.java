package com.andreiolar.abms.client.exception;

public class NoFinishedVotingSessionFound extends Exception {

	private static final long serialVersionUID = -2834417983843960651L;

	public NoFinishedVotingSessionFound() {
		super();
	}

	public NoFinishedVotingSessionFound(String message, Throwable cause) {
		super(message, cause);
	}

	public NoFinishedVotingSessionFound(String message) {
		super(message);
	}

	public NoFinishedVotingSessionFound(Throwable cause) {
		super(cause);
	}
}
