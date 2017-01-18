package com.andreiolar.abms.client.widgets;

import com.andreiolar.abms.shared.UserInfo;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.constants.CollapsibleType;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconPosition;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialCollapsible;
import gwt.material.design.client.ui.MaterialCollapsibleBody;
import gwt.material.design.client.ui.MaterialCollapsibleHeader;
import gwt.material.design.client.ui.MaterialCollapsibleItem;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
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
		collapsibleLabel.setText("DASDASJDANSOJDASODJASDOASJDOAJSDOIA");
		collapsibleBody.add(collapsibleLabel);

		collapsibleItem.add(collapsibleHeader);
		collapsibleItem.add(collapsibleBody);

		collapsible.add(collapsibleItem);

		collapsible.setEnabled(false);
		panel.add(collapsible);

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
