package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.andreiolar.abms.client.rpc.DBGetVotingDescription;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBGetVotingDescriptionImpl extends RemoteServiceServlet implements DBGetVotingDescription {

	private static final long serialVersionUID = 1293377367544175194L;

	@Override
	public String getVotingDescription() throws Exception {
		String description = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "select description from vote_descriptions where vote_id=(select vote_id from votes where active='true' group by vote_id)";
				stmt = conn.prepareStatement(q);
				rs = stmt.executeQuery();

				while (rs.next()) {
					String desc = rs.getString("description");
					description = desc;
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

		if (description == null) {
			throw new Exception("Error getting vote description!");
		}

		return description;
	}

}
