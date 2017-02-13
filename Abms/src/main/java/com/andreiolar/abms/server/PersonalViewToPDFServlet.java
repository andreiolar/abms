package com.andreiolar.abms.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		String content = req.getParameter("content");

		String usernameForFileName = username.replaceAll("\\.", "");

		String fileName = usernameForFileName + "_" + month + FILE_EXTENSION;

		resp.setContentType("application/octet-stream");
		resp.setHeader("Content-Disposition:", "attachment;filename=" + "\"" + fileName + "\"");

		File myFile = null;

		try {
			myFile = generatePdfFileFromPersonalView(username, fileName, content);
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

	private File generatePdfFileFromPersonalView(String username, String fileName, String content) throws Exception {

		File pdfFile = new File(System.getProperty("user.dir") + "/files/personal/" + username, fileName);

		if (!pdfFile.exists()) {
			pdfFile.getParentFile().mkdirs();
		}

		Document document = new Document(PageSize.LETTER);
		PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
		document.open();

		HTMLWorker htmlWorker = new HTMLWorker(document);

		htmlWorker.parse(new StringReader(content));
		document.close();

		return pdfFile;
	}
}
