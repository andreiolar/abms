package com.andreiolar.abms.client.view.impl;

import java.util.Date;

import com.andreiolar.abms.client.view.UserView;
import com.andreiolar.abms.client.widgets.ModalCreator;
import com.andreiolar.abms.shared.UserInfo;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.addins.client.sideprofile.MaterialSideProfile;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.FooterType;
import gwt.material.design.client.constants.IconPosition;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.Position;
import gwt.material.design.client.constants.SideNavType;
import gwt.material.design.client.ui.MaterialContainer;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialFooter;
import gwt.material.design.client.ui.MaterialFooterCopyright;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialModal;
import gwt.material.design.client.ui.MaterialNavBar;
import gwt.material.design.client.ui.MaterialNavBrand;
import gwt.material.design.client.ui.MaterialNavSection;
import gwt.material.design.client.ui.MaterialSideNav;
import gwt.material.design.client.ui.MaterialTooltip;
import gwt.material.design.client.ui.html.Anchor;
import gwt.material.design.client.ui.html.Header;

public class UserPanel extends Composite implements UserView {

	private MaterialContainer container;

	private Presenter presenter;

	private UserInfo userInfo;

	public UserPanel() {
		String sid = Cookies.getCookie("sid");
		if (sid == null) {
			int cookieDuration = 5000;
			Date expires = new Date(System.currentTimeMillis() + cookieDuration);
			Cookies.setCookie("sessionExpired", "Session expired. Please log in again.", expires, null, "/", false);

			Window.Location.replace(GWT.getHostPageBaseURL());
		}

		setUserInfo();

		if (userInfo == null) {
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

			userInfo = new UserInfo(firstName.toString().replaceAll("\"", ""), lastName.toString().replaceAll("\"", ""), date,
					email.toString().replaceAll("\"", ""), mobileNumber.toString().replaceAll("\"", ""), gender.toString().replaceAll("\"", ""),
					address.toString().replaceAll("\"", ""), city.toString().replaceAll("\"", ""), country.toString().replaceAll("\"", ""),
					personalNumber.toString().replaceAll("\"", ""), idSeries.toString().replaceAll("\"", ""),
					username.toString().replaceAll("\"", ""), null, apartmentNumber.toString().replaceAll("\"", ""));
		} catch (Exception e) {
			userInfo = null;
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
		MaterialLink searchLink = new MaterialLink();
		searchLink.setIconType(IconType.POWER_SETTINGS_NEW);
		searchLink.setIconPosition(IconPosition.NONE);
		searchLink.setMarginRight(10.0);
		searchLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				userInfo = null;
				Cookies.removeCookie("sid");
				Window.Location.replace(GWT.getHostPageBaseURL());
			}
		});
		MaterialTooltip seatchTooltip = new MaterialTooltip(searchLink, "Log Out");
		seatchTooltip.setPosition(Position.BOTTOM);

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
		materialNavSection.add(aboutLink);
		materialNavSection.add(searchLink);

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

		String url = "images/icons/male.png";
		if (userInfo.getGender().equals("Female")) {
			url = "images/icons/female.png";
		}

		MaterialImage materialImage = new MaterialImage(url);
		profile.add(materialImage);

		MaterialLabel label = new MaterialLabel("Logged in as: " + userInfo.getFirstName() + " " + userInfo.getLastName());
		label.setTextColor(Color.WHITE);
		profile.add(label);

		MaterialLink link = new MaterialLink();
		link.setText(userInfo.getUsername());
		link.setActivates("dropProfile");
		link.setIconType(IconType.ARROW_DROP_DOWN);
		link.setIconPosition(IconPosition.RIGHT);
		link.setTextColor(Color.WHITE);
		profile.add(link);

		materialSideNav.add(profile);

		MaterialLink complaintsLink = new MaterialLink();
		complaintsLink.setText("Complaints");
		complaintsLink.setTextColor(Color.BLUE);
		complaintsLink.setIconType(IconType.SENTIMENT_VERY_DISSATISFIED);
		complaintsLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				container.clear();
			}
		});
		materialSideNav.add(complaintsLink);

		header.add(materialSideNav);

		/** Dropdown **/
		MaterialDropDown dropDown = new MaterialDropDown("dropProfile");

		MaterialLink editAccountSettingsLink = new MaterialLink();
		editAccountSettingsLink.setText("Edit Account Settings");
		editAccountSettingsLink.setFontSize("1em");
		dropDown.add(editAccountSettingsLink);

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

	@Override
	public void setUsername(String username) {
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

}
