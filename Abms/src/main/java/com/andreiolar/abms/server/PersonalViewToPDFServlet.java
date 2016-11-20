package com.andreiolar.abms.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

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

	private String URL = new String("jdbc:mysql://localhost:3306");
	private String user = "root";
	private String pass = "andrei";
	private String schema = "administrare_bloc";

	private static final long serialVersionUID = -530880619715580880L;

	private static final String FILE_EXTENSION = ".pdf";

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String username = req.getParameter("username");
		String month = req.getParameter("month");

		String usernameForFileName = username.replaceAll("\\.", "");

		String fileName = usernameForFileName + "_" + month + FILE_EXTENSION;

		resp.setContentType("application/octet-stream");
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
		String luna = new String("<p style=\"font-size:20px\"><b><i><u>Raport personalizat al intretinerii aferent lunii "
				+ personalUpkeepInformation.getLuna() + "</u></i></b><br><br></p>");
		String nameAndAptNumber = new String("<p>Raport aferent apartamentului cu numarul: <b><i>" + personalUpkeepInformation.getAptNumber()
				+ "</i></b>.<br>Persoana responsabila: <b><i> " + personalUpkeepInformation.getNume() + "</i></b>.<br>Numar persoane: <b><i>"
				+ personalUpkeepInformation.getNumarPersoane() + "</i></b>.<br><br><br></p>");
		String spatiuComun = new String("<p>Spatiul comun: <b><i>" + personalUpkeepInformation.getSpatiuComun()
				+ " mp</i></b>.<br>Obs: Se va reflecta la incalzire.<br><br><br></p>");
		String suprafataApt = new String("<p>Suprafata apartamentului: <b><i>" + personalUpkeepInformation.getSuprafataApt()
				+ " mp</i></b>.<br>Obs: Se va reflecta la incalzire.<br>Obs: Va fi 0 locatarii cu centrala proprie, respectiv suprafata apartamentului pentru locatarii fara centrala proprie.<br><br><br></p>");
		String incalzire = new String("<p>Incalizre: <b><i>" + personalUpkeepInformation.getIncalzire()
				+ " RON</i></b>.<br>Obs: Se calculeaza in functie de spatiul comun si suprafata apartamentului.<br><br><br></p>");
		String apaCaldaMenajera = new String("<p>Apa calda menajera: <b><i>" + personalUpkeepInformation.getApaCaldaMenajera()
				+ " RON</i></b>.<br>Obs: Se calculeaza TERMO-ACM + AR din ACM.<br>Obs: Va fi 0 pentru locatarii cu centrala proprie.<br><br><br></p>");
		String apaReceSiCanalizare = new String(
				"<p>Apa rece si canalizare: <b><i>" + personalUpkeepInformation.getApaReceSiCanalizare() + " RON</i></b>.<br><br><br></p>");
		String gunoi = new String("<p>Gunoi: <b><i>" + personalUpkeepInformation.getGunoi()
				+ " RON</i></b>.<br>Obs: Se calculeaza: <b><i>10.43 RON * Numar persoane</i></b>.<br><br><br></p>");
		String curent = new String("<p>Curent: <b><i>" + personalUpkeepInformation.getCurent()
				+ " RON</i></b>.<br>Obs: Curentul comun pe scara.<br>Obs: Se calculeaza: <b><i>1 RON * Numar persoane</i></b>.<br><br><br></p>");
		String gaz = new String("<p>Gaz: <b><i>" + personalUpkeepInformation.getGaz() + " RON</i></b>.<br><br><br></p>");
		String servicii = new String("<p>Servicii: <b><i>" + personalUpkeepInformation.getServicii()
				+ " RON</i></b>.<br>Obs: Consta in serviciile comune scarii de bloc. De exemplu: Curatenia.<br><br><br></p>");
		String gospodaresti = new String("<p>Gospodaresti: <b><i>" + personalUpkeepInformation.getGospodaresti() + " RON</i></b>.<br><br><br></p>");
		String costTotal = new String("<p>Total de plata: <b><i><u>" + personalUpkeepInformation.getCostTotal() + " RON</u></i></b>.</p>");

		StringBuilder sb = new StringBuilder();
		sb.append(luna);
		sb.append(nameAndAptNumber);
		sb.append(spatiuComun);
		sb.append(suprafataApt);
		sb.append(incalzire);
		sb.append(apaCaldaMenajera);
		sb.append(apaReceSiCanalizare);
		sb.append(gunoi);
		sb.append(curent);
		sb.append(gaz);
		sb.append(servicii);
		sb.append(gospodaresti);
		sb.append(costTotal);

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

		return personalUpkeepInformation;
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

}
