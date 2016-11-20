package com.andreiolar.abms.client.widgets;

import com.andreiolar.abms.client.constants.DialogBoxConstants;
import com.andreiolar.abms.client.constants.UserMenuConstants;
import com.andreiolar.abms.client.rpc.DBChangePassword;
import com.andreiolar.abms.client.rpc.DBChangePasswordAsync;
import com.andreiolar.abms.client.utils.DialogBoxCreator;
import com.andreiolar.abms.shared.UserInfo;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class PasswordChangeWidget extends Composite implements CustomWidget {

	private UserInfo userInfo;

	public PasswordChangeWidget(UserInfo userInfo) {
		this.userInfo = userInfo;

		initWidget(initializeWidget());
	}

	@Override
	public Widget initializeWidget() {
		VerticalPanel panel = new VerticalPanel();
		panel.setSpacing(10);

		Label description = new Label("Please choose a new password");
		description.getElement().getStyle().setFontSize(24.0, Unit.PX);
		description.getElement().getStyle().setFontWeight(FontWeight.BOLD);

		Label newPassword = new Label("New Password");
		final PasswordTextBox newPasswordBox = new PasswordTextBox();

		Label retypePassword = new Label("Retype Password");
		final PasswordTextBox retypePasswordBox = new PasswordTextBox();

		Grid passwordPanel = new Grid(2, 2);
		passwordPanel.setWidget(0, 0, newPassword);
		passwordPanel.setWidget(0, 1, newPasswordBox);

		passwordPanel.setWidget(1, 0, retypePassword);
		passwordPanel.setWidget(1, 1, retypePasswordBox);

		Button changePasswordButton = new Button();
		changePasswordButton.setText("Change Password");
		changePasswordButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				String newPassword = newPasswordBox.getText();
				String retypePassword = retypePasswordBox.getText();

				String message = "";
				boolean noPassword = false;
				boolean noRetypePassword = false;
				boolean passwordsNotMatch = false;

				if (newPassword == null || newPassword.trim().equals("")) {
					message += "<p>Please type a password!</p>";
					noPassword = true;
				}

				if (retypePassword == null || retypePassword.trim().equals("")) {
					message += "<p>Please retype your password!</p>";
					noRetypePassword = true;
				}

				if (!newPassword.equals(retypePassword)) {
					message += "<p>Passwords must match!</p>";
					passwordsNotMatch = true;
				}

				if (!noPassword && !noRetypePassword) {

					if (!passwordsNotMatch) {
						DOM.getElementById("loading").getStyle().setDisplay(Display.BLOCK);
						DBChangePasswordAsync rpcService = (DBChangePasswordAsync) GWT.create(DBChangePassword.class);
						ServiceDefTarget target = (ServiceDefTarget) rpcService;
						String moduleRelativeURL = GWT.getModuleBaseURL() + "DBChangePasswordImpl";
						target.setServiceEntryPoint(moduleRelativeURL);

						rpcService.changePassword(userInfo.getUsername(), newPassword, new AsyncCallback<Boolean>() {

							@Override
							public void onFailure(Throwable caught) {
								DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
								DialogBox dialogBox = DialogBoxCreator.createDialogBox(UserMenuConstants.PASSWORD_ERROR_TITLE, caught.getMessage(),
										DialogBoxConstants.CLOSE_BUTTON, false, false);
								dialogBox.setGlassEnabled(true);
								dialogBox.setAnimationEnabled(true);
								dialogBox.center();
								dialogBox.show();
							}

							@Override
							public void onSuccess(Boolean result) {
								DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
								DialogBox dialogBox = DialogBoxCreator.createDialogBox(UserMenuConstants.PASSWORD_SUCCESS_TITLE,
										UserMenuConstants.PASSWORD_SUCCESS_MESSAGE, DialogBoxConstants.CLOSE_BUTTON, true, false);
								dialogBox.setGlassEnabled(true);
								dialogBox.setAnimationEnabled(true);
								dialogBox.center();
								dialogBox.show();
							}
						});

					} else {
						DialogBox dialogBox = DialogBoxCreator.createDialogBox(UserMenuConstants.PASSWORD_ERROR_TITLE, message,
								DialogBoxConstants.CLOSE_BUTTON, false, false);
						dialogBox.setGlassEnabled(true);
						dialogBox.setAnimationEnabled(true);
						dialogBox.center();
						dialogBox.show();
					}

				} else {
					DialogBox dialogBox = DialogBoxCreator.createDialogBox(UserMenuConstants.PASSWORD_ERROR_TITLE, message,
							DialogBoxConstants.CLOSE_BUTTON, false, false);
					dialogBox.setGlassEnabled(true);
					dialogBox.setAnimationEnabled(true);
					dialogBox.center();
					dialogBox.show();
				}

			}
		});

		Button reset = new Button();
		reset.setText("Reset");
		reset.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				newPasswordBox.setText(null);
				retypePasswordBox.setText(null);
			}
		});

		Grid buttonPanel = new Grid(1, 2);
		buttonPanel.setWidget(0, 0, changePasswordButton);
		buttonPanel.setWidget(0, 1, reset);
		buttonPanel.setCellSpacing(20);

		panel.add(description);
		panel.add(passwordPanel);
		panel.add(buttonPanel);

		return panel;

	}

}
