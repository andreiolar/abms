package com.andreiolar.abms.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ReplyMessage implements IsSerializable {

	private String text;
	private String username;
	private int convId;

	public ReplyMessage() {
	}

	public ReplyMessage(String text, String username, int lastId) {
		super();
		this.text = text;
		this.username = username;
		this.convId = lastId;
	}

	public String getText() {
		return text;
	}

	public String getUsername() {
		return username;
	}

	public int getConvId() {
		return convId;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setConvId(int convId) {
		this.convId = convId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + convId;
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
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
		ReplyMessage other = (ReplyMessage) obj;
		if (convId != other.convId)
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

}
