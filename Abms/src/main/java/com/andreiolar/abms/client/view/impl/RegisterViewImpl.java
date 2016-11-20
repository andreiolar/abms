package com.andreiolar.abms.client.view.impl;

import java.util.Date;
import java.util.List;

import com.andreiolar.abms.client.constants.DialogBoxConstants;
import com.andreiolar.abms.client.constants.RegistrationFormConstants;
import com.andreiolar.abms.client.rpc.DBCheckForEmail;
import com.andreiolar.abms.client.rpc.DBCheckForEmailAsync;
import com.andreiolar.abms.client.rpc.DBGetCountries;
import com.andreiolar.abms.client.rpc.DBGetCountriesAsync;
import com.andreiolar.abms.client.rpc.DBRegisterUser;
import com.andreiolar.abms.client.rpc.DBRegisterUserAsync;
import com.andreiolar.abms.client.view.RegisterView;
import com.andreiolar.abms.client.widgets.CustomTexBox;
import com.andreiolar.abms.shared.Countries;
import com.andreiolar.abms.shared.Email;
import com.andreiolar.abms.shared.EmptyStringValidator;
import com.andreiolar.abms.shared.IDSeriesValidator;
import com.andreiolar.abms.shared.MobileNumberValidator;
import com.andreiolar.abms.shared.NameValidator;
import com.andreiolar.abms.shared.PersonalNumberValidator;
import com.andreiolar.abms.shared.UserInfo;
import com.andreiolar.abms.shared.UsernameValidator;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class RegisterViewImpl extends Composite implements RegisterView {

	private static Presenter presenter;
	private static String name;

	private static String aptNumber;

	private SimplePanel tabPanel = new SimplePanel();
	private static boolean isEmailValid = false;
	private static String email;

	private static TextBox formEmailBox = new TextBox();
	private static TextBox aptNumberBox = new TextBox();

	public RegisterViewImpl() {

		Widget tabBar = createRegistrationForm();
		tabPanel.add(tabBar);

		initWidget(tabPanel);
	}

	private static Widget createRegistrationForm() {

		final TabPanel tabPanel = new TabPanel();

		// Create the e-mail tab
		Widget emailCheckerWidget = createEmailChecker();
		tabPanel.add(emailCheckerWidget, RegistrationFormConstants.CHECK_EMAIL_TAB);

		// Create the Personal Information Tab
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				Widget personalInformationWidget = createPersonalInformationWidget();
				tabPanel.add(personalInformationWidget, RegistrationFormConstants.DETAILS_TAB);
			}

			@Override
			public void onFailure(Throwable reason) {
				// TODO Auto-generated method stub
			}
		});

		tabPanel.setWidth("40%");
		if (isEmailValid) {
			tabPanel.selectTab(1, true);
		} else {
			tabPanel.selectTab(0, true);
		}

		tabPanel.addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {

			@Override
			public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {
				if (event.getItem() == 1 && !isEmailValid) {
					event.cancel();
				}

				if (isEmailValid) {
					if (event.getItem() == 0) {
						event.cancel();
					}
				}
			}
		});

		return tabPanel;
	}

	private static Widget createPersonalInformationWidget() {
		VerticalPanel panel = new VerticalPanel();
		panel.setSpacing(10);

		Label personalInformationLabel = new Label("Personal Information");
		personalInformationLabel.getElement().getStyle().setFontSize(24.0, Unit.PX);
		personalInformationLabel.getElement().getStyle().setFontWeight(FontWeight.BOLD);

		Label loginInformationLabel = new Label("Login Information");
		loginInformationLabel.getElement().getStyle().setFontSize(24.0, Unit.PX);
		loginInformationLabel.getElement().getStyle().setFontWeight(FontWeight.BOLD);

		// Create fields for the Grid panel

		// First Name
		Label firstName = new Label("First Name");
		final CustomTexBox firstNameBox = new CustomTexBox();
		firstNameBox.setMaxLength(30);
		firstNameBox.addValidator(new NameValidator());
		firstNameBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				validate(firstNameBox);

			}
		});
		Label firstNameObs = new Label("(Max 30 characters a-z and a-Z)");

		// Last Name
		Label lastName = new Label("Last Name");
		final CustomTexBox lastNameBox = new CustomTexBox();
		lastNameBox.setMaxLength(30);
		lastNameBox.addValidator(new NameValidator());
		lastNameBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				validate(lastNameBox);

			}
		});
		Label lastNameObs = new Label("(Max 30 characters a-z and a-Z)");

		// Date of Birth
		Label dateOfBirth = new Label("Date Of Birth");
		// Configure DateBox
		DateTimeFormat dateFormat = DateTimeFormat.getLongDateFormat();
		final DateBox dateBox = new DateBox();
		dateBox.setFormat(new DateBox.DefaultFormat(dateFormat));
		dateBox.getDatePicker().setYearArrowsVisible(true);
		dateBox.getDatePicker().setYearAndMonthDropdownVisible(true);
		dateBox.getDatePicker().setVisibleYearCount(200);
		dateBox.setStyleName("fixed-input");

		// E-Mail Address
		Label emailAddress = new Label("E-Mail Address");
		formEmailBox.setEnabled(false);
		formEmailBox.setStyleName("fixed-input");

		// Apartment Number
		Label apartmentNumberLabel = new Label("Apartment Number");
		aptNumberBox.setStyleName("fixed-input");
		aptNumberBox.setEnabled(false);

		// Mobile Number
		Label mobileNumber = new Label("Mobile Number");
		final CustomTexBox mobileNumberBox = new CustomTexBox();
		mobileNumberBox.setMaxLength(10);
		mobileNumberBox.addValidator(new MobileNumberValidator());
		mobileNumberBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				mobileNumberBox.validate();
			}
		});
		Label mobileNumberObs = new Label("(10 digit number)");

		// Gender
		Label gender = new Label("Gender");
		VerticalPanel genderButtons = new VerticalPanel();
		final RadioButton male = new RadioButton("gender", "Male");
		final RadioButton female = new RadioButton("gender", "Female");
		genderButtons.add(male);
		genderButtons.add(female);

		// Address
		Label address = new Label("Address");
		final TextArea textArea = new TextArea();
		textArea.setVisibleLines(5);
		textArea.setWidth("200px");
		Label addressObs = new Label("(From your current ID)");

		// Country
		Label country = new Label("Country");
		final ListBox countryBox = new ListBox(false);
		Label countryObs = new Label("(From your current ID)");
		fillCountryBox(countryBox);
		countryBox.getElement().getStyle().setWidth(210.0, Unit.PX);
		countryBox.getElement().getStyle().setHeight(25.0, Unit.PX);

		// City
		Label city = new Label("City");
		final CustomTexBox cityBox = new CustomTexBox();
		cityBox.addValidator(new EmptyStringValidator());
		cityBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				cityBox.validate();
			}
		});
		Label cityObs = new Label("(From your current ID)");

		// Personal ID Number
		Label personalNumber = new Label("Personal ID Number");
		final CustomTexBox personalNumberBox = new CustomTexBox();
		personalNumberBox.setMaxLength(13);
		personalNumberBox.addValidator(new PersonalNumberValidator());
		personalNumberBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				validate(personalNumberBox);
			}
		});
		Label personalNumberObs = new Label("(From your current ID. 13 digit number)");

		// ID Series
		Label seriesNumber = new Label("ID Series");
		final CustomTexBox seriesNumberBox = new CustomTexBox();
		seriesNumberBox.setMaxLength(8);
		seriesNumberBox.addValidator(new IDSeriesValidator());
		seriesNumberBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				seriesNumberBox.validate();
			}
		});
		Label seriesNumberObs = new Label("(From your current ID)");

		// Create a Grid to hold the personal information form
		Grid registrationPanel = new Grid(12, 3);

		registrationPanel.setWidget(0, 0, firstName);
		registrationPanel.setWidget(0, 1, firstNameBox);
		registrationPanel.setWidget(0, 2, firstNameObs);

		registrationPanel.setWidget(1, 0, lastName);
		registrationPanel.setWidget(1, 1, lastNameBox);
		registrationPanel.setWidget(1, 2, lastNameObs);

		registrationPanel.setWidget(2, 0, dateOfBirth);
		registrationPanel.setWidget(2, 1, dateBox);

		registrationPanel.setWidget(3, 0, emailAddress);
		registrationPanel.setWidget(3, 1, formEmailBox);

		registrationPanel.setWidget(4, 0, apartmentNumberLabel);
		registrationPanel.setWidget(4, 1, aptNumberBox);

		registrationPanel.setWidget(5, 0, mobileNumber);
		registrationPanel.setWidget(5, 1, mobileNumberBox);
		registrationPanel.setWidget(5, 2, mobileNumberObs);

		registrationPanel.setWidget(6, 0, gender);
		registrationPanel.setWidget(6, 1, genderButtons);

		registrationPanel.setWidget(7, 0, address);
		registrationPanel.setWidget(7, 1, textArea);
		registrationPanel.setWidget(7, 2, addressObs);

		registrationPanel.setWidget(8, 0, city);
		registrationPanel.setWidget(8, 1, cityBox);
		registrationPanel.setWidget(8, 2, cityObs);

		registrationPanel.setWidget(9, 0, country);
		registrationPanel.setWidget(9, 1, countryBox);
		registrationPanel.setWidget(9, 2, countryObs);

		registrationPanel.setWidget(10, 0, personalNumber);
		registrationPanel.setWidget(10, 1, personalNumberBox);
		registrationPanel.setWidget(10, 2, personalNumberObs);

		registrationPanel.setWidget(11, 0, seriesNumber);
		registrationPanel.setWidget(11, 1, seriesNumberBox);
		registrationPanel.setWidget(11, 2, seriesNumberObs);

		/* Fields for the Login Panel */

		// Username
		Label username = new Label("Username");
		final CustomTexBox usernameBox = new CustomTexBox();
		usernameBox.addValidator(new EmptyStringValidator());
		usernameBox.addValidator(new UsernameValidator());

		// Password
		Label password = new Label("Password");
		final PasswordTextBox passwordBox = new PasswordTextBox();
		passwordBox.setStyleName("fixed-input");

		// Retype Password
		Label retypePassword = new Label("Retype Password");
		final PasswordTextBox retypePasswordBox = new PasswordTextBox();
		retypePasswordBox.setStyleName("fixed-input");

		// Create a Grid to hold the login information form
		Grid loginPanel = new Grid(3, 3);

		loginPanel.setWidget(0, 0, username);
		loginPanel.setWidget(0, 1, usernameBox);

		loginPanel.setWidget(1, 0, password);
		loginPanel.setWidget(1, 1, passwordBox);

		loginPanel.setWidget(2, 0, retypePassword);
		loginPanel.setWidget(2, 1, retypePasswordBox);

		/* Buttons */

		// Create submit button
		Button submitButton = new Button();
		submitButton.setText("Submit");
		submitButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				if (firstNameBox.validate() && lastNameBox.validate() && mobileNumberBox.validate() && cityBox.validate()
						&& personalNumberBox.validate() && seriesNumberBox.validate() && usernameBox.validate()) {

					String firstName = firstNameBox.getText();
					String lastName = lastNameBox.getText();
					String mobileNumber = mobileNumberBox.getText();
					String city = cityBox.getText();
					String personalNumber = personalNumberBox.getText();
					String idSeries = seriesNumberBox.getText();
					String username = usernameBox.getText();

					Date dateOfBirth = dateBox.getValue();
					String email = formEmailBox.getText();
					String aptNumber = aptNumberBox.getText();
					String gender = null;

					if (male.getValue()) {
						gender = male.getText();
					} else if (female.getValue()) {
						gender = female.getText();
					}

					String address = textArea.getText();
					String country = countryBox.getValue(countryBox.getSelectedIndex());
					String password = passwordBox.getText();
					String retypedPassword = retypePasswordBox.getText();

					String messageText = "";

					if (dateOfBirth == null) {
						messageText += "<p>Please enter your date of birth!</p>";
					}

					if (gender == null) {
						messageText += "<p>Plese select your gender!</p>";
					}

					if (address == null || address.trim().equals("")) {
						messageText += "<p>Address can't be empty!</p>";
					}

					if (password == null || password.trim().equals("")) {
						messageText += "<p>Please choose a password!</p>";
					}

					if (retypedPassword == null || retypedPassword.trim().equals("")) {
						messageText += "<p>Please retype your password!</p>";
					}

					boolean passwordsNotEmpty = false;

					if ((password != null && !password.trim().equals("")) && (retypedPassword != null && !retypedPassword.trim().equals(""))) {
						passwordsNotEmpty = true;
					}

					if (passwordsNotEmpty && !password.equals(retypedPassword)) {
						messageText += "<p>Passwords must match!</p>";
					}

					if (dateOfBirth == null || gender == null || (address == null || address.trim().equals(""))
							|| (password == null || password.trim().equals(""))
							|| (retypedPassword == null || retypedPassword.trim().equals("") || !(password.equals(retypedPassword)))) {
						DialogBox dialogBox = createDialogBox(RegistrationFormConstants.INVALID_DATA_TITLE, messageText,
								DialogBoxConstants.CLOSE_BUTTON, false);
						dialogBox.setGlassEnabled(true);
						dialogBox.setAnimationEnabled(true);
						dialogBox.center();
						dialogBox.show();
					} else {
						UserInfo userInfo = new UserInfo(firstName, lastName, dateOfBirth, email, mobileNumber, gender, address, city, country,
								personalNumber, idSeries, username, password, aptNumber);

						DOM.getElementById("loading").getStyle().setDisplay(Display.BLOCK);
						DBRegisterUserAsync rpcService = (DBRegisterUserAsync) GWT.create(DBRegisterUser.class);
						ServiceDefTarget target = (ServiceDefTarget) rpcService;
						String moduleRelativeURL = GWT.getModuleBaseURL() + "DBRegisterUserImpl";
						target.setServiceEntryPoint(moduleRelativeURL);

						rpcService.registerUser(userInfo, new AsyncCallback<Boolean>() {

							@Override
							public void onFailure(Throwable caught) {
								DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
								DialogBox dialogBox = createDialogBox(RegistrationFormConstants.DATABASE_ERROR, caught.getMessage(),
										DialogBoxConstants.CLOSE_BUTTON, false);
								dialogBox.setGlassEnabled(true);
								dialogBox.setAnimationEnabled(true);
								dialogBox.center();
								dialogBox.show();

							}

							@Override
							public void onSuccess(Boolean result) {
								DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
								DialogBox dialogBox = createDialogBox(RegistrationFormConstants.DIALOG_BOX_TITLE_SUCCESS,
										RegistrationFormConstants.DATABASE_SUCCESS_MESSAGE, DialogBoxConstants.CLOSE_BUTTON, true);
								dialogBox.setGlassEnabled(true);
								dialogBox.setAnimationEnabled(true);
								dialogBox.center();
								dialogBox.show();
							}
						});

					}

				} else {
					DialogBox dialogBox = createDialogBox(RegistrationFormConstants.INVALID_DATA_TITLE,
							RegistrationFormConstants.INVALID_DATA_MESSAGE, DialogBoxConstants.CLOSE_BUTTON, false);
					dialogBox.setGlassEnabled(true);
					dialogBox.setAnimationEnabled(true);
					dialogBox.center();
					dialogBox.show();
				}

			}
		});

		// Create reset button
		Button resetButton = new Button();
		resetButton.setText("Reset");
		resetButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				firstNameBox.setText(null);
				firstNameBox.setErrorStyles(true);
				lastNameBox.setText(null);
				lastNameBox.setErrorStyles(true);
				dateBox.setValue(null);
				mobileNumberBox.setText(null);
				mobileNumberBox.setErrorStyles(true);
				male.setValue(false);
				female.setValue(false);
				textArea.setText(null);
				cityBox.setText(null);
				cityBox.setErrorStyles(true);
				countryBox.setSelectedIndex(0);
				personalNumberBox.setText(null);
				personalNumberBox.setErrorStyles(true);
				seriesNumberBox.setText(null);
				seriesNumberBox.setErrorStyles(true);
				usernameBox.setText(null);
				usernameBox.setErrorStyles(true);
				passwordBox.setText(null);
				retypePasswordBox.setText(null);
			}
		});

		// Create abort button
		Button abortButton = new Button();
		abortButton.setText("Abort");
		abortButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Window.Location.replace("http://127.0.0.1:8888/AdministrareBloc.html");
			}
		});

		// Create Grid to hold the buttons
		Grid buttonPanel = new Grid(1, 3);

		buttonPanel.setWidget(0, 0, submitButton);
		buttonPanel.setWidget(0, 1, resetButton);
		buttonPanel.setWidget(0, 2, abortButton);
		buttonPanel.setCellSpacing(20);

		// Add Grid to Vertical Panel
		panel.add(personalInformationLabel);
		panel.add(registrationPanel);
		panel.add(loginInformationLabel);
		panel.add(loginPanel);
		panel.add(buttonPanel);
		panel.getWidget(4).getElement().getStyle().setFloat(Float.RIGHT);

		return panel;
	}

	private static void fillCountryBox(final ListBox countryBox) {
		DOM.getElementById("loading").getStyle().setDisplay(Display.BLOCK);
		DBGetCountriesAsync rpcService = (DBGetCountriesAsync) GWT.create(DBGetCountries.class);
		ServiceDefTarget target = (ServiceDefTarget) rpcService;
		String moduleRelativeURL = GWT.getModuleBaseURL() + "DBGetCountriesImpl";
		target.setServiceEntryPoint(moduleRelativeURL);

		rpcService.getCountries(new AsyncCallback<Countries>() {

			@Override
			public void onSuccess(Countries result) {
				DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
				List<String> countries = result.getCountries();

				for (String country : countries) {
					countryBox.addItem(country);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
				// DO NOTHING

			}
		});

	}

	private static void validate(CustomTexBox customBox) {
		customBox.validate();
	}

	private static Widget createEmailChecker() {
		VerticalPanel panel = new VerticalPanel();
		panel.setSpacing(30);

		Label informationLabel = new Label(RegistrationFormConstants.INFORMATION);
		panel.add(informationLabel);

		HorizontalPanel hPanel = new HorizontalPanel();
		Label emailLabel = new Label(RegistrationFormConstants.EMAIL_LABEL);
		final TextBox emailBox = new TextBox();
		hPanel.add(emailLabel);
		hPanel.add(emailBox);
		panel.add(hPanel);

		emailLabel.addStyleName("emailLabel");
		emailBox.addStyleName("emailField");

		Button submitButton = new Button();
		submitButton.setText(RegistrationFormConstants.CHECK);
		submitButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				email = emailBox.getText();
				formEmailBox.setText(email);
				GWT.runAsync(new RunAsyncCallback() {

					@Override
					public void onSuccess() {
						checkIfEmailExists(email);

					}

					@Override
					public void onFailure(Throwable reason) {
						// TODO Auto-generated method stub

					}
				});

				emailBox.setText(null);
			}
		});
		panel.add(submitButton);

		return panel;
	}

	protected static void checkIfEmailExists(String emailAddress) {
		DOM.getElementById("loading").getStyle().setDisplay(Display.BLOCK);
		DBCheckForEmailAsync rpcService = (DBCheckForEmailAsync) GWT.create(DBCheckForEmail.class);
		ServiceDefTarget target = (ServiceDefTarget) rpcService;
		String moduleRelativeURL = GWT.getModuleBaseURL() + "DBCheckForEmailImpl";
		target.setServiceEntryPoint(moduleRelativeURL);

		rpcService.checkForEmail(emailAddress, new AsyncCallback<Email>() {

			@Override
			public void onSuccess(Email result) {
				DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
				DialogBox dialogBox = createDialogBox(RegistrationFormConstants.DIALOG_BOX_TITLE_SUCCESS,
						RegistrationFormConstants.DIALOG_BOX_SUCCESS_MESSAGE, DialogBoxConstants.CLOSE_BUTTON, false);
				dialogBox.setGlassEnabled(true);
				dialogBox.setAnimationEnabled(true);
				dialogBox.center();
				dialogBox.show();
				aptNumberBox.setText(result.getApartmentNumber());
				isEmailValid = true;
			}

			@Override
			public void onFailure(Throwable caught) {
				DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
				DialogBox errorDialogBox = createDialogBox(RegistrationFormConstants.DIALOG_BOX_TITLE,
						RegistrationFormConstants.DIALOG_BOX_ERROR_MESSAGE, DialogBoxConstants.CLOSE_BUTTON, false);
				errorDialogBox.setGlassEnabled(true);
				errorDialogBox.setAnimationEnabled(true);
				errorDialogBox.center();
				errorDialogBox.show();
				isEmailValid = false;
			}

		});
	}

	private static DialogBox createDialogBox(String title, String message, String buttonText, final boolean goToHomepage) {
		// Create dialog box and set the caption text
		final DialogBox dialogBox = new DialogBox();
		dialogBox.ensureDebugId(DialogBoxConstants.DEBUG_ID);
		dialogBox.setText(title);

		// Create a table to layout the content
		VerticalPanel dialogContents = new VerticalPanel();
		dialogContents.setSpacing(6);
		dialogBox.setWidget(dialogContents);

		// Add text to the top of the dialog
		HTML details = new HTML(message);
		dialogContents.add(details);
		dialogContents.setCellHorizontalAlignment(details, HasHorizontalAlignment.ALIGN_CENTER);

		// Add a close button to the dialog
		Button closeButton = new Button();
		closeButton.setText(buttonText);
		closeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();

				if (goToHomepage) {
					Window.Location.replace("http://127.0.0.1:8888/AdministrareBloc.html");
				}
			}
		});

		dialogContents.add(closeButton);
		dialogContents.setCellHorizontalAlignment(closeButton, HasHorizontalAlignment.ALIGN_CENTER);

		// Return the dialog Box
		return dialogBox;
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
