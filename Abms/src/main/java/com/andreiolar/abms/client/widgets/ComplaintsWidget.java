package com.andreiolar.abms.client.widgets;

import com.andreiolar.abms.shared.UserInfo;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.addins.client.richeditor.MaterialRichEditor;
import gwt.material.design.client.constants.CollapsibleType;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconPosition;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialCollapsible;
import gwt.material.design.client.ui.MaterialCollapsibleBody;
import gwt.material.design.client.ui.MaterialCollapsibleHeader;
import gwt.material.design.client.ui.MaterialCollapsibleItem;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Hr;
import gwt.material.design.client.ui.html.OptGroup;
import gwt.material.design.client.ui.html.Option;

public class ComplaintsWidget extends Composite implements CustomWidget {

	private UserInfo userInfo;

	public ComplaintsWidget(UserInfo userInfo) {
		this.userInfo = userInfo;

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
		firstNameBox.setText(userInfo.getFirstName());
		firstNameBox.setEnabled(false);
		firstNameBox.setStyleName("complaintsComponent");
		panel.add(firstNameBox);

		MaterialTextBox lastNameBox = new MaterialTextBox();
		lastNameBox.setType(InputType.TEXT);
		lastNameBox.setPlaceholder("Last Name");
		lastNameBox.setMaxLength(30);
		lastNameBox.setLength(30);
		lastNameBox.setIconType(IconType.PERSON);
		lastNameBox.setText(userInfo.getLastName());
		lastNameBox.setEnabled(false);
		lastNameBox.setStyleName("complaintsComponent");
		panel.add(lastNameBox);

		MaterialTextBox mobileNumberBox = new MaterialTextBox();
		mobileNumberBox.setType(InputType.TEL);
		mobileNumberBox.setPlaceholder("Phone Number");
		mobileNumberBox.setMaxLength(10);
		mobileNumberBox.setLength(10);
		mobileNumberBox.setIconType(IconType.SMARTPHONE);
		mobileNumberBox.setText(userInfo.getMobileNumber());
		mobileNumberBox.setStyleName("complaintsComponent");
		panel.add(mobileNumberBox);

		MaterialTextBox cnpBox = new MaterialTextBox();
		cnpBox.setType(InputType.TEXT);
		cnpBox.setPlaceholder("CNP");
		cnpBox.setMaxLength(13);
		cnpBox.setLength(13);
		cnpBox.setIconType(IconType.PERM_IDENTITY);
		cnpBox.setText(userInfo.getPersonalNumber());
		cnpBox.setEnabled(false);
		cnpBox.setStyleName("complaintsComponent");
		panel.add(cnpBox);

		MaterialTextBox personalNumberBox = new MaterialTextBox();
		personalNumberBox.setType(InputType.TEXT);
		personalNumberBox.setPlaceholder("ID Series and Number");
		personalNumberBox.setMaxLength(8);
		personalNumberBox.setLength(8);
		personalNumberBox.setIconType(IconType.PERM_IDENTITY);
		personalNumberBox.setText(userInfo.getIdSeries());
		personalNumberBox.setEnabled(false);
		personalNumberBox.setStyleName("complaintsComponent");
		panel.add(personalNumberBox);

		Div div = new Div();
		div.setHeight("400px");
		div.setStyleName("complaintsEditor");

		MaterialRichEditor editor = new MaterialRichEditor();
		editor.setPlaceholder("Your complaint here.");
		editor.setHeight("150px");
		div.add(editor);
		panel.add(div);

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
