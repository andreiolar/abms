package com.andreiolar.abms.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Email implements IsSerializable {

	private String emailAddress;
	private String apartmentNumber;

	private Email() {
	}

	public Email(String emailAddress, String apartmentNumber) {
		this.emailAddress = emailAddress;
		this.apartmentNumber = apartmentNumber;
	}

	public String getApartmentNumber() {
		return apartmentNumber;
	}

	public void setApartmentNumber(String apartmentNumber) {
		this.apartmentNumber = apartmentNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((apartmentNumber == null) ? 0 : apartmentNumber.hashCode());
		result = prime * result + ((emailAddress == null) ? 0 : emailAddress.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Email other = (Email) obj;
		if (apartmentNumber == null) {
			if (other.apartmentNumber != null)
				return false;
		} else if (!apartmentNumber.equals(other.apartmentNumber))
			return false;
		if (emailAddress == null) {
			if (other.emailAddress != null)
				return false;
		} else if (!emailAddress.equals(other.emailAddress))
			return false;
		return true;
	}

}
