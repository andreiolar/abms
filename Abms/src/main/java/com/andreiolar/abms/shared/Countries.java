package com.andreiolar.abms.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Countries implements IsSerializable {

	private List<String> countries;

	private Countries() {
	}

	public Countries(List<String> countries) {
		this.countries = countries;
	}

	public List<String> getCountries() {
		return countries;
	}

	public void setCountries(List<String> countries) {
		this.countries = countries;
	}

}
