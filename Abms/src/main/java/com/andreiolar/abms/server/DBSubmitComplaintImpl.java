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

import com.andreiolar.abms.client.exception.ComplaintSubmissionException;
import com.andreiolar.abms.client.exception.MailException;
import com.andreiolar.abms.client.rpc.DBSubmitComplaint;
import com.andreiolar.abms.mail.MailSender;
import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfWriter;

@SuppressWarnings("deprecation")
public class DBSubmitComplaintImpl extends RemoteServiceServlet implements DBSubmitComplaint {

	private static final long serialVersionUID = 817411178442933495L;

	public DBSubmitComplaintImpl() {
	}

	@Override
	public Boolean registerComplaint(UserDetails userInfo, String phoneNumber, String complaintTo, String complaint) throws Exception {
		Boolean result = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isMailSend = true;
		String mailMessage = null;

		int inserted = 0;

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "insert into complaints(username, complaint_to, date, phone_number) values(?, ?, CURDATE(), ?)";
				stmt = conn.prepareStatement(q);
				stmt.setString(1, userInfo.getUsername());
				stmt.setString(2, complaintTo);
				stmt.setString(3, phoneNumber);

				inserted = stmt.executeUpdate();
				stmt.close();

				String q2 = "select email from user_info where username=?";
				stmt = conn.prepareStatement(q2);
				stmt.setString(1, userInfo.getUsername());

				rs = stmt.executeQuery();

				String to = null;

				while (rs.next()) {
					to = rs.getString("email");
				}

				String subject = "Complaint to " + complaintTo;
				String messageBody = "You have submitted a complaint to the " + complaintTo
						+ ". <br>They will reach for you as soon as possible. Attached to thes E-Mail you have a PDF file containing your complaint details.<br><br>Regards,<br>Administration";

				File myFile = generateComplaintPDFFile(userInfo, phoneNumber, complaintTo, complaint);

				try {
					MailSender.sendMail(subject, to, messageBody, myFile.getAbsolutePath());
				} catch (Exception ex) {
					mailMessage = ex.getMessage();
					isMailSend = false;
				}

			} catch (Exception ex) {
				throw new RuntimeException("Something went wrong: " + ex.getMessage(), ex);
			}
		} catch (SQLException ex) {
			throw new RuntimeException("Something went wrong: " + ex.getMessage(), ex);
		} finally {
			stmt.close();
			conn.close();
		}

		if (inserted > 0) {
			result = new Boolean(true);
		}

		if (result == null) {
			throw new ComplaintSubmissionException("Error subitting complaint!");
		}

		if (!isMailSend) {
			throw new MailException("Unable to send mail: " + mailMessage);
		}

		return result;
	}

	private File generateComplaintPDFFile(UserDetails userInfo, String phoneNumber, String complaintTo, String complaint) throws Exception {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		String today = dateFormat.format(calendar.getTime());

		String basePath = System.getProperty("user.dir");
		File pdfFile = new File(basePath + "/files/complaints/" + userInfo.getUsername(),
				userInfo.getFirstName() + "_" + userInfo.getLastName() + "_" + complaintTo + "_" + today + ".pdf");

		if (!pdfFile.exists()) {
			pdfFile.getParentFile().mkdirs();
		}

		Document document = new Document(PageSize.LETTER);
		PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
		document.open();

		HTMLWorker htmlWorker = new HTMLWorker(document);
		String personalInforamtionLabel = new String(
				"<p style=\"font-size:20px\">" + "<b><i><u>" + "Personal Information" + "</u></i></b>" + "</p><br>");
		String personalInformation = new String("<p>" + "<b>" + "Name: " + "</b>" + userInfo.getFirstName() + " " + userInfo.getLastName() + "<br>"
				+ "<b>" + "Phone Number: " + "</b>" + phoneNumber + "<br>" + "<b>" + "Personal Number: " + "</b>" + userInfo.getPersonalNumber()
				+ "<br>" + "<b>" + "ID Series: " + "</b>" + userInfo.getIdSeries() + "<br><b>Complaint To: </b>" + complaintTo + "</p><br><br><br>");
		String complaintLabel = new String("<p style=\"font-size:20px\">" + "<b><i><u>" + "Complaint" + "</u></i></b></p><br>");
		String complaintText = new String("<p>" + complaint + "</p>");
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
