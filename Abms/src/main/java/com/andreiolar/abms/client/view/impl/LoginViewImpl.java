package com.andreiolar.abms.client.view.impl;

import java.util.Date;

import com.andreiolar.abms.client.exception.EmailNotFoundException;
import com.andreiolar.abms.client.exception.InvalidCodeException;
import com.andreiolar.abms.client.exception.InvalidCredentialsException;
import com.andreiolar.abms.client.exception.UsernameUnavailableException;
import com.andreiolar.abms.client.place.AdminPlace;
import com.andreiolar.abms.client.place.UserPlace;
import com.andreiolar.abms.client.rpc.DBChangeForgotPassword;
import com.andreiolar.abms.client.rpc.DBChangeForgotPasswordAsync;
import com.andreiolar.abms.client.rpc.DBCheckForEmail;
import com.andreiolar.abms.client.rpc.DBCheckForEmailAsync;
import com.andreiolar.abms.client.rpc.DBCodeChecker;
import com.andreiolar.abms.client.rpc.DBCodeCheckerAsync;
import com.andreiolar.abms.client.rpc.DBConnection;
import com.andreiolar.abms.client.rpc.DBConnectionAsync;
import com.andreiolar.abms.client.rpc.DBForgotPassword;
import com.andreiolar.abms.client.rpc.DBForgotPasswordAsync;
import com.andreiolar.abms.client.rpc.DBRegisterUser;
import com.andreiolar.abms.client.rpc.DBRegisterUserAsync;
import com.andreiolar.abms.client.view.LoginView;
import com.andreiolar.abms.client.widgets.ModalCreator;
import com.andreiolar.abms.shared.Email;
import com.andreiolar.abms.shared.UserInfo;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.dom.client.Style.TextTransform;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.addins.client.stepper.MaterialStep;
import gwt.material.design.addins.client.stepper.MaterialStepper;
import gwt.material.design.client.constants.ButtonType;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.ImageType;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.constants.ModalType;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialDatePicker;
import gwt.material.design.client.ui.MaterialDatePicker.MaterialDatePickerType;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialListBox;
import gwt.material.design.client.ui.MaterialLoader;
import gwt.material.design.client.ui.MaterialModal;
import gwt.material.design.client.ui.MaterialModalContent;
import gwt.material.design.client.ui.MaterialModalFooter;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextArea;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialTitle;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.client.ui.html.Option;

public class LoginViewImpl extends Composite implements LoginView {

	private static Presenter presenter;
	private static String name;

	public LoginViewImpl() {
		String loggedInUser = Cookies.getCookie("sid");
		if (loggedInUser != null) {
			Window.Location.replace(GWT.getHostPageBaseURL() + "#UserPlace:" + loggedInUser);
		}

		String cookieMessage = Cookies.getCookie("badUserInfo");
		if (cookieMessage != null) {
			MaterialToast.fireToast(cookieMessage, "rounded");
		}

		String sessionExpired = Cookies.getCookie("sessionExpired");
		if (sessionExpired != null) {
			MaterialToast.fireToast(sessionExpired, "rounded");
		}

		Widget loginForm = createLoginForm();
		initWidget(loginForm);
	}

