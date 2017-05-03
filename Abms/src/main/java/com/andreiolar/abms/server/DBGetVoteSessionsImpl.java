package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.andreiolar.abms.client.exception.NoFinishedVotingSessionFound;
import com.andreiolar.abms.client.rpc.DBGetVoteSessions;
import com.andreiolar.abms.shared.FinishedVoteSession;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBGetVoteSessionsImpl extends RemoteServiceServlet implements DBGetVoteSessions {

	private static final long serialVersionUID = 6649289418019191897L;

	@Override
	public Map<String, FinishedVoteSession> getVoteSessions(boolean active) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Map<String, FinishedVoteSession> result = null;

		try {
			conn = MyConnection.getConnection();

			try {
				String q = active ? "select * from vote_descriptions" : "select * from vote_descriptions where active=false";
				stmt = conn.prepareStatement(q);
				rs = stmt.executeQuery();

				result = new LinkedHashMap<>();
				while (rs.next()) {
					int voteId = rs.getInt("vote_id");
					String title = rs.getString("title");
					String description = rs.getString("description");

					String q2 = "select * from votes where vote_id=?";
					PreparedStatement stmt2 = conn.prepareStatement(q2);
					stmt2.setString(1, String.valueOf(voteId));

					ResultSet rs2 = stmt2.executeQuery();

					Map<String, Number> results = new HashMap<>();
					while (rs2.next()) {
						String voteOption = rs2.getString("vote_option");
						int numberOfVotes = rs2.getInt("number_of_votes");

						results.put(voteOption, numberOfVotes);
					}

					rs2.close();
					stmt2.close();

					FinishedVoteSession finishedVoteSession = new FinishedVoteSession(String.valueOf(voteId), title, description, results);
					result.put(String.valueOf(voteId), finishedVoteSession);
				}
			} catch (Exception e) {
				throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
			} finally {
				rs.close();
				stmt.close();
			}
		} catch (Exception e) {
			throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
		} finally {
			conn.close();
		}

		if (result == null || result.isEmpty()) {
			throw new NoFinishedVotingSessionFound();
		}

		return result;
	}
}
