package com.andreiolar.abms.client.rpc;

import java.util.List;

import com.andreiolar.abms.shared.PaymentEntry;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PaymentHistoryServiceAsync {

	void listPaymentEntries(String aptNumber, AsyncCallback<List<PaymentEntry>> callback);

}
