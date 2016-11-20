package com.andreiolar.abms.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ContactInfo implements IsSerializable {

	private String familyName;
	private String contactPerson;
	private String apartmentNumber;
	private String email;
	private String phoneNumber;

	public ContactInfo() {
	}

	public ContactInfo(String familyName, String contactPerson, String apartmentNumber, String email, String phoneNumber) {
		this.familyName = familyName;
		this.contactPerson = contactPerson;
		this.apartmentNumber = apartmentNumber;
		this.email = email;
		this.phoneNumber = phoneNumber;
	}

	public String getFamilyName() {
		return familyName;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public String getApartmentNumber() {
		return apartmentNumber;
	}

	public String getEmail() {
		return email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public void setApartmentNumber(String apartmentNumber) {
		this.apartmentNumber = apartmentNumber;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

}
