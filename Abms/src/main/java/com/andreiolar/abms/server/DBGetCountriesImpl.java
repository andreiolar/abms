package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import com.andreiolar.abms.client.rpc.DBGetCountries;
import com.andreiolar.abms.shared.Countries;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBGetCountriesImpl extends RemoteServiceServlet implements DBGetCountries {

	private static final long serialVersionUID = 1L;

	private String URL = new String("jdbc:mysql://localhost:3306");
	private String user = "root";
	private String pass = "andrei";
	private String schema = "administrare_bloc";

	public DBGetCountriesImpl() {
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
	public Countries getCountries() throws Exception {

		Countries countries = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = getConnection();

			try {
				String q = "select * from countries";
				stmt = conn.prepareStatement(q);
				rs = stmt.executeQuery();

				List<String> countryList = new LinkedList<String>();

				while (rs.next()) {
					String country = rs.getString("country_name");
					countryList.add(country);
				}

				countries = new Countries(countryList);
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

		return countries;
	}

}
