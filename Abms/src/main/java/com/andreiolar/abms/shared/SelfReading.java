package com.andreiolar.abms.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SelfReading implements IsSerializable {

	private String aptNumber;
	private String coldWater;
	private String hotWater;
	private String electricity;
	private String gaz;
	private String month;

	public SelfReading() {
	}

	public SelfReading(String aptNumber, String coldWater, String hotWater, String electricity, String gaz, String month) {
		this.aptNumber = aptNumber;
		this.coldWater = coldWater;
		this.hotWater = hotWater;
		this.electricity = electricity;
		this.gaz = gaz;
		this.month = month;
	}

	public String getAptNumber() {
		return aptNumber;
	}

	public void setAptNumber(String aptNumber) {
		this.aptNumber = aptNumber;
	}

	public String getColdWater() {
		return coldWater;
	}

	public String getHotWater() {
		return hotWater;
	}

	public String getElectricity() {
		return electricity;
	}

	public String getGaz() {
		return gaz;
	}

	public void setColdWater(String coldWater) {
		this.coldWater = coldWater;
	}

	public void setHotWater(String hotWater) {
		this.hotWater = hotWater;
	}

	public void setElectricity(String electricity) {
		this.electricity = electricity;
	}

	public void setGaz(String gaz) {
		this.gaz = gaz;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aptNumber == null) ? 0 : aptNumber.hashCode());
		result = prime * result + ((coldWater == null) ? 0 : coldWater.hashCode());
		result = prime * result + ((electricity == null) ? 0 : electricity.hashCode());
		result = prime * result + ((gaz == null) ? 0 : gaz.hashCode());
		result = prime * result + ((hotWater == null) ? 0 : hotWater.hashCode());
		result = prime * result + ((month == null) ? 0 : month.hashCode());
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
		SelfReading other = (SelfReading) obj;
		if (aptNumber == null) {
			if (other.aptNumber != null)
				return false;
		} else if (!aptNumber.equals(other.aptNumber))
			return false;
		if (coldWater == null) {
			if (other.coldWater != null)
				return false;
		} else if (!coldWater.equals(other.coldWater))
			return false;
		if (electricity == null) {
			if (other.electricity != null)
				return false;
		} else if (!electricity.equals(other.electricity))
			return false;
		if (gaz == null) {
			if (other.gaz != null)
				return false;
		} else if (!gaz.equals(other.gaz))
			return false;
		if (hotWater == null) {
			if (other.hotWater != null)
				return false;
		} else if (!hotWater.equals(other.hotWater))
			return false;
		if (month == null) {
			if (other.month != null)
				return false;
		} else if (!month.equals(other.month))
			return false;
		return true;
	}

}
