package com.andreiolar.abms.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Message implements IsSerializable {

	private int number;
	private String message;
	private String from;
	private String date;

	public Message() {
	}

	public Message(int number, String message, String from, String date) {
		this.number = number;
		this.message = message;
		this.from = from;
		this.date = date;
	}

	public int getNumber() {
		return number;
	}

	public String getMessage() {
		return message;
	}

	public String getFrom() {
		return from;
	}

	public String getDate() {
		return date;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + number;
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
		Message other = (Message) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (number != other.number)
			return false;
		return true;
	}

}
