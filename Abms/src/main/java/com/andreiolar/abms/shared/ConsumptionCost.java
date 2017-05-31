package com.andreiolar.abms.shared;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.andreiolar.abms.client.constants.PriceConstants;

public class ConsumptionCost implements Cost {

	private SelfReading reading;

	public ConsumptionCost() {
	}

	public ConsumptionCost(SelfReading reading) {
		this.reading = reading;
	}

	public double getElectricityCost() {
		double electricity = Integer.parseInt(reading.getElectricity()) * PriceConstants.ELECTRICITY_PRICE;
		BigDecimal electricityCost = new BigDecimal(electricity).setScale(2, RoundingMode.CEILING);

		return electricityCost.doubleValue();
	}

	public double getGasCost() {
		double gas = Integer.parseInt(reading.getGaz()) * PriceConstants.GAS_PRICE;
		BigDecimal gasCost = new BigDecimal(gas).setScale(2, RoundingMode.CEILING);

		return gasCost.doubleValue();
	}

	@Override
	public double getTotalCost() {
		return getElectricityCost() + getGasCost();
	}

}
