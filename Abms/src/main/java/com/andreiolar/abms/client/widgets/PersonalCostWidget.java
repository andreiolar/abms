package com.andreiolar.abms.client.widgets;

import com.andreiolar.abms.shared.PersonalUpkeepInformation;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class PersonalCostWidget extends Composite implements CustomWidget {

	private PersonalUpkeepInformation personalUpkeepInformation;

	public PersonalCostWidget(PersonalUpkeepInformation personalUpkeepInformation) {
		this.personalUpkeepInformation = personalUpkeepInformation;

		initWidget(initializeWidget());
	}

	@Override
	public Widget initializeWidget() {
		VerticalPanel panel = new VerticalPanel();

		HTML month = new HTML("<p style=\"font-size:20px\"><b><i><u>Raport personalizat al intretinerii aferent lunii "
				+ personalUpkeepInformation.getLuna() + "</u></i></b></p>");
		HTML nameAndAptNumber = new HTML("<p>Raport aferent apartamentului cu numarul: <b><i>" + personalUpkeepInformation.getAptNumber()
				+ "</i></b>.<br>Persoana responsabila: <b><i> " + personalUpkeepInformation.getNume() + "</i></b>.<br>Numar persoane: <b><i>"
				+ personalUpkeepInformation.getNumarPersoane() + "</i></b>.</p>");
		HTML spatiuComun = new HTML(
				"<p>Spatiul comun: <b><i>" + personalUpkeepInformation.getSpatiuComun() + " mp</i></b>.<br>Obs: Se va reflecta la incalzire.</p>");
		HTML suprafataApt = new HTML("<p>Suprafata apartamentului: <b><i>" + personalUpkeepInformation.getSuprafataApt()
				+ " mp</i></b>.<br>Obs: Se va reflecta la incalzire.<br>Obs: Va fi 0 pentru locatarii cu centrala proprie, respectiv suprafata apartamentului pentru locatarii fara centrala proprie.</p>");
		HTML incalzire = new HTML("<p>Incalizre: <b><i>" + personalUpkeepInformation.getIncalzire()
				+ " RON</i></b>.<br>Obs: Se calculeaza in functie de spatiul comun si suprafata apartamentului.</p>");
		HTML apaCaldaMenajera = new HTML("<p>Apa calda menajera: <b><i>" + personalUpkeepInformation.getApaCaldaMenajera()
				+ " RON</i></b>.<br>Obs: Se calculeaza TERMO-ACM + AR din ACM.<br>Obs: Va fi 0 pentru locatarii cu centrala proprie.</p>");
		HTML apaReceSiCanalizare = new HTML(
				"<p>Apa rece si canalizare: <b><i>" + personalUpkeepInformation.getApaReceSiCanalizare() + " RON</i></b>.</p>");
		HTML gunoi = new HTML("<p>Gunoi: <b><i>" + personalUpkeepInformation.getGunoi()
				+ " RON</i></b>.<br>Obs: Se calculeaza: <b><i>10.43 RON * Numar persoane</i></b>.</p>");
		HTML curent = new HTML("<p>Curent: <b><i>" + personalUpkeepInformation.getCurent()
				+ " RON</i></b>.<br>Obs: Curentul comun pe scara.<br>Obs: Se calculeaza: <b><i>1 RON * Numar persoane</i></b>.</p>");
		HTML gaz = new HTML("<p>Gaz: <b><i>" + personalUpkeepInformation.getGaz() + " RON</i></b>.</p>");
		HTML servicii = new HTML("<p>Servicii: <b><i>" + personalUpkeepInformation.getServicii()
				+ " RON</i></b>.<br>Obs: Consta in serviciile comune scarii de bloc. De exemplu: Curatenia.</p>");
		HTML gospodaresti = new HTML("<p>Gospodaresti: <b><i>" + personalUpkeepInformation.getGospodaresti() + " RON</i></b>.</p>");
		HTML costTotal = new HTML("<p>Total de plata: <b><i><u>" + personalUpkeepInformation.getCostTotal() + " RON</u></i></b>.</p>");

		panel.add(month);
		panel.add(nameAndAptNumber);
		panel.add(spatiuComun);
		panel.add(suprafataApt);
		panel.add(incalzire);
		panel.add(apaCaldaMenajera);
		panel.add(apaReceSiCanalizare);
		panel.add(gunoi);
		panel.add(curent);
		panel.add(gaz);
		panel.add(servicii);
		panel.add(gospodaresti);
		panel.add(costTotal);

		return panel;
	}

}
