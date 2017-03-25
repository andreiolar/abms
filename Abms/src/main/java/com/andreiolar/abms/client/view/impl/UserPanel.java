package com.andreiolar.abms.client.view.impl;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.andreiolar.abms.client.exception.OldPasswordNotCorrectException;
import com.andreiolar.abms.client.rpc.ChangePassword;
import com.andreiolar.abms.client.rpc.ChangePasswordAsync;
import com.andreiolar.abms.client.rpc.DBGetContactInfo;
import com.andreiolar.abms.client.rpc.DBGetContactInfoAsync;
import com.andreiolar.abms.client.view.UserView;
import com.andreiolar.abms.client.widgets.ComplaintsWidget;
import com.andreiolar.abms.client.widgets.ConsumptionWidget;
import com.andreiolar.abms.client.widgets.GeneralCostsWidget;
import com.andreiolar.abms.client.widgets.MessengerWidget;
import com.andreiolar.abms.client.widgets.ModalCreator;
import com.andreiolar.abms.client.widgets.PersonalCostsWidget;
import com.andreiolar.abms.client.widgets.ProfileInfoWidget;
import com.andreiolar.abms.client.widgets.VoteResultsWidget;
import com.andreiolar.abms.client.widgets.VoteWidget;
import com.andreiolar.abms.shared.ContactInfo;
import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.TextDecoration;
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

import gwt.material.design.addins.client.fileuploader.MaterialFileUploader;
import gwt.material.design.addins.client.fileuploader.MaterialUploadLabel;
import gwt.material.design.addins.client.fileuploader.base.UploadFile;
import gwt.material.design.addins.client.fileuploader.events.SuccessEvent;
import gwt.material.design.addins.client.fileuploader.events.SuccessEvent.SuccessHandler;
import gwt.material.design.addins.client.sideprofile.MaterialSideProfile;
import gwt.material.design.client.constants.ButtonType;
import gwt.material.design.client.constants.Color;
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
import gwt.material.design.client.ui.html.Header;
import gwt.material.design.client.ui.html.Hr;
import gwt.material.design.client.ui.html.UnorderedList;
import gwt.material.design.client.ui.table.MaterialDataTable;
import gwt.material.design.client.ui.table.cell.TextColumn;
import gwt.material.design.client.ui.table.cell.WidgetColumn;

public class UserPanel extends Composite implements UserView {

	private MaterialContainer container;

	private Presenter presenter;

	private UserDetails userDetails;

