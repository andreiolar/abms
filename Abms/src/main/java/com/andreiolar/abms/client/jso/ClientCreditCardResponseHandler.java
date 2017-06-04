package com.andreiolar.abms.client.jso;

public interface ClientCreditCardResponseHandler {

	void onCreditCardReceived(int status, ClientCreditCardResponse creditCardResponse);
}
