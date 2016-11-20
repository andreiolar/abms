package com.andreiolar.abms.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfFileFromExcelCreator {

	public static void createPDFfromExcel(File excelFile) throws Exception {
		String currentMonth = DateUtils.getCurrentMonth();

		FileInputStream inputStream = new FileInputStream(excelFile);
		HSSFWorkbook workbook = new HSSFWorkbook(inputStream);

		Sheet sheet = workbook.getSheetAt(0);

		Iterator<Row> rowIterator = sheet.iterator();

		Document document = new Document(PageSize.A4.rotate());
		PdfWriter.getInstance(document, new FileOutputStream(System.getProperty("user.dir") + "/files/general/Upkeep_" + currentMonth + ".pdf"));
		document.open();

		int numberOfCells = getNumberOfCells(sheet);

		PdfPTable table = new PdfPTable(numberOfCells);
		table.setWidthPercentage(100);
		PdfPCell pdfCell;

		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();

			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				cell.setCellType(Cell.CELL_TYPE_STRING);
				pdfCell = new PdfPCell(new Phrase(cell.getStringCellValue()));
				table.addCell(pdfCell);
			}
		}

		document.add(table);
		document.close();

		workbook.close();

		zipFiles(currentMonth);

	}

	private static void zipFiles(String month) throws Exception {
		File excelFile = new File(System.getProperty("user.dir") + "/files/general/Upkeep_" + month + ".xls");
		File pdfFile = new File(System.getProperty("user.dir") + "/files/general/Upkeep_" + month + ".pdf");

		ZipOutputStream out = new ZipOutputStream(
				new FileOutputStream(new File(System.getProperty("user.dir") + "/files/general/Upkeep_" + month + ".zip")));
		addToZipFile(out, excelFile);
		addToZipFile(out, pdfFile);

		out.close();
	}

	private static void addToZipFile(ZipOutputStream out, File file) throws Exception {
		FileInputStream fis = new FileInputStream(file);
		ZipEntry zipEntry = new ZipEntry(file.getName());
		out.putNextEntry(zipEntry);

		byte[] bytes = new byte[1024];
		int length;

		while ((length = fis.read(bytes)) >= 0) {
			out.write(bytes, 0, length);
		}

		fis.close();
	}

	private static int getNumberOfCells(Sheet sheet) {

		int physicalNumberOfCells = 0;

		for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
			Row row = sheet.getRow(i);

			if (row == null) {
				continue;
			}

			if (row.getRowNum() == 4) {
				physicalNumberOfCells = row.getPhysicalNumberOfCells();
				break;
			}
		}

		return physicalNumberOfCells;

	}
}
