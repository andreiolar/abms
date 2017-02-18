package com.andreiolar.abms.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Vote implements IsSerializable {

	private String voteId;
	private String title;
	private String description;
	private boolean active;
	private List<String> voteOptions;

	public Vote() {
	}

	public Vote(String voteId, String title, String description, boolean active, List<String> voteOptions) {
		this.voteId = voteId;
		this.title = title;
		this.description = description;
		this.active = active;
		this.voteOptions = voteOptions;
	}

	public String getVoteId() {
		return voteId;
	}

	public List<String> getVoteOptions() {
		return voteOptions;
	}

	public void setVoteOptions(List<String> voteOptions) {
		this.voteOptions = voteOptions;
	}

	public void setVoteId(String voteId) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((voteId == null) ? 0 : voteId.hashCode());
		result = prime * result + ((voteOptions == null) ? 0 : voteOptions.hashCode());
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
		Vote other = (Vote) obj;
		if (active != other.active)
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (voteId == null) {
			if (other.voteId != null)
				return false;
		} else if (!voteId.equals(other.voteId))
			return false;
		if (voteOptions == null) {
			if (other.voteOptions != null)
				return false;
		} else if (!voteOptions.equals(other.voteOptions))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Vote [voteId=" + voteId + ", title=" + title + ", description=" + description + ", active=" + active + ", voteOptions=" + voteOptions
				+ "]";
	}
}
