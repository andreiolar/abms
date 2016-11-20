package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.andreiolar.abms.client.rpc.DBForgotPassword;
import com.andreiolar.abms.mail.MailSender;
import com.andreiolar.abms.security.BCrypt;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBForgotPasswordImpl extends RemoteServiceServlet implements DBForgotPassword {

	private static final long serialVersionUID = 681017190615010623L;

	@Override
	public Boolean sendMailToServer(String email) throws Exception {
		Boolean result = new Boolean(false);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String token = null;
		int executed = 0;

		try {
			conn = MyConnection.getConnection();
			try {
				String q = "select email from user_info where email=?";
				stmt = conn.prepareStatement(q);
				stmt.setString(1, email);

				rs = stmt.executeQuery();

				if (rs.next()) {
					String q2 = "insert into password_recovery(email, token) values(?, ?)";
					stmt = conn.prepareStatement(q2);
					stmt.setString(1, email);
					token = BCrypt.hashpw(email, BCrypt.gensalt());
					stmt.setString(2, token);

					executed = stmt.executeUpdate();

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

		if (executed > 0) {
			String subject = "Password Recovery";
			String to = email;
			String message = "In order to reset your password plese click the link below! <br><br><a href=\"http://127.0.0.1:8888/AdministrareBloc.html#PasswordRecoveryPlace:"
					+ token + "\">Click Me!</a> ";

			MailSender.sendMail(subject, to, message, null);
		} else {
			throw new Exception("Error processing your request. Plese check your E-Mail Address again!");
		}

		return result;
	}

}
