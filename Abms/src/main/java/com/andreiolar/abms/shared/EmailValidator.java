package com.andreiolar.abms.shared;

public class EmailValidator extends Validator {

	@Override
	public boolean validate(String value) {
		if (value.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
			errorMessage = "";
			return true;
		} else {
			errorMessage = "Enter valid email Id";
			return false;
		}
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}
}