package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.andreiolar.abms.client.exception.VoteSubmissionException;
import com.andreiolar.abms.client.rpc.DBSubmitVote;
import com.andreiolar.abms.mail.MailSender;
import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBSubmitVoteImpl extends RemoteServiceServlet implements DBSubmitVote {

	private static final long serialVersionUID = -2156996004200792183L;

	@Override
	public void submitVoteToDB(String voteId, String option, String title, String description, UserDetails userDetails) throws Exception {
		int result = 0;
		int result2 = 0;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int id = Integer.parseInt(voteId);

		boolean done = false;

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "update votes set number_of_votes = number_of_votes + 1 where vote_id=? and vote_option=?";
				stmt = conn.prepareStatement(q);
				stmt.setInt(1, id);
				stmt.setString(2, option);

				result = stmt.executeUpdate();

			} catch (Exception e) {
				throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
			} finally {
				stmt.close();
			}

			if (result > 0) {
				try {
					String q = "insert into user_votes(apartment_number, vote_id, vote_option) values(?,?,?)";
					stmt = conn.prepareStatement(q);
					stmt.setString(1, userDetails.getApartmentNumber());
					stmt.setString(2, voteId);
					stmt.setString(3, option);

					result2 = stmt.executeUpdate();
				} catch (Exception e) {
					throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
				} finally {
					stmt.close();
				}

				if (result2 > 0) {
					done = true;

					String subject = "Voted";
					String to = userDetails.getEmail();
					String message = "<p>" + "Hello " + userDetails.getFirstName() + " " + userDetails.getLastName() + "," + "<br><br>"
							+ "You have successfully voted to the voting session:<br/>" + "Vote ID: <b>" + voteId + "</b>." + "<br>"
							+ "Vote title: <b>" + title + "</b>" + "<br>" + "" + "Vote description: <b>" + description + "</b><br/>"
							+ "You voted: <b>" + option + "</b><br/><br/>" + "Best regards," + "<br>" + "Administration" + "</p>";

					MailSender.sendMail(subject, to, message, null);
				}

			}
		} catch (Exception e) {
			throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
		} finally {
			conn.close();
		}

		if (!done) {
			throw new VoteSubmissionException("Error submitting vote. Please tey again.");
		}
	}
}
