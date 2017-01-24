package com.andreiolar.abms.server;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.andreiolar.abms.client.constants.UserTypes;
import com.andreiolar.abms.client.exception.UsernameUnavailableException;
import com.andreiolar.abms.client.rpc.DBRegisterUser;
import com.andreiolar.abms.mail.MailSender;
import com.andreiolar.abms.security.BCrypt;
import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBRegisterUserImpl extends RemoteServiceServlet implements DBRegisterUser {

	private static final long serialVersionUID = 1L;

	public DBRegisterUserImpl() {
	}

	@SuppressWarnings("resource")
	@Override
	public Boolean registerUser(UserDetails userDetails) throws UsernameUnavailableException {
		Boolean result = null;
		Connection conn = null;
		PreparedStatement stmt = null;

		boolean usernameAlreadyExists = false;
		String aptNumber = null;

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "select * from users where BINARY username=?";
				stmt = conn.prepareStatement(q);
				stmt.setString(1, userDetails.getUsername());
				ResultSet rs = stmt.executeQuery();

				if (rs.next()) {
					usernameAlreadyExists = true;
				}

				rs.close();
			} catch (SQLException sqle) {
				throw new RuntimeException("Something went wrong when checking for username: " + sqle.getMessage(), sqle);
			}

			try {
				String q = "select apartment_number from email_for_registration where email=?";
				stmt = conn.prepareStatement(q);
				stmt.setString(1, userDetails.getEmail());
				ResultSet rs = stmt.executeQuery();

				while (rs.next()) {
					aptNumber = rs.getString("apartment_number");
				}

				rs.close();
			} catch (SQLException sqle) {

			}

			if (!usernameAlreadyExists && aptNumber != null) {
				try {
					String q = "insert into user_info(first_name, last_name, date_of_birth, email, mobile_number, gender, address, city, country, personal_number, id_series, username, apartment_number) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
					stmt = conn.prepareStatement(q);
					stmt.setString(1, userDetails.getFirstName());
					stmt.setString(2, userDetails.getLastName());
					java.util.Date date = userDetails.getDateOfBirth();
					Date sqlDate = new Date(date.getTime());
					stmt.setDate(3, sqlDate);
					stmt.setString(4, userDetails.getEmail());
					stmt.setString(5, userDetails.getMobileNumber());
					stmt.setString(6, userDetails.getGender());
					stmt.setString(7, userDetails.getAddress());
					stmt.setString(8, userDetails.getCity());
					stmt.setString(9, userDetails.getCountry());
					stmt.setString(10, userDetails.getPersonalNumber());
					stmt.setString(11, userDetails.getIdSeries());
					stmt.setString(12, userDetails.getUsername());
					stmt.setString(13, aptNumber);

					stmt.executeUpdate();
				} catch (Exception ex) {
					throw new RuntimeException("Something went wrong when registering user details: " + ex.getMessage(), ex);
				}

				try {
					String q = "insert into users(username, password, type) values(?,?,?)";
					stmt = conn.prepareStatement(q);
					stmt.setString(1, userDetails.getUsername());

					// Hash the password
					String hashedPassword = BCrypt.hashpw(userDetails.getPassword(), BCrypt.gensalt());

					stmt.setString(2, hashedPassword);
					stmt.setString(3, UserTypes.USER);

					stmt.executeUpdate();
				} catch (Exception ex) {
					throw new RuntimeException("Something went wrong when registering user credentials: " + ex.getMessage(), ex);
				}

				try {
					String q = "delete from email_for_registration where email=?";
					stmt = conn.prepareStatement(q);
					stmt.setString(1, userDetails.getEmail());

					stmt.executeUpdate();
				} catch (Exception ex) {
					throw new RuntimeException(
							"Something when wrong when deleting already registered user E-Mail Address from waiting list: " + ex.getMessage(), ex);
				}

				String subject = "Welcome " + userDetails.getFirstName() + " " + userDetails.getLastName();
				String to = userDetails.getEmail();
				String message = "<p>" + subject + "," + "<br><br>" + "You have been successfully registered to your ABMS." + "<br>"
						+ "If you have questions please don't hesitate to contact your Administrator. You'll find all necesarry contact information, by clicking the <b>Hotline</b> Button on your hommepage, or by navigating to Help -&gt; About."
						+ "<br><br>" + "Login information:" + "<br>" + "Username: " + userDetails.getUsername() + "<br>" + "Password: NOT VISIBLE"
						+ "<br><br>" + "In case you forget your password please use the form available on the Login Page." + "<br><br>"
						+ "In case of found bugs or software suggestions please contact your Administrator." + "<br><br>" + "Best regards," + "<br>"
						+ "Administration" + "</p>";

				MailSender.sendMail(subject, to, message, null);

				String femalePicture = System.getProperty("user.dir") + "/images/icons/female.png";
				String malePicture = System.getProperty("user.dir") + "/images/icons/male.png";

				String userDirectory = userDetails.getUsername().replaceAll("\\.", "");
				String destination = System.getProperty("user.dir") + "/files/profile_pictures/" + userDirectory + ".png";

				File destinationFile = new File(destination);
				if (destinationFile.getParentFile() != null) {
					destinationFile.getParentFile().mkdirs();
				}

				if (userDetails.getGender().equals("Female")) {
					Files.copy(new File(femalePicture).toPath(), new File(destination).toPath(), StandardCopyOption.REPLACE_EXISTING);
				} else {
					Files.copy(new File(malePicture).toPath(), new File(destination).toPath(), StandardCopyOption.REPLACE_EXISTING);
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException("Something went wrong: " + ex.getMessage(), ex);
		} finally {
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
			}

		}

		if (usernameAlreadyExists) {
			throw new UsernameUnavailableException("Username already exists. Please choose another one.");
		}

		if (aptNumber == null) {
			throw new RuntimeException("Error retrieving apartament number for registering user.");
		}

		return result;
	}

}
