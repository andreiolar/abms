package com.andreiolar.abms.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SelfReading implements IsSerializable {

	private String aptNumber;
	private String coldWater;
	private String hotWater;
	private String electricity;
	private String gaz;
	private String date;

	public SelfReading() {
	}

	public SelfReading(String aptNumber, String coldWater, String hotWater, String electricity, String gaz, String date) {
		this.aptNumber = aptNumber;
		this.coldWater = coldWater;
		this.hotWater = hotWater;
		this.electricity = electricity;
		this.gaz = gaz;
		this.date = date;
	}

	public SelfReading(String aptNumber, String coldWater, String hotWater, String electricity, String gaz) {
		this.aptNumber = aptNumber;
		this.coldWater = coldWater;
		this.hotWater = hotWater;
		this.electricity = electricity;
		this.gaz = gaz;
	}

	public SelfReading(String electricity, String gaz) {
		this.electricity = electricity;
		this.gaz = gaz;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aptNumber == null) ? 0 : aptNumber.hashCode());
		result = prime * result + ((coldWater == null) ? 0 : coldWater.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((electricity == null) ? 0 : electricity.hashCode());
		result = prime * result + ((gaz == null) ? 0 : gaz.hashCode());
		result = prime * result + ((hotWater == null) ? 0 : hotWater.hashCode());
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
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
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
		return true;
	}
}
