package com.andreiolar.abms.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PersonalUpkeepInformationWrapper implements IsSerializable {

	private PersonalUpkeepInformation personalUpkeepInformation;
	private boolean status;

	public PersonalUpkeepInformationWrapper() {
	}

	public PersonalUpkeepInformationWrapper(PersonalUpkeepInformation personalUpkeepInformation, boolean status) {
		this.personalUpkeepInformation = personalUpkeepInformation;
		this.status = status;
	}

	public PersonalUpkeepInformation getPersonalUpkeepInformation() {
		return personalUpkeepInformation;
	}

	public void setPersonalUpkeepInformation(PersonalUpkeepInformation personalUpkeepInformation) {
		this.personalUpkeepInformation = personalUpkeepInformation;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}
