package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.andreiolar.abms.client.exception.AptNumberUnavailableException;
import com.andreiolar.abms.client.exception.EmailUnavailableException;
import com.andreiolar.abms.client.rpc.DBRegisterTenant;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBRegisterTenantImpl extends RemoteServiceServlet implements DBRegisterTenant {

	private static final long serialVersionUID = -4922566502544725777L;

	@Override
	public void registerTenant(String email, String aptNumber) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		boolean emailExists = false;
		boolean aptNumberExists = false;

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "select * from email_for_registration where email=?";
				stmt = conn.prepareStatement(q);
				stmt.setString(1, email);

				rs = stmt.executeQuery();
				if (rs.next()) {
					emailExists = true;
				}
			} catch (Exception e) {
				throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
			} finally {
				rs.close();
				stmt.close();
			}

			if (!emailExists) {
				try {
					String q = "select * from user_info where apartment_number=?";
					stmt = conn.prepareStatement(q);
					stmt.setString(1, aptNumber);

					rs = stmt.executeQuery();
					if (rs.next()) {
						aptNumberExists = true;
					}
				} catch (Exception e) {
					throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
				} finally {
					rs.close();
					stmt.close();
				}

				try {
					String q = "select * from email_for_registration where apartment_number=?";
					stmt = conn.prepareStatement(q);
					stmt.setString(1, aptNumber);

					rs = stmt.executeQuery();
					if (rs.next()) {
						aptNumberExists = true;
					}
				} catch (Exception e) {
					throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
				} finally {
					rs.close();
					stmt.close();
				}

				if (!aptNumberExists) {
					try {
						String q = "insert into email_for_registration(email, apartment_number) values(?,?)";
						stmt = conn.prepareStatement(q);
						stmt.setString(1, email);
						stmt.setString(2, aptNumber);

						stmt.executeUpdate();
					} catch (Exception e) {
						throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
					} finally {
						stmt.close();
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
		} finally {
			conn.close();
		}

		if (emailExists) {
			throw new EmailUnavailableException();
		}

		if (aptNumberExists) {
			throw new AptNumberUnavailableException();
		}
	}
}
