package com.andreiolar.abms.client.widgets;

import com.andreiolar.abms.client.constants.DialogBoxConstants;
import com.andreiolar.abms.client.constants.RegistrationFormConstants;
import com.andreiolar.abms.client.constants.UserMenuConstants;
import com.andreiolar.abms.client.rpc.DBUpdateUser;
import com.andreiolar.abms.client.rpc.DBUpdateUserAsync;
import com.andreiolar.abms.client.utils.DialogBoxCreator;
import com.andreiolar.abms.shared.EmptyStringValidator;
import com.andreiolar.abms.shared.IDSeriesValidator;
import com.andreiolar.abms.shared.MobileNumberValidator;
import com.andreiolar.abms.shared.NameValidator;
import com.andreiolar.abms.shared.UserInfo;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class AccountSettingsWidget extends Composite implements CustomWidget {

	private UserInfo userInfo;

	public AccountSettingsWidget(UserInfo userInfo) {
		this.userInfo = userInfo;

		initWidget(initializeWidget());
	}

	@Override
	public Widget initializeWidget() {
		VerticalPanel panel = new VerticalPanel();
		panel.setSpacing(10);

		Label personalInformationLabel = new Label("Personal Information");
		personalInformationLabel.getElement().getStyle().setFontSize(24.0, Unit.PX);
		personalInformationLabel.getElement().getStyle().setFontWeight(FontWeight.BOLD);

		// First Name
		Label firstName = new Label("First Name");
		final CustomTexBox firstNameBox = new CustomTexBox();
		firstNameBox.setMaxLength(30);
		firstNameBox.setText(userInfo.getFirstName());
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
		lastNameBox.setText(userInfo.getLastName());
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
		dateBox.setValue(userInfo.getDateOfBirth());
		dateBox.setFormat(new DateBox.DefaultFormat(dateFormat));
		dateBox.getDatePicker().setYearArrowsVisible(true);
		dateBox.setEnabled(false);
		dateBox.setStyleName("fixed-input");

		// E-Mail Address
		Label emailAddress = new Label("E-Mail Address");
		final TextBox formEmailBox = new TextBox();
		formEmailBox.setText(userInfo.getEmail());
		formEmailBox.setStyleName("fixed-input");

		// Mobile Number
		Label mobileNumber = new Label("Mobile Number");
		final CustomTexBox mobileNumberBox = new CustomTexBox();
		mobileNumberBox.setMaxLength(10);
		mobileNumberBox.setText(userInfo.getMobileNumber());
		mobileNumberBox.addValidator(new MobileNumberValidator());
		mobileNumberBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				mobileNumberBox.validate();
			}
		});
		Label mobileNumberObs = new Label("(10 digit number)");

		// Address
		Label address = new Label("Address");
		final TextArea textArea = new TextArea();
		textArea.setText(userInfo.getAddress());
		textArea.setVisibleLines(5);
		textArea.setWidth("200px");
		Label addressObs = new Label("(From your current ID)");

		// Country
		Label country = new Label("Country");
		final TextBox countryBox = new TextBox();
		countryBox.setText(userInfo.getCountry());
		Label countryObs = new Label("(From the current ID)");
		countryBox.setStyleName("fixed-input");

		// City
		Label city = new Label("City");
		final CustomTexBox cityBox = new CustomTexBox();
		cityBox.addValidator(new EmptyStringValidator());
		cityBox.setText(userInfo.getCity());
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
		personalNumberBox.setText(userInfo.getPersonalNumber());
		personalNumberBox.setEnabled(false);
		Label personalNumberObs = new Label("(Cannot be changed)");

		// ID Series
		Label seriesNumber = new Label("ID Series");
		final CustomTexBox seriesNumberBox = new CustomTexBox();
		seriesNumberBox.setMaxLength(8);
		seriesNumberBox.setText(userInfo.getIdSeries());
		seriesNumberBox.addValidator(new IDSeriesValidator());
		seriesNumberBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				seriesNumberBox.validate();
			}
		});
		Label seriesNumberObs = new Label("(From your current ID)");

		// Create a Grid to hold the personal information form
		Grid registrationPanel = new Grid(10, 3);

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

		registrationPanel.setWidget(4, 0, mobileNumber);
		registrationPanel.setWidget(4, 1, mobileNumberBox);
		registrationPanel.setWidget(4, 2, mobileNumberObs);

		registrationPanel.setWidget(5, 0, address);
		registrationPanel.setWidget(5, 1, textArea);
		registrationPanel.setWidget(5, 2, addressObs);

		registrationPanel.setWidget(6, 0, city);
		registrationPanel.setWidget(6, 1, cityBox);
		registrationPanel.setWidget(6, 2, cityObs);

		registrationPanel.setWidget(7, 0, country);
		registrationPanel.setWidget(7, 1, countryBox);
		registrationPanel.setWidget(7, 2, countryObs);

		registrationPanel.setWidget(8, 0, personalNumber);
		registrationPanel.setWidget(8, 1, personalNumberBox);
		registrationPanel.setWidget(8, 2, personalNumberObs);

		registrationPanel.setWidget(9, 0, seriesNumber);
		registrationPanel.setWidget(9, 1, seriesNumberBox);
		registrationPanel.setWidget(9, 2, seriesNumberObs);

		/* Buttons */
		Button submitButton = new Button();
		submitButton.setText("Submit");
		submitButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String firstName = firstNameBox.getText();
				String lastName = lastNameBox.getText();
				String email = formEmailBox.getText();
				String mobileNumber = mobileNumberBox.getText();
				String address = textArea.getText();
				String city = cityBox.getText();
				String country = countryBox.getText();
				String personalNumber = personalNumberBox.getText();
				String idSeries = seriesNumberBox.getText();

				if (!firstName.equals(userInfo.getFirstName()) || !lastName.equals(userInfo.getLastName()) || !email.equals(userInfo.getEmail())
						|| !mobileNumber.equals(userInfo.getMobileNumber()) || !address.equals(userInfo.getAddress())
						|| !city.equals(userInfo.getCity()) || !country.equals(userInfo.getCountry()) || !idSeries.equals(userInfo.getIdSeries())) {

					if (firstNameBox.validate() && lastNameBox.validate() && mobileNumberBox.validate() && cityBox.validate()
							&& personalNumberBox.validate() && seriesNumberBox.validate()) {

						DOM.getElementById("loading").getStyle().setDisplay(Display.BLOCK);
						DBUpdateUserAsync rpc = (DBUpdateUserAsync) GWT.create(DBUpdateUser.class);
						ServiceDefTarget tar = (ServiceDefTarget) rpc;
						String moduleURL = GWT.getModuleBaseURL() + "DBUpdateUserImpl";
						tar.setServiceEntryPoint(moduleURL);

						rpc.updateUser(firstName, lastName, email, mobileNumber, address, city, country, idSeries, personalNumber,
								new AsyncCallback<Boolean>() {

									@Override
									public void onSuccess(Boolean result) {
										DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
										DialogBox dialogBox = DialogBoxCreator.createDialogBox(UserMenuConstants.SUCCESS_UPDATE_USER_TITLE,
												UserMenuConstants.SUCCESS_UPDATE_USER_MESSAGE, DialogBoxConstants.CLOSE_BUTTON, false, false);
										dialogBox.setGlassEnabled(true);
										dialogBox.setAnimationEnabled(true);
										dialogBox.center();
										dialogBox.show();
									}

									@Override
									public void onFailure(Throwable caught) {
										DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
										DialogBox dialogBox = DialogBoxCreator.createDialogBox(UserMenuConstants.FAILED_UPDATE_USER_TITLE,
												caught.getMessage(), DialogBoxConstants.CLOSE_BUTTON, false, false);
										dialogBox.setGlassEnabled(true);
										dialogBox.setAnimationEnabled(true);
										dialogBox.center();
										dialogBox.show();
									}
								});

					} else {
						DialogBox dialogBox = DialogBoxCreator.createDialogBox(RegistrationFormConstants.INVALID_DATA_TITLE,
								RegistrationFormConstants.INVALID_DATA_MESSAGE, DialogBoxConstants.CLOSE_BUTTON, false, false);
						dialogBox.setGlassEnabled(true);
						dialogBox.setAnimationEnabled(true);
						dialogBox.center();
						dialogBox.show();
					}

				} else {
					DialogBox dialogBox = DialogBoxCreator.createDialogBox(UserMenuConstants.DIALOG_BOX_FAILED_SUBMIT_INFO_TITLE,
							UserMenuConstants.DIALOG_BOX_FAILED_SUBMIT_INFO_MESSAGE, DialogBoxConstants.CLOSE_BUTTON, false, false);
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
				firstNameBox.setText(userInfo.getFirstName());
				firstNameBox.setErrorStyles(true);
				lastNameBox.setText(userInfo.getLastName());
				lastNameBox.setErrorStyles(true);
				formEmailBox.setText(userInfo.getEmail());
				dateBox.setValue(userInfo.getDateOfBirth());
				mobileNumberBox.setText(userInfo.getMobileNumber());
				mobileNumberBox.setErrorStyles(true);
				textArea.setText(userInfo.getAddress());
				cityBox.setText(userInfo.getCity());
				cityBox.setErrorStyles(true);
				countryBox.setText(userInfo.getCountry());
				personalNumberBox.setText(userInfo.getPersonalNumber());
				personalNumberBox.setErrorStyles(true);
				seriesNumberBox.setText(userInfo.getIdSeries());
				seriesNumberBox.setErrorStyles(true);
			}
		});

		// Create Grid to hold the buttons
		Grid buttonPanel = new Grid(1, 2);

		buttonPanel.setWidget(0, 0, submitButton);
		buttonPanel.setWidget(0, 1, resetButton);
		buttonPanel.setCellSpacing(20);

		panel.add(personalInformationLabel);
		panel.add(registrationPanel);
		panel.add(buttonPanel);

		return panel;

	}

	private void validate(CustomTexBox customBox) {
		customBox.validate();
	}

}