	public UserPanel() {
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

	private Widget createMenu() {
		HTMLPanel htmlPanel = new HTMLPanel("");

		Header header = new Header();

		/** Creating the NavBar **/
		MaterialNavBar materialNavBar = new MaterialNavBar();
		materialNavBar.setActivates("sideNav");
		materialNavBar.setPaddingLeft(20.0);
		materialNavBar.setBackgroundColor(Color.BLUE);

		MaterialNavBrand materialNavBrand = new MaterialNavBrand();
		// materialNavBrand.setHref("javascript:window.location.reload()");
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

		MaterialLabel label = new MaterialLabel("Logged in as: " + userDetails.getFirstName() + " " + userDetails.getLastName());
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
		complaintsLink.setText("Complaints");
		complaintsLink.setTextColor(Color.BLUE);
		complaintsLink.setIconType(IconType.SENTIMENT_VERY_DISSATISFIED);
		complaintsLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				container.clear();
				ComplaintsWidget widget = new ComplaintsWidget(userDetails);
				container.add(widget);
			}
		});
		materialSideNav.add(complaintsLink);

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

		/** Self Readings **/
		MaterialLink selfReadingsLink = new MaterialLink();
		selfReadingsLink.setText("Consumption Reading");
		selfReadingsLink.setTextColor(Color.BLUE_DARKEN_2);
		selfReadingsLink.setWaves(WavesType.DEFAULT);
		selfReadingsLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				container.clear();
				ConsumptionWidget consumptionWidget = new ConsumptionWidget(userDetails);
				container.add(consumptionWidget);
			}
		});
		administrationListItems.add(selfReadingsLink);

		/** General Costs View **/
		MaterialLink generalCostsViewLink = new MaterialLink();
		generalCostsViewLink.setText("General Costs View");
		generalCostsViewLink.setTextColor(Color.BLUE_DARKEN_2);
		generalCostsViewLink.setWaves(WavesType.DEFAULT);
		generalCostsViewLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				container.clear();
				GeneralCostsWidget generalCostWidget = new GeneralCostsWidget(userDetails);
				container.add(generalCostWidget);
			}
		});
		administrationListItems.add(generalCostsViewLink);

		/** Personal Costs View **/
		MaterialLink personalCostsViewLink = new MaterialLink();
		personalCostsViewLink.setText("Personal Costs View");
		personalCostsViewLink.setTextColor(Color.BLUE_DARKEN_2);
		personalCostsViewLink.setWaves(WavesType.DEFAULT);
		personalCostsViewLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				container.clear();
				PersonalCostsWidget personalCostsWidget = new PersonalCostsWidget(userDetails);
				container.add(personalCostsWidget);
			}
		});
		administrationListItems.add(personalCostsViewLink);

		administrationBody.add(administrationListItems);
		administrationCollapsibleItem.add(administrationBody);
		administrationCollapsible.add(administrationCollapsibleItem);

		materialSideNav.add(administrationCollapsible);

		/** Voting **/
		MaterialCollapsible votingCollapsible = new MaterialCollapsible();
		MaterialCollapsibleItem votingCollapsibleItem = new MaterialCollapsibleItem();
		MaterialCollapsibleHeader votingCollapsibleHeader = new MaterialCollapsibleHeader();
		votingCollapsibleHeader.setWaves(WavesType.DEFAULT);

		MaterialLink votingLink = new MaterialLink();
		votingLink.setText("Voting");
		votingLink.setIconType(IconType.ACCOUNT_BALANCE);
		votingLink.setTextColor(Color.BLUE);
		votingCollapsibleHeader.add(votingLink);
		votingCollapsibleItem.add(votingCollapsibleHeader);

		MaterialCollapsibleBody votingBody = new MaterialCollapsibleBody();
		UnorderedList votingListItems = new UnorderedList();

		/** Vote **/
		MaterialLink voteLink = new MaterialLink();
		voteLink.setText("Vote");
		voteLink.setTextColor(Color.BLUE_DARKEN_2);
		voteLink.setWaves(WavesType.DEFAULT);
		voteLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				container.clear();
				VoteWidget voteWidget = new VoteWidget(userDetails);
				container.add(voteWidget);
			}
		});
		votingListItems.add(voteLink);

		/** View Vote Results **/
		MaterialLink viewVoteResultsLink = new MaterialLink();
		viewVoteResultsLink.setText("View Vote Results");
		viewVoteResultsLink.setTextColor(Color.BLUE_DARKEN_2);
		viewVoteResultsLink.setWaves(WavesType.DEFAULT);
		viewVoteResultsLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				container.clear();
				VoteResultsWidget voteResultsWidget = new VoteResultsWidget(userDetails);
				container.add(voteResultsWidget);
			}
		});
		votingListItems.add(viewVoteResultsLink);

		votingBody.add(votingListItems);
		votingCollapsibleItem.add(votingBody);
		votingCollapsible.add(votingCollapsibleItem);

		materialSideNav.add(votingCollapsible);

		/** Messenger **/
		MaterialLink messengerLink = new MaterialLink();
		messengerLink.setText("Messenger");
		messengerLink.setTextColor(Color.BLUE);
		messengerLink.setIconType(IconType.MESSAGE);
		messengerLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				container.clear();
				MessengerWidget widget = new MessengerWidget(userDetails);
				container.add(widget);
			}
		});
		materialSideNav.add(messengerLink);

		/** Financial **/
		MaterialCollapsible financialCollapsible = new MaterialCollapsible();
		MaterialCollapsibleItem financialCollapsibleItem = new MaterialCollapsibleItem();
		MaterialCollapsibleHeader financialCollapsibleHeader = new MaterialCollapsibleHeader();
		financialCollapsibleHeader.setWaves(WavesType.DEFAULT);

		MaterialLink financialLink = new MaterialLink();
		financialLink.setText("Financial");
		financialLink.setIconType(IconType.EURO_SYMBOL);
		financialLink.setTextColor(Color.BLUE);
		financialCollapsibleHeader.add(financialLink);
		financialCollapsibleItem.add(financialCollapsibleHeader);

		MaterialCollapsibleBody financialBody = new MaterialCollapsibleBody();
		UnorderedList financialListItems = new UnorderedList();

		/** Pay **/
		MaterialLink payLink = new MaterialLink();
		payLink.setText("Pay");
		payLink.setTextColor(Color.BLUE_DARKEN_2);
		payLink.setWaves(WavesType.DEFAULT);
		payLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				container.clear();

			}
		});
		financialListItems.add(payLink);

		/** View Payment History **/
		MaterialLink viewPaymentHistoryLink = new MaterialLink();
		viewPaymentHistoryLink.setText("View Payment History");
		viewPaymentHistoryLink.setTextColor(Color.BLUE_DARKEN_2);
		viewPaymentHistoryLink.setWaves(WavesType.DEFAULT);
		viewPaymentHistoryLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				container.clear();

			}
		});
		financialListItems.add(viewPaymentHistoryLink);

		financialBody.add(financialListItems);
		financialCollapsibleItem.add(financialBody);
		financialCollapsible.add(financialCollapsibleItem);

		materialSideNav.add(financialCollapsible);

		header.add(materialSideNav);

		/** Dropdown **/
		MaterialDropDown dropDown = new MaterialDropDown("dropProfile");

		MaterialLink editAccountSettingsLink = new MaterialLink();
		editAccountSettingsLink.setText("View Profile Information");
		editAccountSettingsLink.setFontSize("1em");
		editAccountSettingsLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				container.clear();
				ProfileInfoWidget profileInfoWidget = new ProfileInfoWidget(userDetails);
				container.add(profileInfoWidget);
			}
		});
		dropDown.add(editAccountSettingsLink);

		MaterialLink changePasswordLink = new MaterialLink();
		changePasswordLink.setText("Change Password");
		changePasswordLink.setFontSize("1em");
		changePasswordLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				MaterialModal changePasswordModal = createChangePasswordModal();
				RootPanel.get().add(changePasswordModal);
				changePasswordModal.open();
			}
		});
		dropDown.add(changePasswordLink);

		MaterialLink changeProfilePictureLink = new MaterialLink();
		changeProfilePictureLink.setText("Change Profile Picture");
		changeProfilePictureLink.setFontSize("1em");
		changeProfilePictureLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				MaterialModal changeProfilePictureModal = createChangeProfilePictureModal(materialImage);
				RootPanel.get().add(changeProfilePictureModal);
				changeProfilePictureModal.open();
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

	protected MaterialModal createChangeProfilePictureModal(MaterialImage materialImage) {
		MaterialModal materialModal = new MaterialModal();
		materialModal.setType(ModalType.DEFAULT);
		materialModal.setDismissible(false);
		materialModal.setInDuration(500);
		materialModal.setOutDuration(500);

		MaterialModalContent materialModalContent = new MaterialModalContent();
		MaterialTitle materialTitle = new MaterialTitle("Change Profile Picture");
		materialTitle.setTextColor(Color.BLUE);
		materialTitle.setDescription("Please choose a new profile picture.");

		materialModalContent.add(materialTitle);

		// Content
		MaterialFileUploader fileUploader = new MaterialFileUploader();
		fileUploader.setUrl(GWT.getModuleBaseURL() + "uploader");
		fileUploader.setMaxFileSize(10);
		fileUploader.setShadow(1);
		fileUploader.addSuccessHandler(new SuccessHandler<UploadFile>() {

			@Override
			public void onSuccess(SuccessEvent<UploadFile> event) {
				materialModal.close();
				RootPanel.get().remove(materialModal);

				MaterialToast.fireToast("Profile picture uploaded successfully!", "rounded");

				String profilePictureUsername = userDetails.getUsername().replaceAll("\\.", "");

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
			}
		});

		fileUploader.addErrorHandler(new gwt.material.design.addins.client.fileuploader.events.ErrorEvent.ErrorHandler<UploadFile>() {

			@Override
			public void onError(gwt.material.design.addins.client.fileuploader.events.ErrorEvent<UploadFile> event) {
				MaterialToast.fireToast("Error uploading profile picture: " + event.getResponse().getCode() + ": " + event.getResponse().getMessage(),
						"rounded");
			}
		});

		MaterialUploadLabel label = new MaterialUploadLabel("Profile Picture Uploader", "Selcect or drag and drop files to upload.");

		fileUploader.add(label);
		materialModalContent.add(fileUploader);

		MaterialModalFooter materialModalFooter = new MaterialModalFooter();

		MaterialButton closeButton = new MaterialButton();
		closeButton.setText("Close");
		closeButton.setType(ButtonType.FLAT);
		closeButton.addClickHandler(h -> {
			materialModal.close();
			RootPanel.get().remove(materialModal);
		});

		materialModalFooter.add(closeButton);
		materialModal.add(materialModalContent);
		materialModal.add(materialModalFooter);

		return materialModal;
	}

	protected MaterialModal createChangePasswordModal() {
		MaterialModal materialModal = new MaterialModal();
		materialModal.setType(ModalType.DEFAULT);
		materialModal.setDismissible(false);
		materialModal.setInDuration(500);
		materialModal.setOutDuration(500);

		MaterialModalContent materialModalContent = new MaterialModalContent();
		MaterialTitle materialTitle = new MaterialTitle("Change Password");
		materialTitle.setTextColor(Color.BLUE);
		materialTitle.setDescription("Please provide necessary information in order to change your password!");

		materialModalContent.add(materialTitle);

		// Content
		MaterialTextBox oldPasswordBox = new MaterialTextBox();
		oldPasswordBox.setMarginTop(25.0);
		oldPasswordBox.setType(InputType.PASSWORD);
		oldPasswordBox.setPlaceholder("Old Password");
		oldPasswordBox.setLength(10);
		oldPasswordBox.setMaxLength(10);
		oldPasswordBox.setIconType(IconType.LOCK);
		materialModalContent.add(oldPasswordBox);

		MaterialTextBox newPasswordBox = new MaterialTextBox();
		newPasswordBox.setType(InputType.PASSWORD);
		newPasswordBox.setPlaceholder("New Password");
		newPasswordBox.setLength(10);
		newPasswordBox.setMaxLength(10);
		newPasswordBox.setIconType(IconType.LOCK);
		materialModalContent.add(newPasswordBox);

		MaterialTextBox repeatPasswordBox = new MaterialTextBox();
		repeatPasswordBox.setType(InputType.PASSWORD);
		repeatPasswordBox.setPlaceholder("Repeat New Password");
		repeatPasswordBox.setLength(10);
		repeatPasswordBox.setMaxLength(10);
		repeatPasswordBox.setIconType(IconType.LOCK);
		materialModalContent.add(repeatPasswordBox);

		MaterialModalFooter materialModalFooter = new MaterialModalFooter();

		MaterialButton submitButton = new MaterialButton();
		submitButton.setText("Change Password");
		submitButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String oldPassword = oldPasswordBox.getText();
				String newPassword = newPasswordBox.getText();
				String repeatedPassword = repeatPasswordBox.getText();

				boolean canProceed = true;

				if (!oldPassword.matches("[A-Za-z0-9]{5,10}")) {
					canProceed = false;
					oldPasswordBox.setError(
							"Password can only contain uppercase, lowercase letters and 0-9 digits. Password has to be between 5 and 10 characters long.");
				} else {
					oldPasswordBox.setSuccess("");
				}

				if (!newPassword.matches("[A-Za-z0-9]{5,10}")) {
					canProceed = false;
					newPasswordBox.setError(
							"Password can only contain uppercase, lowercase letters and 0-9 digits. Password has to be between 5 and 10 characters long.");
				} else {
					newPasswordBox.setSuccess("");
				}

				if (!repeatedPassword.matches("[A-Za-z0-9]{5,10}")) {
					canProceed = false;
					repeatPasswordBox.setError(
							"Password can only contain uppercase, lowercase letters and 0-9 digits. Password has to be between 5 and 10 characters long.");
				} else {
					repeatPasswordBox.setSuccess("");
				}

				if (canProceed) {
					if (newPassword.equals(repeatedPassword)) {
						submitButton.setEnabled(false);

						newPasswordBox.setSuccess("");
						repeatPasswordBox.setSuccess("");

						// Send to server
						ChangePasswordAsync changePasswordRpc = (ChangePasswordAsync) GWT.create(ChangePassword.class);
						ServiceDefTarget changePasswordTar = (ServiceDefTarget) changePasswordRpc;
						String changePasswordUrl = GWT.getModuleBaseURL() + "ChangePasswordImpl";
						changePasswordTar.setServiceEntryPoint(changePasswordUrl);

						changePasswordRpc.changePassword(userDetails.getUsername(), oldPassword, newPassword, new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
								submitButton.setEnabled(true);

								if (caught instanceof OldPasswordNotCorrectException) {
									oldPasswordBox.setError("Old password is not correct.");
								} else {
									materialModal.close();
									RootPanel.get().remove(materialModal);
									MaterialToast.fireToast("Error changing password: " + caught.getMessage() + ".", "rounded");
								}
							}

							@Override
							public void onSuccess(Void result) {
								submitButton.setEnabled(true);

								materialModal.close();
								RootPanel.get().remove(materialModal);

								Cookies.removeCookie("sid");

								int cookieDuration = 5000;
								Date expires = new Date(System.currentTimeMillis() + cookieDuration);
								Cookies.setCookie("passwordChanged", "Password changed successfully. Log In required.", expires, null, "/", false);

								Window.Location.replace(GWT.getHostPageBaseURL());
							}
						});

					} else {
						newPasswordBox.setError("Passwords do not match!");
						repeatPasswordBox.setError("Passwords do not match!");
					}
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
	public void setUsername(String username) {
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

}
