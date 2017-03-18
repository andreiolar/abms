package com.andreiolar.abms.client.widgets;

import java.util.Date;

import com.andreiolar.abms.client.exception.UserDetailsNotFoundException;
import com.andreiolar.abms.client.exception.UsernameUnavailableException;
import com.andreiolar.abms.client.rpc.DBGetUserDetails;
import com.andreiolar.abms.client.rpc.DBGetUserDetailsAsync;
import com.andreiolar.abms.client.rpc.DBUpdateProfile;
import com.andreiolar.abms.client.rpc.DBUpdateProfileAsync;
import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialDatePicker;
import gwt.material.design.client.ui.MaterialDatePicker.MaterialDatePickerType;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialListBox;
import gwt.material.design.client.ui.MaterialLoader;
import gwt.material.design.client.ui.MaterialModal;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextArea;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Hr;
import gwt.material.design.client.ui.html.Option;

public class ProfileInfoWidget extends Composite implements CustomWidget {

	private UserDetails userDetails;

	public ProfileInfoWidget(UserDetails userDetails) {
		this.userDetails = userDetails;

		initWidget(initializeWidget());
	}

	@Override
	public Widget initializeWidget() {
		MaterialPanel panel = new MaterialPanel();

		MaterialLabel title = new MaterialLabel("Profile Information");
		title.setTextColor(Color.BLUE);
		title.setTextAlign(TextAlign.CENTER);
		title.setFontSize("36px");
		title.setFontWeight(FontWeight.BOLD);
		panel.add(title);

		panel.add(new Hr());

		MaterialPanel subPanel = new MaterialPanel();

		MaterialLoader.showLoading(true);

		DBGetUserDetailsAsync getUserDetailsRpc = (DBGetUserDetailsAsync) GWT.create(DBGetUserDetails.class);
		ServiceDefTarget getUserDetailsTarget = (ServiceDefTarget) getUserDetailsRpc;
		String getUserDetailsUrl = GWT.getModuleBaseURL() + "DBGetUserDetailsImpl";
		getUserDetailsTarget.setServiceEntryPoint(getUserDetailsUrl);

		getUserDetailsRpc.geUserDetails(userDetails.getUsername(), new AsyncCallback<UserDetails>() {

			@Override
			public void onSuccess(UserDetails result) {
				MaterialLoader.showLoading(false);

				MaterialPanel mainPanel = new MaterialPanel();
				mainPanel.addStyleName("center-panel-60-border");
				mainPanel.setMarginTop(25.0);

				mainPanel.add(new ProfileInformationEntry("First Name", result.getFirstName()));
				mainPanel.add(new ProfileInformationEntry("Last Name", result.getLastName()));
				mainPanel.add(new ProfileInformationEntry("E-Mail", result.getEmail()));
				mainPanel.add(new ProfileInformationEntry("Username", result.getUsername()));
				mainPanel.add(new ProfileInformationEntry("Mobile Number", result.getMobileNumber()));
				mainPanel.add(new ProfileInformationEntry("Gender", result.getGender()));
				mainPanel.add(new ProfileInformationEntry("Date of Birth", result.getDateOfBirth().toString()));
				mainPanel.add(new ProfileInformationEntry("Address", result.getAddress()));
				mainPanel.add(new ProfileInformationEntry("City", result.getCity()));
				mainPanel.add(new ProfileInformationEntry("Country", result.getCountry()));
				mainPanel.add(new ProfileInformationEntry("Personal Number", result.getPersonalNumber()));
				mainPanel.add(new ProfileInformationEntry("Id Series", result.getIdSeries()));

				subPanel.add(mainPanel);

				Div buttonDiv = new Div();
				buttonDiv.addStyleName("center-button-60");

				MaterialButton editAccountButton = new MaterialButton();
				editAccountButton.setText("Edit Account");
				editAccountButton.setWidth("100%");
				editAccountButton.setHeight("50px");
				editAccountButton.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						subPanel.clear();

						MaterialTextBox firstNameBox = new MaterialTextBox();
						firstNameBox.setMarginTop(50.0);
						firstNameBox.setMarginLeft(25.0);
						firstNameBox.setType(InputType.TEXT);
						firstNameBox.setPlaceholder("First Name");
						firstNameBox.setText(result.getFirstName());
						firstNameBox.setMaxLength(30);
						firstNameBox.setWidth("30%");
						firstNameBox.setLength(30);
						firstNameBox.setIconType(IconType.PERSON);
						subPanel.add(firstNameBox);

						MaterialTextBox lastNameBox = new MaterialTextBox();
						lastNameBox.setType(InputType.TEXT);
						lastNameBox.setMarginLeft(25.0);
						lastNameBox.setPlaceholder("Last Name");
						lastNameBox.setText(result.getLastName());
						lastNameBox.setMaxLength(30);
						lastNameBox.setLength(30);
						lastNameBox.setWidth("30%");
						lastNameBox.setIconType(IconType.PERSON);
						subPanel.add(lastNameBox);

						MaterialTextBox emailTextBox = new MaterialTextBox();
						emailTextBox.setType(InputType.EMAIL);
						emailTextBox.setPlaceholder("E-Mail");
						emailTextBox.setText(result.getEmail());
						emailTextBox.setMarginLeft(25.0);
						emailTextBox.setWidth("30%");
						emailTextBox.setIconType(IconType.ACCOUNT_CIRCLE);
						subPanel.add(emailTextBox);

						MaterialTextBox usernameBox = new MaterialTextBox();
						usernameBox.setType(InputType.TEXT);
						usernameBox.setPlaceholder("Username");
						usernameBox.setText(result.getUsername());
						usernameBox.setMarginLeft(25.0);
						usernameBox.setLength(25);
						usernameBox.setWidth("30%");
						usernameBox.setMaxLength(25);
						usernameBox.setIconType(IconType.ACCOUNT_CIRCLE);
						subPanel.add(usernameBox);

						MaterialTextBox mobileNumberBox = new MaterialTextBox();
						mobileNumberBox.setType(InputType.TEL);
						mobileNumberBox.setPlaceholder("Mobile Number");
						mobileNumberBox.setText(result.getMobileNumber());
						mobileNumberBox.setMaxLength(10);
						mobileNumberBox.setWidth("30%");
						mobileNumberBox.setMarginLeft(25.0);
						mobileNumberBox.setLength(10);
						mobileNumberBox.setIconType(IconType.SMARTPHONE);
						subPanel.add(mobileNumberBox);

						MaterialListBox genderBox = new MaterialListBox();
						genderBox.setWidth("30%");
						genderBox.setMarginLeft(25.0);
						genderBox.setPlaceholder("Gender");
						genderBox.add(new Option("Male"));
						genderBox.add(new Option("Female"));
						genderBox.setSelectedValue(result.getGender());
						subPanel.add(genderBox);

						MaterialDatePicker dateOfBirthBox = new MaterialDatePicker();
						dateOfBirthBox.setPlaceholder("Date of Birth");
						dateOfBirthBox.setDate(result.getDateOfBirth());
						dateOfBirthBox.setSelectionType(MaterialDatePickerType.YEAR);
						dateOfBirthBox.setDateMax(new Date());
						dateOfBirthBox.setMarginLeft(25.0);
						dateOfBirthBox.setWidth("30%");
						dateOfBirthBox.setIconType(IconType.DATE_RANGE);
						subPanel.add(dateOfBirthBox);

						MaterialTextArea addressBox = new MaterialTextArea();
						addressBox.setType(InputType.TEXT);
						addressBox.setPlaceholder("Address");
						addressBox.setText(result.getAddress());
						addressBox.setWidth("30%");
						addressBox.setMarginLeft(25.0);
						addressBox.setLength(200);
						addressBox.setIconType(IconType.HOME);
						subPanel.add(addressBox);

						MaterialTextBox cityBox = new MaterialTextBox();
						cityBox.setType(InputType.TEXT);
						cityBox.setPlaceholder("Home City");
						cityBox.setText(result.getCity());
						cityBox.setMaxLength(30);
						cityBox.setWidth("30%");
						cityBox.setMarginLeft(25.0);
						cityBox.setLength(30);
						cityBox.setIconType(IconType.LOCATION_CITY);
						subPanel.add(cityBox);

						MaterialTextBox countryBox = new MaterialTextBox();
						countryBox.setType(InputType.TEXT);
						countryBox.setPlaceholder("Home Country");
						countryBox.setText(result.getCountry());
						countryBox.setWidth("30%");
						countryBox.setMarginLeft(25.0);
						countryBox.setMaxLength(30);
						countryBox.setLength(30);
						countryBox.setIconType(IconType.PUBLIC);
						subPanel.add(countryBox);

						MaterialTextBox cnpBox = new MaterialTextBox();
						cnpBox.setType(InputType.TEXT);
						cnpBox.setPlaceholder("CNP");
						cnpBox.setText(result.getPersonalNumber());
						cnpBox.setWidth("30%");
						cnpBox.setMarginLeft(25.0);
						cnpBox.setMaxLength(13);
						cnpBox.setLength(13);
						cnpBox.setIconType(IconType.PERM_IDENTITY);
						subPanel.add(cnpBox);

						MaterialTextBox personalNumberBox = new MaterialTextBox();
						personalNumberBox.setType(InputType.TEXT);
						personalNumberBox.setPlaceholder("ID Series and Number");
						personalNumberBox.setText(result.getIdSeries());
						personalNumberBox.setWidth("30%");
						personalNumberBox.setMarginLeft(25.0);
						personalNumberBox.setMaxLength(8);
						personalNumberBox.setLength(8);
						personalNumberBox.setIconType(IconType.PERM_IDENTITY);
						subPanel.add(personalNumberBox);

						Div buttonsDiv = new Div();
						buttonsDiv.setDisplay(Display.FLEX);
						buttonsDiv.setMarginTop(50.0);
						buttonsDiv.setMarginLeft(25.0);
						buttonsDiv.setMarginBottom(100.0);

						MaterialButton saveChangesButton = new MaterialButton();
						saveChangesButton.setText("Save Changes");
						saveChangesButton.setWidth("15%");
						saveChangesButton.setMarginRight(25.0);
						saveChangesButton.setWaves(WavesType.LIGHT);
						saveChangesButton.addClickHandler(new ClickHandler() {

							@Override
							public void onClick(ClickEvent event) {
								String firstName = firstNameBox.getText();
								String lastName = lastNameBox.getText();
								String email = emailTextBox.getText();
								String username = usernameBox.getText();
								String mobileNumber = mobileNumberBox.getText();
								String gender = genderBox.getSelectedValue();
								Date dateOfBirth = dateOfBirthBox.getDate();
								String address = addressBox.getText();
								String city = cityBox.getText();
								String country = countryBox.getText();
								String cnp = cnpBox.getText();
								String personalNumber = personalNumberBox.getText();

								boolean canProcceed = true;

								if (!firstName.matches("[A-Za-z]{1,30}")) {
									canProcceed = false;
									firstNameBox.setError("First name cannot be empty or exceed 30 characters.");
								} else {
									firstNameBox.setSuccess("");
								}

								if (!lastName.matches("[A-Za-z]{1,30}")) {
									canProcceed = false;
									lastNameBox.setError("Last name cannot be empty or exceed 30 characters.");
								} else {
									lastNameBox.setSuccess("");
								}

								if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.(?:[a-zA-Z]{2,6})$")) {
									canProcceed = false;
									emailTextBox.setError("Not a valid E-Mail Address.");
								} else {
									emailTextBox.setSuccess("");
								}

								if (!username.matches("[A-Za-z0-9._-]{5,25}")) {
									canProcceed = false;
									usernameBox.setError(
											"Username can only contain uppercase, lowercase letters, 0-9 digits and following special characters . _ - and must be between 5 and 25 characters long.");
								} else {
									usernameBox.setSuccess("");
								}

								if (dateOfBirth == null) {
									canProcceed = false;
									dateOfBirthBox.setError("Plese enter your date of birth.");
								} else {
									dateOfBirthBox.setSuccess("");
								}

								if (!mobileNumber.matches("[0-9]{10}")) {
									canProcceed = false;
									mobileNumberBox.setError("Only digits are allowed. Phone number must have 10 digits.");
								} else {
									mobileNumberBox.setSuccess("");
								}

								if (address.length() < 1 || address.length() > 200) {
									canProcceed = false;
									addressBox.setError("Address cannot be empty or exceed 200 characters.");
								} else {
									addressBox.setSuccess("");
								}

								if (!city.matches("[A-Za-z]{1,30}")) {
									canProcceed = false;
									cityBox.setError("City cannot be empty or exceed 30 characters.");
								} else {
									cityBox.setSuccess("");
								}

								if (!country.matches("[A-Za-z]{1,30}")) {
									canProcceed = false;
									countryBox.setError("Country cannot be empty or exceed 30 characters.");
								} else {
									countryBox.setSuccess("");
								}

								if (!cnp.matches("[0-9]{13}")) {
									canProcceed = false;
									cnpBox.setError("Only digits are allowed. CNP must have 13 digits.");
								} else {
									cnpBox.setSuccess("");
								}

								if (!personalNumber.matches("[A-Za-z]{2}[0-9]{6}")) {
									canProcceed = false;
									personalNumberBox
											.setError("Personal number and series cannot be empty. 2 letters followed by 6 numbers are required.");
								} else {
									personalNumberBox.setSuccess("");
								}

								if (canProcceed) {
									UserDetails newDetails = new UserDetails(firstName, lastName, dateOfBirth, email, mobileNumber, gender, address,
											city, country, cnp, personalNumber, username, null, userDetails.getApartmentNumber());

									DBUpdateProfileAsync updateProfileRpc = (DBUpdateProfileAsync) GWT.create(DBUpdateProfile.class);
									ServiceDefTarget updateProfileTarget = (ServiceDefTarget) updateProfileRpc;
									String updateProfileUrl = GWT.getModuleBaseURL() + "DBUpdateProfileImpl";
									updateProfileTarget.setServiceEntryPoint(updateProfileUrl);

									updateProfileRpc.updateProfile(newDetails, userDetails.getUsername(), new AsyncCallback<Boolean>() {

										@Override
										public void onFailure(Throwable caught) {
											if (caught instanceof UsernameUnavailableException) {
												usernameBox.setError("Username already exists.");
											} else {
												MaterialToast.fireToast("Unable to update profile: " + caught.getMessage(), "rounded");
											}
										}

										@Override
										public void onSuccess(Boolean result) {
											if (result) {
												Cookies.removeCookie("sid");

												int cookieDuration = 5000;
												Date expires = new Date(System.currentTimeMillis() + cookieDuration);
												Cookies.setCookie("usernameChanged",
														"Profile update successfully, but username was changed. Log In required.", expires, null, "/",
														false);

												Window.Location.replace(GWT.getHostPageBaseURL());
											} else {
												panel.clear();
												panel.add(new ProfileInfoWidget(newDetails));
											}
										}
									});
								}
							}
						});

						MaterialButton revertButton = new MaterialButton();
						revertButton.setText("Revert");
						revertButton.setWidth("15%");
						revertButton.setWaves(WavesType.LIGHT);
						revertButton.setBackgroundColor(Color.LIGHT_BLUE_LIGHTEN_3);
						revertButton.addClickHandler(new ClickHandler() {

							@Override
							public void onClick(ClickEvent event) {
								firstNameBox.setText(result.getFirstName());
								firstNameBox.setSuccess("");

								lastNameBox.setText(result.getLastName());
								lastNameBox.setSuccess("");

								emailTextBox.setText(result.getEmail());
								emailTextBox.setSuccess("");

								usernameBox.setText(result.getUsername());
								usernameBox.setSuccess("");

								mobileNumberBox.setText(result.getMobileNumber());
								mobileNumberBox.setSuccess("");

								genderBox.setSelectedValue(result.getGender());

								dateOfBirthBox.setDate(result.getDateOfBirth());
								dateOfBirthBox.setSuccess("");

								addressBox.setText(result.getAddress());
								addressBox.setSuccess("");

								cityBox.setText(result.getCity());
								cityBox.setSuccess("");

								countryBox.setText(result.getCountry());
								countryBox.setSuccess("");

								cnpBox.setText(result.getPersonalNumber());
								cnpBox.setSuccess("");

								personalNumberBox.setText(result.getIdSeries());
								personalNumberBox.setSuccess("");
							}
						});

						buttonsDiv.add(saveChangesButton);
						buttonsDiv.add(revertButton);

						subPanel.add(buttonsDiv);
					}
				});
				buttonDiv.add(editAccountButton);
				subPanel.add(buttonDiv);
			}

			@Override
			public void onFailure(Throwable caught) {
				MaterialLoader.showLoading(false);

				if (caught instanceof UserDetailsNotFoundException) {
					MaterialToast.fireToast("Profile Information for user " + userDetails.getUsername() + " could not be loaded. Please try again.",
							"rounded");
				} else {
					MaterialModal errorModal = ModalCreator.createModal(caught);
					RootPanel.get().add(errorModal);
					errorModal.open();
				}
			}
		});

		panel.add(subPanel);

		return panel;
	}

}
