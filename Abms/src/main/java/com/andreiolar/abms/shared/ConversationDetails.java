package com.andreiolar.abms.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ConversationDetails implements IsSerializable {

	private int id;
	private String conversationWith;
	private String conversationWithFirstName;
	private String conversationWithLastName;
	private String conversationWithGender;
	private String lastMessage;
	private String lastMessageDate;

	public ConversationDetails() {
	}

	public ConversationDetails(String conversationWith, String conversationWithFirstName, String conversationWithLastName, String lastMessage,
			String lastMessageDate, String conversationWithGender) {
		this.conversationWith = conversationWith;
		this.conversationWithFirstName = conversationWithFirstName;
		this.conversationWithLastName = conversationWithLastName;
		this.lastMessage = lastMessage;
		this.lastMessageDate = lastMessageDate;
		this.conversationWithGender = conversationWithGender;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getConversationWith() {
		return conversationWith;
	}

	public void setConversationWith(String conversationWith) {
		this.conversationWith = conversationWith;
	}

	public String getConversationWithFirstName() {
		return conversationWithFirstName;
	}

	public void setConversationWithFirstName(String conversationWithFirstName) {
		this.conversationWithFirstName = conversationWithFirstName;
	}

	public String getConversationWithLastName() {
		return conversationWithLastName;
	}

	public void setConversationWithLastName(String conversationWithLastName) {
		this.conversationWithLastName = conversationWithLastName;
	}

	public String getLastMessage() {
		return lastMessage;
	}

	public void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}

	public String getLastMessageDate() {
		return lastMessageDate;
	}

	public void setLastMessageDate(String lastMessageDate) {
		this.lastMessageDate = lastMessageDate;
	}

	public String getConversationWithGender() {
		return conversationWithGender;
	}

	public void setConversationWithGender(String conversationWithGender) {
		this.conversationWithGender = conversationWithGender;
	}

	@Override
	public String toString() {
		return "ConversationDetails [id=" + id + ", conversationWith=" + conversationWith + ", conversationWithFirstName=" + conversationWithFirstName
				+ ", conversationWithLastName=" + conversationWithLastName + ", lastMessage=" + lastMessage + ", lastMessageDate=" + lastMessageDate
				+ "]";
	}
}