	public Widget createLoginForm() {
		MaterialRow materialRow = new MaterialRow();
		materialRow.setBackgroundColor(Color.GREY_LIGHTEN_4);
		materialRow.addStyleName("login-demo");

		MaterialColumn materialColumn = new MaterialColumn(12, 8, 6);
		materialColumn.setOffset("l3");

		MaterialPanel panel = new MaterialPanel();
		panel.setShadow(1);
		panel.addStyleName("panel");

		MaterialPanel fieldPanel = new MaterialPanel();
		fieldPanel.addStyleName("fieldPanel");

		MaterialImage materialImage = new MaterialImage("images/icons/ab.png");
		materialImage.setType(ImageType.CIRCLE);
		materialImage.addStyleName("imgProfile");

		MaterialTextBox userBox = new MaterialTextBox();
		userBox.setType(InputType.TEXT);
		userBox.setPlaceholder("Username");

		MaterialTextBox passwordBox = new MaterialTextBox();
		passwordBox.setType(InputType.PASSWORD);
		passwordBox.setPlaceholder("Password");

		MaterialCheckBox loggedIn = new MaterialCheckBox();
		loggedIn.setText("Keep me logged in");

		MaterialButton loginButton = new MaterialButton();
		loginButton.setWaves(WavesType.LIGHT);
		loginButton.setText("Log In");
		loginButton.setWidth("100%");
		loginButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String username = userBox.getText();
				String password = passwordBox.getText();

				performUserConnection(username, password, loginButton, loggedIn);
			}
		});

		passwordBox.addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					String username = userBox.getText();
					String password = passwordBox.getText();

					performUserConnection(username, password, loginButton, loggedIn);
				}
			}
		});

		MaterialButton registerButton = new MaterialButton();
		registerButton.setWaves(WavesType.LIGHT);
		registerButton.setText("Register");
		registerButton.setWidth("100%");
		registerButton.getElement().getStyle().setMarginTop(20.0, Unit.PX);
		registerButton.setBackgroundColor(Color.LIGHT_BLUE_LIGHTEN_3);
		registerButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				MaterialModal registerPanel = createRegistrationPanel();
				RootPanel.get().add(registerPanel);
				registerPanel.open();
			}
		});

		MaterialRow rowAction = new MaterialRow();
		rowAction.addStyleName("rowAction");

		MaterialColumn mCol = new MaterialColumn(12, 12, 6);
		mCol.setWidth("100%");

		MaterialButton forgotPaswordLink = new MaterialButton();
		forgotPaswordLink.setText("Forgot Password?");
		forgotPaswordLink.setTextColor(Color.BLUE);
		forgotPaswordLink.setType(ButtonType.FLAT);
		forgotPaswordLink.getElement().getStyle().setMarginTop(10.0, Unit.PX);
		forgotPaswordLink.setFloat(Float.RIGHT);
		forgotPaswordLink.getElement().getStyle().setTextDecoration(TextDecoration.UNDERLINE);
		forgotPaswordLink.getElement().getStyle().setTextTransform(TextTransform.CAPITALIZE);
		forgotPaswordLink.getElement().getStyle().setFontStyle(FontStyle.ITALIC);
		forgotPaswordLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				MaterialModal forgotPasswordPanel = createForgotPasswordPanel();
				RootPanel.get().add(forgotPasswordPanel);
				forgotPasswordPanel.open();
			}
		});

		mCol.add(loggedIn);
		mCol.add(forgotPaswordLink);
		rowAction.add(mCol);

		fieldPanel.add(materialImage);
		fieldPanel.add(userBox);
		fieldPanel.add(passwordBox);
		fieldPanel.add(loginButton);
		fieldPanel.add(registerButton);
		fieldPanel.add(rowAction);

		panel.add(fieldPanel);
		materialColumn.add(panel);
		materialRow.add(materialColumn);

		return materialRow;
	}

	protected MaterialModal createForgotPasswordPanel() {
		MaterialModal forgotPasswordPanel = new MaterialModal();
		forgotPasswordPanel.setType(ModalType.DEFAULT);
		forgotPasswordPanel.setDismissible(false);
		forgotPasswordPanel.setInDuration(500);
		forgotPasswordPanel.setOutDuration(500);

		MaterialModalContent materialModalContent = new MaterialModalContent();
		MaterialTitle materialTitle = new MaterialTitle("Password Recovery");
		materialTitle.setDescription(
				"Please provide all necessary information in order to reset your password. A code will be sent to you by E-Mail which will be used in the next step.");

		materialModalContent.add(materialTitle);

		MaterialTextBox emailTextBox = new MaterialTextBox();
		emailTextBox.setType(InputType.EMAIL);
		emailTextBox.setPlaceholder("E-Mail");
		emailTextBox.setIconType(IconType.ACCOUNT_CIRCLE);
		materialModalContent.add(emailTextBox);

		MaterialButton processEmailButton = new MaterialButton();
		processEmailButton.setWaves(WavesType.LIGHT);
		processEmailButton.setText("Send Code");
		processEmailButton.setWidth("100%");
		processEmailButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String email = emailTextBox.getText();
				processEmailButton.setEnabled(false);

				if (email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.(?:[a-zA-Z]{2,6})$")) {
					DBForgotPasswordAsync rpcService = (DBForgotPasswordAsync) GWT.create(DBForgotPassword.class);
					ServiceDefTarget target = (ServiceDefTarget) rpcService;
					String moduleRelativeURL = GWT.getModuleBaseURL() + "DBForgotPasswordImpl";
					target.setServiceEntryPoint(moduleRelativeURL);

					rpcService.sendMailToServer(email, new AsyncCallback<Boolean>() {

						@Override
						public void onSuccess(Boolean result) {
							materialTitle.setDescription("Please enter the code you recieved by E-Mail.");
							materialModalContent.remove(emailTextBox);
							materialModalContent.remove(processEmailButton);

							MaterialTextBox codeBox = new MaterialTextBox();
							codeBox.setType(InputType.TEXT);
							codeBox.setPlaceholder("Confirmation Code");
							codeBox.setIconType(IconType.LOCK);
							materialModalContent.add(codeBox);

							MaterialButton processCodeButton = new MaterialButton();
							processCodeButton.setWaves(WavesType.LIGHT);
							processCodeButton.setText("Proceed");
							processCodeButton.setWidth("100%");
							processCodeButton.addClickHandler(new ClickHandler() {

								@Override
								public void onClick(ClickEvent event) {
									String code = codeBox.getText();
									processCodeButton.setEnabled(false);

									DBCodeCheckerAsync rpcService = (DBCodeCheckerAsync) GWT.create(DBCodeChecker.class);
									ServiceDefTarget target = (ServiceDefTarget) rpcService;
									String moduleRelativeURL = GWT.getModuleBaseURL() + "DBCodeCheckerImpl";
									target.setServiceEntryPoint(moduleRelativeURL);

									rpcService.checkCode(code, email, new AsyncCallback<Void>() {

										@Override
										public void onSuccess(Void result) {
											materialTitle.setDescription("Please choose a new password.");
											materialModalContent.remove(codeBox);
											materialModalContent.remove(processCodeButton);

											MaterialTextBox passwordBox = new MaterialTextBox();
											passwordBox.setType(InputType.PASSWORD);
											passwordBox.setPlaceholder("Password");
											passwordBox.setLength(10);
											passwordBox.setMaxLength(10);
											passwordBox.setIconType(IconType.LOCK);
											materialModalContent.add(passwordBox);

											MaterialTextBox repeatPasswordBox = new MaterialTextBox();
											repeatPasswordBox.setType(InputType.PASSWORD);
											repeatPasswordBox.setPlaceholder("Repeat Password");
											repeatPasswordBox.setLength(10);
											repeatPasswordBox.setMaxLength(10);
											repeatPasswordBox.setIconType(IconType.LOCK);
											materialModalContent.add(repeatPasswordBox);

											MaterialButton processPasswordButton = new MaterialButton();
											processPasswordButton.setWaves(WavesType.LIGHT);
											processPasswordButton.setText("Change Password");
											processPasswordButton.setWidth("100%");
											processPasswordButton.addClickHandler(new ClickHandler() {

												@Override
												public void onClick(ClickEvent event) {
													String password = passwordBox.getText();
													String repeatPassword = repeatPasswordBox.getText();
													boolean canProceed = true;

													if (!password.matches("[A-Za-z0-9]{5,10}")) {
														canProceed = false;
														passwordBox.setError(
																"Password can only contain uppercase, lowercase letters and 0-9 digits. Password has to be between 5 and 10 characters long.");
													} else {
														passwordBox.setSuccess("");
													}

													if (!repeatPassword.matches("[A-Za-z0-9]{5,10}")) {
														canProceed = false;
														repeatPasswordBox.setError(
																"Password can only contain uppercase, lowercase letters and 0-9 digits. Password has to be between 5 and 10 characters long.");
													} else {
														repeatPasswordBox.setSuccess("");
													}

													if (canProceed) {
														if (!repeatPassword.equals(password)) {
															repeatPasswordBox.setError("Passwords must match.");
														} else {
															processPasswordButton.setEnabled(false);

															DBChangeForgotPasswordAsync rpcService = (DBChangeForgotPasswordAsync) GWT
																	.create(DBChangeForgotPassword.class);
															ServiceDefTarget target = (ServiceDefTarget) rpcService;
															String moduleRelativeURL = GWT.getModuleBaseURL() + "DBChangeForgotPasswordImpl";
															target.setServiceEntryPoint(moduleRelativeURL);

															rpcService.resetPassword(email, password, new AsyncCallback<Boolean>() {

																@Override
																public void onSuccess(Boolean result) {
																	forgotPasswordPanel.close();
																	RootPanel.get().remove(forgotPasswordPanel);

																	MaterialToast.fireToast("Password changed successfully!", "rounded");
																}

																@Override
																public void onFailure(Throwable caught) {
																	processPasswordButton.setEnabled(true);

																	if (!(caught instanceof RuntimeException)) {
																		MaterialToast.fireToast(caught.getMessage(), "rounded");
																	}
																}
															});
														}
													}
												}
											});
											materialModalContent.add(processPasswordButton);
										}

										@Override
										public void onFailure(Throwable caught) {
											processCodeButton.setEnabled(true);

											if (caught instanceof InvalidCodeException) {
												codeBox.setError(caught.getMessage());
											} else {
												MaterialModal errorModal = ModalCreator.createModal(caught);
												RootPanel.get().add(errorModal);
												errorModal.open();
											}
										}
									});
								}
							});
							materialModalContent.add(processCodeButton);
						}

						@Override
						public void onFailure(Throwable caught) {
							processEmailButton.setEnabled(true);
							MaterialModal errorModal = ModalCreator.createModal(caught);
							RootPanel.get().add(errorModal);
							errorModal.open();
						}
					});
				} else {
					processEmailButton.setEnabled(true);
					emailTextBox.setError("Not a valid E-Mail Address.");
				}
			}
		});
		materialModalContent.add(processEmailButton);

		MaterialModalFooter materialModalFooter = new MaterialModalFooter();
		MaterialButton closeButton = new MaterialButton();
		closeButton.setText("Close");
		closeButton.setType(ButtonType.FLAT);
		closeButton.addClickHandler(h -> {
			forgotPasswordPanel.close();
			RootPanel.get().remove(forgotPasswordPanel);
		});

		materialModalFooter.add(closeButton);
		forgotPasswordPanel.add(materialModalContent);
		forgotPasswordPanel.add(materialModalFooter);

		return forgotPasswordPanel;
	}

	protected MaterialModal createRegistrationPanel() {
		MaterialModal registerPanel = new MaterialModal();
		registerPanel.setDismissible(false);
		registerPanel.setInDuration(500);
		registerPanel.setOutDuration(500);

		MaterialModalContent content = new MaterialModalContent();

		MaterialStepper stepper = new MaterialStepper();

		MaterialStep stepOne = new MaterialStep();
		MaterialStep stepTwo = new MaterialStep();
		stepTwo.setEnabled(false);
		MaterialStep stepThree = new MaterialStep();
		stepThree.setEnabled(false);

		// Step 1
		stepOne.setStep(1);
		stepOne.setTitle("E-Mail Verification");
		stepOne.setDescription("Your registration elegibility will be verified.");

		MaterialPanel stepOnePanel = new MaterialPanel();
		stepOnePanel.setWidth("100%");

		MaterialTextBox emailTextBox = new MaterialTextBox();
		emailTextBox.setType(InputType.EMAIL);
		emailTextBox.setPlaceholder("E-Mail");
		emailTextBox.setIconType(IconType.ACCOUNT_CIRCLE);
		stepOnePanel.add(emailTextBox);

		stepOne.add(stepOnePanel);

		MaterialButton continueStepOneButton = new MaterialButton();
		continueStepOneButton.setText("Next Step");
		continueStepOneButton.setGrid("l4");
		continueStepOneButton.setMarginTop(12.0);
		continueStepOneButton.setTextColor(Color.WHITE);
		continueStepOneButton.setWaves(WavesType.DEFAULT);
		continueStepOneButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String email = emailTextBox.getText();

				if (email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.(?:[a-zA-Z]{2,6})$")) {
					MaterialLoader.showLoading(true);

					DBCheckForEmailAsync rpcService = (DBCheckForEmailAsync) GWT.create(DBCheckForEmail.class);
					ServiceDefTarget target = (ServiceDefTarget) rpcService;
					String moduleRelativeURL = GWT.getModuleBaseURL() + "DBCheckForEmailImpl";
					target.setServiceEntryPoint(moduleRelativeURL);

					rpcService.checkForEmail(email, new AsyncCallback<Email>() {

						@Override
						public void onSuccess(Email result) {
							MaterialLoader.showLoading(false);
							emailTextBox.setSuccess("");
							stepper.setSuccess("");

							stepTwo.setEnabled(true);
							stepper.nextStep();
							stepOne.setEnabled(false);

						}

						@Override
						public void onFailure(Throwable caught) {
							MaterialLoader.showLoading(false);

							if (caught instanceof EmailNotFoundException) {
								stepper.setError("Some errors occured!");
								emailTextBox.setError(
										"Provided E-Mail Address was not found. Please contact your Administrator for further information.");
							} else {
								stepper.setError("Something went wrong");
								emailTextBox.removeErrorModifiers();
								MaterialModal errorModal = ModalCreator.createModal(caught);
								RootPanel.get().add(errorModal);
								errorModal.open();
							}

						}
					});
				} else {
					stepper.setError("Some errors occured!");
					emailTextBox.setError("Not a valid E-Mail Address.");
				}
			}
		});
		stepOne.add(continueStepOneButton);

		MaterialButton previousStepOneButton = new MaterialButton();
		previousStepOneButton.setText("Cancel");
		previousStepOneButton.setGrid("l4");
		previousStepOneButton.setMarginTop(12.0);
		previousStepOneButton.setType(ButtonType.FLAT);
		previousStepOneButton.setWaves(WavesType.DEFAULT);
		previousStepOneButton.addClickHandler(ch -> {
			stepper.prevStep();
			RootPanel.get().remove(registerPanel);
			registerPanel.close();
			MaterialToast.fireToast("Registration process aborted by user!", "rounded");
		});
		stepOne.add(previousStepOneButton);

		// Step 2
		stepTwo.setStep(2);
		stepTwo.setTitle("Registration: Personal Details");
		stepTwo.setDescription("Your personal details will be processed.");

		MaterialPanel stepTwoPanel = new MaterialPanel();
		stepTwoPanel.setWidth("100%");

		/** Start adding registration widgets **/

		MaterialTextBox firstNameBox = new MaterialTextBox();
		firstNameBox.setType(InputType.TEXT);
		firstNameBox.setPlaceholder("First Name");
		firstNameBox.setMaxLength(30);
		firstNameBox.setLength(30);
		firstNameBox.setIconType(IconType.PERSON);
		stepTwoPanel.add(firstNameBox);

		MaterialTextBox lastNameBox = new MaterialTextBox();
		lastNameBox.setType(InputType.TEXT);
		lastNameBox.setPlaceholder("Last Name");
		lastNameBox.setMaxLength(30);
		lastNameBox.setLength(30);
		lastNameBox.setIconType(IconType.PERSON);
		stepTwoPanel.add(lastNameBox);

		MaterialDatePicker dateOfBirthBox = new MaterialDatePicker();
		dateOfBirthBox.setPlaceholder("Date of Birth");
		dateOfBirthBox.setSelectionType(MaterialDatePickerType.YEAR);
		dateOfBirthBox.setDate(new Date(0));
		dateOfBirthBox.setDateMax(new Date());
		dateOfBirthBox.setIconType(IconType.DATE_RANGE);
		stepTwoPanel.add(dateOfBirthBox);

		MaterialTextBox mobileNumberBox = new MaterialTextBox();
		mobileNumberBox.setType(InputType.TEL);
		mobileNumberBox.setPlaceholder("Phone Number");
		mobileNumberBox.setMaxLength(10);
		mobileNumberBox.setLength(10);
		mobileNumberBox.setIconType(IconType.SMARTPHONE);
		stepTwoPanel.add(mobileNumberBox);

		MaterialListBox genderBox = new MaterialListBox();
		genderBox.setPlaceholder("Gender");
		genderBox.add(new Option("Male"));
		genderBox.add(new Option("Female"));
		stepTwoPanel.add(genderBox);

		MaterialTextArea addressBox = new MaterialTextArea();
		addressBox.setType(InputType.TEXT);
		addressBox.setPlaceholder("Address");
		addressBox.setLength(200);
		addressBox.setIconType(IconType.HOME);
		stepTwoPanel.add(addressBox);

		MaterialTextBox cityBox = new MaterialTextBox();
		cityBox.setType(InputType.TEXT);
		cityBox.setPlaceholder("Home City");
		cityBox.setMaxLength(30);
		cityBox.setLength(30);
		cityBox.setIconType(IconType.LOCATION_CITY);
		stepTwoPanel.add(cityBox);

		MaterialTextBox countryBox = new MaterialTextBox();
		countryBox.setType(InputType.TEXT);
		countryBox.setPlaceholder("Home Country");
		countryBox.setMaxLength(30);
		countryBox.setLength(30);
		countryBox.setIconType(IconType.PUBLIC);
		stepTwoPanel.add(countryBox);

		MaterialTextBox cnpBox = new MaterialTextBox();
		cnpBox.setType(InputType.TEXT);
		cnpBox.setPlaceholder("CNP");
		cnpBox.setMaxLength(13);
		cnpBox.setLength(13);
		cnpBox.setIconType(IconType.PERM_IDENTITY);
		stepTwoPanel.add(cnpBox);

		MaterialTextBox personalNumberBox = new MaterialTextBox();
		personalNumberBox.setType(InputType.TEXT);
		personalNumberBox.setPlaceholder("ID Series and Number");
		personalNumberBox.setMaxLength(8);
		personalNumberBox.setLength(8);
		personalNumberBox.setIconType(IconType.PERM_IDENTITY);
		stepTwoPanel.add(personalNumberBox);

		/** End adding registration widgets **/

		stepTwo.add(stepTwoPanel);

		MaterialButton continueStepTwoButton = new MaterialButton();
		continueStepTwoButton.setText("Next Step");
		continueStepTwoButton.setGrid("l4");
		continueStepTwoButton.setMarginTop(12.0);
		continueStepTwoButton.setTextColor(Color.WHITE);
		continueStepTwoButton.setWaves(WavesType.DEFAULT);
		continueStepTwoButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String firstName = firstNameBox.getText();
				String lastName = lastNameBox.getText();
				String mobileNumber = mobileNumberBox.getText();
				String address = addressBox.getText();
				String city = cityBox.getText();
				String country = countryBox.getText();
				String cnp = cnpBox.getText();
				String personalNumber = personalNumberBox.getText();

				boolean canProcceed = true;

				if (!firstName.matches("[A-Za-z]{1,30}")) {
					canProcceed = false;
					stepper.setError("Some errors occured!");
					firstNameBox.setError("First name cannot be empty or exceed 30 characters.");
				} else {
					firstNameBox.setSuccess("");
				}

				if (!lastName.matches("[A-Za-z]{1,30}")) {
					canProcceed = false;
					stepper.setError("Some errors occured!");
					lastNameBox.setError("Last name cannot be empty or exceed 30 characters.");
				} else {
					lastNameBox.setSuccess("");
				}

				if (!mobileNumber.matches("[0-9]{10}")) {
					canProcceed = false;
					stepper.setError("Some errors occured!");
					mobileNumberBox.setError("Only digits are allowed. Phone number must have 10 digits.");
				} else {
					mobileNumberBox.setSuccess("");
				}

				if (address.length() < 1 || address.length() > 200) {
					canProcceed = false;
					stepper.setError("Some errors occured!");
					addressBox.setError("Address cannot be empty or exceed 200 characters.");
				} else {
					addressBox.setSuccess("");
				}

				if (!city.matches("[A-Za-z]{1,30}")) {
					canProcceed = false;
					stepper.setError("Some errors occured!");
					cityBox.setError("City cannot be empty or exceed 30 characters.");
				} else {
					cityBox.setSuccess("");
				}

				if (!country.matches("[A-Za-z]{1,30}")) {
					canProcceed = false;
					stepper.setError("Some errors occured!");
					countryBox.setError("Country cannot be empty or exceed 30 characters.");
				} else {
					countryBox.setSuccess("");
				}

				if (!cnp.matches("[0-9]{13}")) {
					canProcceed = false;
					stepper.setError("Some errors occured!");
					cnpBox.setError("Only digits are allowed. CNP must have 13 digits.");
				} else {
					cnpBox.setSuccess("");
				}

				if (!personalNumber.matches("[A-Za-z]{2}[0-9]{6}")) {
					canProcceed = false;
					stepper.setError("Some errors occured!");
					personalNumberBox.setError("Personal number and series cannot be empty. 2 letters followed by 6 numbers are required.");
				} else {
					personalNumberBox.setSuccess("");
				}

				if (canProcceed) {
					stepper.setSuccess("");
					dateOfBirthBox.setSuccess("");
					genderBox.setSuccess("");

					stepThree.setEnabled(true);
					stepper.nextStep();
					stepTwo.setEnabled(false);

				}
			}
		});
		stepTwo.add(continueStepTwoButton);

		MaterialButton previousStepTwoButton = new MaterialButton();
		previousStepTwoButton.setText("Previous Step");
		previousStepTwoButton.setGrid("l4");
		previousStepTwoButton.setMarginTop(12.0);
		previousStepTwoButton.setType(ButtonType.FLAT);
		previousStepTwoButton.setWaves(WavesType.DEFAULT);
		previousStepTwoButton.addClickHandler(ch -> {
			stepOne.setEnabled(true);
			stepper.prevStep();
			stepTwo.setEnabled(false);
		});
		stepTwo.add(previousStepTwoButton);

		// Step 3
		stepThree.setStep(3);
		stepThree.setTitle("Registration: Login Credentials");
		stepThree.setDescription("Your login credentials will be processed.");

		MaterialPanel stepThreePanel = new MaterialPanel();
		stepThreePanel.setWidth("100%");

		/** Start adding credentials widgets **/

		MaterialTextBox usernameBox = new MaterialTextBox();
		usernameBox.setType(InputType.TEXT);
		usernameBox.setPlaceholder("Username");
		usernameBox.setLength(25);
		usernameBox.setMaxLength(25);
		usernameBox.setIconType(IconType.ACCOUNT_CIRCLE);
		stepThreePanel.add(usernameBox);

		MaterialTextBox passwordBox = new MaterialTextBox();
		passwordBox.setType(InputType.PASSWORD);
		passwordBox.setPlaceholder("Password");
		passwordBox.setLength(10);
		passwordBox.setMaxLength(10);
		passwordBox.setIconType(IconType.LOCK);
		stepThreePanel.add(passwordBox);

		MaterialTextBox repeatPasswordBox = new MaterialTextBox();
		repeatPasswordBox.setType(InputType.PASSWORD);
		repeatPasswordBox.setPlaceholder("Repeat Password");
		repeatPasswordBox.setLength(10);
		repeatPasswordBox.setMaxLength(10);
		repeatPasswordBox.setIconType(IconType.LOCK);
		stepThreePanel.add(repeatPasswordBox);

		/** End adding credentials widgets **/

		stepThree.add(stepThreePanel);

		MaterialButton continueStepThreeButton = new MaterialButton();
		continueStepThreeButton.setText("Finish");
		continueStepThreeButton.setGrid("l4");
		continueStepThreeButton.setMarginTop(12.0);
		continueStepThreeButton.setTextColor(Color.WHITE);
		continueStepThreeButton.setWaves(WavesType.DEFAULT);
		continueStepThreeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String username = usernameBox.getText();
				String password = passwordBox.getText();
				String repeatPassword = repeatPasswordBox.getText();

				boolean canProceed = true;

				if (!username.matches("[A-Za-z0-9._-]{5,25}")) {
					canProceed = false;
					stepper.setError("Some errors occured!");
					usernameBox.setError(
							"Username can only contain uppercase, lowercase letters, 0-9 digits and following special characters . _ - and must be between 5 and 25 characters long.");
				} else {
					usernameBox.setSuccess("");
				}

				if (!password.matches("[A-Za-z0-9]{5,10}")) {
					canProceed = false;
					stepper.setError("Some errors occured!");
					passwordBox.setError(
							"Password can only contain uppercase, lowercase letters and 0-9 digits. Password has to be between 5 and 10 characters long.");
				} else {
					passwordBox.setSuccess("");
				}

				if (!repeatPassword.matches("[A-Za-z0-9]{5,10}")) {
					canProceed = false;
					stepper.setError("Some errors occured!");
					repeatPasswordBox.setError(
							"Password can only contain uppercase, lowercase letters and 0-9 digits. Password has to be between 5 and 10 characters long.");
				} else {
					repeatPasswordBox.setSuccess("");
				}

				if (canProceed) {
					if (!repeatPassword.equals(password)) {
						canProceed = false;
						stepper.setError("Some errors occured!");
						repeatPasswordBox.setError("Passwords must match.");
					} else {
						repeatPasswordBox.setSuccess("");

						String firstName = firstNameBox.getText();
						String lastName = lastNameBox.getText();
						Date dateOfBirth = dateOfBirthBox.getDate();
						String email = emailTextBox.getText();
						String mobileNumber = mobileNumberBox.getText();
						String gender = genderBox.getSelectedItemText();
						String address = addressBox.getText();
						String city = cityBox.getText();
						String country = countryBox.getText();
						String cnp = cnpBox.getText();
						String personalNumber = personalNumberBox.getText();

						DBRegisterUserAsync rpcService = (DBRegisterUserAsync) GWT.create(DBRegisterUser.class);
						ServiceDefTarget target = (ServiceDefTarget) rpcService;
						String moduleRelativeURL = GWT.getModuleBaseURL() + "DBRegisterUserImpl";
						target.setServiceEntryPoint(moduleRelativeURL);

						UserInfo userInfo = new UserInfo(firstName, lastName, dateOfBirth, email, mobileNumber, gender, address, city, country, cnp,
								personalNumber, username, password, null);

						rpcService.registerUser(userInfo, new AsyncCallback<Boolean>() {

							@Override
							public void onSuccess(Boolean result) {
								stepper.setSuccess("");
								RootPanel.get().remove(registerPanel);
								registerPanel.close();
								MaterialToast.fireToast("User: " + username + " has successfully registered!", "rounded");
							}

							@Override
							public void onFailure(Throwable caught) {
								if (caught instanceof UsernameUnavailableException) {
									usernameBox.setError(caught.getMessage());
								} else {
									MaterialModal errorModal = ModalCreator.createModal(caught);
									RootPanel.get().add(errorModal);
									errorModal.open();
								}
							}

						});

					}
				}

			}
		});
		stepThree.add(continueStepThreeButton);

		MaterialButton previousStepThreeButton = new MaterialButton();
		previousStepThreeButton.setText("Previous Step");
		previousStepThreeButton.setGrid("l4");
		previousStepThreeButton.setMarginTop(12.0);
		previousStepThreeButton.setType(ButtonType.FLAT);
		previousStepThreeButton.setWaves(WavesType.DEFAULT);
		previousStepThreeButton.addClickHandler(ch -> {
			stepTwo.setEnabled(true);
			stepper.prevStep();
			stepThree.setEnabled(false);
		});
		stepThree.add(previousStepThreeButton);

		stepper.add(stepOne);
		stepper.add(stepTwo);
		stepper.add(stepThree);

		content.add(stepper);

		registerPanel.add(content);

		return registerPanel;
	}

	private static void performUserConnection(String username, String password, MaterialButton loginButton, MaterialCheckBox loggedIn) {
		loginButton.setEnabled(false);

		DBConnectionAsync rpcService = (DBConnectionAsync) GWT.create(DBConnection.class);
		ServiceDefTarget target = (ServiceDefTarget) rpcService;
		String moduleRelativeURL = GWT.getModuleBaseURL() + "DBConnectionImpl";
		target.setServiceEntryPoint(moduleRelativeURL);

		rpcService.authenticateUser(username, password, new AsyncCallback<UserInfo>() {

			@Override
			public void onSuccess(UserInfo userInfo) {
				loginButton.setEnabled(true);

				JSONObject userInfoObject = new JSONObject();
				userInfoObject.put("firstName", new JSONString(userInfo.getFirstName()));
				userInfoObject.put("lastName", new JSONString(userInfo.getLastName()));
				userInfoObject.put("dateOfBirth", new JSONString(userInfo.getDateOfBirth().toString()));
				userInfoObject.put("email", new JSONString(userInfo.getEmail()));
				userInfoObject.put("mobileNumber", new JSONString(userInfo.getMobileNumber()));
				userInfoObject.put("gender", new JSONString(userInfo.getGender()));
				userInfoObject.put("address", new JSONString(userInfo.getAddress()));
				userInfoObject.put("city", new JSONString(userInfo.getCity()));
				userInfoObject.put("country", new JSONString(userInfo.getCountry()));
				userInfoObject.put("personalNumber", new JSONString(userInfo.getPersonalNumber()));
				userInfoObject.put("idSeries", new JSONString(userInfo.getIdSeries()));
				userInfoObject.put("apartmentNumber", new JSONString(userInfo.getApartmentNumber()));
				userInfoObject.put("username", new JSONString(userInfo.getUsername()));

				if (loggedIn.getValue()) {
					final long DURATION = 1000 * 60 * 60 * 24 * 1; // 1 day
					Date expires = new Date(System.currentTimeMillis() + DURATION);
					Cookies.setCookie("sid", userInfoObject.toString(), expires, null, "/", false);
				} else {
					final long DURATION = 1000 * 60 * 60; // 1 hour
					Date expires = new Date(System.currentTimeMillis() + DURATION);
					Cookies.setCookie("sid", userInfoObject.toString(), expires, null, "/", false);
				}

				if (userInfo.getType().equals("User")) {
					presenter.goTo(new UserPlace(userInfoObject.toString()));
				} else if (userInfo.getType().equals("Admin")) {
					presenter.goTo(new AdminPlace(username));
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				loginButton.setEnabled(true);

				if (caught instanceof InvalidCredentialsException) {
					MaterialToast.fireToast(caught.getMessage(), "rounded");
				} else {
					MaterialModal materialModal = ModalCreator.createModal("Something went wrong", caught);
					RootPanel.get().add(materialModal);
					materialModal.open();
				}

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
