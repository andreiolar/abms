package com.andreiolar.abms.client.exception;

public class ConsumptionReportNotFoundException extends Exception {

	private static final long serialVersionUID = -4230476965881978556L;

	public ConsumptionReportNotFoundException() {
		super();
	}

	public ConsumptionReportNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConsumptionReportNotFoundException(String message) {
		super(message);
	}

	public ConsumptionReportNotFoundException(Throwable cause) {
		super(cause);
	}
}
