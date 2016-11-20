package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.andreiolar.abms.client.rpc.DBGetUserVote;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBGetUserVoteImpl extends RemoteServiceServlet implements DBGetUserVote {

	private static final long serialVersionUID = -2960794497716533763L;

	@Override
	public String getUserVote(String username, String voteId) throws Exception {

		String votedOption = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "select vote_option from user_votes where apartment_number=(select apartment_number from user_info where username=?) and vote_id=?";
				stmt = conn.prepareStatement(q);
				stmt.setString(1, username);
				stmt.setInt(2, Integer.valueOf(voteId));
				rs = stmt.executeQuery();

				while (rs.next()) {
					votedOption = rs.getString("vote_option");
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

		if (votedOption == null || votedOption.trim().equals("")) {
			throw new Exception("You did not vote in this voting session!");
		}

		return votedOption;
	}

}
