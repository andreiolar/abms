package com.andreiolar.abms.client.widgets;

import com.andreiolar.abms.client.rpc.GetHtmlTableFromExel;
import com.andreiolar.abms.client.rpc.GetHtmlTableFromExelAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class GeneralCostWidget extends Composite implements CustomWidget {

	public GeneralCostWidget() {
		initWidget(initializeWidget());
	}

	@Override
	public Widget initializeWidget() {

		final HTML html = new HTML();

		DOM.getElementById("loading").getStyle().setDisplay(Display.BLOCK);
		GetHtmlTableFromExelAsync rpcService = (GetHtmlTableFromExelAsync) GWT.create(GetHtmlTableFromExel.class);
		ServiceDefTarget target = (ServiceDefTarget) rpcService;
		String moduleRelativeURL = GWT.getModuleBaseURL() + "GetHtmlTableFromExelImpl";
		target.setServiceEntryPoint(moduleRelativeURL);

		rpcService.getTableFromExcel(new AsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
				html.setHTML(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
				html.setHTML(
						"<p>Error getting information from server!</p><p>Please contact your administrator. A possibility may be that he did not uploaded the excel file for the current month.</p>");
			}
		});

		return html;

	}

}
