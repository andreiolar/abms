package com.andreiolar.abms.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ConsumptionCostReport implements IsSerializable {

	private String electricity;
	private String gas;
	private String cost;
	private boolean status;

	public ConsumptionCostReport() {
	}

	public ConsumptionCostReport(String electricity, String gas, String cost, boolean status) {
		this.electricity = electricity;
		this.gas = gas;
		this.cost = cost;
		this.status = status;
	}

	public String getElectricity() {
		return electricity;
	}

	public void setElectricity(String electricity) {
		this.electricity = electricity;
	}

	public String getGas() {
		return gas;
	}

	public void setGas(String gas) {
		this.gas = gas;
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
