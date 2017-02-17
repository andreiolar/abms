package com.andreiolar.abms.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.andreiolar.abms.shared.PersonalUpkeepInformation;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfWriter;

@SuppressWarnings("deprecation")
public class PersonalViewToPDFServlet extends HttpServlet {

	private static final long serialVersionUID = -530880619715580880L;

	private static final String FILE_EXTENSION = ".pdf";

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String username = req.getParameter("username");
		String month = req.getParameter("month");

		String usernameForFileName = username.replaceAll("\\.", "");

		String fileName = usernameForFileName + "_" + month + FILE_EXTENSION;

		resp.setContentType("application/pdf");
		resp.setHeader("Content-Disposition:", "attachment;filename=" + "\"" + fileName + "\"");

		File myFile = null;

		try {
			myFile = generatePdfFileFromPersonalView(username, month, fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}

		OutputStream out = resp.getOutputStream();
		FileInputStream in = new FileInputStream(myFile);
		byte[] buffer = new byte[4096];
		int length;

		while ((length = in.read(buffer)) > 0) {
			out.write(buffer, 0, length);
		}

		in.close();
		out.flush();
	}

	private File generatePdfFileFromPersonalView(String username, String month, String fileName) throws Exception {

		File pdfFile = new File(System.getProperty("user.dir") + "/files/personal/" + username, fileName);

		if (!pdfFile.exists()) {
			pdfFile.getParentFile().mkdirs();
		}

		PersonalUpkeepInformation personalUpkeepInformation = getPersonalUpkeepInformationForUsername(username, month);

		Document document = new Document(PageSize.LETTER);
		PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
		document.open();

		HTMLWorker htmlWorker = new HTMLWorker(document);

		String title = "<span>Personalized upkeep report for <span style=\"color: #9e9e9e\">" + personalUpkeepInformation.getLuna()
				+ "</span></span><br/><br/>";
		String personalDetails = "<span>Upkeep report for apartment: <span style=\"color: #9e9e9e\">" + personalUpkeepInformation.getAptNumber()
				+ "</span><br/>Responsible person: <span style=\"color: #9e9e9e\">" + personalUpkeepInformation.getNume()
				+ "</span><br/>Number of persons: <span style=\"color: #9e9e9e\">" + personalUpkeepInformation.getNumarPersoane()
				+ "</span></span><br/><br/>";
		String commonSpace = "<span>Common space: <span style=\"color: #9e9e9e\">" + personalUpkeepInformation.getSpatiuComun()
				+ " mp</span><br/>Note: It will be reflected within the heating costs.</span><br/><br/>";
		String surface = "<span>Apartment surface: <span style=\"color: #9e9e9e\">" + personalUpkeepInformation.getSuprafataApt()
				+ " mp</span><br/>Note: It will be 0 for tenants with own heating plants, otherwise the whole apartment surface will be displayed.</span><br/><br/>";
		String heating = "<span>Heating: <span style=\"color: #9e9e9e\">" + personalUpkeepInformation.getIncalzire()
				+ " RON</span><br/>Note: It is calculated based on the common space and the apartment surface.</span><br/><br/>";
		String hotWater = "<span>Hot water: <span style=\"color: #9e9e9e\">" + personalUpkeepInformation.getApaCaldaMenajera()
				+ " RON</span><br/>Note: It is calculated as following: TERMO-ACM + AR din ACM.<br>Note: It will be 0 for tenants with own heating plants.</span><br/><br/>";
		String coldWater = "<span>Cold water and sewerage: <span style=\"color: #9e9e9e\">" + personalUpkeepInformation.getApaReceSiCanalizare()
				+ " RON</span></span><br/><br/>";
		String garbage = "<span>Garbage: <span style=\"color: #9e9e9e\">" + personalUpkeepInformation.getGunoi()
				+ " RON</span><br/>Note: It is calculated as following: <span style=\"color: #9e9e9e\">10.43 RON * Number of persons</span>.</span><br/><br/>";
		String electricity = "<span>Curent: <span style=\"color: #9e9e9e\">" + personalUpkeepInformation.getCurent()
				+ " RON</span><br/>Note: Common apartment building consumpion. It is calculated as following: <span style=\"color: #9e9e9e\">1 RON * Number of persons</span>.</span><br/><br/>";
		String gas = "<span>Gas: <span style=\"color: #9e9e9e\">" + personalUpkeepInformation.getGaz() + " RON</span></span><br/><br/>";
		String services = "<span>Other services: <span style=\"color: #9e9e9e\">" + personalUpkeepInformation.getServicii()
				+ " RON</span><br/>Note: Common services for the apartment building. For example: Cleaning.</span><br/><br/>";
		String home = "<span>Other apartment costs:: <span style=\"color: #9e9e9e\">" + personalUpkeepInformation.getGospodaresti()
				+ " RON</span></span><br/><br/><br/><br/>";

		BigDecimal totalCost = new BigDecimal(personalUpkeepInformation.getCostTotal()).setScale(2, RoundingMode.CEILING);
		String total = "<span style=\"font-size: 36px; color: #2196f3;\"><b>" + totalCost
				+ "</b> <sup>RON</sup></span><br/><span style=\"color: #9e9e9e\">Total to be paid</span>";

		StringBuilder sb = new StringBuilder();
		sb.append(title);
		sb.append(personalDetails);
		sb.append(commonSpace);
		sb.append(surface);
		sb.append(heating);
		sb.append(hotWater);
		sb.append(coldWater);
		sb.append(garbage);
		sb.append(electricity);
		sb.append(gas);
		sb.append(services);
		sb.append(home);
		sb.append(total);

		htmlWorker.parse(new StringReader(sb.toString()));
		document.close();

		return pdfFile;
	}

	private PersonalUpkeepInformation getPersonalUpkeepInformationForUsername(String username, String month) throws Exception {

		PersonalUpkeepInformation personalUpkeepInformation = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = MyConnection.getConnection();

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

		return personalUpkeepInformation;
	}
}
