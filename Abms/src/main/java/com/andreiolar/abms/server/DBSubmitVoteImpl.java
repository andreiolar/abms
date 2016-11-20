package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.andreiolar.abms.client.rpc.DBSubmitVote;
import com.andreiolar.abms.mail.MailSender;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBSubmitVoteImpl extends RemoteServiceServlet implements DBSubmitVote {

	private static final long serialVersionUID = -2156996004200792183L;

	@Override
	public void submitVoteToDB(String option, String username) throws Exception {
		int result = 0;
		int result2 = 0;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "update votes set number_of_votes = number_of_votes + 1 where vote_option=? and active='true'";
				stmt = conn.prepareStatement(q);
				stmt.setString(1, option);

				result = stmt.executeUpdate();

			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				stmt.close();
			}

			try {
				String q = "insert into user_votes(apartment_number, vote_id, vote_option) values((select apartment_number from user_info where username=?),(select vote_id from votes where active='true' group by vote_id),?)";
				stmt = conn.prepareStatement(q);
				stmt.setString(1, username);
				stmt.setString(2, option);

				result2 = stmt.executeUpdate();

				String q2 = "select vote_id from votes where active='true' group by vote_id";
				stmt = conn.prepareStatement(q2);

				String voteId = null;
				rs = stmt.executeQuery();

				if (rs.next()) {
					voteId = rs.getString("vote_id");
				}

				String q3 = "select email, first_name, last_name from user_info where username=?";
				stmt = conn.prepareStatement(q3);
				stmt.setString(1, username);

				rs = stmt.executeQuery();

				if (rs.next()) {
					String email = rs.getString("email");
					String firstName = rs.getString("first_name");
					String lastName = rs.getString("last_name");

					String subject = "Voted";
					String to = email;
					String message = "<p>" + "Hello " + firstName + " " + lastName + "," + "<br><br>"
							+ "You have successfully voted to the voting session with ID: <b>" + voteId + "</b>." + "<br>" + "Your voting option: <b>"
							+ option + "</b>" + "<br><br>" + "Best regards," + "<br>" + "Administration" + "</p>";

					MailSender.sendMail(subject, to, message, null);
				}

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

		if (result == 0 && result2 == 0) {
			throw new Exception("Error when submitting vote!");
		}

	}

}
