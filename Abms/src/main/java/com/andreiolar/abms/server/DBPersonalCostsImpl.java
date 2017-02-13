package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.andreiolar.abms.client.exception.PersonalUpkeepInformationNotFoundException;
import com.andreiolar.abms.client.rpc.DBPersonalCosts;
import com.andreiolar.abms.shared.PersonalUpkeepInformation;
import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBPersonalCostsImpl extends RemoteServiceServlet implements DBPersonalCosts {

	private static final long serialVersionUID = 1L;

	public DBPersonalCostsImpl() {
	}

	@Override
	public PersonalUpkeepInformation getPersonalUpkeepInformation(UserDetails userDetails, String month) throws Exception {
		PersonalUpkeepInformation personalUpkeepInformation = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "select * from personal_upkeep_information where aptNumber=? and luna=?";
				stmt = conn.prepareStatement(q);
				stmt.setString(1, userDetails.getApartmentNumber());
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
				throw new RuntimeException("Something went wrong: " + ex.getMessage(), ex);
			}

		} catch (Exception ex) {
			throw new RuntimeException("Something went wrong: " + ex.getMessage(), ex);
		} finally {
			rs.close();
			stmt.close();
			conn.close();
		}

		if (personalUpkeepInformation == null) {
			throw new PersonalUpkeepInformationNotFoundException();
		}

		return personalUpkeepInformation;
	}

}
