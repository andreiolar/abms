package com.andreiolar.abms.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.andreiolar.abms.client.rpc.DBSubmitComplaint;
import com.andreiolar.abms.mail.MailSender;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfWriter;

public class DBSubmitComplaintImpl extends RemoteServiceServlet implements DBSubmitComplaint {

	private static final long serialVersionUID = 1L;

	public DBSubmitComplaintImpl() {
	}

	@Override
	public Boolean addComplaint(String username, String phoneNumber, String complaintTo, String firstName, String lastName, String personalId,
			String idSeries, String message) throws Exception {
		Boolean result = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		int inserted = 0;

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "insert into complaints(username, complaint_to, date, phone_number) values(?, ?, CURDATE(), ?)";
				stmt = conn.prepareStatement(q);
				stmt.setString(1, username);
				stmt.setString(2, complaintTo);
				stmt.setString(3, phoneNumber);

				inserted = stmt.executeUpdate();

				String q2 = "select email from user_info where username=?";
				stmt = conn.prepareStatement(q2);
				stmt.setString(1, username);

				rs = stmt.executeQuery();

				String to = null;

				while (rs.next()) {
					to = rs.getString("email");
				}

				String subject = "Complaint to " + complaintTo;
				String messageBody = "You have submitted a complaint to the " + complaintTo
						+ ". <br>They will reach for you as soon as possible. Attached to thes E-Mail you have a PDF file containing your complaint details.<br><br>Regards,<br>Administration";

				File myFile = generateComplaintPDFFile(complaintTo, firstName, lastName, phoneNumber, personalId, idSeries, message, username);

				MailSender.sendMail(subject, to, messageBody, myFile.getAbsolutePath());

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			stmt.close();
			conn.close();
		}

		if (inserted > 0) {
			result = new Boolean(true);
		}

		if (result == null) {
			throw new Exception("Error subitting complaint!");
		}

		return result;
	}

	private File generateComplaintPDFFile(String complaintTo, String firstName, String lastName, String phoneNumber, String personalId,
			String idSeries, String message, String username) throws Exception {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		String today = dateFormat.format(calendar.getTime());

		String basePath = System.getProperty("user.dir");
		File pdfFile = new File(basePath + "/files/complaints/" + username, firstName + "_" + lastName + "_" + complaintTo + "_" + today + ".pdf");

		if (!pdfFile.exists()) {
			pdfFile.getParentFile().mkdirs();
		}

		Document document = new Document(PageSize.LETTER);
		PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
		document.open();

		HTMLWorker htmlWorker = new HTMLWorker(document);
		String personalInforamtionLabel = new String(
				"<p style=\"font-size:20px\">" + "<b><i><u>" + "Personal Information" + "</u></i></b>" + "</p><br>");
		String personalInformation = new String("<p>" + "<b>" + "Name: " + "</b>" + firstName + " " + lastName + "<br>" + "<b>" + "Phone Number: "
				+ "</b>" + phoneNumber + "<br>" + "<b>" + "Personal Number: " + "</b>" + personalId + "<br>" + "<b>" + "ID Series: " + "</b>"
				+ idSeries + "<br><b>Complaint To: </b>" + complaintTo + "</p><br><br><br>");
		String complaintLabel = new String("<p style=\"font-size:20px\">" + "<b><i><u>" + "Complaint" + "</u></i></b></p><br>");
		String complaintText = new String("<p>" + message + "</p>");
		String submittedOn = new String(
				"<br><br><br><p style=\"font-size:20px\">" + "<b><i><u>" + "Submitted on: " + today + "</u></i></b>" + "</p>");

		StringBuilder sb = new StringBuilder();
		sb.append(personalInforamtionLabel);
		sb.append(personalInformation);
		sb.append(complaintLabel);
		sb.append(complaintText);
		sb.append(submittedOn);

		htmlWorker.parse(new StringReader(sb.toString()));
		document.close();

		return pdfFile;
	}

}
