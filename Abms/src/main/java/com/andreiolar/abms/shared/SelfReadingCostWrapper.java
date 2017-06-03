package com.andreiolar.abms.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SelfReadingCostWrapper implements IsSerializable {

	private SelfReading selfReading;
	private String cost;
	private boolean status;

	public SelfReadingCostWrapper() {
	}

	public SelfReadingCostWrapper(SelfReading selfReading, String cost, boolean status) {
		this.selfReading = selfReading;
		this.cost = cost;
		this.status = status;
	}

	public SelfReading getSelfReading() {
		return selfReading;
	}

	public void setSelfReading(SelfReading selfReading) {
		this.selfReading = selfReading;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}
