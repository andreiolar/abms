package com.andreiolar.abms.client.jso;

import com.arcbees.stripe.client.jso.CreditCardResponse;

public class ClientCreditCardResponse extends CreditCardResponse {

	protected ClientCreditCardResponse() {
	}

	public final native Error getError() /*-{
		return this.error;
	}-*/;
}
