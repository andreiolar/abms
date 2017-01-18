package com.andreiolar.abms.client.widgets;

import com.andreiolar.abms.client.constants.DialogBoxConstants;
import com.andreiolar.abms.client.constants.UserMenuConstants;
import com.andreiolar.abms.client.rpc.DBSubmitComplaint;
import com.andreiolar.abms.client.rpc.DBSubmitComplaintAsync;
import com.andreiolar.abms.client.utils.DialogBoxCreator;
import com.andreiolar.abms.client.view.impl.RichTextToolbar;
import com.andreiolar.abms.shared.UserInfo;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ComplaintWidget_bkp extends Composite {

	private String complaintToWho;
	private String complaintTo;

	private UserInfo userInfo;

	public ComplaintWidget_bkp(UserInfo userInfo, String complaintToWho, String complaintTo) {
		this.userInfo = userInfo;
		this.complaintToWho = complaintToWho;
		this.complaintTo = complaintTo;

		initWidget(initializeWidget());

	}

	private Widget initializeWidget() {
		VerticalPanel vPanel = new VerticalPanel();
		DecoratorPanel dPanel = new DecoratorPanel();
		dPanel.getElement().getStyle().setMarginBottom(20.0, Unit.PX);
		dPanel.setWidget(new HTML(complaintToWho));

		vPanel.add(dPanel);

		// First Name
		Label firstName = new Label("First Name");
		final TextBox firstNameBox = new TextBox();
		firstNameBox.setMaxLength(30);
		firstNameBox.setText(userInfo.getFirstName());
		firstNameBox.setEnabled(false);

		// Last Name
		Label lastName = new Label("Last Name");
		final TextBox lastNameBox = new TextBox();
		lastNameBox.setMaxLength(30);
		lastNameBox.setText(userInfo.getLastName());
		lastNameBox.setEnabled(false);

		// Mobile Number
		Label mobileNumber = new Label("Contact Number");
		final TextBox mobileNumberBox = new TextBox();
		mobileNumberBox.setMaxLength(10);

		// Personal ID Number
		Label personalNumber = new Label("Personal ID Number");
		final TextBox personalNumberBox = new TextBox();
		personalNumberBox.setMaxLength(13);
		personalNumberBox.setText(userInfo.getPersonalNumber());
		personalNumberBox.setEnabled(false);

		// ID Series
		Label seriesNumber = new Label("ID Series");
		final TextBox seriesNumberBox = new TextBox();
		seriesNumberBox.setMaxLength(8);
		seriesNumberBox.setText(userInfo.getIdSeries());
		seriesNumberBox.setEnabled(false);

		// Rich text field
		Label complaintLabel = new Label("Complaint");
		final RichTextArea area = new RichTextArea();
		area.setSize("90%", "14em");
		RichTextToolbar toolbar = new RichTextToolbar(area);
		toolbar.setWidth("100%");

		Grid grid = new Grid(2, 1);
		grid.setStyleName("cw-RichText");
		grid.setWidget(0, 0, toolbar);
		grid.setWidget(1, 0, area);

		// Create the form
		Grid form = new Grid(7, 2);

		form.setWidget(0, 0, firstName);
		form.setWidget(0, 1, firstNameBox);

		form.setWidget(1, 0, lastName);
		form.setWidget(1, 1, lastNameBox);

		form.setWidget(2, 0, mobileNumber);
		form.setWidget(2, 1, mobileNumberBox);

		form.setWidget(3, 0, personalNumber);
		form.setWidget(3, 1, personalNumberBox);

		form.setWidget(4, 0, seriesNumber);
		form.setWidget(4, 1, seriesNumberBox);

		form.setWidget(5, 0, complaintLabel);
		form.setWidget(5, 1, grid);

		Button submitButton = new Button("Submit");
		submitButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				String mobileNumber = mobileNumberBox.getText();
				String text = area.getText();

				String message = "";
				boolean mobileNumberEmpty = false;
				boolean textEmpty = false;

				if (mobileNumber == null || mobileNumber.trim().equals("")) {
					message += "<p>Please enter a phone number!</p>";
					mobileNumberEmpty = true;
				}

				if (text == null || text.trim().equals("")) {
					message += "<p>Please enter complaint!</p>";
					textEmpty = true;
				}

				if (mobileNumberEmpty || textEmpty) {
					DialogBox dialogBox = DialogBoxCreator.createDialogBox(UserMenuConstants.DIALOG_BOX_INVAILD_COMPLAINT_DATA_TITLE, message,
							DialogBoxConstants.CLOSE_BUTTON, false, false);
					dialogBox.setGlassEnabled(true);
					dialogBox.setAnimationEnabled(true);
					dialogBox.center();
					dialogBox.show();
				} else {

					DOM.getElementById("loading").getStyle().setDisplay(Display.BLOCK);
					DBSubmitComplaintAsync rpcService = (DBSubmitComplaintAsync) GWT.create(DBSubmitComplaint.class);
					ServiceDefTarget target = (ServiceDefTarget) rpcService;
					String moduleRelativeURL = GWT.getModuleBaseURL() + "DBSubmitComplaintImpl";
					target.setServiceEntryPoint(moduleRelativeURL);

					rpcService.addComplaint(userInfo.getUsername(), mobileNumber, complaintTo, firstNameBox.getText(), lastNameBox.getText(),
							personalNumberBox.getText(), seriesNumberBox.getText(), text, new AsyncCallback<Boolean>() {

								@Override
								public void onSuccess(Boolean result) {
									DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
									DialogBox dialogBox = DialogBoxCreator.createDialogBox(UserMenuConstants.DIALOG_BOX_SUCCESSFUL_COMPLAINT_TITLE,
											UserMenuConstants.DIALOG_BOX_SUCCESSFUL_COMPLAINT_MESSAGE, DialogBoxConstants.CLOSE_BUTTON, false, false);
									dialogBox.setGlassEnabled(true);
									dialogBox.setAnimationEnabled(true);
									dialogBox.center();
									dialogBox.show();
								}

								@Override
								public void onFailure(Throwable caught) {
									DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
									DialogBox dialogBox = DialogBoxCreator.createDialogBox(UserMenuConstants.DIALOG_BOX_FAILED_COMPLAINT_TITLE,
											UserMenuConstants.DIALOG_BOX_FAILED_COMPLAINT_MESSAGE + caught.getMessage(),
											DialogBoxConstants.CLOSE_BUTTON, false, false);
									dialogBox.setGlassEnabled(true);
									dialogBox.setAnimationEnabled(true);
									dialogBox.center();
									dialogBox.show();
								}

							});
				}

				mobileNumberBox.setText(null);
				area.setText(null);
			}
		});

		Button resetButton = new Button("Reset");
		resetButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				mobileNumberBox.setText(null);
				area.setText(null);
			}
		});

		form.setWidget(6, 0, submitButton);
		form.setWidget(6, 1, resetButton);

		vPanel.add(form);

		return vPanel;
	}
}
