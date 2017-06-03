package com.andreiolar.abms.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UpkeepCostReport implements IsSerializable {

	private int aptNumber;
	private String costTotal;
	private boolean status;

	public UpkeepCostReport() {
	}

	public UpkeepCostReport(int aptNumber, String costTotal, boolean status) {
		this.aptNumber = aptNumber;
		this.costTotal = costTotal;
		this.status = status;
	}

	public int getAptNumber() {
		return aptNumber;
	}

	public void setAptNumber(int aptNumber) {
		this.aptNumber = aptNumber;
	}

	public String getCostTotal() {
		return costTotal;
	}

	public void setCostTotal(String costTotal) {
		this.costTotal = costTotal;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}
