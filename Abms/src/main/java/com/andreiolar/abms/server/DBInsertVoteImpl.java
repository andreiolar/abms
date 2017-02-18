package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import com.andreiolar.abms.client.rpc.DBInsertVote;
import com.andreiolar.abms.mail.MailSender;
import com.andreiolar.abms.shared.Vote;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBInsertVoteImpl extends RemoteServiceServlet implements DBInsertVote {

	private static final long serialVersionUID = 2148006173124583692L;

	@Override
	public Boolean insertVote(Vote vote, String voteDescription) throws Exception {
		Boolean result = new Boolean(false);
		int execute = 0;
		int executeDescription = 0;
		int size = 0;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = MyConnection.getConnection();

			try {

				String voteId = vote.getVoteId();
				String active = null;
				List<String> voteOptions = null;
				size = voteOptions.size();

				String q = "insert into votes(vote_option, vote_id, active) values(?,?,?)";

				StringBuilder sb = new StringBuilder();
				sb.append("Voting Options: <br>");

				for (String voteOption : voteOptions) {
					stmt = conn.prepareStatement(q);
					stmt.setString(1, voteOption);
					stmt.setInt(2, Integer.parseInt(voteId));
					stmt.setString(3, active);
					sb.append("<b>" + voteOption + "</b><br>");

					execute += stmt.executeUpdate();
				}

				String q2 = "select first_name, last_name, email from user_info";
				stmt = conn.prepareStatement(q2);

				rs = stmt.executeQuery();

				while (rs.next()) {
					String firstName = rs.getString("first_name");
					String lastName = rs.getString("last_name");
					String email = rs.getString("email");

					String subject = "New Voting Session";
					String to = email;
					String message = "<p>" + "Hello " + firstName + " " + lastName + "," + "<br><br>" + "A new voting session has started."
							+ "<br><br>" + sb.toString() + "<br>" + "Fell free to vote!" + "<br><br>" + "Best regards," + "<br>" + "Administration"
							+ "</p>";

					MailSender.sendMail(subject, to, message, null);
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				stmt.close();
			}

			try {
				String q = "insert into vote_descriptions(vote_id, description) values(?,?)";
				stmt = conn.prepareStatement(q);
				stmt.setInt(1, Integer.parseInt(vote.getVoteId()));
				stmt.setString(2, voteDescription);

				executeDescription = stmt.executeUpdate();
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				stmt.close();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			conn.close();
		}

		if (execute != size) {
			throw new Exception("Error creating voting session!");
		}

		if (executeDescription == 0) {
			throw new Exception("Error inserting vote description!");
		}

		if (execute == size && executeDescription > 0) {
			result = new Boolean(true);
		}

		if (result == null || result.booleanValue() == false) {
			throw new Exception("Something went wrong during creating the voting session!");
		}

		return result;
	}

}
