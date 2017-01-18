package com.andreiolar.abms.client.view.impl;

import com.andreiolar.abms.client.constants.AdministrareBlocConstants;
import com.andreiolar.abms.client.constants.DialogBoxConstants;
import com.andreiolar.abms.client.constants.UserMenuConstants;
import com.andreiolar.abms.client.place.LoginPlace;
import com.andreiolar.abms.client.rpc.DBGetUserInfo;
import com.andreiolar.abms.client.rpc.DBGetUserInfoAsync;
import com.andreiolar.abms.client.rpc.DBPersonalCosts;
import com.andreiolar.abms.client.rpc.DBPersonalCostsAsync;
import com.andreiolar.abms.client.rpc.DateGetter;
import com.andreiolar.abms.client.rpc.DateGetterAsync;
import com.andreiolar.abms.client.utils.DialogBoxCreator;
import com.andreiolar.abms.client.utils.WidgetUtils;
import com.andreiolar.abms.client.view.UserView;
import com.andreiolar.abms.client.widgets.AboutWidget;
import com.andreiolar.abms.client.widgets.AccountSettingsWidget;
import com.andreiolar.abms.client.widgets.ComplaintWidget_bkp;
import com.andreiolar.abms.client.widgets.ContactViewWidget;
import com.andreiolar.abms.client.widgets.ConversationWidget;
import com.andreiolar.abms.client.widgets.GeneralCostWidget;
import com.andreiolar.abms.client.widgets.InstInfoWidget;
import com.andreiolar.abms.client.widgets.MessageDialogBox;
import com.andreiolar.abms.client.widgets.PasswordChangeWidget;
import com.andreiolar.abms.client.widgets.PersonalCostWidget;
import com.andreiolar.abms.client.widgets.SelfReadingWidget;
import com.andreiolar.abms.client.widgets.VotingResultsWidget;
import com.andreiolar.abms.client.widgets.VotingWidget;
import com.andreiolar.abms.shared.PersonalUpkeepInformation;
import com.andreiolar.abms.shared.UserInfo;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class UserViewImpl extends Composite implements UserView {

	private ScrollPanel scroller = new ScrollPanel();
	private String username;
	private static UserInfo userInfo;

	private String currentMonth;
	private String previousMonth;
	private String currentYear;

	private Integer previousMonthInt;

	private Presenter presenter;

	private AdministrareBlocConstants constants = GWT.create(AdministrareBlocConstants.class);

	public UserViewImpl() {

		String sessionId = Cookies.getCookie("sid");

		if (sessionId == null) {
			presenter.goTo(new LoginPlace(""));
		} else {
			Widget mainMenu = createMenu();
			getCurrentMonth();
			getPreviousMonth();
			getCurrentYear();
			getPreviousMonthInt();
			initWidget(mainMenu);
		}

	}

	private void setUsername() {
		String token = History.getToken();
		Window.alert(token);
		this.username = token.substring(token.indexOf(":") + 1, token.length());
	}

	public String getUsername() {
		return username;
	}

	public void setUserInfo(String username) {
		DBGetUserInfoAsync rpcService = (DBGetUserInfoAsync) GWT.create(DBGetUserInfo.class);
		ServiceDefTarget target = (ServiceDefTarget) rpcService;
		String moduleRelativeURL = GWT.getModuleBaseURL() + "DBGetUserInfoImpl";
		target.setServiceEntryPoint(moduleRelativeURL);

		rpcService.getUserInfo(username, new AsyncCallback<UserInfo>() {

			@Override
			public void onSuccess(UserInfo result) {
				UserViewImpl.userInfo = result;
			}

			@Override
			public void onFailure(Throwable caught) {
				// Window.alert(caught.getMessage());
			}
		});
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	private Widget createMenu() {

		setUsername();
		setUserInfo(username);

		String sessionId = Cookies.getCookie("sid");

		if (!sessionId.equals(username)) {
			presenter.goTo(new LoginPlace(""));
		}

		VerticalPanel vPanel = new VerticalPanel();
		HTML title = new HTML("<p style=\"font-size:35px\"><b><i>Apartment Building Management System</i></b></p>");
		Widget menu = createMenuBar();
		HTML footer = new HTML("<p><i>Copyright &copy; 2016 Andrei Olar</i></p>");

		Widget defaultWidget = WidgetUtils.createDefaultPresentationWidget(username);
		scroller.add(defaultWidget);

		DecoratorPanel decPanel = new DecoratorPanel();
		decPanel.setWidget(scroller);

		scroller.setAlwaysShowScrollBars(true);

		vPanel.add(title);
		vPanel.add(menu);
		vPanel.add(decPanel);
		vPanel.add(footer);

		vPanel.setCellHorizontalAlignment(footer, HasHorizontalAlignment.ALIGN_CENTER);

		scroller.setStyleName("scroll-panel");
		decPanel.setStyleName("dec-panel");

		return vPanel;

	}

	private Widget createMenuBar() {

		// Create the menu bar
		MenuBar menu = new MenuBar();
		menu.setAutoOpen(true);
		menu.setWidth("100%");
		menu.setAnimationEnabled(true);

		menu.setStyleName("navigation-menu");

		// Adding a sub menu of emergency services
		MenuBar emergencyServicesMenu = new MenuBar(true);

		// Create the complaints menu
		MenuBar complaintsMenu = new MenuBar(true);
		complaintsMenu.setAnimationEnabled(true);

		menu.addItem(new MenuItem(constants.complaints(), complaintsMenu));

		// Add complaints menu items
		emergencyServicesMenu.addItem(UserMenuConstants.MENU_ITEM_LOCAL_POLICE, new Command() {

			@Override
			public void execute() {
				scroller.clear();
				Widget localPoliceForm = new ComplaintWidget_bkp(userInfo, UserMenuConstants.COMPLAINT_NOTE_LOCAL_POLICE,
						UserMenuConstants.MENU_ITEM_LOCAL_POLICE);
				scroller.add(localPoliceForm);
			}

		});

		emergencyServicesMenu.addItem(UserMenuConstants.MENU_ITEM_NATIONAL_POLICE, new Command() {

			@Override
			public void execute() {
				scroller.clear();
				Widget nationalPoliceForm = new ComplaintWidget_bkp(userInfo, UserMenuConstants.COMPLAINT_NOTE_NATIONAL_POLICE,
						UserMenuConstants.MENU_ITEM_NATIONAL_POLICE);
				scroller.add(nationalPoliceForm);
			}
		});

		emergencyServicesMenu.addItem(UserMenuConstants.MENU_ITEM_MEDICAL_SERVICE, new Command() {

			@Override
			public void execute() {
				scroller.clear();
				Widget medicalServiceForm = new ComplaintWidget_bkp(userInfo, UserMenuConstants.COMPLAINT_NOTE_MEDICAL_SERVICE,
						UserMenuConstants.MENU_ITEM_MEDICAL_SERVICE);
				scroller.add(medicalServiceForm);
			}
		});

		emergencyServicesMenu.addItem(UserMenuConstants.MENU_ITEM_FIREFIGHTER_SERVICE, new Command() {

			@Override
			public void execute() {
				scroller.clear();
				Widget firefighterServiceForm = new ComplaintWidget_bkp(userInfo, UserMenuConstants.COMPLAINT_NOTE_FIREFIGHTER_SERVICE,
						UserMenuConstants.MENU_ITEM_FIREFIGHTER_SERVICE);
				scroller.add(firefighterServiceForm);
			}
		});

		complaintsMenu.addItem(UserMenuConstants.MENU_ITEM_EMERGENCY_SERVICES, emergencyServicesMenu);

		// End of Emergency Services Menu

		// Start of Public Transportation Menu
		MenuBar publicTransportationMenu = new MenuBar(true);

		publicTransportationMenu.addItem(UserMenuConstants.MENU_ITEM_PUBLIC_TRANSPORT, new Command() {

			@Override
			public void execute() {
				scroller.clear();
				Widget publicTransportForm = new ComplaintWidget_bkp(userInfo, UserMenuConstants.COMPLAINT_NOTE_PUBLIC_TRANSPORT,
						UserMenuConstants.MENU_ITEM_PUBLIC_TRANSPORT);
				scroller.add(publicTransportForm);
			}
		});

		publicTransportationMenu.addItem(UserMenuConstants.MENU_ITEM_NATIONAL_RAILWAY, new Command() {

			@Override
			public void execute() {
				scroller.clear();
				Widget firefighterServiceForm = new ComplaintWidget_bkp(userInfo, UserMenuConstants.COMPLAINT_NOTE_NATIONAL_RAILWAY,
						UserMenuConstants.MENU_ITEM_NATIONAL_RAILWAY);
				scroller.add(firefighterServiceForm);
			}
		});

		publicTransportationMenu.addItem(UserMenuConstants.MENU_ITEM_AIRPORT, new Command() {

			@Override
			public void execute() {
				scroller.clear();
				Widget airportForm = new ComplaintWidget_bkp(userInfo, UserMenuConstants.COMPLAINT_NOTE_AIRPORT, UserMenuConstants.MENU_ITEM_AIRPORT);
				scroller.add(airportForm);
			}
		});

		complaintsMenu.addItem(UserMenuConstants.MENU_ITEM_PUBLIC_TRANSPORTATION, publicTransportationMenu);

		// End of Public Transportation Menu

		// Start of Salubrity Menu

		complaintsMenu.addSeparator();

		complaintsMenu.addItem(UserMenuConstants.MENU_ITEM_SALUBRITY, new Command() {

			@Override
			public void execute() {
				scroller.clear();
				Widget salubrityForm = new ComplaintWidget_bkp(userInfo, UserMenuConstants.COMPLAINT_NOTE_SALUBRITY,
						UserMenuConstants.MENU_ITEM_SALUBRITY);
				scroller.add(salubrityForm);
			}
		});

		// End of Salubrity Menu

		// Start of Town Hall Menu

		complaintsMenu.addItem(UserMenuConstants.MENU_ITEM_TOWN_HALL, new Command() {

			@Override
			public void execute() {
				scroller.clear();
				Widget townHallForm = new ComplaintWidget_bkp(userInfo, UserMenuConstants.COMPLAINT_NOTE_TOWN_HALL,
						UserMenuConstants.MENU_ITEM_TOWN_HALL);
				scroller.add(townHallForm);
			}
		});

		// End of Town Hall Menu

		/* End of Complaints Menu */

		/* Create the Administration Menu */
		MenuBar administrationMenu = new MenuBar(true);
		administrationMenu.setAnimationEnabled(true);
		menu.addItem(new MenuItem(UserMenuConstants.MENU_ITEM_ADMINISTRATION, administrationMenu));

		// Add contact info Menu
		administrationMenu.addItem(UserMenuConstants.MENU_ITEM_CONTACT_INFO, new Command() {

			@Override
			public void execute() {
				scroller.clear();
				Widget contactView = new ContactViewWidget(userInfo);
				scroller.add(contactView);
			}

		});
		// End of Contact Info Menu

		administrationMenu.addItem(UserMenuConstants.MENU_ITEM_SELF_READINGS, new Command() {

			@Override
			public void execute() {
				scroller.clear();
				Widget selfReadingsWidget = new SelfReadingWidget(previousMonth, currentYear, userInfo);
				scroller.add(selfReadingsWidget);
			}
		});

		administrationMenu.addSeparator();

		MenuBar upkeepMenu = new MenuBar(true);

		upkeepMenu.addItem(UserMenuConstants.MENU_ITEM_GENERAL_VIEW, new Command() {

			@Override
			public void execute() {
				scroller.clear();
				Widget generalCostsView = new GeneralCostWidget();
				final PopupPanel contextMenu = new PopupPanel(true);

				VerticalPanel verticalPanel = populateContextMenu();

				contextMenu.add(verticalPanel);
				contextMenu.hide();

				generalCostsView.sinkEvents(Event.ONCONTEXTMENU);
				generalCostsView.addHandler(new ContextMenuHandler() {

					@Override
					public void onContextMenu(ContextMenuEvent event) {
						event.preventDefault();
						event.stopPropagation();

						int x = event.getNativeEvent().getClientX();
						int y = event.getNativeEvent().getClientY();

						contextMenu.setPopupPosition(x, y);
						contextMenu.show();

					}
				}, ContextMenuEvent.getType());

				scroller.add(generalCostsView);
			}
		});

		upkeepMenu.addItem(UserMenuConstants.MENU_ITEM_PERSONAL_VIEW, new Command() {

			@Override
			public void execute() {
				scroller.clear();

				DOM.getElementById("loading").getStyle().setDisplay(Display.BLOCK);
				DBPersonalCostsAsync rpcService = (DBPersonalCostsAsync) GWT.create(DBPersonalCosts.class);
				ServiceDefTarget target = (ServiceDefTarget) rpcService;
				String moduleRelativeURL = GWT.getModuleBaseURL() + "DBPersonalCostsImpl";
				target.setServiceEntryPoint(moduleRelativeURL);

				rpcService.getPersonalUpkeepInformation(username, currentMonth, new AsyncCallback<PersonalUpkeepInformation>() {

					@Override
					public void onFailure(Throwable caught) {
						DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
						DialogBox dialogBox = DialogBoxCreator.createDialogBox(UserMenuConstants.DIALOG_BOX_FAILED_GETTING_PERSONAL_COSTS_TITLE,
								UserMenuConstants.DIALOG_BOX_FAILED_GETTING_PERSONAL_COSTS_MESSAGE + ": " + caught.getMessage(),
								DialogBoxConstants.CLOSE_BUTTON, false, false);
						dialogBox.setGlassEnabled(true);
						dialogBox.setAnimationEnabled(true);
						dialogBox.center();
						dialogBox.show();
					}

					@Override
					public void onSuccess(PersonalUpkeepInformation result) {
						DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
						Widget personalViewWidget = new PersonalCostWidget(result);

						final PopupPanel contextMenu = new PopupPanel(true);
						Widget contextMenuWidget = createPersonalViewContextMenu();
						contextMenu.add(contextMenuWidget);
						contextMenu.hide();

						personalViewWidget.sinkEvents(Event.ONCONTEXTMENU);
						personalViewWidget.addHandler(new ContextMenuHandler() {

							@Override
							public void onContextMenu(ContextMenuEvent event) {
								event.preventDefault();
								event.stopPropagation();

								int x = event.getNativeEvent().getClientX();
								int y = event.getNativeEvent().getClientY();

								contextMenu.setPopupPosition(x, y);
								contextMenu.show();

							}
						}, ContextMenuEvent.getType());

						scroller.add(personalViewWidget);
					}
				});

			}
		});

		administrationMenu.addItem(UserMenuConstants.MENU_MONTHLY_COSTS, upkeepMenu);

		/* Create the Voting Menu */
		MenuBar votingMenu = new MenuBar(true);
		votingMenu.setAnimationEnabled(true);
		menu.addItem(new MenuItem(UserMenuConstants.MENU_ITEM_VOTING, votingMenu));

		votingMenu.addItem(UserMenuConstants.MENU_ITEM_VOTE, new Command() {

			@Override
			public void execute() {
				scroller.clear();
				Widget voteWidget = new VotingWidget(userInfo);
				scroller.add(voteWidget);

			}
		});

		votingMenu.addItem(UserMenuConstants.MENU_ITEM_VIEW_RESULTS, new Command() {

			@Override
			public void execute() {
				scroller.clear();
				Widget resultsWidget = new VotingResultsWidget(userInfo, false);
				scroller.add(resultsWidget);
			}
		});

		MenuBar messagingMenu = new MenuBar(true);
		messagingMenu.setAnimationEnabled(true);
		menu.addItem(new MenuItem(UserMenuConstants.MENU_ITEM_MESSAGING, messagingMenu));

		messagingMenu.addItem(UserMenuConstants.MENU_ITEM_CONVERSATIONS, new Command() {

			@Override
			public void execute() {
				scroller.clear();
				Widget conversationWidget = new ConversationWidget(userInfo);
				scroller.add(conversationWidget);

			}
		});

		messagingMenu.addItem(UserMenuConstants.MENU_ITEM_COMPOSE, new Command() {

			@Override
			public void execute() {
				scroller.clear();
				MessageDialogBox messageDialog = new MessageDialogBox(userInfo);
				DialogBox dialogBox = messageDialog.initializeDialogBox();

				scroller.add(dialogBox);

				dialogBox.setGlassEnabled(true);
				dialogBox.setAnimationEnabled(true);
				dialogBox.center();
				dialogBox.show();
			}
		});

		MenuBar helpMenu = new MenuBar(true);
		helpMenu.setAnimationEnabled(true);
		menu.addItem(new MenuItem(UserMenuConstants.MENU_ITEM_HELP, helpMenu));

		helpMenu.addItem(UserMenuConstants.MENU_ITEM_ABOUT, new Command() {

			@Override
			public void execute() {
				scroller.clear();
				Widget aboutWidget = new AboutWidget();
				scroller.add(aboutWidget);
			}
		});

		helpMenu.addItem(UserMenuConstants.MENU_ITEM_INST_INFO, new Command() {

			@Override
			public void execute() {
				scroller.clear();
				Widget instInfoWifged = new InstInfoWidget();
				scroller.add(instInfoWifged);

			}
		});

		MenuBar optionsMenu = new MenuBar(true);
		optionsMenu.setAnimationEnabled(true);
		menu.addItem(new MenuItem(UserMenuConstants.MENU_ITEM_OPTIONS, optionsMenu));

		optionsMenu.addItem(UserMenuConstants.MENU_ITEM_EDIT_ACCOUNT_SETTINGS, new Command() {

			@Override
			public void execute() {
				scroller.clear();
				Widget editAccountSettingsWidget = new AccountSettingsWidget(userInfo);
				scroller.add(editAccountSettingsWidget);

			}
		});

		optionsMenu.addItem(UserMenuConstants.MENU_ITEM_CHANGE_PASSWORD, new Command() {

			@Override
			public void execute() {
				scroller.clear();
				Widget changePasswordWidget = new PasswordChangeWidget(userInfo);
				scroller.add(changePasswordWidget);

			}

		});

		optionsMenu.addSeparator();

		optionsMenu.addItem(UserMenuConstants.LOGOUT, new Command() {

			@Override
			public void execute() {
				Window.Location.replace("http://127.0.0.1:8888/Abms.html");
			}
		});

		return menu;

	}

	protected Widget createPersonalViewContextMenu() {

		Button pdfButton = new Button();
		String pdfHTML = "<div><img src = '"
				+ "/images/icons/pdf_small_logo.png' height = '10px' width = '10px'></img><label>   Export as PDF</label></div>";
		pdfButton.setHTML(pdfHTML);
		pdfButton.setSize("140px", "30px");

		pdfButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String url = GWT.getModuleBaseURL() + "pdfGenerator?username=" + username + "&month=" + currentMonth;
				Window.open(url, "_self", "status=0,toolbar=0,menubar=0,location=0");
			}
		});

		return pdfButton;

	}

	@Override
	public void setPresenter(Presenter presenter) {

	}

	private VerticalPanel populateContextMenu() {
		Button excelButton = new Button();
		String excelHTML = "<div><img src = '"
				+ "/images/icons/small_excel_logo.png' height = '10px' width = '10px'></img><label>   Export as Excel</label></div>";
		excelButton.setHTML(excelHTML);
		excelButton.setSize("140px", "30px");

		excelButton.addClickHandler(new ClickHandler() {

			String fileInfo = "Upkeep_" + currentMonth + ".xls";

			@Override
			public void onClick(ClickEvent event) {
				String url = GWT.getModuleBaseURL() + "downloadServlet?fileInfo=" + fileInfo;
				Window.open(url, "_self", "status=0,toolbar=0,menubar=0,location=0");
			}
		});

		Button pdfButton = new Button();
		String pdfHTML = "<div><img src = '"
				+ "/images/icons/pdf_small_logo.png' height = '10px' width = '10px'></img><label>   Export as PDF</label></div>";
		pdfButton.setHTML(pdfHTML);
		pdfButton.setSize("140px", "30px");

		pdfButton.addClickHandler(new ClickHandler() {

			String fileInfo = "Upkeep_" + currentMonth + ".pdf";

			@Override
			public void onClick(ClickEvent event) {
				String url = GWT.getModuleBaseURL() + "downloadServlet?fileInfo=" + fileInfo;
				Window.open(url, "_self", "status=0,toolbar=0,menubar=0,location=0");
			}
		});

		Button zipButton = new Button();
		String zipHTML = "<div><img src = '"
				+ "/images/icons/winzip-logo.png' height = '10px' width = '10px'></img><label>   Export both as ZIP</label></div>";
		zipButton.setHTML(zipHTML);
		zipButton.setSize("140px", "30px");

		zipButton.addClickHandler(new ClickHandler() {

			String fileInfo = "Upkeep_" + currentMonth + ".zip";

			@Override
			public void onClick(ClickEvent event) {
				String url = GWT.getModuleBaseURL() + "downloadServlet?fileInfo=" + fileInfo;
				Window.open(url, "_self", "status=0,toolbar=0,menubar=0,location=0");
			}
		});

		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.add(excelButton);
		verticalPanel.add(pdfButton);
		verticalPanel.add(zipButton);

		return verticalPanel;
	}

	private String getCurrentMonth() {
		DateGetterAsync rpcService = (DateGetterAsync) GWT.create(DateGetter.class);
		ServiceDefTarget target = (ServiceDefTarget) rpcService;
		String moduleRelativeURL = GWT.getModuleBaseURL() + "DateGetterImpl";
		target.setServiceEntryPoint(moduleRelativeURL);

		String month = "";

		rpcService.getCurrentMonth(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				// Don't do anything
			}

			@Override
			public void onSuccess(String result) {
				currentMonth = result;
			}
		});

		return month;
	}

	private String getPreviousMonth() {
		DateGetterAsync rpcService = (DateGetterAsync) GWT.create(DateGetter.class);
		ServiceDefTarget target = (ServiceDefTarget) rpcService;
		String moduleRelativeURL = GWT.getModuleBaseURL() + "DateGetterImpl";
		target.setServiceEntryPoint(moduleRelativeURL);

		String month = "";

		rpcService.getPreviousMonth(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				// Don't do anything
			}

			@Override
			public void onSuccess(String result) {
				previousMonth = result;
			}
		});

		return month;
	}

	private String getCurrentYear() {
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

	private String getPreviousMonthInt() {
		DateGetterAsync rpcService = (DateGetterAsync) GWT.create(DateGetter.class);
		ServiceDefTarget target = (ServiceDefTarget) rpcService;
		String moduleRelativeURL = GWT.getModuleBaseURL() + "DateGetterImpl";
		target.setServiceEntryPoint(moduleRelativeURL);

		String month = "";

		rpcService.getPreviousMonthInt(new AsyncCallback<Integer>() {

			@Override
			public void onFailure(Throwable caught) {
				// Don't do anything
			}

			@Override
			public void onSuccess(Integer result) {
				previousMonthInt = result;
			}
		});

		return month;
	}

	@Override
	public void setUsername(String username) {
		// TODO Auto-generated method stub
	}

}
