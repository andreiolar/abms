package com.andreiolar.abms.client.widgets;

import java.util.Date;

import com.andreiolar.abms.client.utils.DateUtil;
import com.andreiolar.abms.shared.ConversationMessage;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.addins.client.bubble.MaterialBubble;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Position;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialRow;

public class Message extends Composite implements CustomWidget {

	private ConversationMessage conversationMessage;
	private String loggedInUser;

	public Message(ConversationMessage conversationMessage, String loggedInUser) {
		this.conversationMessage = conversationMessage;
		this.loggedInUser = loggedInUser;

		initWidget(initializeWidget());
	}

	@Override
	public Widget initializeWidget() {
		MaterialRow row = new MaterialRow();
		row.setMarginBottom(0);

		MaterialImage profilePicture = new MaterialImage();
		profilePicture.setBackgroundColor(Color.GREY_LIGHTEN_3);
		if (loggedInUser.equals(conversationMessage.getUsername())) {
			profilePicture.setFloat(Float.RIGHT);
			profilePicture.setMarginLeft(12);
			profilePicture.setMarginRight(25);
		} else {
			profilePicture.setFloat(Float.LEFT);
			profilePicture.setMarginRight(12);
			profilePicture.setMarginLeft(25);
		}

		profilePicture.setMarginTop(8);
		profilePicture.setWidth("40px");
		profilePicture.setHeight("40px");
		profilePicture.setShadow(1);
		profilePicture.setCircle(true);
		String profilePictureUsername = conversationMessage.getUsername().replaceAll("\\.", "");
		profilePicture.setUrl("http://res.cloudinary.com/andreiolar/image/upload/" + profilePictureUsername + ".png");
		profilePicture.addErrorHandler(new ErrorHandler() {

			@Override
			public void onError(ErrorEvent event) {
				if (conversationMessage.getGender().equals("Female")) {
					profilePicture.setUrl("images/icons/female.png");
				} else {
					profilePicture.setUrl("images/icons/male.png");
				}

			}
		});

		MaterialBubble bubble = new MaterialBubble();
		if (loggedInUser.equals(conversationMessage.getUsername())) {
			bubble.setBackgroundColor(Color.BLUE);
			bubble.setPosition(Position.RIGHT);
			bubble.setFloat(Float.RIGHT);
		} else {
			bubble.setBackgroundColor(Color.WHITE);
			bubble.setPosition(Position.LEFT);
			bubble.setFloat(Float.LEFT);
		}

		MaterialLabel text = new MaterialLabel();
		text.setText(conversationMessage.getMessage());
		if (loggedInUser.equals(conversationMessage.getUsername())) {
			text.setTextColor(Color.WHITE);
		} else {
			text.setTextColor(Color.BLACK);
		}

		Date messageDate = DateUtil.getConversationDate(conversationMessage.getDate());
		String displayDate = DateUtil.getConversationDisplayDate(messageDate);

		MaterialLabel date = new MaterialLabel();
		date.setText(displayDate);
		date.setFloat(Float.RIGHT);
		date.setFontSize("0.6em");
		if (loggedInUser.equals(conversationMessage.getUsername())) {
			date.setTextColor(Color.WHITE);
		} else {
			date.setTextColor(Color.BLACK);
		}

		bubble.add(text);
		bubble.add(date);

		row.add(profilePicture);
		row.add(bubble);

		return row;
	}

}
