package com.andreiolar.abms.client.widgets;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import com.andreiolar.abms.client.exception.PersonalUpkeepInformationNotFoundException;
import com.andreiolar.abms.client.rpc.DBPersonalCosts;
import com.andreiolar.abms.client.rpc.DBPersonalCostsAsync;
import com.andreiolar.abms.client.utils.DateUtil;
import com.andreiolar.abms.shared.PersonalUpkeepInformation;
import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.addins.client.popupmenu.MaterialPopupMenu;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialLoader;
import gwt.material.design.client.ui.MaterialModal;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Hr;

public class PersonalCostsWidget extends Composite implements CustomWidget {

	private UserDetails userDetails;

	private String previousMonth;
	private String year;

	public PersonalCostsWidget(UserDetails userDetails) {
		this.userDetails = userDetails;
		this.previousMonth = DateUtil.getPreviousMonthAsString(new Date());
		this.year = DateUtil.getYearForPreviousMonth(new Date());

		initWidget(initializeWidget());
	}

	@Override
	public Widget initializeWidget() {
		MaterialPanel panel = new MaterialPanel();

		MaterialLabel title = new MaterialLabel("Personal Costs View");
		title.setTextColor(Color.BLUE);
		title.setTextAlign(TextAlign.CENTER);
		title.setFontSize("36px");
		title.setFontWeight(FontWeight.BOLD);
		panel.add(title);

		panel.add(new Hr());

		DBPersonalCostsAsync service = (DBPersonalCostsAsync) GWT.create(DBPersonalCosts.class);
		ServiceDefTarget target = (ServiceDefTarget) service;
		String moduleRelativeURL = GWT.getModuleBaseURL() + "DBPersonalCostsImpl";
		target.setServiceEntryPoint(moduleRelativeURL);

		MaterialLoader.showLoading(true);

		service.getPersonalUpkeepInformation(userDetails, previousMonth + " " + year, new AsyncCallback<PersonalUpkeepInformation>() {

			@Override
			public void onSuccess(PersonalUpkeepInformation result) {
				MaterialLoader.showLoading(false);

				MaterialLabel label = new MaterialLabel();
				label.getElement()
						.setInnerHTML("Below you will find all informations needed about your personal upkeep costs for " + previousMonth + " " + year
								+ "." + "<br/><br/>"
								+ "Note: This view can be exported as PDF by right clicking anywere in the view and selecting Export as PDF");
				label.setMarginTop(25.0);
				label.setMarginLeft(25.0);
				label.setFontSize("18px");
				panel.add(label);

				MaterialPanel costsDescriptionPanel = new MaterialPanel();
				costsDescriptionPanel.setShadow(2);
				costsDescriptionPanel.setStyleName("pay-personal-costs-view");

				MaterialLabel month = new MaterialLabel();
				month.getElement().setInnerHTML("Personalized upkeep report for <span style=\"color: #9e9e9e\">" + result.getLuna() + "</span>");
				month.setMarginTop(25.0);
				month.setMarginLeft(25.0);
				costsDescriptionPanel.add(month);

				MaterialLabel nameAndAptNumber = new MaterialLabel();
				nameAndAptNumber.getElement()
						.setInnerHTML("Upkeep report for apartment: <span style=\"color: #9e9e9e\">" + result.getAptNumber() + "</span>" + "<br/>"
								+ "Responsible person: <span style=\"color: #9e9e9e\">" + result.getNume() + "</span>" + "<br/>"
								+ "Number of persons: <span style=\"color: #9e9e9e\">" + result.getNumarPersoane() + "</span>");
				nameAndAptNumber.setMarginTop(25.0);
				nameAndAptNumber.setMarginLeft(25.0);
				costsDescriptionPanel.add(nameAndAptNumber);

				MaterialLabel spatiuComun = new MaterialLabel();
				spatiuComun.getElement().setInnerHTML("Common space: <span style=\"color: #9e9e9e\">" + result.getSpatiuComun() + " mp</span>"
						+ "<br/>Note: It will be reflected within the heating costs.");
				spatiuComun.setMarginTop(25.0);
				spatiuComun.setMarginLeft(25.0);
				costsDescriptionPanel.add(spatiuComun);

				MaterialLabel suprafataApt = new MaterialLabel();
				suprafataApt.getElement()
						.setInnerHTML("Apartment surface: <span style=\"color: #9e9e9e\">" + result.getSuprafataApt() + " mp</span>" + "<br/>"
								+ "Note: It will be reflected within the heating costs." + "<br/>"
								+ "Note: It will be 0 for tenants with own heating plants, otherwise the whole apartment surface will be displayed.");
				suprafataApt.setMarginTop(25.0);
				suprafataApt.setMarginLeft(25.0);
				costsDescriptionPanel.add(suprafataApt);

				MaterialLabel incalzire = new MaterialLabel();
				incalzire.getElement().setInnerHTML("Heating: <span style=\"color: #9e9e9e\">" + result.getIncalzire() + " RON</span>" + "<br/>"
						+ "Note: It is calculated based on the common space and the apartment surface.");
				incalzire.setMarginTop(25.0);
				incalzire.setMarginLeft(25.0);
				costsDescriptionPanel.add(incalzire);

				MaterialLabel apaCaldaMenajera = new MaterialLabel();
				apaCaldaMenajera.getElement().setInnerHTML("Hot water: <span style=\"color: #9e9e9e\">" + result.getApaCaldaMenajera()
						+ " RON</span><br>Note: It is calculated as following: TERMO-ACM + AR din ACM.<br>Note: It will be 0 for tenants with own heating plants.");
				apaCaldaMenajera.setMarginTop(25.0);
				apaCaldaMenajera.setMarginLeft(25.0);
				costsDescriptionPanel.add(apaCaldaMenajera);

				MaterialLabel apaReceSiCanalizare = new MaterialLabel();
				apaReceSiCanalizare.getElement()
						.setInnerHTML("Cold water and sewerage: <span style=\"color: #9e9e9e\">" + result.getApaReceSiCanalizare() + " RON</span>");
				apaReceSiCanalizare.setMarginTop(25.0);
				apaReceSiCanalizare.setMarginLeft(25.0);
				costsDescriptionPanel.add(apaReceSiCanalizare);

				MaterialLabel gunoi = new MaterialLabel();
				gunoi.getElement().setInnerHTML("Garbage: <span style=\"color: #9e9e9e\">" + result.getGunoi() + " RON</span>" + "<br/>"
						+ "Note: It is calculated as following: <span style=\"color: #9e9e9e\">10.43 RON * Number of persons</span>.");
				gunoi.setMarginTop(25.0);
				gunoi.setMarginLeft(25.0);
				costsDescriptionPanel.add(gunoi);

				MaterialLabel curent = new MaterialLabel();
				curent.getElement()
						.setInnerHTML("Electricity: <span style=\"color: #9e9e9e\">" + result.getCurent() + " RON</span>" + "<br/>"
								+ "Note: Common apartment building consumpion. <br/>"
								+ "Note: It is calculated as following: <span style=\"color: #9e9e9e\">1 RON * Number of persons</span>.");
				curent.setMarginTop(25.0);
				curent.setMarginLeft(25.0);
				costsDescriptionPanel.add(curent);

				MaterialLabel gaz = new MaterialLabel();
				gaz.getElement().setInnerHTML("Gas: <span style=\"color: #9e9e9e\">" + result.getGaz() + " RON</span>");
				gaz.setMarginTop(25.0);
				gaz.setMarginLeft(25.0);
				costsDescriptionPanel.add(gaz);

				MaterialLabel servicii = new MaterialLabel();
				servicii.getElement().setInnerHTML("Other services: <span style=\"color: #9e9e9e\">" + result.getServicii() + " RON</span>" + "<br/>"
						+ "Note: Common services for the apartment building. For example: Cleaning.");
				servicii.setMarginTop(25.0);
				servicii.setMarginLeft(25.0);
				costsDescriptionPanel.add(servicii);

				MaterialLabel gospodaresti = new MaterialLabel();
				gospodaresti.getElement()
						.setInnerHTML("Other apartment costs: <span style=\"color: #9e9e9e\">" + result.getGospodaresti() + " RON</span>");
				gospodaresti.setMarginTop(25.0);
				gospodaresti.setMarginLeft(25.0);
				costsDescriptionPanel.add(gospodaresti);

				MaterialPanel moneyPanel = new MaterialPanel();
				moneyPanel.setWidth("40%");
				moneyPanel.setMarginTop(50.0);
				moneyPanel.setMarginLeft(25.0);
				moneyPanel.setPaddingBottom(25.0);

				BigDecimal cost = new BigDecimal(result.getCostTotal());
				BigDecimal costScaled = cost.setScale(2, RoundingMode.CEILING);

				MaterialLabel sumLabel = new MaterialLabel();
				sumLabel.getElement().setInnerHTML("<b>" + costScaled + "</b>" + " <sup>RON</sup>");
				sumLabel.setFontSize("60px");
				sumLabel.setTextColor(Color.BLUE);
				moneyPanel.add(sumLabel);

				MaterialLabel sumTextLabel = new MaterialLabel();
				sumTextLabel.setText("Total to be paid");
				sumTextLabel.setTextColor(Color.GREY);
				moneyPanel.add(sumTextLabel);

				costsDescriptionPanel.add(moneyPanel);
				panel.add(costsDescriptionPanel);

				MaterialPopupMenu contextMenu = new MaterialPopupMenu();

				MaterialLink pdfLink = new MaterialLink();
				pdfLink.setPadding(12.0);
				pdfLink.setDisplay(Display.BLOCK);
				pdfLink.setText("Export as PDF");
				pdfLink.setIconType(IconType.PICTURE_AS_PDF);
				pdfLink.setTextColor(Color.BLUE);
				pdfLink.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						String url = GWT.getModuleBaseURL() + "pdfGenerator?username=" + userDetails.getUsername() + "&month=" + previousMonth + " "
								+ year;
						Window.open(url, "_blank", "status=0,toolbar=0,menubar=0,location=0");
					}

				});

				contextMenu.add(pdfLink);

				costsDescriptionPanel.sinkEvents(Event.ONCONTEXTMENU);
				costsDescriptionPanel.addHandler(new ContextMenuHandler() {

					@Override
					public void onContextMenu(ContextMenuEvent event) {
						event.preventDefault();
						event.stopPropagation();

						int x = event.getNativeEvent().getClientX();
						int y = event.getNativeEvent().getClientY();

						RootPanel.get().add(contextMenu);
						contextMenu.setPopupPosition(x, y);
						contextMenu.open();
					}
				}, ContextMenuEvent.getType());

				Div buttonDiv = new Div();
				buttonDiv.setStyleName("personal-costs-pay-center-button");
				MaterialButton payNowButton = new MaterialButton();
				payNowButton.setWaves(WavesType.LIGHT);
				payNowButton.setWidth("100%");
				payNowButton.setHeight("50px");
				payNowButton.setText("PAY NOW");
				payNowButton.setTextColor(Color.WHITE);
				payNowButton.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						Window.alert("Test: Va urma...");
					}
				});

				buttonDiv.add(payNowButton);

				panel.add(buttonDiv);
			}

			@Override
			public void onFailure(Throwable caught) {
				MaterialLoader.showLoading(false);
				if (caught instanceof PersonalUpkeepInformationNotFoundException) {

				} else {
					MaterialModal materialModal = ModalCreator.createErrorModal("Something went wrong", caught);
					RootPanel.get().add(materialModal);
					materialModal.open();
				}
			}
		});

		return panel;
	}

}
