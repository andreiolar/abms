package com.andreiolar.abms.shared;

import com.andreiolar.abms.client.constants.PriceConstants;

public class ConsumptionPayment implements Payment {

	private SelfReading reading;

	public ConsumptionPayment() {
	}

	public ConsumptionPayment(SelfReading reading) {
		this.reading = reading;

	}

	@Override
	public double getTotalCost() {
		int gas = Integer.parseInt(reading.getGaz());
		int electricity = Integer.parseInt(reading.getElectricity());

		return gas * PriceConstants.GAS_PRICE + electricity * PriceConstants.ELECTRICITY_PRICE;
	}

}
