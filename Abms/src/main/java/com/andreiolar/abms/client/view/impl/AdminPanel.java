package com.andreiolar.abms.client.view.impl;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.andreiolar.abms.client.exception.AptNumberUnavailableException;
import com.andreiolar.abms.client.exception.EmailUnavailableException;
import com.andreiolar.abms.client.exception.NoReadingsFoundForDateException;
import com.andreiolar.abms.client.rpc.DBGetContactInfo;
import com.andreiolar.abms.client.rpc.DBGetContactInfoAsync;
import com.andreiolar.abms.client.rpc.DBGetReadingsForDate;
import com.andreiolar.abms.client.rpc.DBGetReadingsForDateAsync;
import com.andreiolar.abms.client.rpc.DBRegisterTenant;
import com.andreiolar.abms.client.rpc.DBRegisterTenantAsync;
import com.andreiolar.abms.client.rpc.DBRetreiveSubmittedComplaints;
import com.andreiolar.abms.client.rpc.DBRetreiveSubmittedComplaintsAsync;
import com.andreiolar.abms.client.view.AdminView;
import com.andreiolar.abms.client.widgets.ModalCreator;
import com.andreiolar.abms.shared.ContactInfo;
import com.andreiolar.abms.shared.SelfReading;
import com.andreiolar.abms.shared.SubmittedComplaint;
import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.addins.client.sideprofile.MaterialSideProfile;
import gwt.material.design.client.constants.ButtonType;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.FooterType;
import gwt.material.design.client.constants.IconPosition;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.constants.ModalType;
import gwt.material.design.client.constants.Position;
import gwt.material.design.client.constants.SideNavType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.data.SelectionType;
import gwt.material.design.client.data.component.RowComponent;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialCollapsible;
import gwt.material.design.client.ui.MaterialCollapsibleBody;
import gwt.material.design.client.ui.MaterialCollapsibleHeader;
import gwt.material.design.client.ui.MaterialCollapsibleItem;
import gwt.material.design.client.ui.MaterialContainer;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialFooter;
import gwt.material.design.client.ui.MaterialFooterCopyright;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialLoader;
import gwt.material.design.client.ui.MaterialModal;
import gwt.material.design.client.ui.MaterialModalContent;
import gwt.material.design.client.ui.MaterialModalFooter;
import gwt.material.design.client.ui.MaterialNavBar;
import gwt.material.design.client.ui.MaterialNavBrand;
import gwt.material.design.client.ui.MaterialNavSection;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialSideNav;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialTitle;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.client.ui.MaterialTooltip;
import gwt.material.design.client.ui.html.Anchor;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Header;
import gwt.material.design.client.ui.html.Hr;
import gwt.material.design.client.ui.html.UnorderedList;
import gwt.material.design.client.ui.table.MaterialDataTable;
import gwt.material.design.client.ui.table.cell.TextColumn;
import gwt.material.design.client.ui.table.cell.WidgetColumn;

public class AdminPanel extends Composite implements AdminView {

	private MaterialContainer container;

	private Presenter presenter;

	private UserDetails userDetails;

	public AdminPanel() {
		String sid = Cookies.getCookie("sid");
		if (sid == null) {
			int cookieDuration = 5000;
			Date expires = new Date(System.currentTimeMillis() + cookieDuration);
			Cookies.setCookie("sessionExpired", "Session expired. Please log in again.", expires, null, "/", false);

			Window.Location.replace(GWT.getHostPageBaseURL());
		}

		setUserInfo();

		if (userDetails == null) {
			int cookieDuration = 5000;
			Date expires = new Date(System.currentTimeMillis() + cookieDuration);
			Cookies.setCookie("badUserInfo", "Something bad happened. Please log in again.", expires, null, "/", false);

			Window.Location.replace(GWT.getHostPageBaseURL());
		}

		container = new MaterialContainer();
		container.setFontSize("1em");
		container.addStyleName("main-container");

		Widget mainMenu = createMenu();
		initWidget(mainMenu);
	}

