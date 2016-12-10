package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import com.andreiolar.abms.client.rpc.DBForgotPassword;
import com.andreiolar.abms.mail.MailSender;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBForgotPasswordImpl extends RemoteServiceServlet implements DBForgotPassword {

	private static final long serialVersionUID = 681017190615010623L;

	@Override
	public Boolean sendMailToServer(String email) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		boolean isValidEmail = false;
		int executed = 0;

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "select email from user_info where email=?";
				stmt = conn.prepareStatement(q);
				stmt.setString(1, email);

				rs = stmt.executeQuery();

				if (rs.next()) {
					isValidEmail = true;
				}

			} catch (SQLException ex) {
				ex.printStackTrace();
			} finally {
				rs.close();
				stmt.close();
			}

			if (isValidEmail) {
				try {
					String q = "INSERT INTO password_recovery(email, token) VALUES (?, ?) ON DUPLICATE KEY UPDATE token = VALUES(token)";
					stmt = conn.prepareStatement(q);
					stmt.setString(1, email);

					Random random = new Random();
					int token = random.nextInt(999999 - 1 + 1) + 1;
					stmt.setString(2, String.valueOf(token));

					executed = stmt.executeUpdate();

					if (executed > 0) {
						String subject = "Password Recovery";
						String to = email;
						String message = "<p>"
								+ "You have successuflly requested a new password reset. In order to reset your password please use the token below."
								+ "</p>" + "<p>Token: <b>" + token + "</b></p>" + "<br />" + "Thank you,<br />" + "Administration";
						MailSender.sendMail(subject, to, message, null);
					}
				} catch (SQLException sqle) {
					sqle.printStackTrace();
				} finally {
					stmt.close();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			conn.close();
		}

		return true;
	}

}
