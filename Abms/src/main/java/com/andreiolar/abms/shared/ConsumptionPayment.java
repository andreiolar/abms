package com.andreiolar.abms.shared;

import com.andreiolar.abms.client.constants.PriceConstants;
import com.google.gwt.user.client.rpc.IsSerializable;

public class ConsumptionPayment implements IsSerializable {

	private double totalCost = 0;

	public ConsumptionPayment() {
		// TODO Auto-generated constructor stub
	}

	public ConsumptionPayment(SelfReading reading) {
		int gas = Integer.parseInt(reading.getGaz());
		int electricity = Integer.parseInt(reading.getElectricity());

		this.totalCost = gas * PriceConstants.GAS_PRICE + electricity * PriceConstants.ELECTRICITY_PRICE;
	}

	public double getTotalCost() {
		return totalCost;
	}

}
