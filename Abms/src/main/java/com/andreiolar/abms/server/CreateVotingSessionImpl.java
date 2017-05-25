package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Set;

import com.andreiolar.abms.client.rpc.CreateVotingSession;
import com.andreiolar.abms.mail.MailSender;
import com.andreiolar.abms.shared.VoteSession;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class CreateVotingSessionImpl extends RemoteServiceServlet implements CreateVotingSession {

	private static final long serialVersionUID = 6909478527642162465L;

	@Override
	public Void createVotingSession(VoteSession voteSession, Set<String> votingOptions) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		int generatedId = 0;
		int executed = 0;

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "insert into vote_descriptions(title, description, active) values(?,?,?)";
				stmt = conn.prepareStatement(q, Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1, voteSession.getTitle());
				stmt.setString(2, voteSession.getDescription());
				stmt.setBoolean(3, true);

				stmt.executeUpdate();

				ResultSet rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					generatedId = rs.getInt(1);
				}
			} catch (Exception e) {
				throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
			} finally {
				stmt.close();
			}

			if (generatedId != 0) {
				try {
					for (String voteOption : votingOptions) {
						String q = "insert into votes(vote_option, number_of_votes, vote_id) values(?,?,?)";
						stmt = conn.prepareStatement(q);
						stmt.setString(1, voteOption);
						stmt.setInt(2, 0);
						stmt.setInt(3, generatedId);

						executed += stmt.executeUpdate();
					}
				} catch (Exception e) {
					throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
				} finally {
					stmt.close();
				}
			}

			if (executed != 0) {
				try {
					String q = "select first_name, last_name, email from user_info";
					stmt = conn.prepareStatement(q);

					ResultSet rs = stmt.executeQuery();

					while (rs.next()) {
						String firstName = rs.getString("first_name");
						String lastName = rs.getString("last_name");
						String email = rs.getString("email");

						if (firstName.contains("Administrator") || lastName.contains("Administrator")) {
							continue;
						}

						String subject = "New Voting Session";
						String to = email;
						String message = "<p>" + "Hello " + firstName + " " + lastName + "," + "<br><br>"
								+ "A new Voting Session has been created. Voting is possible under Voting -&gt; Vote" + "<br><br>" + "Vote title: <b>"
								+ voteSession.getTitle() + "</b>" + "<br>" + "" + "Vote description: <b>" + voteSession.getDescription() + "</b><br/>"
								+ "Please express your opinion regarding the above described topic by voting in this Voting Session." + "<br><br>"
								+ "Best regards," + "<br>" + "Administration" + "</p>";

						MailSender.sendMail(subject, to, message, null);
					}

				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					stmt.close();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
		} finally {
			conn.close();
		}

		if (executed == 0) {
			throw new RuntimeException("An unexpected error ocurred. Please contact your System Administrator.");
		}

		return null;
	}

}
