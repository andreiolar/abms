package com.andreiolar.abms.shared;

import com.andreiolar.abms.client.rpc.DBCheckAvailableUsername;
import com.andreiolar.abms.client.rpc.DBCheckAvailableUsernameAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class UsernameValidator extends Validator {

	private boolean result = false;

	@Override
	public boolean validate(String value) {

		DBCheckAvailableUsernameAsync rpcService = (DBCheckAvailableUsernameAsync) GWT.create(DBCheckAvailableUsername.class);
		ServiceDefTarget target = (ServiceDefTarget) rpcService;
		String moduleRelativeURL = GWT.getModuleBaseURL() + "DBCheckAvailableUsernameImpl";
		target.setServiceEntryPoint(moduleRelativeURL);

		rpcService.checkForUsername(value, new AsyncCallback<User>() {

			@Override
			public void onSuccess(User user) {
				result = false;
			}

			@Override
			public void onFailure(Throwable caught) {
				result = true;
			}
		});

		if (result) {
			errorMessage = "";
			return true;
		} else {
			errorMessage = "Username already in use";
			return false;
		}
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}
}
