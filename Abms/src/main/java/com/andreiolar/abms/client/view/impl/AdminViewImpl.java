package com.andreiolar.abms.client.view.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.andreiolar.abms.client.constants.AdminMenuConstants;
import com.andreiolar.abms.client.constants.DialogBoxConstants;
import com.andreiolar.abms.client.constants.RegistrationFormConstants;
import com.andreiolar.abms.client.constants.UserMenuConstants;
import com.andreiolar.abms.client.place.LoginPlace;
import com.andreiolar.abms.client.rpc.DBChangeUsername;
import com.andreiolar.abms.client.rpc.DBChangeUsernameAsync;
import com.andreiolar.abms.client.rpc.DBCheckIfActiveVotingSession;
import com.andreiolar.abms.client.rpc.DBCheckIfActiveVotingSessionAsync;
import com.andreiolar.abms.client.rpc.DBCheckIfVoteIDAvailable;
import com.andreiolar.abms.client.rpc.DBCheckIfVoteIDAvailableAsync;
import com.andreiolar.abms.client.rpc.DBDeactivateVote;
import com.andreiolar.abms.client.rpc.DBDeactivateVoteAsync;
import com.andreiolar.abms.client.rpc.DBGetActiveVoteID;
import com.andreiolar.abms.client.rpc.DBGetActiveVoteIDAsync;
import com.andreiolar.abms.client.rpc.DBGetComplaintInfo;
import com.andreiolar.abms.client.rpc.DBGetComplaintInfoAsync;
import com.andreiolar.abms.client.rpc.DBGetSelfReadings;
import com.andreiolar.abms.client.rpc.DBGetSelfReadingsAsync;
import com.andreiolar.abms.client.rpc.DBGetUserInfo;
import com.andreiolar.abms.client.rpc.DBGetUserInfoAsync;
import com.andreiolar.abms.client.rpc.DBInsertEmail;
import com.andreiolar.abms.client.rpc.DBInsertEmailAsync;
import com.andreiolar.abms.client.rpc.DBInsertVote;
import com.andreiolar.abms.client.rpc.DBInsertVoteAsync;
import com.andreiolar.abms.client.rpc.DBUpdateUser;
import com.andreiolar.abms.client.rpc.DBUpdateUserAsync;
import com.andreiolar.abms.client.rpc.DateGetter;
import com.andreiolar.abms.client.rpc.DateGetterAsync;
import com.andreiolar.abms.client.utils.DialogBoxCreator;
import com.andreiolar.abms.client.utils.WidgetUtils;
import com.andreiolar.abms.client.view.AdminView;
import com.andreiolar.abms.client.widgets.AboutWidget;
import com.andreiolar.abms.client.widgets.CustomTexBox;
import com.andreiolar.abms.client.widgets.InstInfoWidget;
import com.andreiolar.abms.client.widgets.VotingResultsWidget;
import com.andreiolar.abms.shared.ComplaintInfo;
import com.andreiolar.abms.shared.EmptyStringValidator;
import com.andreiolar.abms.shared.IDSeriesValidator;
import com.andreiolar.abms.shared.MobileNumberValidator;
import com.andreiolar.abms.shared.NameValidator;
import com.andreiolar.abms.shared.SelfReading;
import com.andreiolar.abms.shared.UserInfo;
import com.andreiolar.abms.shared.Vote;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.ListDataProvider;

public class AdminViewImpl extends Composite implements AdminView {

	private String username;
	private static UserInfo userInfo;

	private ScrollPanel scroller = new ScrollPanel();

