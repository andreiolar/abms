package com.andreiolar.abms.server;

import java.io.File;

import com.andreiolar.abms.client.rpc.GetHtmlTableFromExel;
import com.andreiolar.abms.utils.DateUtils;
import com.andreiolar.abms.utils.ExcelToHTMLConverter;
import com.andreiolar.abms.utils.PdfFileFromExcelCreator;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class GetHtmlTableFromExelImpl extends RemoteServiceServlet implements GetHtmlTableFromExel {

	private static final long serialVersionUID = 6929079300878956447L;

	@Override
	public String getTableFromExcel() throws Exception {

		String currentMonth = DateUtils.getCurrentMonth();

		File excelFile = new File(System.getProperty("user.dir") + "/files/general/Upkeep_" + currentMonth + ".xls");

		String html = null;

		try {
			html = ExcelToHTMLConverter.generateTableFromExcel(excelFile);
			PdfFileFromExcelCreator.createPDFfromExcel(excelFile);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		if (html == null) {
			throw new Exception("Error  parsing Excel file: " + excelFile.getName());
		}

		return html;
	}

}
