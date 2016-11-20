package com.andreiolar.abms.client.view.impl;

import com.andreiolar.abms.client.constants.DialogBoxConstants;
import com.andreiolar.abms.client.constants.PasswordRecoveryConstants;
import com.andreiolar.abms.client.rpc.DBChangeForgotPassword;
import com.andreiolar.abms.client.rpc.DBChangeForgotPasswordAsync;
import com.andreiolar.abms.client.utils.DialogBoxCreator;
import com.andreiolar.abms.client.view.PasswordRecoveryView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class PasswordRecoveryViewImpl extends Composite implements PasswordRecoveryView {

	private VerticalPanel panel = new VerticalPanel();

	private PasswordTextBox passwordBox = new PasswordTextBox();
	private PasswordTextBox retypePasswordBox = new PasswordTextBox();

	private Button submitButton = new Button();
	private Button cancelButton = new Button();

	public PasswordRecoveryViewImpl() {
		panel.setSpacing(8);
		Widget resetForm = createResetForm();
		panel.add(resetForm);
		panel.setStyleName("loginPanel");

		initWidget(panel);
	}

	private Widget createResetForm() {
		FlexTable layout = new FlexTable();
		layout.setCellSpacing(6);
		layout.setWidth("300px");
		FlexCellFormatter cellFormatter = layout.getFlexCellFormatter();

		// Add a title to the form
		layout.setHTML(0, 0, PasswordRecoveryConstants.FORM_NAME);
		cellFormatter.setColSpan(0, 0, 2);
		cellFormatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);

		// Add form options
		layout.setHTML(1, 0, PasswordRecoveryConstants.NEW_PASSWORD);
		layout.setWidget(1, 1, passwordBox);
		layout.setHTML(2, 0, PasswordRecoveryConstants.RETYPE_PASSWORD);
		layout.setWidget(2, 1, retypePasswordBox);

		passwordBox.setStyleName("fixed-input");
		retypePasswordBox.setStyleName("fixed-input");

		// Create Buttons
		submitButton.setHTML(PasswordRecoveryConstants.SUBMIT_BUTTON);
		submitButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String password = passwordBox.getText();
				String retypedPassword = retypePasswordBox.getText();

				String message = "";
				boolean passwordSet = true;
				boolean retypedPasswordSet = true;
				boolean passwordsMatch = true;

				if (password == null || password.trim().equals("")) {
					message += "<p>Please choose a password!</p>";
					passwordSet = false;
				}

				if (retypedPassword == null || retypedPassword.trim().equals("")) {
					message += "<p>Please retype your password!</p>";
					retypedPasswordSet = false;
				}

				if (!password.equals(retypedPassword)) {
					message += "<p>Passwords must match!</p>";
					passwordsMatch = false;
				}

				if (passwordSet && retypedPasswordSet && passwordsMatch) {
					DBChangeForgotPasswordAsync rpcService = (DBChangeForgotPasswordAsync) GWT.create(DBChangeForgotPassword.class);
					ServiceDefTarget target = (ServiceDefTarget) rpcService;
					String moduleRelativeURL = GWT.getModuleBaseURL() + "DBChangeForgotPasswordImpl";
					target.setServiceEntryPoint(moduleRelativeURL);

					String url = Window.Location.getHref();
					String place = url.substring(url.indexOf("#") + 1, url.length());

					String token = place.substring(place.indexOf(":") + 1, place.length());

					rpcService.resetPassword(token, retypedPassword, new AsyncCallback<Boolean>() {

						@Override
						public void onSuccess(Boolean result) {
							DialogBox dialogBox = DialogBoxCreator.createDialogBox(PasswordRecoveryConstants.SUCCESS_RESETING_PASSWORD_TITLE,
									PasswordRecoveryConstants.SUCCESS_RESETING_PASSWORD_MESSAGE, DialogBoxConstants.CLOSE_BUTTON, true, false);
							dialogBox.setGlassEnabled(true);
							dialogBox.setAnimationEnabled(true);
							dialogBox.center();
							dialogBox.show();

						}

						@Override
						public void onFailure(Throwable caught) {
							DialogBox dialogBox = DialogBoxCreator.createDialogBox(PasswordRecoveryConstants.FAILURE_RESETING_PASSWORD_TITLE,
									caught.getMessage(), DialogBoxConstants.CLOSE_BUTTON, false, false);
							dialogBox.setGlassEnabled(true);
							dialogBox.setAnimationEnabled(true);
							dialogBox.center();
							dialogBox.show();

						}
					});

				} else {
					DialogBox dialogBox = DialogBoxCreator.createDialogBox(PasswordRecoveryConstants.INVALID_PASSWORDS, message,
							DialogBoxConstants.CLOSE_BUTTON, false, false);
					dialogBox.setGlassEnabled(true);
					dialogBox.setAnimationEnabled(true);
					dialogBox.center();
					dialogBox.show();
				}
			}
		});

		cancelButton.setHTML(PasswordRecoveryConstants.CANCEL_BUTTON);
		cancelButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Window.Location.replace("http://127.0.0.1:8888/Abms.html");
			}
		});

		// Create panel to hold buttons
		HorizontalPanel hPanel = new HorizontalPanel();

		hPanel.setSpacing(10);
		hPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_DEFAULT);

		hPanel.add(submitButton);
		hPanel.add(cancelButton);

		// Add horizontal panel to layout
		layout.setWidget(3, 0, hPanel);
		cellFormatter.setColSpan(3, 0, 3);

		DecoratorPanel decPanel = new DecoratorPanel();
		decPanel.setWidget(layout);

		return decPanel;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPresenter(Presenter presenter) {
		// TODO Auto-generated method stub

	}

}
