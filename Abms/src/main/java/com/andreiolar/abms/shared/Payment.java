package com.andreiolar.abms.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public interface Payment extends IsSerializable {

	double getTotalCost();
}
