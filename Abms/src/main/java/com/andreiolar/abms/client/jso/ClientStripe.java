package com.andreiolar.abms.client.jso;

import com.arcbees.stripe.client.CreditCard;
import com.arcbees.stripe.client.Stripe;

public interface ClientStripe extends Stripe {

	void getCreditCardToken(CreditCard creditCard, ClientCreditCardResponseHandler creditCardResponseHandler);
}
