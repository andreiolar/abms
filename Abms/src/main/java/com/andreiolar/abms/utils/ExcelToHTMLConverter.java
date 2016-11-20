package com.andreiolar.abms.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

public class ExcelToHTMLConverter {

	private static final String[] FILE_TYPES = {"xls", "xlsx"};

	private static final String HTML_H_TABLE_START = "<table class=\"header-table\">";
	private static final String HTML_H_TABLE_END = "</table>";
	private static final String HTML_TABLE_START = "<table class=\"upkeep-table\">";
	private static final String HTML_TABLE_END = "</table>";
	private static final String HTML_TH_START = "<th class=\"table-head\" ";
	private static final String HTML_TH_END = "</th>";
	private static final String HTML_TR_START = "<tr class=\"table-row\">";
	private static final String HTML_TR_END = "</tr>";
	private static final String HTML_TD_START = "<td class=\"table-data\">";
	private static final String HTML_TD_END = "</td>";
	private static final String HTML_DIV_START = "<div style=\"width:117px\">";
	private static final String HTML_DIV_END = "</div>";

	public static String generateTableFromExcel(File excelFile) throws Exception {

		StringBuilder sb;
		Workbook workbook = null;
		String fileName = excelFile.getName();

		if (fileName.toLowerCase().endsWith(FILE_TYPES[0])) {
			workbook = new HSSFWorkbook(new FileInputStream(excelFile));
		}

		sb = new StringBuilder();
		sb = new StringBuilder(20000);
		sb.append(HTML_H_TABLE_START);

		Sheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rows = sheet.rowIterator();
		Iterator<Cell> cells = null;

		// Header table
		while (rows.hasNext()) {
			Row row = rows.next();
			cells = row.cellIterator();

			sb.append(HTML_TR_START);

			outer : while (cells.hasNext()) {
				Cell cell = cells.next();

				if (row.getRowNum() < 4) {
					for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
						CellRangeAddress region = sheet.getMergedRegion(i);

						int columnIndex = region.getFirstColumn();
						int rowNum = region.getFirstRow();

						int lastRow = region.getLastRow();

						if (rowNum == cell.getRowIndex() && columnIndex == cell.getColumnIndex()) {
							String value = sheet.getRow(rowNum).getCell(columnIndex).getStringCellValue();

							int rowspan = lastRow - rowNum + 1;

							sb.append(HTML_TH_START + "rowspan=\"" + rowspan + "\" style=\"width:120px\">");
							sb.append(value);
							sb.append(HTML_TH_END);

							continue outer;
						}

						if (cell.getCellType() == Cell.CELL_TYPE_BLANK || cell == null) {
							continue;
						}

					}

					if (row.getRowNum() == 3) {
						sb.append("<th>");
						sb.append(cell.getStringCellValue());
						sb.append("</th>");
					}

				}

			}

			sb.append(HTML_TR_END);

		}

		sb.append(HTML_H_TABLE_END);

		Iterator<Row> rowsIt = sheet.rowIterator();
		Iterator<Cell> cellsIt = null;

		sb.append(HTML_TABLE_START);

		// Normal table
		while (rowsIt.hasNext()) {
			Row row = rowsIt.next();
			cellsIt = row.cellIterator();

			sb.append(HTML_TR_START);

			while (cellsIt.hasNext()) {
				Cell cell = cellsIt.next();

				if (row.getRowNum() > 3) {
					sb.append(HTML_TD_START);
					sb.append(HTML_DIV_START);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					String cellValue = cell.getStringCellValue();
					sb.append(cellValue);
					sb.append(HTML_DIV_END);
					sb.append(HTML_TD_END);
				}
			}

			sb.append(HTML_TR_END);
		}

		sb.append(HTML_TABLE_END);

		workbook.close();

		return sb.toString();
	}

}
