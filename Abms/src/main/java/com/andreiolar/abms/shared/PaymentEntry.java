package com.andreiolar.abms.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PaymentEntry implements IsSerializable {

	private String trasactionId;
	private String description;
	private String cost;
	private String date;

	public PaymentEntry() {
	}

	public PaymentEntry(String trasactionId, String description, String cost, String date) {
		this.trasactionId = trasactionId;
		this.description = description;
		this.cost = cost;
		this.date = date;
	}

	public String getTrasactionId() {
		return trasactionId;
	}

	public void setTrasactionId(String trasactionId) {
		this.trasactionId = trasactionId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