	private String months[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
	private String currentYear;

	private static Presenter presenter;

	private static TextBox activeVoteID = new TextBox();

	public AdminViewImpl() {

		String sessionId = Cookies.getCookie("sid");

		if (sessionId != null) {
			Widget mainMenu = createMenu();
			initWidget(mainMenu);
		} else {
			presenter.goTo(new LoginPlace(""));
		}

	}

	private void setUsername() {
		String url = Window.Location.getHref();
		String place = url.substring(url.indexOf("#") + 1, url.length());

		this.username = place.substring(place.indexOf(":") + 1, place.length());
	}

	private Widget createMenu() {
		setUsername();
		setUserInfo(username);
		setCurrentYear();

		String sessionId = Cookies.getCookie("sid");

		if (!sessionId.equals(username)) {
			presenter.goTo(new LoginPlace(""));
		}

		VerticalPanel panel = new VerticalPanel();
		HTML title = new HTML("<p style=\"font-size:35px\"><b><i>Apartment Building Management System</i></b></p>");
		Widget menu = createMenuBar();
		HTML footer = new HTML("<p><i>Copyright &copy; 2016 Andrei Olar</i></p>");

		Widget defaultWidget = WidgetUtils.createDefaultPresentationWidget(username);
		scroller.add(defaultWidget);

		DecoratorPanel decPanel = new DecoratorPanel();
		decPanel.setWidget(scroller);

		scroller.setAlwaysShowScrollBars(true);

		panel.add(title);
		panel.add(menu);
		panel.add(decPanel);
		panel.add(footer);

		panel.setCellHorizontalAlignment(footer, HasHorizontalAlignment.ALIGN_CENTER);

		scroller.setStyleName("scroll-panel");
		decPanel.setStyleName("dec-panel");

		return panel;
	}

	private Widget createMenuBar() {
		// Create the menu bar
		MenuBar menu = new MenuBar();
		menu.setAutoOpen(true);
		menu.setWidth("100%");
		menu.setAnimationEnabled(true);

		menu.setStyleName("navigation-menu");

		// Create the complaints menu
		MenuBar complaintsMenu = new MenuBar(true);
		complaintsMenu.setAnimationEnabled(true);
		menu.addItem(new MenuItem(AdminMenuConstants.MENU_ITEM_COMPLAINTS, complaintsMenu));

		complaintsMenu.addItem(AdminMenuConstants.MENU_ITEM_VIEW_COMPLAINTS, new Command() {

			@Override
			public void execute() {
				scroller.clear();
				Widget complaintsViewerWidget = createComplaintsViewerWidget();
				scroller.add(complaintsViewerWidget);
			}
		});

		MenuBar administrationMenu = new MenuBar(true);
		administrationMenu.setAnimationEnabled(true);
		menu.addItem(new MenuItem(AdminMenuConstants.MENU_ITEM_ADMINISTRATION, administrationMenu));

		administrationMenu.addItem(AdminMenuConstants.MENU_ITEM_CONTACT_VIEW, new Command() {

			@Override
			public void execute() {
				scroller.clear();
				// Widget contactViewWidget = new ContactViewWidget(userInfo);
				// scroller.add(contactViewWidget);
			}
		});

		administrationMenu.addItem(AdminMenuConstants.MENU_ITEM_ADD_EMAIL, new Command() {

			@Override
			public void execute() {
				scroller.clear();
				Widget emailWidget = createEmailWidget();
				emailWidget.getElement().getStyle().setMarginTop(50.0, Unit.PX);
				scroller.add(emailWidget);

			}
		});

		MenuBar upkeepMenu = new MenuBar(true);

		administrationMenu.addSeparator();

		upkeepMenu.addItem(AdminMenuConstants.MENU_ITEM_UPLOAD_UPKEEP, new Command() {

			@Override
			public void execute() {
				scroller.clear();
				Widget upkeepUploadWidget = createUploadFormWidget();
				scroller.add(upkeepUploadWidget);
			}
		});

		upkeepMenu.addItem(AdminMenuConstants.MENU_ITEM_VIEW_READINGS, new Command() {

			@Override
			public void execute() {
				scroller.clear();
				Widget viewReadingsWidget = createReadingsWidget();
				scroller.add(viewReadingsWidget);
			}
		});

		administrationMenu.addItem(AdminMenuConstants.MENU_ITEM_UPKEEP, upkeepMenu);

		MenuBar votingMenu = new MenuBar(true);
		votingMenu.setAnimationEnabled(true);
		menu.addItem(new MenuItem(AdminMenuConstants.MENU_ITEM_VOTING, votingMenu));

		votingMenu.addItem(AdminMenuConstants.MENU_ITEM_CREATE_VOTE, new Command() {

			@Override
			public void execute() {
				scroller.clear();
				Widget voteCreatorWidget = createVoteCreatorWidget();
				scroller.add(voteCreatorWidget);

			}
		});

		votingMenu.addItem(AdminMenuConstants.MENU_ITEM_VIEW_RESULTS, new Command() {

			@Override
			public void execute() {
				scroller.clear();
				Widget viewResultsWidget = new VotingResultsWidget(userInfo, true);
				scroller.add(viewResultsWidget);
			}
		});

		MenuBar helpMenu = new MenuBar(true);
		helpMenu.setAnimationEnabled(true);
		menu.addItem(new MenuItem(AdminMenuConstants.MENU_ITEM_HELP, helpMenu));

		helpMenu.addItem(AdminMenuConstants.MENU_ITEM_ABOUT, new Command() {

			@Override
			public void execute() {
				scroller.clear();
				Widget aboutWidget = new AboutWidget();
				scroller.add(aboutWidget);
			}
		});

		helpMenu.addItem(AdminMenuConstants.MENU_ITEM_INST_INFO, new Command() {

			@Override
			public void execute() {
				scroller.clear();
				Widget instInfoWifged = new InstInfoWidget();
				scroller.add(instInfoWifged);

			}
		});

		MenuBar optionsMenu = new MenuBar(true);
		optionsMenu.setAnimationEnabled(true);
		menu.addItem(new MenuItem(AdminMenuConstants.MENU_ITEM_OPTIONS, optionsMenu));

		optionsMenu.addItem(AdminMenuConstants.MENU_ITEM_UPDATE_USER_INFO, new Command() {

			@Override
			public void execute() {
				scroller.clear();
				Widget updateUserInfoWidget = createUpdateUserInfoWidget();
				scroller.add(updateUserInfoWidget);

			}
		});

		optionsMenu.addSeparator();

		optionsMenu.addItem(AdminMenuConstants.LOGOUT, new Command() {

			@Override
			public void execute() {
				Window.Location.replace("http://127.0.0.1:8888/Abms.html");
			}
		});

		return menu;
	}

	protected Widget createUpdateUserInfoWidget() {
		final VerticalPanel panel = new VerticalPanel();
		panel.setSpacing(10);

		final Label usernameLabel = new Label("Username");;
		usernameLabel.getElement().getStyle().setFontSize(24.0, Unit.PX);
		usernameLabel.getElement().getStyle().setFontWeight(FontWeight.BOLD);

		// Username
		Label usernameL = new Label();
		usernameL.setText("Username: ");

		final TextBox usernameBox = new TextBox();
		usernameBox.setStyleName("fixed-input");

		// Grid for username
		final Grid usernameGrid = new Grid(1, 2);

		// Get button
		final Button getPersonalDataButton = new Button();
		getPersonalDataButton.setText("Retrieve Data");
		getPersonalDataButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				String username = usernameBox.getText();

				boolean usernameSet = true;

				String message = "";

				if (username == null || username.trim().equals("")) {
					message += "<p>Please enter a username!</p>";
					usernameSet = false;
				}

				if (usernameSet) {
					DBGetUserInfoAsync rpcService = (DBGetUserInfoAsync) GWT.create(DBGetUserInfo.class);
					ServiceDefTarget target = (ServiceDefTarget) rpcService;
					String moduleRelativeURL = GWT.getModuleBaseURL() + "DBGetUserInfoImpl";
					target.setServiceEntryPoint(moduleRelativeURL);

					DOM.getElementById("loading").getStyle().setDisplay(Display.BLOCK);

					rpcService.getUserInfo(username, new AsyncCallback<UserInfo>() {

						@Override
						public void onFailure(Throwable caught) {
							DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
							DialogBox dialogBox = DialogBoxCreator.createDialogBox(AdminMenuConstants.DIALOG_BOX_FAILURE_GET_USER_INFO,
									caught.getMessage(), DialogBoxConstants.CLOSE_BUTTON, false, false);
							dialogBox.setGlassEnabled(true);
							dialogBox.setAnimationEnabled(true);
							dialogBox.center();
							dialogBox.show();

							panel.clear();
							panel.add(usernameLabel);
							panel.add(usernameGrid);
							panel.add(getPersonalDataButton);

						}

						@Override
						public void onSuccess(final UserInfo userInfo) {
							DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
							panel.clear();
							panel.add(usernameLabel);

							TextBox usernameB = (TextBox) usernameGrid.getWidget(0, 1);
							usernameB.setText(userInfo.getUsername());

							panel.add(usernameGrid);
							panel.add(getPersonalDataButton);

							// Personal Information Label
							final Label personalInformationLabel = new Label("Personal Information");;
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
									firstNameBox.validate();

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
									lastNameBox.validate();

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
							Label addressObs = new Label("(From the current ID)");

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
							Label cityObs = new Label("(From the current ID)");

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
							Label seriesNumberObs = new Label("(From the current ID)");

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

							// Buttons
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

									if (!firstName.equals(userInfo.getFirstName()) || !lastName.equals(userInfo.getLastName())
											|| !email.equals(userInfo.getEmail()) || !mobileNumber.equals(userInfo.getMobileNumber())
											|| !address.equals(userInfo.getAddress()) || !city.equals(userInfo.getCity())
											|| !country.equals(userInfo.getCountry()) || !idSeries.equals(userInfo.getIdSeries())) {

										if (firstNameBox.validate() && lastNameBox.validate() && mobileNumberBox.validate() && cityBox.validate()
												&& personalNumberBox.validate() && seriesNumberBox.validate()) {

											DBUpdateUserAsync rpc = (DBUpdateUserAsync) GWT.create(DBUpdateUser.class);
											ServiceDefTarget tar = (ServiceDefTarget) rpc;
											String moduleURL = GWT.getModuleBaseURL() + "DBUpdateUserImpl";
											tar.setServiceEntryPoint(moduleURL);
											DOM.getElementById("loading").getStyle().setDisplay(Display.BLOCK);
											rpc.updateUser(firstName, lastName, email, mobileNumber, address, city, country, idSeries, personalNumber,
													new AsyncCallback<Boolean>() {

														@Override
														public void onSuccess(Boolean result) {
															DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
															DialogBox dialogBox = DialogBoxCreator.createDialogBox(
																	UserMenuConstants.SUCCESS_UPDATE_USER_TITLE,
																	UserMenuConstants.SUCCESS_UPDATE_USER_MESSAGE, DialogBoxConstants.CLOSE_BUTTON,
																	false, true);
															dialogBox.setGlassEnabled(true);
															dialogBox.setAnimationEnabled(true);
															dialogBox.center();
															dialogBox.show();
														}

														@Override
														public void onFailure(Throwable caught) {
															DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
															DialogBox dialogBox = DialogBoxCreator.createDialogBox(
																	UserMenuConstants.FAILED_UPDATE_USER_TITLE, caught.getMessage(),
																	DialogBoxConstants.CLOSE_BUTTON, false, false);
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
												UserMenuConstants.DIALOG_BOX_FAILED_SUBMIT_INFO_MESSAGE, DialogBoxConstants.CLOSE_BUTTON, false,
												false);
										dialogBox.setGlassEnabled(true);
										dialogBox.setAnimationEnabled(true);
										dialogBox.center();
										dialogBox.show();
									}

								}
							});

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

							// Create a change login information label
							final Label loginInformation = new Label("Login Information");;
							loginInformation.getElement().getStyle().setFontSize(24.0, Unit.PX);
							loginInformation.getElement().getStyle().setFontWeight(FontWeight.BOLD);

							// Username
							Label usernameL2 = new Label();
							usernameL2.setText("Username: ");

							final TextBox usernameBox2 = new TextBox();
							usernameBox2.setStyleName("fixed-input");
							usernameBox2.setText(userInfo.getUsername());

							// Grid for username
							final Grid usernameGrid2 = new Grid(1, 2);
							usernameGrid2.setWidget(0, 0, usernameL2);
							usernameGrid2.setWidget(0, 1, usernameBox2);

							Button loginSubmitButton = new Button();
							loginSubmitButton.setText("Submit");
							loginSubmitButton.addClickHandler(new ClickHandler() {

								@Override
								public void onClick(ClickEvent event) {
									String originalUser = usernameBox.getText();
									String user = usernameBox2.getText();

									if (!user.equals(originalUser)) {

										if (user != null && !user.trim().equals("")) {
											DOM.getElementById("loading").getStyle().setDisplay(Display.BLOCK);
											DBChangeUsernameAsync rpc = (DBChangeUsernameAsync) GWT.create(DBChangeUsername.class);
											ServiceDefTarget tar = (ServiceDefTarget) rpc;
											String moduleURL = GWT.getModuleBaseURL() + "DBChangeUsernameImpl";
											tar.setServiceEntryPoint(moduleURL);

											rpc.changeUsername(originalUser, user, new AsyncCallback<Boolean>() {

												@Override
												public void onFailure(Throwable caught) {
													DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
													DialogBox dialogBox = DialogBoxCreator.createDialogBox(
															UserMenuConstants.DIALOG_BOX_FAILED_SUBMIT_INFO_TITLE, caught.getMessage(),
															DialogBoxConstants.CLOSE_BUTTON, false, false);
													dialogBox.setGlassEnabled(true);
													dialogBox.setAnimationEnabled(true);
													dialogBox.center();
													dialogBox.show();

												}

												@Override
												public void onSuccess(Boolean result) {
													DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
													DialogBox dialogBox = DialogBoxCreator.createDialogBox(
															AdminMenuConstants.DIALOG_BOX_SUCCESS_UPDATE_USERNAME_TITLE,
															AdminMenuConstants.DIALOG_BOX_SUCCESS_UPDATE_USERNAME_MESSAGE,
															DialogBoxConstants.CLOSE_BUTTON, false, true);
													dialogBox.setGlassEnabled(true);
													dialogBox.setAnimationEnabled(true);
													dialogBox.center();
													dialogBox.show();
												}
											});

										} else {
											DialogBox dialogBox = DialogBoxCreator.createDialogBox(
													UserMenuConstants.DIALOG_BOX_FAILED_SUBMIT_INFO_TITLE,
													AdminMenuConstants.DIALOG_BOX_FAILED_SUBMIT_INFO_USER_EMPTY_MESSAGE,
													DialogBoxConstants.CLOSE_BUTTON, false, false);
											dialogBox.setGlassEnabled(true);
											dialogBox.setAnimationEnabled(true);
											dialogBox.center();
											dialogBox.show();
										}

									} else {
										DialogBox dialogBox = DialogBoxCreator.createDialogBox(UserMenuConstants.DIALOG_BOX_FAILED_SUBMIT_INFO_TITLE,
												UserMenuConstants.DIALOG_BOX_FAILED_SUBMIT_INFO_MESSAGE, DialogBoxConstants.CLOSE_BUTTON, false,
												false);
										dialogBox.setGlassEnabled(true);
										dialogBox.setAnimationEnabled(true);
										dialogBox.center();
										dialogBox.show();
									}

								}

							});

							Button loginResetButton = new Button();
							loginResetButton.setText("Reset");
							loginResetButton.addClickHandler(new ClickHandler() {

								@Override
								public void onClick(ClickEvent event) {
									usernameBox2.setText(userInfo.getUsername());
								}
							});

							// Create Grid to hold the buttons
							Grid loginButtonPanel = new Grid(1, 2);

							loginButtonPanel.setWidget(0, 0, loginSubmitButton);
							loginButtonPanel.setWidget(0, 1, loginResetButton);
							loginButtonPanel.setCellSpacing(20);

							panel.add(personalInformationLabel);
							panel.add(registrationPanel);
							panel.add(buttonPanel);
							panel.add(loginInformation);
							panel.add(usernameGrid2);
							panel.add(loginButtonPanel);

						}
					});

				} else {
					DialogBox dialogBox = DialogBoxCreator.createDialogBox(AdminMenuConstants.DIALOG_BOX_FAILURE_USERNAME_NOT_SET, message,
							DialogBoxConstants.CLOSE_BUTTON, false, false);
					dialogBox.setGlassEnabled(true);
					dialogBox.setAnimationEnabled(true);
					dialogBox.center();
					dialogBox.show();
				}
			}

		});

		usernameGrid.setWidget(0, 0, usernameL);
		usernameGrid.setWidget(0, 1, usernameBox);

		panel.add(usernameLabel);
		panel.add(usernameGrid);
		panel.add(getPersonalDataButton);

		return panel;
	}

	protected Widget createEmailWidget() {
		FlexTable table = new FlexTable();
		table.setCellSpacing(6);
		table.setWidth("300px");
		FlexCellFormatter cellFormatter = table.getFlexCellFormatter();

		// Add a title to the form
		table.setHTML(0, 0, AdminMenuConstants.FORM_NAME);
		cellFormatter.setColSpan(0, 0, 2);
		cellFormatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);

		// Email
		final CustomTexBox emailBox = new CustomTexBox();
		emailBox.setMaxLength(50);
		// emailBox.addValidator(new EmailValidator());
		emailBox.setStyleName("fixed-input");

		emailBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				emailBox.validate();
			}
		});

		// Apartment Number
		final TextBox apartmentNumberBox = new TextBox();
		apartmentNumberBox.setMaxLength(3);
		apartmentNumberBox.setStyleName("fixed-input");

		// Add Button
		Button addButton = new Button();
		addButton.setText("Add");
		addButton.setWidth("100px");
		addButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				String email = emailBox.getText();
				String apartmentNumber = apartmentNumberBox.getText();

				boolean emailSet = true;
				boolean apartmentNumberSet = true;

				String message = "";

				if (email == null || email.trim().equals("")) {
					message += "<p>Please enter an E-Mail address!</p>";
					emailSet = false;
				}

				if (apartmentNumber == null || apartmentNumber.trim().equals("")) {
					message += "<p>Pleae enter the apartment number!</p>";
					apartmentNumberSet = false;
				}

				if (emailSet && apartmentNumberSet) {
					DOM.getElementById("loading").getStyle().setDisplay(Display.BLOCK);
					DBInsertEmailAsync rpcService = (DBInsertEmailAsync) GWT.create(DBInsertEmail.class);
					ServiceDefTarget target = (ServiceDefTarget) rpcService;
					String moduleRelativeURL = GWT.getModuleBaseURL() + "DBInsertEmailImpl";
					target.setServiceEntryPoint(moduleRelativeURL);

					rpcService.insertEmail(email, apartmentNumber, new AsyncCallback<Boolean>() {

						@Override
						public void onFailure(Throwable caught) {
							DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
							DialogBox dialogBox = DialogBoxCreator.createDialogBox(AdminMenuConstants.DIALOG_BOX_FAILURE_ADD_EMAIL_TITLE,
									caught.getMessage(), DialogBoxConstants.CLOSE_BUTTON, false, false);
							dialogBox.setGlassEnabled(true);
							dialogBox.setAnimationEnabled(true);
							dialogBox.center();
							dialogBox.show();

						}

						@Override
						public void onSuccess(Boolean result) {
							DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
							DialogBox dialogBox = DialogBoxCreator.createDialogBox(AdminMenuConstants.DIALOG_BOX_SUCCESS_ADD_EMAIL_TITLE,
									AdminMenuConstants.DIALOG_BOX_SUCCESS_ADD_EMAIL_MESSAGE, DialogBoxConstants.CLOSE_BUTTON, false, false);
							dialogBox.setGlassEnabled(true);
							dialogBox.setAnimationEnabled(true);
							dialogBox.center();
							dialogBox.show();

							emailBox.setText(null);
							apartmentNumberBox.setText(null);
						}
					});

				} else {
					DialogBox dialogBox = DialogBoxCreator.createDialogBox(AdminMenuConstants.DIALOG_BOX_FAILURE_ADD_EMAIL_TITLE, message,
							DialogBoxConstants.CLOSE_BUTTON, false, false);
					dialogBox.setGlassEnabled(true);
					dialogBox.setAnimationEnabled(true);
					dialogBox.center();
					dialogBox.show();
				}

			}
		});

		table.setHTML(1, 0, AdminMenuConstants.E_MAIL);
		table.setWidget(1, 1, emailBox);
		table.setHTML(2, 0, AdminMenuConstants.APT_NUMBER);
		table.setWidget(2, 1, apartmentNumberBox);
		table.setWidget(3, 1, addButton);

		DecoratorPanel decPanel = new DecoratorPanel();
		decPanel.setWidget(table);

		return decPanel;
	}

	protected Widget createVoteCreatorWidget() {
		getActiveVoteID();
		VerticalPanel panel = new VerticalPanel();

		// Description Label
		HTML descriptionLabel = new HTML(
				"<p style=\"font-size:20px\">Please create a description for this voting session.<br>Note: Max. 500 characters.<br>Obs: For new lines please use HTML '&lt;br&gt;'.</p>");

		// TextArea
		final TextArea textArea = new TextArea();
		textArea.setSize("500px", "200px");
		textArea.getElement().setAttribute("maxlength", "500");

		// Vote ID Label
		HTML voteIdLabel = new HTML(
				"<p style=\"font-size:20px\">Please choose a vote ID.<br>Note: Numbers only.<br>Obs: Error will occur if vote ID already exists.</p>");

		// Horizontal Panel
		HorizontalPanel hPanel = new HorizontalPanel();

		// Vote ID TextBox
		final TextBox voteIDBox = new TextBox();
		voteIDBox.setMaxLength(11);

		// Label
		Label voteId = new Label();
		voteId.setText("Vote ID: ");

		// Check Button
		Button checkButton = new Button();
		checkButton.setText("Check");
		checkButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String voteId = voteIDBox.getText();

				DOM.getElementById("loading").getStyle().setDisplay(Display.BLOCK);
				DBCheckIfVoteIDAvailableAsync rpcService = (DBCheckIfVoteIDAvailableAsync) GWT.create(DBCheckIfVoteIDAvailable.class);
				ServiceDefTarget target = (ServiceDefTarget) rpcService;
				String moduleRelativeURL = GWT.getModuleBaseURL() + "DBCheckIfVoteIDAvailableImpl";
				target.setServiceEntryPoint(moduleRelativeURL);

				rpcService.checkVoteId(voteId, new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
						DialogBox dialogBox = DialogBoxCreator.createDialogBox(AdminMenuConstants.DIALOG_BOX_VOTE_ID_EXISTS_TITLE,
								caught.getMessage(), DialogBoxConstants.CLOSE_BUTTON, false, false);
						dialogBox.setGlassEnabled(true);
						dialogBox.setAnimationEnabled(true);
						dialogBox.center();
						dialogBox.show();
					}

					@Override
					public void onSuccess(Boolean result) {
						DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
						DialogBox dialogBox = DialogBoxCreator.createDialogBox(AdminMenuConstants.DIALOG_BOX_VOTE_ID_NOT_EXISTS_TITLE,
								AdminMenuConstants.DIALOG_BOX_VOTE_ID_NOT_EXISTS_MESSAGE, DialogBoxConstants.CLOSE_BUTTON, false, false);
						dialogBox.setGlassEnabled(true);
						dialogBox.setAnimationEnabled(true);
						dialogBox.center();
						dialogBox.show();
					}
				});

			}
		});

		// Deactivate Vote Button
		Button deactivateVote = new Button();
		deactivateVote.setText("Deactivate Vote");
		deactivateVote.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final DialogBox dialogBox = new DialogBox();
				dialogBox.setTitle("Vote Deactivation");

				VerticalPanel panel = new VerticalPanel();
				panel.setSpacing(6);
				dialogBox.setWidget(panel);

				HTML details = new HTML();
				details.setHTML("<p>Please press the deactivate Button in order to deactivate the vote.</p>");
				panel.add(details);
				panel.setCellHorizontalAlignment(details, HasHorizontalAlignment.ALIGN_CENTER);

				activeVoteID.setEnabled(false);
				activeVoteID.setSize("75px", "20px");
				panel.add(activeVoteID);
				panel.setCellHorizontalAlignment(activeVoteID, HasHorizontalAlignment.ALIGN_CENTER);

				HorizontalPanel hPanel = new HorizontalPanel();

				Button deactivateButton = new Button();
				deactivateButton.setText("Deactivate");
				deactivateButton.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {

						final String voteId = activeVoteID.getText();

						if (voteId != null && !voteId.trim().equals("")) {
							DOM.getElementById("loading").getStyle().setDisplay(Display.BLOCK);
							DBDeactivateVoteAsync rpcService = (DBDeactivateVoteAsync) GWT.create(DBDeactivateVote.class);
							ServiceDefTarget target = (ServiceDefTarget) rpcService;
							String moduleRelativeURL = GWT.getModuleBaseURL() + "DBDeactivateVoteImpl";
							target.setServiceEntryPoint(moduleRelativeURL);

							rpcService.deactivateVote(voteId, new AsyncCallback<Boolean>() {

								@Override
								public void onFailure(Throwable caught) {
									DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
									DialogBox dialogBox = DialogBoxCreator.createDialogBox(
											AdminMenuConstants.DIALOG_BOX_FAILED_DEACTIVATE_VOTE_ID_TITLE, caught.getMessage(),
											DialogBoxConstants.CLOSE_BUTTON, false, false);
									dialogBox.setGlassEnabled(true);
									dialogBox.setAnimationEnabled(true);
									dialogBox.center();
									dialogBox.show();

								}

								@Override
								public void onSuccess(Boolean result) {
									DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
									DialogBox dialogBox = DialogBoxCreator.createDialogBox(
											AdminMenuConstants.DIALOG_BOX_SUCCESS_DEACTIVATE_VOTE_ID_TITLE,
											AdminMenuConstants.DIALOG_BOX_SUCCESS_DEACTIVATE_VOTE_ID_MESSAGE, DialogBoxConstants.CLOSE_BUTTON, false,
											false);
									dialogBox.setGlassEnabled(true);
									dialogBox.setAnimationEnabled(true);
									dialogBox.center();
									dialogBox.show();

									activeVoteID.setText(null);
								}
							});

						} else {
							DialogBox dialogBox = DialogBoxCreator.createDialogBox(AdminMenuConstants.DIALOG_BOX_VOTE_ID_NOT_EXISTS_TITLE,
									AdminMenuConstants.DIALOG_BOX_VOTE_ID_NOT_ACTIVE_MESSAGE, DialogBoxConstants.CLOSE_BUTTON, false, false);
							dialogBox.setGlassEnabled(true);
							dialogBox.setAnimationEnabled(true);
							dialogBox.center();
							dialogBox.show();
						}

						dialogBox.hide();
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

				hPanel.add(deactivateButton);
				hPanel.add(cancelButton);

				hPanel.getWidget(0).getElement().getStyle().setMarginRight(20.0, Unit.PX);
				hPanel.getWidget(0).getElement().getStyle().setMarginTop(20.0, Unit.PX);
				hPanel.getWidget(1).getElement().getStyle().setMarginTop(20.0, Unit.PX);

				panel.add(hPanel);

				panel.setCellHorizontalAlignment(hPanel, HasHorizontalAlignment.ALIGN_CENTER);

				dialogBox.setGlassEnabled(true);
				dialogBox.setAnimationEnabled(true);
				dialogBox.center();
				dialogBox.show();
			}
		});

		hPanel.add(voteId);
		hPanel.add(voteIDBox);
		hPanel.add(checkButton);
		hPanel.add(deactivateVote);

		hPanel.getWidget(0).getElement().getStyle().setMarginRight(20.0, Unit.PX);
		hPanel.getWidget(0).getElement().getStyle().setMarginTop(5.0, Unit.PX);
		hPanel.getWidget(1).getElement().getStyle().setMarginRight(20.0, Unit.PX);
		hPanel.getWidget(2).getElement().getStyle().setMarginRight(20.0, Unit.PX);

		// Create a FlexTable
		final FlexTable flexTable = new FlexTable();
		FlexCellFormatter cellFormatter = flexTable.getFlexCellFormatter();
		flexTable.addStyleName("cw-FlexTable");
		flexTable.setWidth("32em");
		flexTable.setCellSpacing(5);
		flexTable.setCellPadding(3);

		// Add some text
		cellFormatter.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);
		flexTable.setHTML(0, 0, "Vote Options");
		cellFormatter.setColSpan(0, 0, 2);

		// Add a button that will add more rows to the table
		Button addRowsButton = new Button();
		addRowsButton.setText("Add Option");
		addRowsButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addRow(flexTable);
			}
		});
		addRowsButton.addStyleName("sc-FixedWidthButton");

		// Add a button that will remove rows from the table
		Button removeRowButton = new Button();
		removeRowButton.setText("Remove Option");
		removeRowButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				removeRow(flexTable);
			}
		});
		removeRowButton.addStyleName("sc-FixedWidthButton");

		VerticalPanel buttonPanel = new VerticalPanel();
		buttonPanel.setStyleName("cw-FlexTable-buttonPanel");
		buttonPanel.add(addRowsButton);
		buttonPanel.add(removeRowButton);
		flexTable.setWidget(0, 1, buttonPanel);
		cellFormatter.setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);

		HTML tableDescription = new HTML(
				"<p style=\"font-size:20px\">Please add voting options.<br>Note: At least 2 options have to be added in order to create a voting session.</p>");

		HorizontalPanel buttons = new HorizontalPanel();

		Button submitButton = new Button();
		submitButton.setText("Submit");
		submitButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final String text = textArea.getText();
				final String voteID = voteIDBox.getText();
				final List<String> options = new ArrayList<String>();

				int rowCount = flexTable.getRowCount();
				for (int i = 1; i < rowCount; i++) {
					Widget widget = flexTable.getWidget(i, 1);
					TextBox textBox = (TextBox) widget;

					String option = textBox.getText();
					if (option != null && !options.contains(option.trim())) {
						options.add(option);
					}

				}

				boolean hasText = true;
				boolean hasVoteId = true;
				boolean hasOptions = true;
				boolean isNumber = true;

				String message = "";

				if (text == null || text.trim().equals("")) {
					message += "<p>Please enter a description for this voting session!</p>";
					hasOptions = false;
				}

				if (voteID == null || voteID.trim().equals("")) {
					message += "<p>Please enter a vote ID for this voting session!</p>";
					hasVoteId = false;

				}

				if (!isNumeric(voteID)) {
					message += "<p>Vote ID has to be a number!</p>";
					isNumber = false;
				}

				if (options.size() < 2) {
					message += "<p>Voting session must contain at least 2 voting options!</p>";
					hasOptions = false;
				}

				if (hasText && hasVoteId && isNumber && hasOptions) {
					DOM.getElementById("loading").getStyle().setDisplay(Display.BLOCK);
					DBCheckIfVoteIDAvailableAsync rpcService = (DBCheckIfVoteIDAvailableAsync) GWT.create(DBCheckIfVoteIDAvailable.class);
					ServiceDefTarget target = (ServiceDefTarget) rpcService;
					String moduleRelativeURL = GWT.getModuleBaseURL() + "DBCheckIfVoteIDAvailableImpl";
					target.setServiceEntryPoint(moduleRelativeURL);

					rpcService.checkVoteId(voteID, new AsyncCallback<Boolean>() {

						@Override
						public void onFailure(Throwable caught) {
							DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
							DialogBox dialogBox = DialogBoxCreator.createDialogBox(AdminMenuConstants.DIALOG_BOX_VOTE_ID_EXISTS_TITLE,
									caught.getMessage(), DialogBoxConstants.CLOSE_BUTTON, false, false);
							dialogBox.setGlassEnabled(true);
							dialogBox.setAnimationEnabled(true);
							dialogBox.center();
							dialogBox.show();
						}

						@Override
						public void onSuccess(Boolean result) {
							DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
							DBCheckIfActiveVotingSessionAsync rpcService = (DBCheckIfActiveVotingSessionAsync) GWT
									.create(DBCheckIfActiveVotingSession.class);
							ServiceDefTarget target = (ServiceDefTarget) rpcService;
							String moduleRelativeURL = GWT.getModuleBaseURL() + "DBCheckIfActiveVotingSessionImpl";
							target.setServiceEntryPoint(moduleRelativeURL);

							DOM.getElementById("loading").getStyle().setDisplay(Display.BLOCK);
							rpcService.ckeckForActiveVotingSession(new AsyncCallback<Boolean>() {

								@Override
								public void onFailure(Throwable caught) {
									DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
									DialogBox dialogBox = DialogBoxCreator.createDialogBox(AdminMenuConstants.DIALOG_BOX_FAILED_ACTIVE_VOTE_EXISTS,
											caught.getMessage(), DialogBoxConstants.CLOSE_BUTTON, false, false);
									dialogBox.setGlassEnabled(true);
									dialogBox.setAnimationEnabled(true);
									dialogBox.center();
									dialogBox.show();

								}

								@Override
								public void onSuccess(Boolean result) {
									DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
									Vote vote = new Vote(options, voteID, "true");
									DOM.getElementById("loading").getStyle().setDisplay(Display.BLOCK);
									DBInsertVoteAsync rpcService = (DBInsertVoteAsync) GWT.create(DBInsertVote.class);
									ServiceDefTarget target = (ServiceDefTarget) rpcService;
									String moduleRelativeURL = GWT.getModuleBaseURL() + "DBInsertVoteImpl";
									target.setServiceEntryPoint(moduleRelativeURL);

									rpcService.insertVote(vote, text, new AsyncCallback<Boolean>() {

										@Override
										public void onFailure(Throwable caught) {
											DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
											DialogBox dialogBox = DialogBoxCreator.createDialogBox(
													AdminMenuConstants.DIALOG_BOX_FAILED_INSERT_VOTE_TITLE, caught.getMessage(),
													DialogBoxConstants.CLOSE_BUTTON, false, false);
											dialogBox.setGlassEnabled(true);
											dialogBox.setAnimationEnabled(true);
											dialogBox.center();
											dialogBox.show();

										}

										@Override
										public void onSuccess(Boolean result) {
											DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
											DialogBox dialogBox = DialogBoxCreator.createDialogBox(
													AdminMenuConstants.DIALOG_BOX_SUCCESS_INSERT_VOTE_TITLE,
													AdminMenuConstants.DIALOG_BOX_SUCCESS_INSERT_VOTE_MESSAGE, DialogBoxConstants.CLOSE_BUTTON, false,
													false);
											dialogBox.setGlassEnabled(true);
											dialogBox.setAnimationEnabled(true);
											dialogBox.center();
											dialogBox.show();

											textArea.setText(null);
											voteIDBox.setText(null);
											removeAllRows(flexTable);

										}
									});
								}
							});
						}
					});
				} else {
					DialogBox dialogBox = DialogBoxCreator.createDialogBox(AdminMenuConstants.DIALOG_BOX_FAILED_VOTE_INFO_TITLE, message,
							DialogBoxConstants.CLOSE_BUTTON, false, false);
					dialogBox.setGlassEnabled(true);
					dialogBox.setAnimationEnabled(true);
					dialogBox.center();
					dialogBox.show();
				}
			}
		});

		Button resetButton = new Button();
		resetButton.setText("Reset");
		resetButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				textArea.setText(null);
				voteIDBox.setText(null);
				removeAllRows(flexTable);
			}
		});

		buttons.add(submitButton);
		buttons.add(resetButton);

		buttons.getElement().getStyle().setMarginTop(30.0, Unit.PX);
		buttons.getWidget(0).getElement().getStyle().setMarginRight(20.0, Unit.PX);

		panel.add(descriptionLabel);
		panel.add(textArea);
		panel.add(voteIdLabel);
		panel.add(hPanel);
		panel.add(tableDescription);
		panel.add(flexTable);
		panel.add(buttons);

		panel.getWidget(4).getElement().getStyle().setMarginTop(20.0, Unit.PX);

		return panel;
	}

	private void getActiveVoteID() {
		DOM.getElementById("loading").getStyle().setDisplay(Display.BLOCK);
		DBGetActiveVoteIDAsync rpcService = (DBGetActiveVoteIDAsync) GWT.create(DBGetActiveVoteID.class);
		ServiceDefTarget target = (ServiceDefTarget) rpcService;
		String moduleRelativeURL = GWT.getModuleBaseURL() + "DBGetActiveVoteIDImpl";
		target.setServiceEntryPoint(moduleRelativeURL);

		rpcService.getActiveVoteID(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
				// DO NOTHING
			}

			@Override
			public void onSuccess(String result) {
				DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
				activeVoteID.setText(result);
			}
		});
	}

	public static boolean isNumeric(String str) {
		for (char c : str.toCharArray()) {
			if (!Character.isDigit(c))
				return false;
		}
		return true;
	}

	protected void removeAllRows(FlexTable flexTable) {
		int numRows = flexTable.getRowCount();

		if (numRows > 1) {
			for (int i = 1; i < numRows; i++) {
				flexTable.removeRow(i);
				flexTable.getFlexCellFormatter().setRowSpan(0, 1, i);
				i--;
			}
		}
	}

	protected void removeRow(FlexTable flexTable) {
		int numRows = flexTable.getRowCount();
		if (numRows > 1) {
			flexTable.removeRow(numRows - 1);
			flexTable.getFlexCellFormatter().setRowSpan(0, 1, numRows - 1);
		}
	}

	protected void addRow(FlexTable flexTable) {
		int numRows = flexTable.getRowCount();
		flexTable.setWidget(numRows, 0, new Label("Option " + (numRows)));
		flexTable.setWidget(numRows, 1, new TextBox());
		flexTable.getFlexCellFormatter().setRowSpan(0, 1, numRows + 1);

	}

	protected Widget createReadingsWidget() {
		final VerticalPanel panel = new VerticalPanel();

		final HTML dateDescription = new HTML(
				"<p style=\"font-size:20px\"><b>Please choose the date (Month and Year) you want to view Readings for.</b></p>");

		// Panel to hold month and Year lists
		final HorizontalPanel hPanel = new HorizontalPanel();

		HTML monthDescription = new HTML("Month: ");

		final ListBox monthListBox = new ListBox();
		for (int i = 0; i < months.length; i++) {
			monthListBox.addItem(months[i]);
		}

		monthListBox.setSize("75px", "20px");

		HTML yearDescription = new HTML("Year: ");

		final ListBox yearListBox = new ListBox();
		for (int i = 2016; i <= java.lang.Integer.parseInt(currentYear); i++) {
			yearListBox.addItem(String.valueOf(i));
		}

		yearListBox.setSize("75px", "20px");

		hPanel.add(monthDescription);
		hPanel.add(monthListBox);
		hPanel.add(yearDescription);
		hPanel.add(yearListBox);

		hPanel.getWidget(0).getElement().getStyle().setMarginRight(10.0, Unit.PX);
		hPanel.getWidget(1).getElement().getStyle().setMarginRight(40.0, Unit.PX);
		hPanel.getWidget(2).getElement().getStyle().setMarginRight(10.0, Unit.PX);

		final Button viewReadingsButton = new Button();
		viewReadingsButton.setText("View");
		viewReadingsButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				panel.clear();
				panel.add(dateDescription);
				panel.add(hPanel);
				panel.add(viewReadingsButton);
				String date = monthListBox.getSelectedItemText() + "-" + yearListBox.getSelectedItemText();
				Widget readingsInfo = populateReadingsTable(date);
				panel.add(readingsInfo);
			}
		});

		panel.add(dateDescription);
		panel.add(hPanel);
		panel.add(viewReadingsButton);

		panel.getWidget(2).getElement().getStyle().setMarginTop(20.0, Unit.PX);
		panel.getWidget(2).getElement().getStyle().setMarginBottom(20.0, Unit.PX);

		return panel;
	}

	protected Widget populateReadingsTable(String date) {
		final CellTable<SelfReading> table = new CellTable<SelfReading>();

		table.setWidth("100%");
		table.setAutoFooterRefreshDisabled(true);
		table.setAutoHeaderRefreshDisabled(true);

		final ListDataProvider<SelfReading> dataProvider = new ListDataProvider<SelfReading>();

		ListHandler<SelfReading> listHandler = new ListHandler<SelfReading>(dataProvider.getList());

		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		SimplePager pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		pager.setDisplay(table);
		pager.setPageSize(20);

		// Create the apartment number column
		TextColumn<SelfReading> aptNumberColumn = new TextColumn<SelfReading>() {

			@Override
			public String getValue(SelfReading object) {
				return object.getAptNumber();
			}
		};
		table.setColumnWidth(aptNumberColumn, 10, Unit.PCT);

		// Create the Cold Water column
		TextColumn<SelfReading> coldWaterColumn = new TextColumn<SelfReading>() {

			@Override
			public String getValue(SelfReading object) {
				return object.getColdWater();
			}
		};
		table.setColumnWidth(coldWaterColumn, 10, Unit.PCT);

		// Create the Hot Water column
		TextColumn<SelfReading> hotWaterColumn = new TextColumn<SelfReading>() {

			@Override
			public String getValue(SelfReading object) {
				return object.getHotWater();
			}
		};
		table.setColumnWidth(hotWaterColumn, 10, Unit.PCT);

		// Create the Electricity column
		TextColumn<SelfReading> electricityColumn = new TextColumn<SelfReading>() {

			@Override
			public String getValue(SelfReading object) {
				return object.getElectricity();
			}
		};
		table.setColumnWidth(electricityColumn, 10, Unit.PCT);

		// Create the Gaz column
		TextColumn<SelfReading> gazColumn = new TextColumn<SelfReading>() {

			@Override
			public String getValue(SelfReading object) {
				return object.getGaz();
			}
		};
		table.setColumnWidth(gazColumn, 10, Unit.PCT);

		table.addColumnSortHandler(listHandler);

		// Add the columns
		table.addColumn(aptNumberColumn, AdminMenuConstants.APARTMENT_NUMBER_COLUMN);
		table.addColumn(coldWaterColumn, AdminMenuConstants.COLD_WATER_COLUMN);
		table.addColumn(hotWaterColumn, AdminMenuConstants.HOT_WATER_COLUMN);
		table.addColumn(electricityColumn, AdminMenuConstants.ELECTRICITY_COLUMN);
		table.addColumn(gazColumn, AdminMenuConstants.GAZ_COLUMN);

		DOM.getElementById("loading").getStyle().setDisplay(Display.BLOCK);
		DBGetSelfReadingsAsync rpcService = (DBGetSelfReadingsAsync) GWT.create(DBGetSelfReadings.class);
		ServiceDefTarget target = (ServiceDefTarget) rpcService;
		String moduleRelativeURL = GWT.getModuleBaseURL() + "DBGetSelfReadingsImpl";
		target.setServiceEntryPoint(moduleRelativeURL);

		rpcService.getSelfReadings(date, new AsyncCallback<List<SelfReading>>() {

			@Override
			public void onFailure(Throwable caught) {
				DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
				DialogBox dialogBox = DialogBoxCreator.createDialogBox(AdminMenuConstants.DIALOG_BOX_FAILED_RETRIEVING_SELF_READINGS,
						caught.getMessage(), DialogBoxConstants.CLOSE_BUTTON, false, false);
				dialogBox.setGlassEnabled(true);
				dialogBox.setAnimationEnabled(true);
				dialogBox.center();
				dialogBox.show();
			}

			@Override
			public void onSuccess(List<SelfReading> result) {
				DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
				table.setRowCount(result.size());

				dataProvider.addDataDisplay(table);

				List<SelfReading> list = dataProvider.getList();
				for (SelfReading selfReading : result) {
					list.add(selfReading);
				}

			}
		});

		VerticalPanel vPanel = new VerticalPanel();
		vPanel.add(table);
		vPanel.add(pager);

		table.addStyleName("cellTable");
		pager.addStyleName("pager");

		return vPanel;
	}

	protected Widget createUploadFormWidget() {
		VerticalPanel vPanel = new VerticalPanel();

		String UPLOAD_ACTION_URL = GWT.getModuleBaseURL() + "upload";

		// Create a FormPanel and point it at a service
		final FormPanel form = new FormPanel();
		form.setAction(UPLOAD_ACTION_URL);

		// Because we're going to add a FileUpload widget, we'll need to set the form to use the POST method, and multipart MIME encoding
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);

		// Create a panel to hold all of the form widgets
		VerticalPanel panel = new VerticalPanel();
		form.setWidget(panel);

		// Create a FileUpload widget
		FileUpload upload = new FileUpload();
		upload.setName("uploadFormElement");
		panel.add(upload);

		// Add a submit Button
		Button submitButton = new Button();
		submitButton.setText("Submit");
		submitButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				DOM.getElementById("loading").getStyle().setDisplay(Display.BLOCK);
				form.submit();
			}
		});

		form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {

			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
				Window.alert(event.getResults());
			}
		});
		panel.add(submitButton);

		HTML description = new HTML("<p>Please use the form below to upload the Excel File containing the upkeep costs for the current month.</p>");

		vPanel.add(description);
		vPanel.add(form);

		return vPanel;
	}

	protected Widget createComplaintsViewerWidget() {
		// Create a CelLTable
		final CellTable<ComplaintInfo> table = new CellTable<ComplaintInfo>();

		table.setWidth("100%", true);
		table.setAutoFooterRefreshDisabled(true);
		table.setAutoHeaderRefreshDisabled(true);

		final ListDataProvider<ComplaintInfo> dataProvider = new ListDataProvider<ComplaintInfo>();

		ListHandler<ComplaintInfo> listHandler = new ListHandler<ComplaintInfo>(dataProvider.getList());

		// Create a pager to control the table
		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		SimplePager pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		pager.setDisplay(table);
		pager.setPageSize(25);

		// Create the complaintId column
		final TextColumn<ComplaintInfo> complaintIdColumn = new TextColumn<ComplaintInfo>() {

			@Override
			public String getValue(ComplaintInfo object) {
				return object.getComplaintId();
			}
		};
		table.setColumnWidth(complaintIdColumn, 10, Unit.PCT);
		complaintIdColumn.setSortable(true);
		listHandler.setComparator(complaintIdColumn, new Comparator<ComplaintInfo>() {

			@Override
			public int compare(ComplaintInfo o1, ComplaintInfo o2) {
				Integer comp1 = Integer.parseInt(o1.getComplaintId());
				Integer comp2 = Integer.parseInt(o2.getComplaintId());

				return comp1.compareTo(comp2);
			}
		});

		// Create the name column
		TextColumn<ComplaintInfo> nameColumn = new TextColumn<ComplaintInfo>() {

			@Override
			public String getValue(ComplaintInfo object) {
				return object.getName();
			}
		};
		table.setColumnWidth(nameColumn, 20, Unit.PCT);
		nameColumn.setSortable(true);
		listHandler.setComparator(nameColumn, new Comparator<ComplaintInfo>() {

			@Override
			public int compare(ComplaintInfo o1, ComplaintInfo o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		// Create the phone number column
		TextColumn<ComplaintInfo> phoneNumberColumn = new TextColumn<ComplaintInfo>() {

			@Override
			public String getValue(ComplaintInfo object) {
				return object.getPhoneNumber();
			}
		};
		table.setColumnWidth(phoneNumberColumn, 10, Unit.PCT);

		// Create the date column
		TextColumn<ComplaintInfo> dateColumn = new TextColumn<ComplaintInfo>() {

			@Override
			public String getValue(ComplaintInfo object) {
				return object.getDate();
			}
		};
		table.setColumnWidth(dateColumn, 10, Unit.PCT);
		dateColumn.setSortable(true);
		listHandler.setComparator(dateColumn, new Comparator<ComplaintInfo>() {

			@Override
			public int compare(ComplaintInfo o1, ComplaintInfo o2) {
				return o1.getDate().compareTo(o2.getDate());
			}
		});

		// Create the complaintTo column
		TextColumn<ComplaintInfo> complaintToColumn = new TextColumn<ComplaintInfo>() {

			@Override
			public String getValue(ComplaintInfo object) {
				return object.getComplaintTo();
			}
		};
		table.setColumnWidth(complaintToColumn, 20, Unit.PCT);

		table.addColumnSortHandler(listHandler);

		// Add the columns
		table.addColumn(complaintIdColumn, AdminMenuConstants.COMPLAINT_ID_COLUMN);
		table.addColumn(nameColumn, AdminMenuConstants.COMPLAINT_NAME_COLUMN);
		table.addColumn(phoneNumberColumn, AdminMenuConstants.COMPLAINT_PHONE_NUMBER_COLUMN);
		table.addColumn(dateColumn, AdminMenuConstants.COMPLAINT_DATE_COLUMN);
		table.addColumn(complaintToColumn, AdminMenuConstants.COMPLAINT_TO_COLUMN);

		DOM.getElementById("loading").getStyle().setDisplay(Display.BLOCK);
		DBGetComplaintInfoAsync rpcService = (DBGetComplaintInfoAsync) GWT.create(DBGetComplaintInfo.class);
		ServiceDefTarget target = (ServiceDefTarget) rpcService;
		String moduleRelativeURL = GWT.getModuleBaseURL() + "DBGetComplaintInfoImpl";
		target.setServiceEntryPoint(moduleRelativeURL);

		rpcService.getComplaintInfo(new AsyncCallback<List<ComplaintInfo>>() {

			@Override
			public void onSuccess(List<ComplaintInfo> result) {
				DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
				table.setRowCount(result.size());

				dataProvider.addDataDisplay(table);

				List<ComplaintInfo> list = dataProvider.getList();
				for (ComplaintInfo info : result) {
					list.add(info);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
				DialogBox dialogBox = DialogBoxCreator.createDialogBox(AdminMenuConstants.DIALOG_BOX_FAILED_RETRIEVING_COMPLAINTS_TITLE,
						caught.getMessage(), DialogBoxConstants.CLOSE_BUTTON, false, false);
				dialogBox.setGlassEnabled(true);
				dialogBox.setAnimationEnabled(true);
				dialogBox.center();
				dialogBox.show();
			}
		});

		VerticalPanel panel = new VerticalPanel();
		panel.add(table);
		panel.add(pager);

		table.addStyleName("cellTable");
		pager.addStyleName("pager");

		return panel;
	}

	@Override
	public void setName(String username) {
	}

	@Override
	public void setPresenter(Presenter presenter) {
	}

	private String setCurrentYear() {
		DateGetterAsync rpcService = (DateGetterAsync) GWT.create(DateGetter.class);
		ServiceDefTarget target = (ServiceDefTarget) rpcService;
		String moduleRelativeURL = GWT.getModuleBaseURL() + "DateGetterImpl";
		target.setServiceEntryPoint(moduleRelativeURL);

		String month = "";

		rpcService.getCurrentYear(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				// Don't do anything
			}

			@Override
			public void onSuccess(String result) {
				currentYear = result;
			}
		});

		return month;
	}

	public void setUserInfo(String username) {
		DBGetUserInfoAsync rpcService = (DBGetUserInfoAsync) GWT.create(DBGetUserInfo.class);
		ServiceDefTarget target = (ServiceDefTarget) rpcService;
		String moduleRelativeURL = GWT.getModuleBaseURL() + "DBGetUserInfoImpl";
		target.setServiceEntryPoint(moduleRelativeURL);

		rpcService.getUserInfo(username, new AsyncCallback<UserInfo>() {

			@Override
			public void onSuccess(UserInfo result) {
				AdminViewImpl.userInfo = result;
			}

			@Override
			public void onFailure(Throwable caught) {
				// Window.alert(caught.getMessage());
			}
		});
	}

}
