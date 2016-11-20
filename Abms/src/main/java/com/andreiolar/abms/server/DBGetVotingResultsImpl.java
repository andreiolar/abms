package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;

import com.andreiolar.abms.client.rpc.DBGetVotingResults;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBGetVotingResultsImpl extends RemoteServiceServlet implements DBGetVotingResults {

	private static final long serialVersionUID = 7161068729668304322L;

	@Override
	public Map<String, Number> getVotingResults(String voteId, boolean all) throws Exception {
		Map<String, Number> results = new LinkedHashMap<String, Number>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "select vote_option, number_of_votes from votes where active='false' and vote_id=?";

				if (all) {
					q = "select vote_option, number_of_votes from votes where vote_id=?";
				}

				stmt = conn.prepareStatement(q);
				stmt.setInt(1, Integer.parseInt(voteId));

				rs = stmt.executeQuery();

				while (rs.next()) {
					String voteOption = rs.getString("vote_option");
					Integer numberOfVotes = rs.getInt("number_of_votes");

					results.put(voteOption, numberOfVotes);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				rs.close();
				stmt.close();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			conn.close();
		}

		if (results == null || results.isEmpty()) {
			throw new Exception("Error getting voting details for voting session nr: " + voteId);
		}

		return results;
	}

}
