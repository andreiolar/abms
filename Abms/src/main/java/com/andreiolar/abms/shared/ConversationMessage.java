package com.andreiolar.abms.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ConversationMessage implements IsSerializable {

	private String id;
	private String username;
	private String gender;
	private String message;
	private String date;

	public ConversationMessage() {
	}

	public ConversationMessage(String id, String username, String gender, String message, String date) {
		this.id = id;
		this.username = username;
		this.gender = gender;
		this.message = message;
		this.date = date;
	}

	public ConversationMessage(String id, String username, String message, String date) {
		this.id = id;
		this.username = username;
		this.message = message;
		this.date = date;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
