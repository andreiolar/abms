package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.andreiolar.abms.client.exception.UsernameUnavailableException;
import com.andreiolar.abms.client.rpc.DBUpdateProfile;
import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBUpdateProfileImpl extends RemoteServiceServlet implements DBUpdateProfile {

	private static final long serialVersionUID = -4095426760834709115L;

	@Override
	public boolean updateProfile(UserDetails newDetails, String updatedUsername) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;

		boolean usernameChanged = false;
		boolean usernameAlreadyExists = false;

		try {
			conn = MyConnection.getConnection();

			if (!newDetails.getUsername().equals(updatedUsername)) {
				try {
					String q = "select * from users where BINARY username=?";
					stmt = conn.prepareStatement(q);
					stmt.setString(1, newDetails.getUsername());
					ResultSet rs = stmt.executeQuery();

					if (rs.next()) {
						usernameAlreadyExists = true;
					}

					rs.close();
				} catch (Exception e) {
					throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
				} finally {
					stmt.close();
				}

				if (!usernameAlreadyExists) {
					updateUserInfo(newDetails, updatedUsername, conn, stmt);

					try {
						String q = "update users set username=? where username=?";
						stmt = conn.prepareStatement(q);
						stmt.setString(1, newDetails.getUsername());
						stmt.setString(2, updatedUsername);

						stmt.executeUpdate();
					} catch (Exception e) {
						throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
					} finally {
						stmt.close();
					}

					try {
						String q = "update complaints set username=? where username=?";
						stmt = conn.prepareStatement(q);
						stmt.setString(1, newDetails.getUsername());
						stmt.setString(2, updatedUsername);

						stmt.executeUpdate();
					} catch (Exception e) {
						throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
					} finally {
						stmt.close();
					}

					try {
						String q = "update conversation set user_one=? where user_one=?";
						stmt = conn.prepareStatement(q);
						stmt.setString(1, newDetails.getUsername());
						stmt.setString(2, updatedUsername);

						stmt.executeUpdate();
					} catch (Exception e) {
						throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
					} finally {
						stmt.close();
					}

					try {
						String q = "update conversation set user_two=? where user_two=?";
						stmt = conn.prepareStatement(q);
						stmt.setString(1, newDetails.getUsername());
						stmt.setString(2, updatedUsername);

						stmt.executeUpdate();
					} catch (Exception e) {
						throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
					} finally {
						stmt.close();
					}

					try {
						String q = "update conversation_reply set username=? where username=?";
						stmt = conn.prepareStatement(q);
						stmt.setString(1, newDetails.getUsername());
						stmt.setString(2, updatedUsername);

						stmt.executeUpdate();
					} catch (Exception e) {
						throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
					} finally {
						stmt.close();
					}

					usernameChanged = true;
				}
			} else {
				updateUserInfo(newDetails, updatedUsername, conn, stmt);
			}
		} catch (Exception e) {
			throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
		} finally {
			conn.close();
		}

		if (usernameAlreadyExists) {
			throw new UsernameUnavailableException();
		}

		return usernameChanged;
	}

	private void updateUserInfo(UserDetails newDetails, String updatedUsername, Connection conn, PreparedStatement stmt) throws Exception {
		try {
			String q = "update user_info set first_name=?, last_name=?, date_of_birth=?, email=?, mobile_number=?, gender=?, address=?, city=?, country=?, personal_number=?, id_series=?, username=? where username=?";
			stmt = conn.prepareStatement(q);
			stmt.setString(1, newDetails.getFirstName());
			stmt.setString(2, newDetails.getLastName());
			Date sqlDate = new Date(newDetails.getDateOfBirth().getTime());
			stmt.setDate(3, sqlDate);
			stmt.setString(4, newDetails.getEmail());
			stmt.setString(5, newDetails.getMobileNumber());
			stmt.setString(6, newDetails.getGender());
			stmt.setString(7, newDetails.getAddress());
			stmt.setString(8, newDetails.getCity());
			stmt.setString(9, newDetails.getCountry());
			stmt.setString(10, newDetails.getPersonalNumber());
			stmt.setString(11, newDetails.getIdSeries());
			stmt.setString(12, newDetails.getUsername());
			stmt.setString(13, updatedUsername);

			stmt.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
		} finally {
			stmt.close();
		}
	}
}