	private Widget createMenu() {
		HTMLPanel htmlPanel = new HTMLPanel("");

		Header header = new Header();

		/** Creating the NavBar **/
		MaterialNavBar materialNavBar = new MaterialNavBar();
		materialNavBar.setActivates("sideNav");
		materialNavBar.setPaddingLeft(20.0);
		materialNavBar.setBackgroundColor(Color.BLUE);

		MaterialNavBrand materialNavBrand = new MaterialNavBrand();
		materialNavBrand.setText("ABMS");
		materialNavBrand.getElement().getStyle().setCursor(Cursor.POINTER);
		materialNavBrand.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				container.clear();
				MaterialImage backgroundImage = new MaterialImage("images/icons/city_1.png");
				backgroundImage.setWidth("100%");
				backgroundImage.setHeight("100%");
				backgroundImage.setTop(0.0);
				backgroundImage.setBottom(0.0);
				backgroundImage.setOpacity(0.5);
				container.add(backgroundImage);
			}
		});

		Anchor a = new Anchor();
		a.add(materialNavBrand);

		materialNavBar.add(a);

		MaterialNavSection materialNavSection = new MaterialNavSection();
		materialNavSection.setFloat(Float.RIGHT);

		/** Log Out **/
		MaterialLink logoutLink = new MaterialLink();
		logoutLink.setIconType(IconType.POWER_SETTINGS_NEW);
		logoutLink.setIconPosition(IconPosition.NONE);
		logoutLink.setMarginRight(10.0);
		logoutLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				userDetails = null;
				Cookies.removeCookie("sid");
				Window.Location.replace(GWT.getHostPageBaseURL());
			}
		});
		MaterialTooltip logoutTooltip = new MaterialTooltip(logoutLink, "Log Out");
		logoutTooltip.setPosition(Position.BOTTOM);

		/** Help **/
		MaterialLink aboutLink = new MaterialLink();
		aboutLink.setIconType(IconType.HELP_OUTLINE);
		aboutLink.setIconPosition(IconPosition.NONE);
		aboutLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				try {
					new RequestBuilder(RequestBuilder.GET, "pages/about.html").sendRequest("", new RequestCallback() {
						@Override
						public void onResponseReceived(Request req, Response resp) {
							HTML html = new HTML(resp.getText());
							MaterialModal aboutModal = ModalCreator.createWidgetModal("About", html);
							RootPanel.get().add(aboutModal);
							aboutModal.open();
						}

						@Override
						public void onError(Request res, Throwable throwable) {
							MaterialModal errorModal = ModalCreator.createModal(throwable);
							RootPanel.get().add(errorModal);
							errorModal.open();
						}
					});
				} catch (RequestException e) {
					MaterialModal errorModal = ModalCreator.createModal(e);
					RootPanel.get().add(errorModal);
					errorModal.open();
				}
			}
		});
		MaterialTooltip helpTooltip = new MaterialTooltip(aboutLink, "About");
		helpTooltip.setPosition(Position.BOTTOM);

		/** Institution Information **/
		MaterialLink instLink = new MaterialLink();
		instLink.setIconType(IconType.INFO_OUTLINE);
		instLink.setIconPosition(IconPosition.NONE);
		instLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				try {
					new RequestBuilder(RequestBuilder.GET, "pages/institution_information.html").sendRequest("", new RequestCallback() {
						@Override
						public void onResponseReceived(Request req, Response resp) {
							HTML html = new HTML(resp.getText());
							MaterialModal aboutModal = ModalCreator.createWidgetModal("Institution Information", html);
							RootPanel.get().add(aboutModal);
							aboutModal.open();
						}

						@Override
						public void onError(Request res, Throwable throwable) {
							MaterialModal errorModal = ModalCreator.createModal(throwable);
							RootPanel.get().add(errorModal);
							errorModal.open();
						}
					});
				} catch (RequestException e) {
					MaterialModal errorModal = ModalCreator.createModal(e);
					RootPanel.get().add(errorModal);
					errorModal.open();
				}
			}
		});
		MaterialTooltip instTooltip = new MaterialTooltip(instLink, "Institution Information");
		instTooltip.setPosition(Position.BOTTOM);

		/** Contact Information **/
		MaterialLink contactLink = new MaterialLink();
		contactLink.setIconType(IconType.CONTACT_PHONE);
		contactLink.setIconPosition(IconPosition.NONE);
		contactLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				try {
					new RequestBuilder(RequestBuilder.GET, "pages/contact_info.html").sendRequest("", new RequestCallback() {
						@Override
						public void onResponseReceived(Request req, Response resp) {
							HTML html = new HTML(resp.getText());
							MaterialModal contactInformationModal = ModalCreator.createWidgetModal("Contact Information", html);
							contactInformationModal.setWidth("400px");
							RootPanel.get().add(contactInformationModal);
							contactInformationModal.open();
						}

						@Override
						public void onError(Request res, Throwable throwable) {
							MaterialModal errorModal = ModalCreator.createModal(throwable);
							RootPanel.get().add(errorModal);
							errorModal.open();
						}
					});
				} catch (RequestException e) {
					MaterialModal errorModal = ModalCreator.createModal(e);
					RootPanel.get().add(errorModal);
					errorModal.open();
				}
			}
		});
		MaterialTooltip contactTooltip = new MaterialTooltip(contactLink, "Contact Information");
		contactTooltip.setPosition(Position.BOTTOM);

		/** Refresh **/
		MaterialLink homeLink = new MaterialLink();
		homeLink.setIconType(IconType.HOME);
		homeLink.setIconPosition(IconPosition.NONE);
		homeLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				container.clear();
				MaterialImage backgroundImage = new MaterialImage("images/icons/city_1.png");
				backgroundImage.setWidth("100%");
				backgroundImage.setHeight("100%");
				backgroundImage.setTop(0.0);
				backgroundImage.setBottom(0.0);
				backgroundImage.setOpacity(0.5);
				container.add(backgroundImage);
			}
		});
		MaterialTooltip homeTooltip = new MaterialTooltip(homeLink, "Home");
		homeTooltip.setPosition(Position.BOTTOM);

		materialNavSection.add(homeLink);
		materialNavSection.add(contactLink);
		materialNavSection.add(instLink);
		materialNavSection.add(aboutLink);
		materialNavSection.add(logoutLink);

		materialNavBar.add(materialNavSection);

		header.add(materialNavBar);

		/** Creating the SideNav **/
		MaterialSideNav materialSideNav = new MaterialSideNav();
		materialSideNav.setType(SideNavType.FIXED);
		materialSideNav.setId("sideNav");
		materialSideNav.setCloseOnClick(false);
		materialSideNav.setWidth(280);
		materialSideNav.setBackgroundColor(Color.GREY_LIGHTEN_5);

		MaterialSideProfile profile = new MaterialSideProfile();
		profile.setUrl("images/icons/profile.png");

		String profilePictureUsername = userDetails.getUsername().replaceAll("\\.", "");

		MaterialImage materialImage = new MaterialImage();
		materialImage.setUrl("http://res.cloudinary.com/andreiolar/image/upload/" + profilePictureUsername + ".png");
		materialImage.addErrorHandler(new ErrorHandler() {

			@Override
			public void onError(ErrorEvent event) {
				if (userDetails.getGender().equals("Female")) {
					materialImage.setUrl("images/icons/female.png");
				} else {
					materialImage.setUrl("images/icons/male.png");
				}
			}
		});

		profile.add(materialImage);

		MaterialLabel label = new MaterialLabel("Logged in as: Administrator");
		label.setTextColor(Color.WHITE);
		profile.add(label);

		MaterialLink link = new MaterialLink();
		link.setText(userDetails.getUsername());
		link.setActivates("dropProfile");
		link.setIconType(IconType.ARROW_DROP_DOWN);
		link.setIconPosition(IconPosition.RIGHT);
		link.setTextColor(Color.WHITE);
		profile.add(link);

		materialSideNav.add(profile);

		/** Complaints **/
		MaterialLink complaintsLink = new MaterialLink();
		complaintsLink.setText("View Submitted Complaints");
		complaintsLink.setTextColor(Color.BLUE);
		complaintsLink.setIconType(IconType.SENTIMENT_VERY_DISSATISFIED);
		complaintsLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				constructComplaintsWidget();
			}
		});
		materialSideNav.add(complaintsLink);

		header.add(materialSideNav);

		/** Administration **/
		MaterialCollapsible administrationCollapsible = new MaterialCollapsible();
		MaterialCollapsibleItem administrationCollapsibleItem = new MaterialCollapsibleItem();
		MaterialCollapsibleHeader administrationCollapsibleHeader = new MaterialCollapsibleHeader();
		administrationCollapsibleHeader.setWaves(WavesType.DEFAULT);

		MaterialLink administrationLink = new MaterialLink();
		administrationLink.setText("Administration");
		administrationLink.setIconType(IconType.LOCATION_CITY);
		administrationLink.setTextColor(Color.BLUE);
		administrationCollapsibleHeader.add(administrationLink);
		administrationCollapsibleItem.add(administrationCollapsibleHeader);

		MaterialCollapsibleBody administrationBody = new MaterialCollapsibleBody();
		UnorderedList administrationListItems = new UnorderedList();

		/** Contact Information **/
		MaterialLink contactInformationLink = new MaterialLink();
		contactInformationLink.setText("Contact Information");
		contactInformationLink.setTextColor(Color.BLUE_DARKEN_2);
		contactInformationLink.setWaves(WavesType.DEFAULT);
		contactInformationLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				constructContactInformationWidget();
			}
		});
		administrationListItems.add(contactInformationLink);

		/** View Readings **/
		MaterialLink viewReadingsLink = new MaterialLink();
		viewReadingsLink.setText("View Readings");
		viewReadingsLink.setTextColor(Color.BLUE_DARKEN_2);
		viewReadingsLink.setWaves(WavesType.DEFAULT);
		viewReadingsLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				constructViewReadingsWidget();
			}
		});
		administrationListItems.add(viewReadingsLink);

		/** Add Tenants **/
		MaterialLink addTenantLink = new MaterialLink();
		addTenantLink.setText("Add Tenant");
		addTenantLink.setTextColor(Color.BLUE_DARKEN_2);
		addTenantLink.setWaves(WavesType.DEFAULT);
		addTenantLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				MaterialModal tenantModal = constructAddTenantModal();
				RootPanel.get().add(tenantModal);
				tenantModal.open();
			}
		});
		administrationListItems.add(addTenantLink);

		administrationBody.add(administrationListItems);
		administrationCollapsibleItem.add(administrationBody);
		administrationCollapsible.add(administrationCollapsibleItem);

		materialSideNav.add(administrationCollapsible);

		/** Dropdown **/
		MaterialDropDown dropDown = new MaterialDropDown("dropProfile");

		MaterialLink editAccountSettingsLink = new MaterialLink();
		editAccountSettingsLink.setText("View Profile Information");
		editAccountSettingsLink.setFontSize("1em");
		editAccountSettingsLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				container.clear();
			}
		});
		dropDown.add(editAccountSettingsLink);

		MaterialLink changePasswordLink = new MaterialLink();
		changePasswordLink.setText("Change Password");
		changePasswordLink.setFontSize("1em");
		changePasswordLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
			}
		});
		dropDown.add(changePasswordLink);

		MaterialLink changeProfilePictureLink = new MaterialLink();
		changeProfilePictureLink.setText("Change Profile Picture");
		changeProfilePictureLink.setFontSize("1em");
		changeProfilePictureLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
			}
		});
		dropDown.add(changeProfilePictureLink);

		header.add(dropDown);

		htmlPanel.add(header);

		/** Creating the main container **/
		MaterialImage backgroundImage = new MaterialImage("images/icons/city_1.png");
		backgroundImage.setWidth("100%");
		backgroundImage.setHeight("100%");
		backgroundImage.setTop(0.0);
		backgroundImage.setBottom(0.0);
		backgroundImage.setOpacity(0.5);
		container.add(backgroundImage);

		htmlPanel.add(container);

		/** Creating the footer **/
		MaterialFooter footer = new MaterialFooter();
		footer.setBackgroundColor(Color.BLUE);
		footer.setType(FooterType.FIXED);

		MaterialFooterCopyright copyright = new MaterialFooterCopyright();

		MaterialLabel copyrightLabel = new MaterialLabel();
		copyrightLabel.getElement().setInnerHTML("&copy; 2016-2017 Copyright Andrei Olar");

		copyright.add(copyrightLabel);
		footer.add(copyright);
		htmlPanel.add(footer);

		return htmlPanel;
	}

	protected MaterialModal constructAddTenantModal() {
		MaterialModal materialModal = new MaterialModal();
		materialModal.setType(ModalType.DEFAULT);
		materialModal.setDismissible(false);
		materialModal.setInDuration(500);
		materialModal.setOutDuration(500);

		MaterialModalContent materialModalContent = new MaterialModalContent();
		MaterialTitle materialTitle = new MaterialTitle("Add Tenant");
		materialTitle.setTextColor(Color.BLUE);
		materialTitle.setDescription("Please provide necessary information in order to add a tenant to be eligible for registration!");

		materialModalContent.add(materialTitle);

		// Content
		MaterialTextBox emailTextBox = new MaterialTextBox();
		emailTextBox.setMarginTop(25.0);
		emailTextBox.setType(InputType.EMAIL);
		emailTextBox.setPlaceholder("E-Mail");
		emailTextBox.setIconType(IconType.ACCOUNT_CIRCLE);
		materialModalContent.add(emailTextBox);

		MaterialTextBox aptNumberBox = new MaterialTextBox();
		aptNumberBox.setType(InputType.NUMBER);
		aptNumberBox.setPlaceholder("Apt. Number");
		aptNumberBox.setIconType(IconType.FORMAT_LIST_NUMBERED);
		materialModalContent.add(aptNumberBox);

		MaterialModalFooter materialModalFooter = new MaterialModalFooter();

		MaterialButton submitButton = new MaterialButton();
		submitButton.setText("Add");
		submitButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String email = emailTextBox.getText();
				String aptNumber = aptNumberBox.getText();

				boolean canProceed = true;

				if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.(?:[a-zA-Z]{2,6})$")) {
					canProceed = false;
					emailTextBox.setError("Not a valid E-Mail Address");
				} else {
					emailTextBox.setSuccess("");
				}

				if (!aptNumber.matches("[1-9][0-9]{0,1}")) {
					canProceed = false;
					aptNumberBox.setError("Not a valid apartment number");
				} else {
					aptNumberBox.setSuccess("");
				}

				if (canProceed) {
					MaterialLoader.showLoading(true);

					DBRegisterTenantAsync registerTenantRpc = (DBRegisterTenantAsync) GWT.create(DBRegisterTenant.class);
					ServiceDefTarget registerTenantTarget = (ServiceDefTarget) registerTenantRpc;
					String registerTenantUrl = GWT.getModuleBaseURL() + "DBRegisterTenantImpl";
					registerTenantTarget.setServiceEntryPoint(registerTenantUrl);

					registerTenantRpc.registerTenant(email, aptNumber, new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							MaterialLoader.showLoading(false);

							if (caught instanceof EmailUnavailableException) {
								emailTextBox.setError("E-Mail Address already in registration queue.");
							} else if (caught instanceof AptNumberUnavailableException) {
								aptNumberBox.setError("Aptartment Number already in use.");
							} else {
								MaterialModal errorModal = ModalCreator.createModal(caught);
								RootPanel.get().add(errorModal);
								errorModal.open();
							}
						}

						@Override
						public void onSuccess(Void result) {
							MaterialLoader.showLoading(false);
							MaterialToast.fireToast("Tenant successfully added to registration queue.");
							materialModal.close();
							RootPanel.get().remove(materialModal);
						}
					});
				}

			}
		});

		MaterialButton closeButton = new MaterialButton();
		closeButton.setText("Close");
		closeButton.setType(ButtonType.FLAT);
		closeButton.addClickHandler(h -> {
			materialModal.close();
			RootPanel.get().remove(materialModal);
		});

		materialModalFooter.add(submitButton);
		materialModalFooter.add(closeButton);
		materialModal.add(materialModalContent);
		materialModal.add(materialModalFooter);

		return materialModal;
	}

	protected void constructViewReadingsWidget() {
		container.clear();

		MaterialPanel panel = new MaterialPanel();
		MaterialPanel tablePanel = new MaterialPanel();

		MaterialLabel title = new MaterialLabel("View Readings");
		title.setTextColor(Color.BLUE);
		title.setTextAlign(TextAlign.CENTER);
		title.setFontSize("36px");
		title.setFontWeight(FontWeight.BOLD);
		panel.add(title);

		panel.add(new Hr());

		MaterialComboBox<String> months = new MaterialComboBox<String>();
		months.setWidth("36%");
		months.getElement().getStyle().setMarginRight(2, Unit.PCT);
		months.addItem("January");
		months.addItem("February");
		months.addItem("March");
		months.addItem("April");
		months.addItem("May");
		months.addItem("June");
		months.addItem("July");
		months.addItem("August");
		months.addItem("September");
		months.addItem("October");
		months.addItem("November");
		months.addItem("December");

		DateTimeFormat df = DateTimeFormat.getFormat("yyyy");
		String formattedDate = df.format(new Date());
		int year = Integer.parseInt(formattedDate);

		MaterialComboBox<String> years = new MaterialComboBox<String>();
		years.setWidth("36%");
		years.getElement().getStyle().setMarginRight(2, Unit.PCT);
		for (int i = year; i >= 2016; i--) {
			years.addItem(String.valueOf(i));
		}

		MaterialButton searchButton = new MaterialButton();
		searchButton.setText("Search");
		searchButton.setWidth("24%");
		searchButton.getElement().getStyle().setMarginTop(1, Unit.EM);
		searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String month = months.getSelectedValue();
				String year = years.getSelectedValue();

				MaterialLoader.showLoading(true);

				DBGetReadingsForDateAsync readingsRpc = (DBGetReadingsForDateAsync) GWT.create(DBGetReadingsForDate.class);
				ServiceDefTarget readingsTarget = (ServiceDefTarget) readingsRpc;
				String readingsUrl = GWT.getModuleBaseURL() + "DBGetReadingsForDateImpl";
				readingsTarget.setServiceEntryPoint(readingsUrl);

				readingsRpc.getReadingsForDate(month + " " + year, new AsyncCallback<List<SelfReading>>() {

					@Override
					public void onFailure(Throwable caught) {
						MaterialLoader.showLoading(false);
						if (caught instanceof NoReadingsFoundForDateException) {
							tablePanel.clear();

							MaterialPanel errorPanel = new MaterialPanel();
							errorPanel.addStyleName("no-readings-found-panel");

							MaterialLabel label = new MaterialLabel();
							label.setTextColor(Color.BLUE);
							label.setTextAlign(TextAlign.CENTER);
							label.setFontSize("18px");
							label.setText("No readings found for " + month + " " + year);

							errorPanel.add(label);
							tablePanel.add(errorPanel);
						} else {
							MaterialModal errorModal = ModalCreator.createModal(caught);
							RootPanel.get().add(errorModal);
							errorModal.open();
						}
					}

					@Override
					public void onSuccess(List<SelfReading> result) {
						MaterialLoader.showLoading(false);
						tablePanel.clear();

						MaterialDataTable<SelfReading> table = new MaterialDataTable<SelfReading>();
						table.setUseStickyHeader(true);
						table.setUseCategories(false);
						table.setUseRowExpansion(false);
						table.setSelectionType(SelectionType.NONE);
						table.setRedraw(true);
						table.setStyleName("readings-table");

						tablePanel.add(table);

						table.getTableTitle().setText("Readings for " + month + " " + year);

						table.addColumn(new TextColumn<SelfReading>() {

							@Override
							public String getHeaderWidth() {
								return "20%";
							}

							@Override
							public Comparator<? super RowComponent<SelfReading>> getSortComparator() {
								return (o1, o2) -> Integer.compare(Integer.parseInt(o1.getData().getAptNumber()),
										Integer.parseInt(o2.getData().getAptNumber()));
							}

							@Override
							public String getValue(SelfReading object) {
								return object.getAptNumber();
							}
						}, "Apt. Number");

						table.addColumn(new TextColumn<SelfReading>() {

							@Override
							public String getHeaderWidth() {
								return "20%";
							};

							@Override
							public Comparator<? super RowComponent<SelfReading>> getSortComparator() {
								return (o1, o2) -> Integer.compare(Integer.parseInt(o1.getData().getColdWater()),
										Integer.parseInt(o2.getData().getColdWater()));
							}

							@Override
							public String getValue(SelfReading object) {
								return object.getColdWater();
							}
						}, "Cold Water");

						table.addColumn(new TextColumn<SelfReading>() {

							@Override
							public String getHeaderWidth() {
								return "20%";
							};

							@Override
							public Comparator<? super RowComponent<SelfReading>> getSortComparator() {
								return (o1, o2) -> Integer.compare(Integer.parseInt(o1.getData().getHotWater()),
										Integer.parseInt(o2.getData().getHotWater()));
							}

							@Override
							public String getValue(SelfReading object) {
								return object.getHotWater();
							}
						}, "Hot Water");

						table.addColumn(new TextColumn<SelfReading>() {

							@Override
							public String getHeaderWidth() {
								return "20%";
							};

							@Override
							public Comparator<? super RowComponent<SelfReading>> getSortComparator() {
								return (o1, o2) -> Integer.compare(Integer.parseInt(o1.getData().getElectricity()),
										Integer.parseInt(o2.getData().getElectricity()));
							}

							@Override
							public String getValue(SelfReading object) {
								return object.getElectricity();
							}
						}, "Electricity");

						table.addColumn(new TextColumn<SelfReading>() {

							@Override
							public String getHeaderWidth() {
								return "20%";
							};

							@Override
							public Comparator<? super RowComponent<SelfReading>> getSortComparator() {
								return (o1, o2) -> Integer.compare(Integer.parseInt(o1.getData().getGaz()), Integer.parseInt(o2.getData().getGaz()));
							}

							@Override
							public String getValue(SelfReading object) {
								return object.getGaz();
							}
						}, "Gas");

						table.setRowData(0, result);
						table.setRowCount(result.size());
						table.refreshView();
					}
				});
			}
		});

		Div dateSelectionDiv = new Div();
		dateSelectionDiv.setDisplay(Display.FLEX);
		dateSelectionDiv.addStyleName("date-selection");
		dateSelectionDiv.add(months);
		dateSelectionDiv.add(years);
		dateSelectionDiv.add(searchButton);

		panel.add(dateSelectionDiv);
		panel.add(tablePanel);

		container.add(panel);
	}

	/**
	 * Used to construct the {@link MaterialDataTable} representing the Complaints Widget.
	 **/
	private void constructComplaintsWidget() {
		container.clear();

		MaterialPanel panel = new MaterialPanel();

		MaterialLabel title = new MaterialLabel("View Submitted Complaints");
		title.setTextColor(Color.BLUE);
		title.setTextAlign(TextAlign.CENTER);
		title.setFontSize("36px");
		title.setFontWeight(FontWeight.BOLD);
		panel.add(title);

		panel.add(new Hr());

		MaterialDataTable<SubmittedComplaint> table = new MaterialDataTable<SubmittedComplaint>();
		table.setUseStickyHeader(true);
		table.setUseCategories(false);
		table.setUseRowExpansion(false);
		table.setSelectionType(SelectionType.NONE);
		table.setRedraw(true);
		table.setStyleName("submitted-complaints-table");

		panel.add(table);

		container.add(panel);

		table.getTableTitle().setText("Submitted Complaints");

		table.addColumn(new TextColumn<SubmittedComplaint>() {

			@Override
			public String getHeaderWidth() {
				return "25%";
			}

			@Override
			public Comparator<? super RowComponent<SubmittedComplaint>> getSortComparator() {
				return (o1, o2) -> Integer.compare(o1.getData().getAptNumber(), o2.getData().getAptNumber());
			}

			@Override
			public String getValue(SubmittedComplaint object) {
				return String.valueOf(object.getAptNumber());
			}
		}, "Apt. Number");

		table.addColumn(new TextColumn<SubmittedComplaint>() {

			@Override
			public String getHeaderWidth() {
				return "25%";
			};

			@Override
			public java.util.Comparator<? super gwt.material.design.client.data.component.RowComponent<SubmittedComplaint>> getSortComparator() {
				return (o1, o2) -> Long.compare(o1.getData().getDateSubmitted().getTime(), o2.getData().getDateSubmitted().getTime());
			};

			@Override
			public String getValue(SubmittedComplaint object) {
				DateTimeFormat df = DateTimeFormat.getFormat("yyyy-MM-dd");
				String formattedDate = df.format(object.getDateSubmitted());

				return formattedDate;
			}
		}, "Date Submitted");

		table.addColumn(new TextColumn<SubmittedComplaint>() {

			@Override
			public String getHeaderWidth() {
				return "25%";
			};

			@Override
			public java.util.Comparator<? super gwt.material.design.client.data.component.RowComponent<SubmittedComplaint>> getSortComparator() {
				return (o1, o2) -> o1.getData().getComplaintTo().compareTo(o2.getData().getComplaintTo());
			};

			@Override
			public String getValue(SubmittedComplaint object) {
				return object.getComplaintTo();
			}
		}, "Complaint To");

		table.addColumn(new TextColumn<SubmittedComplaint>() {

			@Override
			public String getHeaderWidth() {
				return "25%";
			};

			@Override
			public boolean isSortable() {
				return false;
			}

			@Override
			public String getValue(SubmittedComplaint object) {
				return object.getPhoneNumber();
			}

		}, "Phone Number");

		MaterialLoader.showLoading(true);

		DBRetreiveSubmittedComplaintsAsync rpcService = (DBRetreiveSubmittedComplaintsAsync) GWT.create(DBRetreiveSubmittedComplaints.class);
		ServiceDefTarget target = (ServiceDefTarget) rpcService;
		String moduleRelativeURL = GWT.getModuleBaseURL() + "DBRetreiveSubmittedComplaintsImpl";
		target.setServiceEntryPoint(moduleRelativeURL);

		rpcService.retreiveSubmittedComplaints(new AsyncCallback<List<SubmittedComplaint>>() {

			@Override
			public void onSuccess(List<SubmittedComplaint> result) {
				MaterialLoader.showLoading(false);
				table.setRowData(0, result);
				table.setRowCount(result.size());
				table.refreshView();
			}

			@Override
			public void onFailure(Throwable caught) {
				MaterialLoader.showLoading(false);

				MaterialModal errorModal = ModalCreator.createModal(caught);
				RootPanel.get().add(errorModal);
				errorModal.open();
			}
		});
	}

	private void setUserInfo() {
		String token = History.getToken();
		String json = token.substring(token.indexOf(":") + 1, token.length());

		try {
			JSONObject userInfoObject;
			JSONValue jsonValue = JSONParser.parseStrict(json);
			if ((userInfoObject = jsonValue.isObject()) == null) {
				Window.alert("Error parsing JSON");
			}

			JSONValue firstName = userInfoObject.get("firstName");
			JSONValue lastName = userInfoObject.get("lastName");
			JSONValue dateOfBirth = userInfoObject.get("dateOfBirth");
			JSONValue email = userInfoObject.get("email");
			JSONValue mobileNumber = userInfoObject.get("mobileNumber");
			JSONValue gender = userInfoObject.get("gender");
			JSONValue address = userInfoObject.get("address");
			JSONValue city = userInfoObject.get("city");
			JSONValue country = userInfoObject.get("country");
			JSONValue personalNumber = userInfoObject.get("personalNumber");
			JSONValue idSeries = userInfoObject.get("idSeries");
			JSONValue apartmentNumber = userInfoObject.get("apartmentNumber");
			JSONValue username = userInfoObject.get("username");

			DateTimeFormat df = DateTimeFormat.getFormat("yyyy-MM-dd");
			Date date = df.parse(dateOfBirth.toString().replaceAll("\"", ""));

			userDetails = new UserDetails(firstName.toString().replaceAll("\"", ""), lastName.toString().replaceAll("\"", ""), date,
					email.toString().replaceAll("\"", ""), mobileNumber.toString().replaceAll("\"", ""), gender.toString().replaceAll("\"", ""),
					address.toString().replaceAll("\"", ""), city.toString().replaceAll("\"", ""), country.toString().replaceAll("\"", ""),
					personalNumber.toString().replaceAll("\"", ""), idSeries.toString().replaceAll("\"", ""),
					username.toString().replaceAll("\"", ""), null, apartmentNumber.toString().replaceAll("\"", ""));
		} catch (Exception e) {
			userDetails = null;
		}
	}

	/**
	 * Used to construct the {@link MaterialDataTable} representing the Contact Information Widget.
	 **/
	protected void constructContactInformationWidget() {
		container.clear();

		MaterialPanel panel = new MaterialPanel();

		MaterialLabel title = new MaterialLabel("Contact Information");
		title.setTextColor(Color.BLUE);
		title.setTextAlign(TextAlign.CENTER);
		title.setFontSize("36px");
		title.setFontWeight(FontWeight.BOLD);
		panel.add(title);

		panel.add(new Hr());

		MaterialDataTable<ContactInfo> table = new MaterialDataTable<ContactInfo>();
		table.setUseStickyHeader(true);
		table.setUseCategories(false);
		table.setUseRowExpansion(false);
		table.setSelectionType(SelectionType.NONE);
		table.setRedraw(true);
		table.setStyleName("contact-info-table");

		panel.add(table);

		container.add(panel);

		table.getTableTitle().setText("Neighbors Contact Information");

		table.addColumn(new WidgetColumn<ContactInfo, MaterialImage>() {

			@Override
			public MaterialImage getValue(ContactInfo object) {
				String profilePictureUsername = object.getUsername().replaceAll("\\.", "");

				MaterialImage materialImage = new MaterialImage();
				materialImage.setUrl("http://res.cloudinary.com/andreiolar/image/upload/" + profilePictureUsername + ".png");
				materialImage.addErrorHandler(new ErrorHandler() {

					@Override
					public void onError(ErrorEvent event) {
						if (object.getGender().equals("Female")) {
							materialImage.setUrl("images/icons/female.png");
						} else {
							materialImage.setUrl("images/icons/male.png");
						}

					}
				});

				materialImage.setWidth("40px");
				materialImage.setHeight("40px");
				materialImage.setPadding(4);
				materialImage.setMarginTop(8);
				materialImage.setBackgroundColor(Color.GREY_LIGHTEN_2);
				materialImage.setCircle(true);

				return materialImage;
			}

			@Override
			public String getHeaderWidth() {
				return "10%";
			}

		});

		table.addColumn(new TextColumn<ContactInfo>() {

			@Override
			public String getHeaderWidth() {
				return "10%";
			}

			@Override
			public Comparator<? super RowComponent<ContactInfo>> getSortComparator() {
				return (o1, o2) -> o1.getData().getFamilyName().compareTo(o2.getData().getFamilyName());
			}

			@Override
			public String getValue(ContactInfo object) {
				return object.getFamilyName();
			}
		}, "Family Name");

		table.addColumn(new TextColumn<ContactInfo>() {

			@Override
			public String getHeaderWidth() {
				return "20%";
			}

			@Override
			public Comparator<? super RowComponent<ContactInfo>> getSortComparator() {
				return (o1, o2) -> o1.getData().getContactPerson().compareTo(o2.getData().getContactPerson());
			}

			@Override
			public String getValue(ContactInfo object) {
				return object.getContactPerson();
			}
		}, "Contact Person");

		table.addColumn(new TextColumn<ContactInfo>() {

			@Override
			public String getHeaderWidth() {
				return "10%";
			}

			@Override
			public Comparator<? super RowComponent<ContactInfo>> getSortComparator() {
				return (o1, o2) -> Integer.compare(Integer.parseInt(o1.getData().getApartmentNumber()),
						Integer.parseInt(o2.getData().getApartmentNumber()));
			}

			@Override
			public String getValue(ContactInfo object) {
				return object.getApartmentNumber();
			}
		}, "Apt. Number");

		table.addColumn(new TextColumn<ContactInfo>() {

			@Override
			public String getHeaderWidth() {
				return "20%";
			}

			@Override
			public Comparator<? super RowComponent<ContactInfo>> getSortComparator() {
				return (o1, o2) -> o1.getData().getEmail().compareTo(o2.getData().getEmail());
			}

			@Override
			public String getValue(ContactInfo object) {
				return object.getEmail();
			}
		}, "E-Mail Address");

		table.addColumn(new TextColumn<ContactInfo>() {

			@Override
			public String getHeaderWidth() {
				return "10%";
			}

			@Override
			public Comparator<? super RowComponent<ContactInfo>> getSortComparator() {
				return (o1, o2) -> o1.getData().getPhoneNumber().compareTo(o2.getData().getPhoneNumber());
			}

			@Override
			public String getValue(ContactInfo object) {
				return object.getPhoneNumber();
			}
		}, "Phone Number");

		table.addColumn(new WidgetColumn<ContactInfo, MaterialButton>() {

			@Override
			public String getHeaderWidth() {
				return "20%";
			}

			@Override
			public MaterialButton getValue(ContactInfo object) {
				if (object.getUsername().equals(userDetails.getUsername())) {
					MaterialButton sendMessageButton = new MaterialButton();
					sendMessageButton.setType(ButtonType.FLAT);

					return sendMessageButton;
				}

				MaterialButton sendMessageButton = new MaterialButton();
				sendMessageButton.setText("SEND MESSAGE");
				sendMessageButton.setTextColor(Color.BLUE);
				sendMessageButton.setType(ButtonType.FLAT);
				sendMessageButton.getElement().getStyle().setTextDecoration(TextDecoration.UNDERLINE);

				sendMessageButton.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						Window.alert("Test: " + object.getContactPerson());
					}
				});

				return sendMessageButton;
			}
		});

		MaterialLoader.showLoading(true);
		DBGetContactInfoAsync rpcService = (DBGetContactInfoAsync) GWT.create(DBGetContactInfo.class);
		ServiceDefTarget target = (ServiceDefTarget) rpcService;
		String moduleRelativeURL = GWT.getModuleBaseURL() + "DBGetContactInfoImpl";
		target.setServiceEntryPoint(moduleRelativeURL);

		rpcService.getContacts(new AsyncCallback<List<ContactInfo>>() {

			@Override
			public void onSuccess(List<ContactInfo> result) {
				MaterialLoader.showLoading(false);
				table.setRowData(0, result);
				table.setRowCount(result.size());
				table.refreshView();
			}

			@Override
			public void onFailure(Throwable caught) {
				MaterialLoader.showLoading(false);
				MaterialToast.fireToast("Unable to retrieve neighbors contact information.", "rounded");
			}
		});
	}

	@Override
	public void setName(String username) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setPresenter(Presenter presenter) {
		// TODO Auto-generated method stub
	}

}
