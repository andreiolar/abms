package com.andreiolar.abms.client.widgets;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;

public class ProfileInformationEntry extends Composite implements CustomWidget {

	private String label;
	private String value;

	public ProfileInformationEntry(String label, String value) {
		this.label = label;
		this.value = value;

		initWidget(initializeWidget());
	}

	@Override
	public Widget initializeWidget() {
		MaterialPanel panel = new MaterialPanel();
		panel.setPaddingTop(25.0);
		panel.setDisplay(Display.FLEX);

		MaterialLabel widgetLabel = new MaterialLabel();
		widgetLabel.setText(label);
		widgetLabel.setPaddingLeft(10.0);
		widgetLabel.setFontSize("18px");
		widgetLabel.setFontWeight(FontWeight.BOLD);
		widgetLabel.setTextColor(Color.BLUE);

		MaterialLabel widgetValue = new MaterialLabel();
		widgetValue.setText(value);
		widgetValue.setFontSize("18px");
		widgetValue.setPaddingRight(10.0);
		widgetValue.setTextColor(Color.GREY);
		widgetValue.addStyleName("margin-left-auto");

		panel.add(widgetLabel);
		panel.add(widgetValue);

		return panel;
	}
}
