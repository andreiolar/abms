package com.andreiolar.abms.client.rpc;

import java.util.List;

import com.andreiolar.abms.shared.PaymentEntry;
import com.google.gwt.user.client.rpc.RemoteService;

public interface PaymentHistoryService extends RemoteService {

	public List<PaymentEntry> listPaymentEntries(String aptNumber) throws Exception;
}
