package com.andreiolar.abms.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UserCosts implements IsSerializable {

	private double upkeepCost;
	private double readingCost;
	private double totalCost;
	private String month;

	public UserCosts() {
	}

	public UserCosts(double upkeepCost, double readingCost, double totalCost, String month) {
		this.upkeepCost = upkeepCost;
		this.readingCost = readingCost;
		this.totalCost = totalCost;
		this.month = month;
	}

	public double getUpkeepCost() {
		return upkeepCost;
	}

	public void setUpkeepCost(double upkeepCost) {
		this.upkeepCost = upkeepCost;
	}

	public double getReadingCost() {
		return readingCost;
	}

	public void setReadingCost(double readingCost) {
		this.readingCost = readingCost;
	}

	public double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}
}
