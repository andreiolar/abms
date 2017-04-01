package com.andreiolar.abms.shared;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SubmittedComplaint implements IsSerializable {

	private int aptNumber;
	private Date dateSubmitted;
	private String complaintTo;
	private String phoneNumber;

	public SubmittedComplaint() {
	}

	public SubmittedComplaint(int aptNumber, Date dateSubmitted, String complaintTo, String phoneNumber) {
		this.aptNumber = aptNumber;
		this.dateSubmitted = dateSubmitted;
		this.complaintTo = complaintTo;
		this.phoneNumber = phoneNumber;
	}

	public int getAptNumber() {
		return aptNumber;
	}

	public void setAptNumber(int aptNumber) {
		this.aptNumber = aptNumber;
	}

	public Date getDateSubmitted() {
		return dateSubmitted;
	}

	public void setDateSubmitted(Date dateSubmitted) {
		this.dateSubmitted = dateSubmitted;
	}

	public String getComplaintTo() {
		return complaintTo;
	}

	public void setComplaintTo(String complaintTo) {
		this.complaintTo = complaintTo;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
