package com.andreiolar.abms.client.utils;

import com.andreiolar.abms.client.constants.DialogBoxConstants;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DialogBoxCreator {

	public static DialogBox createDialogBox(String title, String message, String buttonText, final boolean goToHomepage, final boolean reload) {
		// Create dialog box and set the caption text
		final DialogBox dialogBox = new DialogBox();
		dialogBox.ensureDebugId(DialogBoxConstants.DEBUG_ID);
		dialogBox.setText(title);

		// Create a table to layout the content
		VerticalPanel dialogContents = new VerticalPanel();
		dialogContents.setSpacing(6);
		dialogBox.setWidget(dialogContents);

		// Add text to the top of the dialog
		HTML details = new HTML(message);
		dialogContents.add(details);
		dialogContents.setCellHorizontalAlignment(details, HasHorizontalAlignment.ALIGN_CENTER);

		// Add a close button to the dialog
		Button closeButton = new Button();
		closeButton.setText(buttonText);
		closeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();

				if (goToHomepage) {
					Window.Location.replace("http://127.0.0.1:8888/AdministrareBloc.html");
				}

				if (reload) {
					Window.Location.reload();
				}
			}
		});

		dialogContents.add(closeButton);
		dialogContents.setCellHorizontalAlignment(closeButton, HasHorizontalAlignment.ALIGN_CENTER);

		// Return the dialog Box
		return dialogBox;
	}

	public static DialogBox createDialogBox(String title, SafeHtml message, String buttonText, final boolean goToHomepage, final boolean reload) {
		// Create dialog box and set the caption text
		final DialogBox dialogBox = new DialogBox();
		dialogBox.ensureDebugId(DialogBoxConstants.DEBUG_ID);
		dialogBox.setText(title);

		// Create a table to layout the content
		VerticalPanel dialogContents = new VerticalPanel();
		dialogContents.setSpacing(6);
		dialogBox.setWidget(dialogContents);

		// Add text to the top of the dialog
		HTML details = new HTML(message);
		dialogContents.add(details);
		dialogContents.setCellHorizontalAlignment(details, HasHorizontalAlignment.ALIGN_CENTER);

		// Add a close button to the dialog
		Button closeButton = new Button();
		closeButton.setText(buttonText);
		closeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();

				if (goToHomepage) {
					Window.Location.replace("http://127.0.0.1:8888/AdministrareBloc.html");
				}

				if (reload) {
					Window.Location.reload();
				}
			}
		});

		dialogContents.add(closeButton);
		dialogContents.setCellHorizontalAlignment(closeButton, HasHorizontalAlignment.ALIGN_CENTER);

		// Return the dialog Box
		return dialogBox;
	}

}
