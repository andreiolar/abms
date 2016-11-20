package com.andreiolar.abms.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.andreiolar.abms.db.SimpleDatabaseInserter;
import com.andreiolar.abms.shared.PersonalUpkeepInformation;

public class ExcelToPersonalView {

	private static final String[] FILE_TYPES = {"xls", "xlsx"};

	public static void processFile(File uploadedFile) throws Exception {
		Workbook workbook = null;
		String fileName = uploadedFile.getName();

		String luna = fileName.substring(fileName.indexOf("_") + 1, fileName.indexOf("."));

		if (fileName.toLowerCase().endsWith(FILE_TYPES[0])) {
			workbook = new HSSFWorkbook(new FileInputStream(uploadedFile));
		}

		Sheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rows = sheet.rowIterator();
		Iterator<Cell> cells = null;

		String aptNumber = null;
		String spatiuComun = null;
		String suprafataApt = null;
		String incalzire = null;
		String apaCaldaMenajera = null;
		String apaReceSiCanalizare = null;
		String numarPersoane = null;
		String gunoi = null;
		String curent = null;
		String gaz = null;
		String servicii = null;
		String gospodaresti = null;
		String nume = null;
		String costTotal = null;

		while (rows.hasNext()) {
			Row row = rows.next();
			cells = row.cellIterator();

			while (cells.hasNext()) {

				Cell cell = cells.next();

				if (row.getRowNum() > 3) {
					cell.setCellType(Cell.CELL_TYPE_STRING);
					String cellValue = cell.getStringCellValue();

					switch (cell.getColumnIndex()) {
						case 0 :
							aptNumber = cellValue;
							break;
						case 1 :
							spatiuComun = cellValue;
							break;
						case 2 :
							suprafataApt = cellValue;
							break;
						case 3 :
							incalzire = cellValue;
							break;
						case 4 :
							apaCaldaMenajera = cellValue;
							break;
						case 5 :
							apaReceSiCanalizare = cellValue;
							break;
						case 6 :
							numarPersoane = cellValue;
							break;
						case 7 :
							gunoi = cellValue;
							break;
						case 8 :
							curent = cellValue;
							break;
						case 9 :
							gaz = cellValue;
							break;
						case 10 :
							servicii = cellValue;
							break;
						case 11 :
							gospodaresti = cellValue;
							break;
						case 13 :
							nume = cellValue;
							break;
						case 14 :
							costTotal = cellValue;
							break;
					}
				}

			}

			if (row.getRowNum() > 3) {
				PersonalUpkeepInformation personalUpkeepInformation = new PersonalUpkeepInformation(aptNumber, spatiuComun, suprafataApt, incalzire,
						apaCaldaMenajera, apaReceSiCanalizare, numarPersoane, gunoi, curent, gaz, servicii, gospodaresti, nume, costTotal, luna);
				SimpleDatabaseInserter.insert(personalUpkeepInformation, "personal_upkeep_information");
			}
		}

		workbook.close();

	}

}
