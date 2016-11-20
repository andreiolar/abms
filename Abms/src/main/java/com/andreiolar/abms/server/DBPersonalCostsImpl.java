package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import com.andreiolar.abms.client.rpc.DBPersonalCosts;
import com.andreiolar.abms.shared.PersonalUpkeepInformation;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBPersonalCostsImpl extends RemoteServiceServlet implements DBPersonalCosts {

	private static final long serialVersionUID = 1L;

	private String URL = new String("jdbc:mysql://localhost:3306");
	private String user = "root";
	private String pass = "andrei";
	private String schema = "administrare_bloc";

	public DBPersonalCostsImpl() {
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
	public PersonalUpkeepInformation getPersonalUpkeepInformation(String username, String month) throws Exception {
		PersonalUpkeepInformation personalUpkeepInformation = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = getConnection();

			try {
				String q = "select * from personal_upkeep_information where aptNumber=(select apartment_number from user_info where username=?) and luna=?";
				stmt = conn.prepareStatement(q);
				stmt.setString(1, username);
				stmt.setString(2, month);

				rs = stmt.executeQuery();

				while (rs.next()) {
					String apartmentNumber = rs.getString("aptNumber");
					String spatiuComun = rs.getString("spatiuComun");
					String suprafataApt = rs.getString("suprafataApt");
					String incalzire = rs.getString("incalzire");
					String apaCaldaMenajera = rs.getString("apaCaldaMenajera");
					String apaReceSiCanalizare = rs.getString("apaReceSiCanalizare");
					String numarPersoane = rs.getString("numarPersoane");
					String gunoi = rs.getString("gunoi");
					String curent = rs.getString("curent");
					String gaz = rs.getString("gaz");
					String servicii = rs.getString("servicii");
					String gospodaresti = rs.getString("gospodaresti");
					String nume = rs.getString("nume");
					String costTotal = rs.getString("costTotal");
					String luna = rs.getString("luna");

					personalUpkeepInformation = new PersonalUpkeepInformation(apartmentNumber, spatiuComun, suprafataApt, incalzire, apaCaldaMenajera,
							apaReceSiCanalizare, numarPersoane, gunoi, curent, gaz, servicii, gospodaresti, nume, costTotal, luna);
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			rs.close();
			stmt.close();
			conn.close();
		}

		if (personalUpkeepInformation == null) {
			throw new Exception("Failed getting specific user upkeep costs information!");
		}

		return personalUpkeepInformation;
	}

}
