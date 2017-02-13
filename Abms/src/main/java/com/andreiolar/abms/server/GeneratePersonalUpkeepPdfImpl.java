package com.andreiolar.abms.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;

import com.andreiolar.abms.client.rpc.GeneratePersonalUpkeepPdf;
import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfWriter;

@SuppressWarnings("deprecation")
public class GeneratePersonalUpkeepPdfImpl extends RemoteServiceServlet implements GeneratePersonalUpkeepPdf {

	private static final long serialVersionUID = -1094559977287098729L;

	@Override
	public void generatePdf(UserDetails userDetails, String date, String htmlContent) throws Exception {
		String usernameForFileName = userDetails.getUsername().replaceAll("\\.", "");

		String fileName = usernameForFileName + "_" + date + ".pdf";

		File myFile = null;

		try {
			myFile = generatePdfFileFromPersonalView(usernameForFileName, fileName, htmlContent);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!myFile.exists()) {
			throw new RuntimeException("Unable to create PDF file.");
		}

	}

	private File generatePdfFileFromPersonalView(String username, String fileName, String content) throws Exception {

		File pdfFile = new File(System.getProperty("user.dir") + "/files/personal/" + username, fileName);

		if (!pdfFile.exists()) {
			pdfFile.getParentFile().mkdirs();
		} else {
			pdfFile.delete();
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
