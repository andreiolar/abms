package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.andreiolar.abms.client.rpc.DBConnection;
import com.andreiolar.abms.mail.MailSender;
import com.andreiolar.abms.security.BCrypt;
import com.andreiolar.abms.shared.User;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBConnectionImpl extends RemoteServiceServlet implements DBConnection {

	private static final long serialVersionUID = 1L;

	private String URL = new String("jdbc:mysql://localhost:3306");
	private String user = "root";
	private String pass = "andrei";
	private String schema = "administrare_bloc";

	public DBConnectionImpl() {
	}

	private Connection getConnection() throws Exception {
		Properties props = new Properties();
		props.setProperty("user", user);
		props.setProperty("password", pass);
		props.setProperty("zeroDateTimeBehavior", "convertToNull");
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection conn = DriverManager.getConnection(URL + "/" + schema, props);

		return conn;
	}

	@Override
	public User authenticateUser(String username, String password) throws Exception {

		User returnUser = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;
		PreparedStatement stmt4 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		String locked = null;

		try {
			conn = getConnection();

			try {

				String q = "select * from users where BINARY username=?";
				stmt = conn.prepareStatement(q);
				stmt.setString(1, username);
				rs = stmt.executeQuery();

				while (rs.next()) {
					int id = rs.getInt("id");
					String user = rs.getString("username");
					String pass = rs.getString("password");
					String type = rs.getString("type");

					long currentTimeMillis = System.currentTimeMillis();
					long validAttempts = currentTimeMillis - (1000 * 60 * 60 * 2); // last 2 hours

					String q2 = "select time from login_attempts where user_id=? and time > ?";
					stmt2 = conn.prepareStatement(q2);
					stmt2.setInt(1, id);
					stmt2.setLong(2, validAttempts);

					rs2 = stmt2.executeQuery();

					int counter = 0;

					while (rs2.next()) {
						counter++;
					}

					if (counter > 5) {

						String q3 = "select first_name, last_name, email from user_info where username=?";
						stmt3 = conn.prepareStatement(q3);
						stmt3.setString(1, user);

						rs3 = stmt3.executeQuery();
						String email = null;
						String firstName = null;
						String lastName = null;
						while (rs3.next()) {
							email = rs3.getString("email");
							firstName = rs3.getString("first_name");
							lastName = rs3.getString("last_name");
						}

						String subject = "Account locked";
						String to = email;
						String message = "<p>" + "Hello " + firstName + " " + lastName + "," + "<br><br>"
								+ "It seems that somebody tried to access your account. The password was entered wrong 5 times, so we decided to lock your account!"
								+ "<br><br>"
								+ "If you lost your password you can always get it back by using the 'Forgot Password' Form. Your account will be automatically unlocked in 2 hours, but we advise you to change your password as soon as possible!"
								+ "<br>Retrying the login procedure with a wrong password will only prolongue your locked time, so please wait for 2 hours."
								+ "<br>" + "Thank you very much for understanding!" + "<br><br>" + "Best regards," + "<br>" + "Administration"
								+ "</p>";

						MailSender.sendMail(subject, to, message, null);
						locked = "locked";
						throw new Exception("Account locked! You have been sent an E-Mail. Please check your E-Mail!");
					}

					if (BCrypt.checkpw(password, pass)) {
						returnUser = new User(id, user, pass, type);

						HttpServletRequest threadLocalRequest = this.getThreadLocalRequest();
						HttpSession session = threadLocalRequest.getSession();
						session.setAttribute("user", user);
					} else {
						String q4 = "insert into login_attempts(user_id, time) values(?,?)";
						stmt4 = conn.prepareStatement(q4);
						stmt4.setInt(1, id);
						stmt4.setLong(2, validAttempts);

						stmt4.executeUpdate();

					}

				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			rs.close();
			stmt.close();
			conn.close();
		}

		if (locked != null) {
			throw new Exception("Account locked! You have been sent an E-Mail. Please check your E-Mail!");
		}

		if (returnUser == null) {
			throw new Exception("Wrong username or password!");
		}

		return returnUser;
	}

}
