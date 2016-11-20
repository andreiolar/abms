package com.andreiolar.abms.client.view.impl;

import java.util.Date;

import com.andreiolar.abms.client.constants.DialogBoxConstants;
import com.andreiolar.abms.client.constants.LoginFormConstants;
import com.andreiolar.abms.client.place.AdminPlace;
import com.andreiolar.abms.client.place.RegisterPlace;
import com.andreiolar.abms.client.place.UserPlace;
import com.andreiolar.abms.client.rpc.DBConnection;
import com.andreiolar.abms.client.rpc.DBConnectionAsync;
import com.andreiolar.abms.client.rpc.DBForgotPassword;
import com.andreiolar.abms.client.rpc.DBForgotPasswordAsync;
import com.andreiolar.abms.client.utils.DialogBoxCreator;
import com.andreiolar.abms.client.view.LoginView;
import com.andreiolar.abms.shared.User;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class LoginViewImpl extends Composite implements LoginView {

	private VerticalPanel panel = new VerticalPanel();
	private TextBox usernameBox = new TextBox();
	private PasswordTextBox passwordBox = new PasswordTextBox();
	private Button loginButton = new Button();
	private Button registerButton = new Button();

	private static Presenter presenter;
	private static String name;

	public LoginViewImpl() {

		panel.setSpacing(8);
		Widget loginForm = createLoginForm();
		panel.add(loginForm);
		panel.setStyleName("loginPanel");

		initWidget(panel);
	}

	@SuppressWarnings("deprecation")
	private Widget createLoginForm() {

		// Create table to layout the form options
		FlexTable layout = new FlexTable();
		layout.setCellSpacing(6);
		layout.setWidth("300px");
		FlexCellFormatter cellFormatter = layout.getFlexCellFormatter();

		// Add a title to the form
		layout.setHTML(0, 0, LoginFormConstants.FORM_NAME);
		cellFormatter.setColSpan(0, 0, 2);
		cellFormatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);

		// Add some standard form options
		layout.setHTML(1, 0, LoginFormConstants.USERNAME);
		layout.setWidget(1, 1, usernameBox);
		layout.setHTML(2, 0, LoginFormConstants.PASSWORD);
		layout.setWidget(2, 1, passwordBox);

		usernameBox.setStyleName("fixed-input");
		passwordBox.setStyleName("fixed-input");

		passwordBox.addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					final String username = usernameBox.getText();
					final String password = passwordBox.getText();
					GWT.runAsync(new RunAsyncCallback() {

						@Override
						public void onSuccess() {
							performUserConnection(username, password);
						}

						@Override
						public void onFailure(Throwable reason) {
							// TODO Auto-generated method stub
						}
					});
				}
			}
		});

		// Create buttons
		loginButton.setHTML(LoginFormConstants.LOGIN_BUTTON);
		loginButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final String username = usernameBox.getText();
				final String password = passwordBox.getText();
				GWT.runAsync(new RunAsyncCallback() {

					@Override
					public void onSuccess() {
						performUserConnection(username, password);
					}

					@Override
					public void onFailure(Throwable reason) {
						// TODO Auto-generated method stub
					}
				});
			}
		});

		registerButton.setHTML(LoginFormConstants.REGISTER_BUTTON);
		registerButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				presenter.goTo(new RegisterPlace(name));
			}
		});

		// Create panel to hold buttons
		HorizontalPanel hPanel = new HorizontalPanel();

		hPanel.setSpacing(10);
		hPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_DEFAULT);

		hPanel.add(loginButton);
		hPanel.add(registerButton);

		// Add horizontal panel to layout
		layout.setWidget(3, 0, hPanel);
		cellFormatter.setColSpan(3, 0, 3);

		// Create advanced options
		Grid advancedOptions = new Grid(1, 1);
		advancedOptions.setCellSpacing(6);

		Hyperlink hyperlink = new Hyperlink();
		hyperlink.setHTML(LoginFormConstants.HYPERLINK_TEXT);
		hyperlink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				DialogBox dialogBox = createForgotPasswordDialogBox();
				dialogBox.setGlassEnabled(true);
				dialogBox.setAnimationEnabled(true);
				dialogBox.center();
				dialogBox.show();
			}
		});
		advancedOptions.setWidget(0, 0, hyperlink);

		// Add advanced options to form in a disclosure panel
		DisclosurePanel advancedDisclosure = new DisclosurePanel(LoginFormConstants.ADVANCED_CRITERIA);
		advancedDisclosure.setAnimationEnabled(true);
		advancedDisclosure.ensureDebugId("disclosurePanel");
		advancedDisclosure.setContent(advancedOptions);

		// Add disclorue panel to layout
		layout.setWidget(4, 0, advancedDisclosure);
		cellFormatter.setColSpan(4, 0, 2);

		// Wrap the contents in a DecoratorPanel
		DecoratorPanel decPanel = new DecoratorPanel();
		decPanel.setWidget(layout);

		return decPanel;

	}

	protected DialogBox createForgotPasswordDialogBox() {
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Password Recovery");

		// Create a table to layout the content
		VerticalPanel dialogContents = new VerticalPanel();
		dialogContents.setSpacing(4);
		dialogBox.setWidget(dialogContents);

		// Add text to the top of the dialog
		HTML details = new HTML("<p><b>Password Recovery</b></p>");
		dialogContents.add(details);
		dialogContents.setCellHorizontalAlignment(details, HasHorizontalAlignment.ALIGN_CENTER);

		// Add text to the top of the dialog
		HTML details2 = new HTML("<p><b>Please enter your E-Mail Address in order to start recovery procedure!</b></p>");
		dialogContents.add(details2);
		dialogContents.setCellHorizontalAlignment(details, HasHorizontalAlignment.ALIGN_CENTER);

		// Add a TextBox
		Label emailLabel = new Label();
		emailLabel.setText("E-Mail");

		final TextBox emailBox = new TextBox();
		emailBox.setStyleName("fixed-input");

		Grid grid = new Grid(1, 2);
		grid.setWidget(0, 0, emailLabel);
		grid.setWidget(0, 1, emailBox);

		grid.getWidget(0, 0).getElement().getStyle().setMarginRight(20.0, Unit.PX);
		grid.getWidget(0, 0).getElement().getStyle().setMarginTop(5.0, Unit.PX);
		grid.getWidget(0, 1).getElement().getStyle().setMarginTop(5.0, Unit.PX);

		// Add Buttons
		HorizontalPanel hPanel = new HorizontalPanel();

		Button submitButton = new Button();
		submitButton.setText("Submit");
		submitButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String email = emailBox.getText();

				DOM.getElementById("loading").getStyle().setDisplay(Display.BLOCK);
				DBForgotPasswordAsync rpcService = (DBForgotPasswordAsync) GWT.create(DBForgotPassword.class);
				ServiceDefTarget target = (ServiceDefTarget) rpcService;
				String moduleRelativeURL = GWT.getModuleBaseURL() + "DBForgotPasswordImpl";
				target.setServiceEntryPoint(moduleRelativeURL);

				rpcService.sendMailToServer(email, new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
						DialogBox dialogBox = DialogBoxCreator.createDialogBox(LoginFormConstants.FAILURE_SEND_EMAIL_TITLE, caught.getMessage(),
								DialogBoxConstants.CLOSE_BUTTON, false, false);
						dialogBox.setGlassEnabled(true);
						dialogBox.setAnimationEnabled(true);
						dialogBox.center();
						dialogBox.show();
					}

					@Override
					public void onSuccess(Boolean result) {
						DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
						DialogBox dialogBox = DialogBoxCreator.createDialogBox(LoginFormConstants.SUCCESS_SEND_EMAIL_TITLE,
								LoginFormConstants.SUCCESS_SEND_EMAIL_MESSAGE, DialogBoxConstants.CLOSE_BUTTON, false, true);
						dialogBox.setGlassEnabled(true);
						dialogBox.setAnimationEnabled(true);
						dialogBox.center();
						dialogBox.show();
					}
				});
			}
		});

		Button cancelButton = new Button();
		cancelButton.setText("Cancel");
		cancelButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});

		hPanel.add(submitButton);
		hPanel.add(cancelButton);

		hPanel.getWidget(0).getElement().getStyle().setMarginRight(20.0, Unit.PX);
		hPanel.getWidget(0).getElement().getStyle().setMarginTop(10.0, Unit.PX);
		hPanel.getWidget(1).getElement().getStyle().setMarginTop(10.0, Unit.PX);

		dialogContents.add(grid);
		dialogContents.setCellHorizontalAlignment(grid, HasHorizontalAlignment.ALIGN_CENTER);
		dialogContents.add(hPanel);
		dialogContents.setCellHorizontalAlignment(hPanel, HasHorizontalAlignment.ALIGN_CENTER);

		return dialogBox;
	}

	private static void performUserConnection(String username, String password) {
		DOM.getElementById("loading").getStyle().setDisplay(Display.BLOCK);
		DBConnectionAsync rpcService = (DBConnectionAsync) GWT.create(DBConnection.class);
		ServiceDefTarget target = (ServiceDefTarget) rpcService;
		String moduleRelativeURL = GWT.getModuleBaseURL() + "DBConnectionImpl";
		target.setServiceEntryPoint(moduleRelativeURL);

		rpcService.authenticateUser(username, password, new AsyncCallback<User>() {

			@Override
			public void onSuccess(User user) {
				DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);

				String username = user.getUsername();
				final long DURATION = 1000 * 60 * 60 * 24 * 1; // 1 day
				Date expires = new Date(System.currentTimeMillis() + DURATION);
				Cookies.setCookie("sid", username, expires, null, "/", false);

				if (user.getType().equals("User")) {
					presenter.goTo(new UserPlace(username));
				} else if (user.getType().equals("Admin")) {
					presenter.goTo(new AdminPlace(username));
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
				DialogBox dialogBox = DialogBoxCreator.createDialogBox(DialogBoxConstants.TITLE, caught.getMessage(), DialogBoxConstants.CLOSE_BUTTON,
						false, false);
				dialogBox.setGlassEnabled(true);
				dialogBox.setAnimationEnabled(true);
				dialogBox.center();
				dialogBox.show();
			}

		});
	}

	@Override
	public void setName(String placeName) {
		this.name = name;
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

}
