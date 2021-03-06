package com.andreiolar.abms.client.widgets;

import java.util.Date;

import com.andreiolar.abms.client.utils.DateUtil;
import com.andreiolar.abms.shared.ConversationDetails;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;

public class ConversationDetail extends Composite implements CustomWidget, HasClickHandlers {

	private ConversationDetails conversationDetails;
	private boolean selected;

	MaterialPanel panel = new MaterialPanel();
	MaterialLabel conversationWithLabel = new MaterialLabel();
	MaterialLabel text = new MaterialLabel();
	MaterialLabel dateLabel = new MaterialLabel();

	public ConversationDetail(ConversationDetails conversationDetails, boolean selected) {
		this.conversationDetails = conversationDetails;
		this.selected = selected;

		initWidget(initializeWidget());
	}

	@Override
	public Widget initializeWidget() {
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
		subPanel.setWidth("50%");

		conversationWithLabel.setText(conversationDetails.getConversationWithFirstName() + " " + conversationDetails.getConversationWithLastName());
		conversationWithLabel.setTextColor(Color.BLACK);
		conversationWithLabel.setFontWeight(FontWeight.BOLD);
		conversationWithLabel.setMarginLeft(12);
		conversationWithLabel.setMarginTop(15);
		subPanel.add(conversationWithLabel);

		text.setText(conversationDetails.getLastMessage());
		text.setMarginLeft(12);
		text.setTextColor(Color.WHITE);
		subPanel.add(text);

		Date conversationDate = DateUtil.getConversationDate(conversationDetails.getLastMessageDate());
		String displayDate = DateUtil.getConversationDisplayDate(conversationDate);

		dateLabel.setText(displayDate);
		dateLabel.setTextColor(Color.WHITE);
		dateLabel.getElement().getStyle().setMarginLeft(10, Unit.PCT);
		dateLabel.setMarginTop(35);

		panel.add(materialImage);
		panel.add(subPanel);
		panel.add(dateLabel);

		panel.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				panel.setBackgroundColor(Color.GREY_LIGHTEN_4);
				conversationWithLabel.setTextColor(Color.BLUE);
				text.setTextColor(Color.BLACK);
				dateLabel.setTextColor(Color.BLACK);
			}
		});

		panel.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				if (!selected) {
					panel.setBackgroundColor(Color.BLUE);
					conversationWithLabel.setTextColor(Color.BLACK);
					text.setTextColor(Color.WHITE);
					dateLabel.setTextColor(Color.WHITE);
				}

			}
		});

		if (selected) {
			panel.setBackgroundColor(Color.GREY_LIGHTEN_4);
			conversationWithLabel.setTextColor(Color.BLUE);
			text.setTextColor(Color.BLACK);
			dateLabel.setTextColor(Color.BLACK);
		} else {
			panel.setBackgroundColor(Color.BLUE);
			conversationWithLabel.setTextColor(Color.BLACK);
			text.setTextColor(Color.WHITE);
			dateLabel.setTextColor(Color.WHITE);
		}

		return panel;
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addDomHandler(handler, ClickEvent.getType());
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;

		if (selected) {
			panel.setBackgroundColor(Color.GREY_LIGHTEN_4);
			conversationWithLabel.setTextColor(Color.BLUE);
			text.setTextColor(Color.BLACK);
			dateLabel.setTextColor(Color.BLACK);
		} else {
			panel.setBackgroundColor(Color.BLUE);
			conversationWithLabel.setTextColor(Color.BLACK);
			text.setTextColor(Color.WHITE);
			dateLabel.setTextColor(Color.WHITE);
		}
	}
}
