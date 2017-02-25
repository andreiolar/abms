package com.andreiolar.abms.client.widgets;

import com.andreiolar.abms.shared.ConversationDetails;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;

public class ConversationDetail extends Composite implements CustomWidget {

	private ConversationDetails conversationDetails;

	public ConversationDetail(ConversationDetails conversationDetails) {
		this.conversationDetails = conversationDetails;

		initWidget(initializeWidget());
	}

	@Override
	public Widget initializeWidget() {
		MaterialPanel panel = new MaterialPanel();
		panel.addStyleName("border-bottom");
		panel.setDisplay(Display.FLEX);
		panel.getElement().getStyle().setCursor(Cursor.POINTER);

		// Picture
		String profilePictureUsername = conversationDetails.getConversationWith().replaceAll("\\.", "");

		MaterialImage materialImage = new MaterialImage();
		materialImage.setUrl("http://res.cloudinary.com/andreiolar/image/upload/" + profilePictureUsername + ".png");
		materialImage.addErrorHandler(new ErrorHandler() {

			@Override
			public void onError(ErrorEvent event) {
				if (conversationDetails.getConversationWithGender().equals("Female")) {
					materialImage.setUrl("images/icons/female.png");
				} else {
					materialImage.setUrl("images/icons/male.png");
				}

			}
		});

		materialImage.setWidth("40px");
		materialImage.setHeight("40px");
		materialImage.setPadding(4);
		materialImage.setMarginTop(12);
		materialImage.setMarginLeft(12);
		materialImage.setMarginBottom(12);
		materialImage.setBackgroundColor(Color.GREY_LIGHTEN_2);
		materialImage.setCircle(true);

		// Panel with first name, last name and reply
		MaterialPanel subPanel = new MaterialPanel();

		MaterialLabel conversationWithLabel = new MaterialLabel();
		conversationWithLabel.setText(conversationDetails.getConversationWithFirstName() + " " + conversationDetails.getConversationWithLastName());
		conversationWithLabel.setTextColor(Color.BLACK);
		conversationWithLabel.setFontWeight(FontWeight.BOLD);
		conversationWithLabel.setMarginLeft(12);
		conversationWithLabel.setMarginTop(15);
		subPanel.add(conversationWithLabel);

		MaterialLabel text = new MaterialLabel();
		text.setText(conversationDetails.getLastMessage());
		text.setMarginLeft(12);
		text.setTextColor(Color.WHITE);
		subPanel.add(text);

		panel.add(materialImage);
		panel.add(subPanel);

		return panel;
	}

}
