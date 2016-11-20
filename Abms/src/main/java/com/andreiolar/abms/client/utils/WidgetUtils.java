package com.andreiolar.abms.client.utils;

import com.andreiolar.abms.client.constants.DialogBoxConstants;
import com.andreiolar.abms.client.constants.UserMenuConstants;
import com.andreiolar.abms.client.widgets.CustomButton;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class WidgetUtils {

	public static Widget createDefaultPresentationWidget(String username) {
		VerticalPanel panel = new VerticalPanel();

		HTML title = new HTML("<p style=\"font-size:35px\"><i>APARTMENT BUILDING<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;MANAGEMENT SYSTEM</i></p>");
		HTML version = new HTML("<p style=\"font-size:20px\">Version: 1.0.0.00</p>");
		HTML loggedInAS = new HTML("<p style=\"font-size:20px\">Logged in as: " + username + "</p>");

		CustomButton hotline = new CustomButton();
		hotline.setText("Hotline");
		Image img = new Image();
		img.setUrl("images/icons/hotline.png");
		hotline.setResource(img);
		hotline.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				DialogBox dialogBox = DialogBoxCreator.createDialogBox(UserMenuConstants.DIALOG_BOX_HOTLINE_TITLE,
						UserMenuConstants.DIALOG_BOX_HOTLINE_MESSAGE, DialogBoxConstants.CLOSE_BUTTON, false, false);
				dialogBox.setGlassEnabled(true);
				dialogBox.setAnimationEnabled(true);
				dialogBox.center();
				dialogBox.show();
			}
		});

		HTML contactInfo = new HTML("<p style=\"font-size:20px\"><font color=\"red\">T:</font> +4 0740 215 030<br>"
				+ "<font color=\"red\">F:</font> +4 0356 442 536<br>olarandrei@gmail.com</p>");

		panel.add(title);
		panel.add(version);
		panel.add(loggedInAS);
		panel.add(hotline);
		panel.add(contactInfo);

		panel.setCellHorizontalAlignment(version, HasHorizontalAlignment.ALIGN_RIGHT);
		panel.setCellHorizontalAlignment(loggedInAS, HasHorizontalAlignment.ALIGN_CENTER);

		return panel;
	}

}
