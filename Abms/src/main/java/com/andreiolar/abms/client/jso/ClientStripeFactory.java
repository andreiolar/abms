package com.andreiolar.abms.client.jso;

public class ClientStripeFactory {

	public static ClientStripe get() {
		return new ClientStripeImpl();
	}
}
