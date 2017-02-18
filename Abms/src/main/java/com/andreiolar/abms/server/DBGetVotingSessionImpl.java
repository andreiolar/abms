package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.andreiolar.abms.client.exception.VoteOptionsNotFoundException;
import com.andreiolar.abms.client.exception.VoteSessionNotActiveException;
import com.andreiolar.abms.client.rpc.DBGetVotingSession;
import com.andreiolar.abms.shared.UserDetails;
import com.andreiolar.abms.shared.Vote;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBGetVotingSessionImpl extends RemoteServiceServlet implements DBGetVotingSession {

	private static final long serialVersionUID = 2843484355908749745L;

	@Override
	public Vote getVotingSession(UserDetails userDetails) throws Exception {
		Vote vote = new Vote();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		String option = null;

		boolean alreadyVoted = false;

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "select * from vote_descriptions where active=true";
				stmt = conn.prepareStatement(q);
				rs = stmt.executeQuery();

				// There should be only one entry
				if (rs.next()) {
					int voteId = rs.getInt("vote_id");
					String title = rs.getString("title");
					String description = rs.getString("description");

					vote.setVoteId(String.valueOf(voteId));
					vote.setTitle(title);
					vote.setDescription(description);
					vote.setActive(true);
				}
			} catch (Exception e) {
				throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
			} finally {
				rs.close();
				stmt.close();
			}

			if (vote.getVoteId() != null) {

				try {
					String q = "select * from user_votes where vote_id=? and apartment_number=? group by vote_id";
					stmt = conn.prepareStatement(q);
					stmt.setString(1, vote.getVoteId());
					stmt.setString(2, userDetails.getApartmentNumber());
					rs = stmt.executeQuery();

					if (rs.next()) {
						alreadyVoted = true;
						option = rs.getString("vote_option");
					}
				} catch (Exception e) {
					throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
				} finally {
					rs.close();
					stmt.close();
				}

				try {
					String q = "select vote_option from votes where vote_id = ?";
					stmt = conn.prepareStatement(q);
					stmt.setInt(1, Integer.parseInt(vote.getVoteId()));
					rs = stmt.executeQuery();

					List<String> voteOptions = new ArrayList<>();
					while (rs.next()) {
						String voteOption = rs.getString("vote_option");
						voteOptions.add(voteOption);
					}

					vote.setVoteOptions(voteOptions);
				} catch (Exception e) {
					throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
				} finally {
					rs.close();
					stmt.close();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
		} finally {
			conn.close();
		}

		if (vote.getVoteId() == null) {
			throw new VoteSessionNotActiveException();
		}

		if (alreadyVoted) {
			return new Vote(null, option, null, false, null);
		}

		if (vote.getVoteOptions() == null || vote.getVoteOptions().isEmpty()) {
			throw new VoteOptionsNotFoundException();
		}

		return vote;
	}

}
