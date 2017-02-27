package com.andreiolar.abms.client.widgets;

import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Hr;

public class ConversationWithWidget extends Composite implements CustomWidget {

	private UserDetails userDetails;

	public ConversationWithWidget(UserDetails userDetails) {
		this.userDetails = userDetails;

		initWidget(initializeWidget());
	}

	@Override
	public Widget initializeWidget() {
		MaterialPanel mainPanel = new MaterialPanel();
		mainPanel.setTextAlign(TextAlign.CENTER);

		MaterialPanel headerPanel = new MaterialPanel();

		MaterialImage profilePicture = new MaterialImage();
		profilePicture.setMarginTop(30);
		profilePicture.setWidth("120px");
		profilePicture.setHeight("120px");
		profilePicture.setShadow(1);
		profilePicture.setCircle(true);
		String profilePictureUsername = userDetails.getUsername().replaceAll("\\.", "");
		profilePicture.setUrl("http://res.cloudinary.com/andreiolar/image/upload/" + profilePictureUsername + ".png");
		profilePicture.addErrorHandler(new ErrorHandler() {

			@Override
			public void onError(ErrorEvent event) {
				if (userDetails.getGender().equals("Female")) {
					profilePicture.setUrl("images/icons/female.png");
				} else {
					profilePicture.setUrl("images/icons/male.png");
				}

			}
		});
		headerPanel.add(profilePicture);

		MaterialLabel nameLabel = new MaterialLabel();
		nameLabel.setText(userDetails.getFirstName() + " " + userDetails.getLastName());
		nameLabel.setMarginTop(30.0);
		nameLabel.setTextColor(Color.BLUE);
		nameLabel.setFontWeight(FontWeight.BOLD);
		nameLabel.setFontSize("22px");
		headerPanel.add(nameLabel);

		Hr mainHr = new Hr();
		mainHr.setMarginTop(30.0);
		headerPanel.add(mainHr);

		MaterialPanel contentPanel = new MaterialPanel();
		contentPanel.addStyleName("conversation-with-panel");
		contentPanel.setMarginTop(30.0);

		MaterialPanel usernamePanel = new MaterialPanel();
		usernamePanel.setDisplay(Display.FLEX);

		MaterialLabel usernameLabel = new MaterialLabel();
		usernameLabel.setText("Username:");
		usernameLabel.setFontWeight(FontWeight.BOLD);
		usernameLabel.setTextColor(Color.BLUE);

		MaterialLabel usernameLabelValue = new MaterialLabel();
		usernameLabelValue.setText(userDetails.getUsername());
		usernameLabelValue.setTextColor(Color.GREY);
		usernameLabelValue.addStyleName("margin-left-auto");

		usernamePanel.add(usernameLabel);
		usernamePanel.add(usernameLabelValue);

		Hr usernameHr = new Hr();
		usernameHr.setMarginTop(15.0);
		usernameHr.setMarginBottom(15.0);

		MaterialPanel aptNumberPanel = new MaterialPanel();
		aptNumberPanel.setDisplay(Display.FLEX);

		MaterialLabel aptNumberLabel = new MaterialLabel();
		aptNumberLabel.setText("Apartment Number:");
		aptNumberLabel.setFontWeight(FontWeight.BOLD);
		aptNumberLabel.setTextColor(Color.BLUE);

		MaterialLabel aptNumberLabelValue = new MaterialLabel();
		aptNumberLabelValue.setText(userDetails.getApartmentNumber());
		aptNumberLabelValue.setTextColor(Color.GREY);
		aptNumberLabelValue.addStyleName("margin-left-auto");

		aptNumberPanel.add(aptNumberLabel);
		aptNumberPanel.add(aptNumberLabelValue);

		Hr aptNumberHr = new Hr();
		aptNumberHr.setMarginTop(15.0);
		aptNumberHr.setMarginBottom(15.0);

		MaterialPanel phonePanel = new MaterialPanel();
		phonePanel.setDisplay(Display.FLEX);

		MaterialLabel phoneLabel = new MaterialLabel();
		phoneLabel.setText("Phone Number:");
		phoneLabel.setFontWeight(FontWeight.BOLD);
		phoneLabel.setTextColor(Color.BLUE);

		MaterialLabel phoneLabelValue = new MaterialLabel();
		phoneLabelValue.setText(userDetails.getMobileNumber());
		phoneLabelValue.setTextColor(Color.GREY);
		phoneLabelValue.addStyleName("margin-left-auto");

		phonePanel.add(phoneLabel);
		phonePanel.add(phoneLabelValue);

		Hr phoneHr = new Hr();
		phoneHr.setMarginTop(15.0);
		phoneHr.setMarginBottom(15.0);

		MaterialPanel mailPanel = new MaterialPanel();
		mailPanel.setDisplay(Display.FLEX);

		MaterialLabel mailLabel = new MaterialLabel();
		mailLabel.setText("E-Mail:");
		mailLabel.setFontWeight(FontWeight.BOLD);
		mailLabel.setTextColor(Color.BLUE);

		MaterialLabel mailLabelValue = new MaterialLabel();
		mailLabelValue.setText(userDetails.getEmail());
		mailLabelValue.setTextColor(Color.GREY);
		mailLabelValue.addStyleName("margin-left-auto");

		mailPanel.add(mailLabel);
		mailPanel.add(mailLabelValue);

		Hr mailHr = new Hr();
		mailHr.setMarginTop(15.0);
		mailHr.setMarginBottom(15.0);

		MaterialPanel dobPanel = new MaterialPanel();
		dobPanel.setDisplay(Display.FLEX);

		MaterialLabel dobLabel = new MaterialLabel();
		dobLabel.setText("Date of Birth:");
		dobLabel.setFontWeight(FontWeight.BOLD);
		dobLabel.setTextColor(Color.BLUE);

		MaterialLabel dobLabelValue = new MaterialLabel();
		dobLabelValue.setText(userDetails.getDateOfBirth().toString());
		dobLabelValue.setTextColor(Color.GREY);
		dobLabelValue.addStyleName("margin-left-auto");

		dobPanel.add(dobLabel);
		dobPanel.add(dobLabelValue);

		Hr dobHr = new Hr();
		dobHr.setMarginTop(15.0);
		dobHr.setMarginBottom(15.0);

		MaterialPanel genderPanel = new MaterialPanel();
		genderPanel.setDisplay(Display.FLEX);

		MaterialLabel genderLabel = new MaterialLabel();
		genderLabel.setText("Gender:");
		genderLabel.setFontWeight(FontWeight.BOLD);
		genderLabel.setTextColor(Color.BLUE);

		MaterialLabel genderLabelValue = new MaterialLabel();
		genderLabelValue.setText(userDetails.getGender());
		genderLabelValue.setTextColor(Color.GREY);
		genderLabelValue.addStyleName("margin-left-auto");

		genderPanel.add(genderLabel);
		genderPanel.add(genderLabelValue);

		contentPanel.add(usernamePanel);
		contentPanel.add(usernameHr);
		contentPanel.add(aptNumberPanel);
		contentPanel.add(aptNumberHr);
		contentPanel.add(phonePanel);
		contentPanel.add(phoneHr);
		contentPanel.add(mailPanel);
		contentPanel.add(mailHr);
		contentPanel.add(dobPanel);
		contentPanel.add(dobHr);
		contentPanel.add(genderPanel);

		mainPanel.add(headerPanel);
		mainPanel.add(contentPanel);

		return mainPanel;
	}

}
