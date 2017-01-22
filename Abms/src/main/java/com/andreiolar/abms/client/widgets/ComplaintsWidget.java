package com.andreiolar.abms.client.widgets;

import com.andreiolar.abms.client.exception.ComplaintSubmissionException;
import com.andreiolar.abms.client.exception.MailException;
import com.andreiolar.abms.client.rpc.DBSubmitComplaint;
import com.andreiolar.abms.client.rpc.DBSubmitComplaintAsync;
import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.addins.client.richeditor.MaterialRichEditor;
import gwt.material.design.client.constants.CollapsibleType;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconPosition;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialCollapsible;
import gwt.material.design.client.ui.MaterialCollapsibleBody;
import gwt.material.design.client.ui.MaterialCollapsibleHeader;
import gwt.material.design.client.ui.MaterialCollapsibleItem;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialLoader;
import gwt.material.design.client.ui.MaterialModal;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Hr;
import gwt.material.design.client.ui.html.OptGroup;
import gwt.material.design.client.ui.html.Option;

public class ComplaintsWidget extends Composite implements CustomWidget {

	public UserDetails userDetails;

	public ComplaintsWidget(UserDetails userDetails) {
		this.userDetails = userDetails;

		initWidget(initializeWidget());
	}

	@Override
	public Widget initializeWidget() {
		MaterialPanel panel = new MaterialPanel();

		MaterialLabel title = new MaterialLabel("Complaints");
		title.setTextColor(Color.BLUE);
		title.setTextAlign(TextAlign.CENTER);
		title.setFontSize("36px");
		title.setFontWeight(FontWeight.BOLD);
		panel.add(title);

		panel.add(new Hr());

		MaterialComboBox<String> institutionsBox = initInstitutions();
		institutionsBox.setStyleName("comboboxInst");
		institutionsBox.setTextAlign(TextAlign.CENTER);
		panel.add(institutionsBox);

		MaterialCollapsible collapsible = new MaterialCollapsible();
		collapsible.setType(CollapsibleType.POPOUT);
		collapsible.setGrid("s12 m6 l8");
		collapsible.setVisibility(Visibility.HIDDEN);
		collapsible.setMarginBottom(40.0);

		MaterialCollapsibleItem collapsibleItem = new MaterialCollapsibleItem();

		MaterialCollapsibleHeader collapsibleHeader = new MaterialCollapsibleHeader();

		MaterialLink collapsibleLink = new MaterialLink();
		collapsibleLink.setText("Important Note!");
		collapsibleLink.setIconType(IconType.INFO_OUTLINE);
		collapsibleLink.setIconPosition(IconPosition.LEFT);
		collapsibleLink.setTextColor(Color.BLUE);
		collapsibleHeader.add(collapsibleLink);

		MaterialCollapsibleBody collapsibleBody = new MaterialCollapsibleBody();

		MaterialLabel collapsibleLabel = new MaterialLabel();
		collapsibleLabel.getElement().setInnerHTML(
				"<b>Important Note!</b><br />You are about to write a complaint. Please think carefully about this. Fake complaints will be punished by law!<br />To submit a complaint to the "
						+ institutionsBox.getSelectedValue()
						+ " please use the form below.<br />Only phone number is editable. If other fields do not match, please edit them in your account settings before writing the actual complaint.");
		collapsibleBody.add(collapsibleLabel);

		institutionsBox.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String selected = event.getValue();
				if (!selected.equals("Select Institution")) {
					collapsibleLabel.getElement().setInnerHTML(
							"<b>Important Note!</b><br />You are about to write a complaint. Please think carefully about this. Fake complaints will be punished by law!<br />To submit a complaint to the "
									+ institutionsBox.getSelectedValue()
									+ " please use the form below.<br />Only phone number is editable. If other fields do not match, please edit them in your account settings before writing the actual complaint.");
					collapsible.setVisibility(Visibility.VISIBLE);
				} else {
					collapsible.setVisibility(Visibility.HIDDEN);
				}
			}
		});

		collapsibleItem.add(collapsibleHeader);
		collapsibleItem.add(collapsibleBody);

		collapsible.add(collapsibleItem);

		panel.add(collapsible);

		// Other inputs
		MaterialTextBox firstNameBox = new MaterialTextBox();
		firstNameBox.setType(InputType.TEXT);
		firstNameBox.setPlaceholder("First Name");
		firstNameBox.setMaxLength(30);
		firstNameBox.setLength(30);
		firstNameBox.setIconType(IconType.PERSON);
		firstNameBox.setText(userDetails.getFirstName());
		firstNameBox.setEnabled(false);
		firstNameBox.setStyleName("complaintsComponent");
		panel.add(firstNameBox);

		MaterialTextBox lastNameBox = new MaterialTextBox();
		lastNameBox.setType(InputType.TEXT);
		lastNameBox.setPlaceholder("Last Name");
		lastNameBox.setMaxLength(30);
		lastNameBox.setLength(30);
		lastNameBox.setIconType(IconType.PERSON);
		lastNameBox.setText(userDetails.getLastName());
		lastNameBox.setEnabled(false);
		lastNameBox.setStyleName("complaintsComponent");
		panel.add(lastNameBox);

		MaterialTextBox mobileNumberBox = new MaterialTextBox();
		mobileNumberBox.setType(InputType.TEL);
		mobileNumberBox.setPlaceholder("Phone Number");
		mobileNumberBox.setMaxLength(10);
		mobileNumberBox.setLength(10);
		mobileNumberBox.setIconType(IconType.SMARTPHONE);
		mobileNumberBox.setText(userDetails.getMobileNumber());
		mobileNumberBox.setStyleName("complaintsComponent");
		panel.add(mobileNumberBox);

		MaterialTextBox cnpBox = new MaterialTextBox();
		cnpBox.setType(InputType.TEXT);
		cnpBox.setPlaceholder("CNP");
		cnpBox.setMaxLength(13);
		cnpBox.setLength(13);
		cnpBox.setIconType(IconType.PERM_IDENTITY);
		cnpBox.setText(userDetails.getPersonalNumber());
		cnpBox.setEnabled(false);
		cnpBox.setStyleName("complaintsComponent");
		panel.add(cnpBox);

		MaterialTextBox personalNumberBox = new MaterialTextBox();
		personalNumberBox.setType(InputType.TEXT);
		personalNumberBox.setPlaceholder("ID Series and Number");
		personalNumberBox.setMaxLength(8);
		personalNumberBox.setLength(8);
		personalNumberBox.setIconType(IconType.PERM_IDENTITY);
		personalNumberBox.setText(userDetails.getIdSeries());
		personalNumberBox.setEnabled(false);
		personalNumberBox.setStyleName("complaintsComponent");
		panel.add(personalNumberBox);

		Div div = new Div();
		div.setHeight("360px");
		div.setStyleName("complaintsEditor");

		MaterialRichEditor editor = new MaterialRichEditor();
		editor.setPlaceholder("Your complaint here.");
		editor.setHeight("150px");
		div.add(editor);
		panel.add(div);

		Div buttonsDiv = new Div();
		buttonsDiv.setStyleName("twoButtons");

		MaterialButton submitButton = new MaterialButton();
		submitButton.setWaves(WavesType.LIGHT);
		submitButton.setText("Submit");
		submitButton.setWidth("15%");
		submitButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String mobileNumber = mobileNumberBox.getText();
				String complaintTo = institutionsBox.getSelectedValue();
				String complaintText = editor.getHTML();

				boolean canProcceed = true;

				if (complaintTo.equals("Select Institution")) {
					canProcceed = false;
					institutionsBox.setError("Please select an institution.");
				} else {
					institutionsBox.setSuccess("");
				}

				if (!mobileNumber.matches("[0-9]{10}")) {
					canProcceed = false;
					mobileNumberBox.setError("Please enter a valid phone number. Only digits are allowed.");
				} else {
					mobileNumberBox.setSuccess("");
				}

				if (canProcceed) {
					MaterialLoader.showLoading(true);
					DBSubmitComplaintAsync rpcService = (DBSubmitComplaintAsync) GWT.create(DBSubmitComplaint.class);
					ServiceDefTarget target = (ServiceDefTarget) rpcService;
					String moduleRelativeURL = GWT.getModuleBaseURL() + "DBSubmitComplaintImpl";
					target.setServiceEntryPoint(moduleRelativeURL);

					rpcService.registerComplaint(userDetails, mobileNumber, complaintTo, complaintText, new AsyncCallback<Boolean>() {

						@Override
						public void onSuccess(Boolean result) {
							MaterialLoader.showLoading(false);
							MaterialToast.fireToast("Complaint submitted successfully!", "rounded");
						}

						@Override
						public void onFailure(Throwable caught) {
							MaterialLoader.showLoading(false);
							if (caught instanceof ComplaintSubmissionException || caught instanceof MailException) {
								MaterialToast.fireToast(caught.getMessage(), "rounded");
							} else {
								MaterialModal errorModal = ModalCreator.createModal(caught);
								RootPanel.get().add(errorModal);
								errorModal.open();
							}
						}
					});

					institutionsBox.setSelectedIndex(0);
					mobileNumberBox.setText(userDetails.getMobileNumber());
					editor.setHTML("");
				}
			}
		});
		buttonsDiv.add(submitButton);

		MaterialButton clearButton = new MaterialButton();
		clearButton.setWaves(WavesType.LIGHT);
		clearButton.setText("Clear");
		clearButton.setWidth("15%");
		clearButton.setMarginLeft(25.0);
		clearButton.setBackgroundColor(Color.LIGHT_BLUE_LIGHTEN_3);
		clearButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				mobileNumberBox.setText(userDetails.getMobileNumber());
				editor.setHTML("");
			}
		});
		buttonsDiv.add(clearButton);

		panel.add(buttonsDiv);

		return panel;
	}

	private MaterialComboBox<String> initInstitutions() {
		MaterialComboBox<String> institutionsBox = new MaterialComboBox<>();
		institutionsBox.setPlaceholder("Institution");

		institutionsBox.add(new Option("Select Institution"));

		OptGroup emergencyServicesGroup = new OptGroup();
		emergencyServicesGroup.setLabel("Emergency Services");
		emergencyServicesGroup.add(new Option("Local Police"));
		emergencyServicesGroup.add(new Option("National Police"));
		emergencyServicesGroup.add(new Option("Medical Service"));
		emergencyServicesGroup.add(new Option("Firefighter Service"));
		institutionsBox.add(emergencyServicesGroup);

		OptGroup publicTransportGroup = new OptGroup();
		publicTransportGroup.setLabel("Transportation");
		publicTransportGroup.add(new Option("Public Transportation"));
		publicTransportGroup.add(new Option("National Railway Company"));
		publicTransportGroup.add(new Option("Airport"));
		institutionsBox.add(publicTransportGroup);

		OptGroup otherGroup = new OptGroup();
		otherGroup.setLabel("Other");
		otherGroup.add(new Option("Salubrity"));
		otherGroup.add(new Option("Town Hall"));
		institutionsBox.add(otherGroup);

		institutionsBox.setSelectedIndex(0);

		return institutionsBox;
	}

}
