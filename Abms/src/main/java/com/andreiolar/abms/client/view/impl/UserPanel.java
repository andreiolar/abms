package com.andreiolar.abms.client.view.impl;

import java.util.Date;

import com.andreiolar.abms.client.view.UserView;
import com.andreiolar.abms.shared.UserInfo;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
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
import gwt.material.design.client.ui.MaterialNavBar;
import gwt.material.design.client.ui.MaterialNavBrand;
import gwt.material.design.client.ui.MaterialNavSection;
import gwt.material.design.client.ui.MaterialSideNav;
import gwt.material.design.client.ui.MaterialTooltip;
import gwt.material.design.client.ui.html.Header;

public class UserPanel extends Composite implements UserView {

	private MaterialContainer container;

	private Presenter presenter;

	private UserInfo userInfo;

	public UserPanel() {
		setUserInfo();

		if (userInfo == null) {
			Window.Location.replace(GWT.getHostPageBaseURL());
		}

		container = new MaterialContainer();
		container.setFontSize("1em");

		Widget mainMenu = createMenu();
		initWidget(mainMenu);
	}

	private void setUserInfo() {
		String token = History.getToken();
		String json = token.substring(token.indexOf(":") + 1, token.length());

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
				personalNumber.toString().replaceAll("\"", ""), idSeries.toString().replaceAll("\"", ""), username.toString().replaceAll("\"", ""),
				null, apartmentNumber.toString().replaceAll("\"", ""));
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

		materialNavBar.add(materialNavBrand);

		MaterialNavSection materialNavSection = new MaterialNavSection();
		materialNavSection.setFloat(Float.RIGHT);

		MaterialLink searchLink = new MaterialLink();
		searchLink.setIconType(IconType.POWER_SETTINGS_NEW);
		searchLink.setIconPosition(IconPosition.NONE);
		searchLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				userInfo = null;
				Window.Location.replace(GWT.getHostPageBaseURL());
			}
		});

		MaterialTooltip seatchTooltip = new MaterialTooltip(searchLink, "Log Out");
		seatchTooltip.setPosition(Position.BOTTOM);

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
		MaterialLabel demoLabel = new MaterialLabel("Demo Text");
		demoLabel.setTextColor(Color.BLACK);
		demoLabel.addStyleName("label");

		container.add(demoLabel);

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
