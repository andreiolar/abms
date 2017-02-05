package com.andreiolar.abms.client.widgets;

import java.util.Date;

import com.andreiolar.abms.client.utils.DateUtil;
import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.addins.client.docviewer.MaterialDocViewer;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.IconPosition;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Hr;

public class GeneralCostsWidget extends Composite implements CustomWidget {

	private UserDetails userDetails;
	private String previousMonth;
	private String year;

	public GeneralCostsWidget(UserDetails userDetails) {
		this.userDetails = userDetails;
		this.previousMonth = DateUtil.getPreviousMonthAsString(new Date());
		this.year = DateUtil.getYearForPreviousMonth(new Date());

		initWidget(initializeWidget());
	}

	@Override
	public Widget initializeWidget() {
		MaterialPanel panel = new MaterialPanel();

		MaterialLabel title = new MaterialLabel("General Costs View");
		title.setTextColor(Color.BLUE);
		title.setTextAlign(TextAlign.CENTER);
		title.setFontSize("36px");
		title.setFontWeight(FontWeight.BOLD);
		panel.add(title);

		panel.add(new Hr());

		MaterialLabel label = new MaterialLabel();
		label.getElement()
				.setInnerHTML("Here you will be able to see a general costs list from all available apartments from " + previousMonth + " " + year
						+ ".<br /><br />In case the document is not visible or could not be loaded properly, following may have happend:<br />&nbsp;- Something happend with the hosting site. Please refresh from time to time.<br/>&nbsp;- The Administrator did not upload the Excel file for "
						+ previousMonth + " " + year + ".<br/><br/>In both cases please contact your Administrator in order to fix things up.");
		label.setMarginTop(25.0);
		label.setMarginLeft(25.0);
		label.setFontSize("18px");
		panel.add(label);

		MaterialDocViewer docViewer = new MaterialDocViewer(
				"http://res.cloudinary.com/andreiolar/raw/upload/Upkeep_" + previousMonth + "_" + year + ".xlsx");
		docViewer.setMarginTop(25.0);
		docViewer.setMarginLeft(50.0);
		docViewer.setWidth("95%");
		docViewer.setHeight("520px");

		panel.add(docViewer);

		MaterialButton dropDownButton = new MaterialButton();
		dropDownButton.setText("Download");
		dropDownButton.setIconType(IconType.ARROW_DROP_UP);
		dropDownButton.setIconPosition(IconPosition.RIGHT);
		dropDownButton.setTextColor(Color.WHITE);
		dropDownButton.setActivates("dp");
		dropDownButton.setMarginLeft(50.0);
		dropDownButton.setMarginTop(25.0);
		dropDownButton.setMarginBottom(75.0);
		dropDownButton.setWidth("20%");
		panel.add(dropDownButton);

		MaterialDropDown dropDown = new MaterialDropDown(dropDownButton);
		dropDown.setMarginLeft(50.0);
		dropDown.setConstrainWidth(true);
		panel.add(dropDown);

		MaterialLink excelLink = new MaterialLink();
		excelLink.setPadding(12.0);
		excelLink.setDisplay(Display.BLOCK);
		excelLink.setText("Export as Excel");
		excelLink.setIconType(IconType.DESCRIPTION);
		excelLink.setTextColor(Color.BLUE);
		excelLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String url = "http://res.cloudinary.com/andreiolar/raw/upload/Upkeep_" + previousMonth + "_" + year + ".xlsx";
				Window.open(url, "_self", "status=0,toolbar=0,menubar=0,location=0");
			}
		});
		dropDown.add(excelLink);

		MaterialLink pdfLink = new MaterialLink();
		pdfLink.setPadding(12.0);
		pdfLink.setDisplay(Display.BLOCK);
		pdfLink.setText("Export as PDF");
		pdfLink.setIconType(IconType.PICTURE_AS_PDF);
		pdfLink.setTextColor(Color.BLUE);
		pdfLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String url = "http://res.cloudinary.com/andreiolar/raw/upload/Upkeep_" + previousMonth + "_" + year + ".pdf";
				Window.open(url, "_blank", "status=0,toolbar=0,menubar=0,location=0");
			}
		});
		dropDown.add(pdfLink);

		MaterialLink zipLink = new MaterialLink();
		zipLink.setPadding(12.0);
		zipLink.setDisplay(Display.BLOCK);
		zipLink.setText("Export both as ZIP");
		zipLink.setIconType(IconType.ARCHIVE);
		zipLink.setTextColor(Color.BLUE);
		zipLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String url = "http://res.cloudinary.com/andreiolar/raw/upload/Upkeep_" + previousMonth + "_" + year + ".zip";
				Window.open(url, "_self", "status=0,toolbar=0,menubar=0,location=0");
			}
		});
		dropDown.add(zipLink);

		return panel;
	}
}
