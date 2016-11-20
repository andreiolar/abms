package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import com.andreiolar.abms.client.constants.UserTypes;
import com.andreiolar.abms.client.rpc.DBRegisterUser;
import com.andreiolar.abms.mail.MailSender;
import com.andreiolar.abms.security.BCrypt;
import com.andreiolar.abms.shared.UserInfo;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBRegisterUserImpl extends RemoteServiceServlet implements DBRegisterUser {

	private static final long serialVersionUID = 1L;

	private String URL = new String("jdbc:mysql://localhost:3306");
	private String user = "root";
	private String pass = "andrei";
	private String schema = "administrare_bloc";

	public DBRegisterUserImpl() {
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

	@SuppressWarnings("resource")
	@Override
	public Boolean registerUser(UserInfo userInfo) throws Exception {

		Boolean result = null;
		Connection conn = null;
		PreparedStatement stmt = null;

		int insertUserInfo = 0;
		int insertLoginCredentials = 0;
		int deleteUsedEmail = 0;

		try {
			conn = getConnection();

			try {
				String q = "insert into user_info(first_name, last_name, date_of_birth, email, mobile_number, gender, address, city, country, personal_number, id_series, username, apartment_number) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
				stmt = conn.prepareStatement(q);
				stmt.setString(1, userInfo.getFirstName());
				stmt.setString(2, userInfo.getLastName());
				java.util.Date date = userInfo.getDateOfBirth();
				Date sqlDate = new Date(date.getTime());
				stmt.setDate(3, sqlDate);
				stmt.setString(4, userInfo.getEmail());
				stmt.setString(5, userInfo.getMobileNumber());
				stmt.setString(6, userInfo.getGender());
				stmt.setString(7, userInfo.getAddress());
				stmt.setString(8, userInfo.getCity());
				stmt.setString(9, userInfo.getCountry());
				stmt.setString(10, userInfo.getPersonalNumber());
				stmt.setString(11, userInfo.getIdSeries());
				stmt.setString(12, userInfo.getUsername());
				stmt.setString(13, userInfo.getApartmentNumber());

				insertUserInfo = stmt.executeUpdate();
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			try {
				String q = "insert into users(username, password, type) values(?,?,?)";
				stmt = conn.prepareStatement(q);
				stmt.setString(1, userInfo.getUsername());

				// Hash the password
				String hashedPassword = BCrypt.hashpw(userInfo.getPassword(), BCrypt.gensalt());

				stmt.setString(2, hashedPassword);
				stmt.setString(3, UserTypes.USER);

				insertLoginCredentials = stmt.executeUpdate();
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			try {
				String q = "delete from email_for_registration where email=?";
				stmt = conn.prepareStatement(q);
				stmt.setString(1, userInfo.getEmail());

				deleteUsedEmail = stmt.executeUpdate();
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			String subject = "Welcome " + userInfo.getFirstName() + " " + userInfo.getLastName();
			String to = userInfo.getEmail();
			String message = "<p>" + subject + "," + "<br><br>" + "You have been successfully registered to your APMS." + "<br>"
					+ "If you have questions please don't hesitate to contact your Administrator. You'll find all necesarry contact information, by clicking the <b>Hotline</b> Button on your hommepage, or by navigating to Help -&gt; About."
					+ "<br><br>" + "Login information:" + "<br>" + "Username: " + userInfo.getUsername() + "<br>" + "Password: NOT VISIBLE"
					+ "<br><br>" + "In case you forget your password please use the form available on the Login Page." + "<br><br>"
					+ "In case of found bugs or software suggestions please contact your Administrator." + "<br><br>" + "Best regards," + "<br>"
					+ "Administration" + "</p>";

			MailSender.sendMail(subject, to, message, null);

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			stmt.close();
			conn.close();
		}

		if (insertLoginCredentials > 0 && insertUserInfo > 0 && deleteUsedEmail > 0) {
			result = new Boolean(true);
		}

		if (result == null) {
			throw new Exception("Error during DB processing!");
		}

		return result;
	}

}
