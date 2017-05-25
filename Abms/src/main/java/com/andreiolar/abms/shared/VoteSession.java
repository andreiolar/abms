package com.andreiolar.abms.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class VoteSession implements IsSerializable {

	private int voteId;
	private String title;
	private String description;
	private boolean active;

	public VoteSession() {
	}

	public VoteSession(int voteId, String title, String description, boolean active) {
		this.voteId = voteId;
		this.title = title;
		this.description = description;
		this.active = active;
	}

	public int getVoteId() {
		return voteId;
	}

	public void setVoteId(int voteId) {
		this.voteId = voteId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
