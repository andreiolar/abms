package com.andreiolar.abms.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Vote implements IsSerializable {

	private List<String> voteOption;
	private String voteId;
	private String active;

	public Vote() {
	}

	public Vote(List<String> voteOption, String voteId, String active) {
		this.voteOption = voteOption;
		this.voteId = voteId;
		this.active = active;
	}

	public List<String> getVoteOption() {
		return voteOption;
	}

	public String getVoteId() {
		return voteId;
	}

	public String getActive() {
		return active;
	}

	public void setVoteOption(List<String> voteOption) {
		this.voteOption = voteOption;
	}

	public void setVoteId(String voteId) {
		this.voteId = voteId;
	}

	public void setActive(String active) {
		this.active = active;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((active == null) ? 0 : active.hashCode());
		result = prime * result + ((voteId == null) ? 0 : voteId.hashCode());
		result = prime * result + ((voteOption == null) ? 0 : voteOption.hashCode());
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
		if (active == null) {
			if (other.active != null)
				return false;
		} else if (!active.equals(other.active))
			return false;
		if (voteId == null) {
			if (other.voteId != null)
				return false;
		} else if (!voteId.equals(other.voteId))
			return false;
		if (voteOption == null) {
			if (other.voteOption != null)
				return false;
		} else if (!voteOption.equals(other.voteOption))
			return false;
		return true;
	}

}
