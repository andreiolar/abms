package com.andreiolar.abms.client.jso;

import com.arcbees.stripe.client.CreditCard;
import com.arcbees.stripe.client.StripeImpl;

public class ClientStripeImpl extends StripeImpl implements ClientStripe {

	@Override
	public void getCreditCardToken(CreditCard creditCard, ClientCreditCardResponseHandler creditCardResponseHandler) {
		getCreditCardToken(creditCard.getCreditCardNumber(), creditCard.getCvc(), creditCard.getExpirationMonth(), creditCard.getExpirationYear(),
				creditCard.getName(), creditCard.getAddressLine1(), creditCard.getAddressLine2(), creditCard.getAddressCity(),
				creditCard.getAddressState(), creditCard.getAddressZip(), creditCard.getAddressCountry(), creditCardResponseHandler);
	}

	private native void getCreditCardToken(String creditCardNumber, String cvc, int expiryMonth, int expiryYear, String name, String addressLine1,
			String addressLine2, String addressCity, String addressState, String addressZip, String addressCountry,
			ClientCreditCardResponseHandler creditCardResponseHandler) /*-{
		var creditCardInfo = {
			number : creditCardNumber,
			cvc : cvc,
			exp_month : expiryMonth,
			exp_year : expiryYear,
			name : name,
			address_line1 : addressLine1,
			address_line2 : addressLine2,
			address_city : addressCity,
			address_state : addressState,
			address_zip : addressZip,
			address_country : addressCountry
		};

		var createTokenCallback = function(status, response) {
			creditCardResponseHandler.@com.andreiolar.abms.client.jso.ClientCreditCardResponseHandler::onCreditCardReceived(ILcom/andreiolar/abms/client/jso/ClientCreditCardResponse;)(status, response);
		}

		$wnd.Stripe.card.createToken(creditCardInfo, createTokenCallback);
	}-*/;
}
