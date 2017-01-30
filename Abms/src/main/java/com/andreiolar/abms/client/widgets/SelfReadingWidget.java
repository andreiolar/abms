package com.andreiolar.abms.client.widgets;

import com.andreiolar.abms.client.constants.DialogBoxConstants;
import com.andreiolar.abms.client.constants.UserMenuConstants;
import com.andreiolar.abms.client.rpc.DBSelfReading;
import com.andreiolar.abms.client.rpc.DBSelfReadingAsync;
import com.andreiolar.abms.client.utils.DialogBoxCreator;
import com.andreiolar.abms.shared.SelfReading;
import com.andreiolar.abms.shared.UserInfo;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SelfReadingWidget extends Composite implements CustomWidget {

	private String previousMonth;
	private String currentYear;

	private UserInfo userInfo;

	public SelfReadingWidget(String previousMonth, String currentYear, UserInfo userInfo) {
		this.previousMonth = previousMonth;
		this.currentYear = currentYear;
		this.userInfo = userInfo;

		initWidget(initializeWidget());
	}

	@Override
	public Widget initializeWidget() {
		VerticalPanel panel = new VerticalPanel();

		HTML description = new HTML("" + "<p>" + "Va rugam dati citirea aferenta lunii " + previousMonth + ".</p>");

		Grid grid = new Grid(5, 2);

		// Apa rece
		Label coldWaterLabel = new Label("Apa Rece");
		final TextBox coldWaterBox = new TextBox();

		// Apa calda
		Label hotWaterLabel = new Label("Apa Calda");
		final TextBox hotWaterBox = new TextBox();

		// Curent
		Label electricityLabel = new Label("Curent");
		final TextBox electicityBox = new TextBox();

		// Gaz
		Label gazLabel = new Label("Gaz");
		final TextBox gazBox = new TextBox();

		// Submit Button
		Button submitButton = new Button();
		submitButton.setText("Submit");
		submitButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				String coldWater = coldWaterBox.getText();
				String hotWater = hotWaterBox.getText();
				String electricity = electicityBox.getText();
				String gaz = gazBox.getText();

				if ((coldWater != null && !coldWater.trim().equals("")) && (hotWater != null && !hotWater.trim().equals(""))
						&& (electricity != null && !electricity.trim().equals("")) && (gaz != null && !gaz.trim().equals(""))) {
					SelfReading reading = new SelfReading(userInfo.getApartmentNumber(), coldWater, hotWater, electricity, gaz,
							previousMonth + "-" + currentYear);

					DOM.getElementById("loading").getStyle().setDisplay(Display.BLOCK);
					DBSelfReadingAsync rpcService = (DBSelfReadingAsync) GWT.create(DBSelfReading.class);
					ServiceDefTarget target = (ServiceDefTarget) rpcService;
					String moduleRelativeURL = GWT.getModuleBaseURL() + "DBSelfReadingImpl";
					target.setServiceEntryPoint(moduleRelativeURL);

					// rpcService.insertReading(userInfo.getUsername(), reading, new AsyncCallback<Boolean>() {
					//
					// @Override
					// public void onSuccess(Boolean result) {
					// DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
					// DialogBox dialogBox = DialogBoxCreator.createDialogBox(UserMenuConstants.DIALOG_BOX_SUCCESSFUL_SUBMIT_READING_TITLE,
					// UserMenuConstants.DIALOG_BOX_SUCCESSFUL_SUBMT_READING_MESSAGE, DialogBoxConstants.CLOSE_BUTTON, false, false);
					// dialogBox.setGlassEnabled(true);
					// dialogBox.setAnimationEnabled(true);
					// dialogBox.center();
					// dialogBox.show();
					//
					// coldWaterBox.setText(null);
					// hotWaterBox.setText(null);
					// electicityBox.setText(null);
					// gazBox.setText(null);
					// }
					//
					// @Override
					// public void onFailure(Throwable caught) {
					// DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
					// DialogBox dialogBox = DialogBoxCreator.createDialogBox(UserMenuConstants.DIALOG_BOX_FAILED_SUBMIT_READING_TITLE,
					// UserMenuConstants.DIALOG_BOX_FAILED_SUBMT_READING_MESSAGE + ": " + caught.getMessage(),
					// DialogBoxConstants.CLOSE_BUTTON, false, false);
					// dialogBox.setGlassEnabled(true);
					// dialogBox.setAnimationEnabled(true);
					// dialogBox.center();
					// dialogBox.show();
					//
					// coldWaterBox.setText(null);
					// hotWaterBox.setText(null);
					// electicityBox.setText(null);
					// gazBox.setText(null);
					// }
					// });
				} else {
					String message = "Please fill all the fields before submitting!";

					DialogBox dialogBox = DialogBoxCreator.createDialogBox(UserMenuConstants.DIALOG_BOX_FAILED_SUBMIT_READING_TITLE, message,
							DialogBoxConstants.CLOSE_BUTTON, false, false);
					dialogBox.setGlassEnabled(true);
					dialogBox.setAnimationEnabled(true);
					dialogBox.center();
					dialogBox.show();
				}

			}
		});

		// Reset Button
		Button resetButton = new Button();
		resetButton.setText("Reset");
		resetButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				coldWaterBox.setText(null);
				hotWaterBox.setText(null);
				electicityBox.setText(null);
				gazBox.setText(null);
			}
		});

		grid.setWidget(0, 0, coldWaterLabel);
		grid.setWidget(0, 1, coldWaterBox);

		grid.setWidget(1, 0, hotWaterLabel);
		grid.setWidget(1, 1, hotWaterBox);

		grid.setWidget(2, 0, electricityLabel);
		grid.setWidget(2, 1, electicityBox);

		grid.setWidget(3, 0, gazLabel);
		grid.setWidget(3, 1, gazBox);

		grid.setWidget(4, 0, submitButton);
		grid.setWidget(4, 1, resetButton);

		panel.add(description);
		panel.add(grid);

		return panel;
	}

}
