package com.andreiolar.abms.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ComplaintInfo implements IsSerializable {

	private String complaintId;
	private String name;
	private String phoneNumber;
	private String date;
	private String complaintTo;

	public ComplaintInfo() {
	}

	public ComplaintInfo(String complaintId, String name, String phoneNumber, String date, String complaintTo) {
		this.complaintId = complaintId;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.date = date;
		this.complaintTo = complaintTo;
	}

	public String getComplaintId() {
		return complaintId;
	}

	public String getName() {
		return name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getDate() {
		return date;
	}

	public String getComplaintTo() {
		return complaintTo;
	}

	public void setComplaintId(String complaintId) {
		this.complaintId = complaintId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setComplaintTo(String complaintTo) {
		this.complaintTo = complaintTo;
	}

}
