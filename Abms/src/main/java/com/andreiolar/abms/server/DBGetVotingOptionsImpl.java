package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.andreiolar.abms.client.rpc.DBGetVotingOptions;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBGetVotingOptionsImpl extends RemoteServiceServlet implements DBGetVotingOptions {

	private static final long serialVersionUID = 659952181143328770L;

	@Override
	public List<String> getVotingOptions(String username) throws Exception {

		List<String> votingOptions = new ArrayList<String>();
		String votedOption = null;
		boolean alreadyVoted = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "select vote_option from user_votes where apartment_number=(select apartment_number from user_info where username=?) and vote_id=(select vote_id from votes where active='true' group by vote_id)";
				stmt = conn.prepareStatement(q);
				stmt.setString(1, username);

				rs = stmt.executeQuery();

				if (rs.next()) {
					alreadyVoted = true;
					votedOption = rs.getString("vote_option");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				rs.close();
				stmt.close();
			}

			try {
				String q = "select * from votes where active='true'";
				stmt = conn.prepareStatement(q);
				rs = stmt.executeQuery();

				while (rs.next()) {
					String voteOption = rs.getString("vote_option");
					votingOptions.add(voteOption);
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
			rs.close();
			stmt.close();
			conn.close();
		}

		if (alreadyVoted) {
			throw new Exception(
					"You have already voted for this voting session, therefore voting is not possible!<br>Your voting option: " + votedOption);
		}

		if (votingOptions == null || votingOptions.isEmpty()) {
			throw new Exception("No active voting options available!");
		}

		return votingOptions;
	}

}
