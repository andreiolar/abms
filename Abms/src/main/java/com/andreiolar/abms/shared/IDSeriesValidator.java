package com.andreiolar.abms.shared;

public class IDSeriesValidator extends Validator {

	@Override
	public boolean validate(String value) {
		if (value.matches("[A-Z]{2}[0-9]{6}")) {
			errorMessage = "";
			return true;
		} else {
			errorMessage = "Enter a valid ID series";
			return false;
		}
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}

}
