package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.andreiolar.abms.client.rpc.DBGetContactInfo;
import com.andreiolar.abms.shared.ContactInfo;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBGetContactInfoImpl extends RemoteServiceServlet implements DBGetContactInfo {

	private static final long serialVersionUID = 1L;

	private String URL = new String("jdbc:mysql://localhost:3306");
	private String user = "root";
	private String pass = "andrei";
	private String schema = "administrare_bloc";

	public DBGetContactInfoImpl() {
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
	public List<ContactInfo> getContacts() throws Exception {

		List<ContactInfo> contacts = new ArrayList<ContactInfo>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = getConnection();

			try {
				String q = "select * from contact_info";
				stmt = conn.prepareStatement(q);
				rs = stmt.executeQuery();

				while (rs.next()) {
					String familyName = rs.getString("family_name");
					String contactPerson = rs.getString("contact_person");
					String apartmentNumber = rs.getString("apartment_number");
					String email = rs.getString("email");
					String mobileNumber = rs.getString("mobile_number");
					String username = rs.getString("username");
					String gender = rs.getString("gender");

					ContactInfo contactInfo = new ContactInfo(familyName, contactPerson, apartmentNumber, email, mobileNumber, username, gender);
					contacts.add(contactInfo);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		if (contacts == null || contacts.isEmpty()) {
			throw new Exception("Error retrieving contacts!");
		}

		return contacts;
	}

}
