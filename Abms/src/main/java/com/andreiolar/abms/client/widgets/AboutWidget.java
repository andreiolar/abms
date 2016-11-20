package com.andreiolar.abms.client.widgets;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class AboutWidget extends Composite implements CustomWidget {

	public AboutWidget() {
		initWidget(initializeWidget());
	}

	@Override
	public Widget initializeWidget() {
		HTML about = new HTML("<p style=\"font-size:15px\">"
				+ "This Apartment Building Management System was designed to help administrators and tenants ease their life by providing basic management functionality."
				+ "<br>" + "Some of the included functions for this managemet solution are: " + "<br>" + "<ul>" + "<li>"
				+ "Complaint System, where you can send complaints to different institutions." + "</li>" + "<li>"
				+ "User Administration system, which includes the following:" + "</li>" + "<ul>" + "<li>" + "Basic Contact Information System"
				+ "</li>" + "<li>" + "A complex view of the individual monthly costs" + "</li>" + "<li>"
				+ "A complex view of the entire apartment building monthly costs" + "</li>" + "<li>" + "A self consumption reading system" + "</li>"
				+ "</ul>" + "<li>" + "A voting system, where you can: " + "</li>" + "<ul>" + "<li>" + "Vote" + "</li>" + "<li>" + "View vote results"
				+ "</li>" + "</ul>" + "<li>" + "A Help system" + "</li>" + "</ul>" + "<br><br><br><br>"
				+ "<i><u>Designed and developed by Andrei Olar</u></i><br>Contact Information: <ul><li>Phone Number: 0740215030</li><li>E-Mail Address: olarandrei@gmail.com</li></ul>"
				+ "</p>");

		return about;

	}

}
