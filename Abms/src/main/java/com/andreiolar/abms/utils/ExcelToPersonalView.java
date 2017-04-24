package com.andreiolar.abms.utils;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.andreiolar.abms.server.MyConnection;
import com.andreiolar.abms.shared.PersonalUpkeepInformation;

public class ExcelToPersonalView {

	private static final String[] FILE_TYPES = {"xls", "xlsx"};

	public static void processFile(File uploadedFile) throws Exception {
		Workbook workbook = null;
		String fileName = uploadedFile.getName();
		List<PersonalUpkeepInformation> allUpkeepInformations = new ArrayList<PersonalUpkeepInformation>();

		String month = fileName.substring(fileName.indexOf("_") + 1, fileName.lastIndexOf("_"));
		String year = fileName.substring(fileName.lastIndexOf("_") + 1, fileName.indexOf("."));
		String luna = month + " " + year;

		if (fileName.toLowerCase().endsWith(FILE_TYPES[0])) {
			workbook = new HSSFWorkbook(new FileInputStream(uploadedFile));
		} else {
			workbook = new XSSFWorkbook(new FileInputStream(uploadedFile));
		}

		Sheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rows = sheet.rowIterator();
		Iterator<Cell> cells = null;

		while (rows.hasNext()) {
			Row row = rows.next();
			cells = row.cellIterator();

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

			while (cells.hasNext()) {

				Cell cell = cells.next();

				if (row.getRowNum() > 1) {
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
						case 4 :
							incalzire = cellValue.length() > 10 ? cellValue.substring(0, 10) : cellValue;
							break;
						case 6 :
							apaCaldaMenajera = cellValue.length() > 10 ? cellValue.substring(0, 10) : cellValue;
							break;
						case 8 :
							apaReceSiCanalizare = cellValue.length() > 10 ? cellValue.substring(0, 10) : cellValue;
							break;
						case 9 :
							numarPersoane = cellValue;
							break;
						case 10 :
							gunoi = cellValue.length() > 10 ? cellValue.substring(0, 10) : cellValue;
							break;
						case 11 :
							curent = cellValue.length() > 10 ? cellValue.substring(0, 10) : cellValue;
							break;
						case 12 :
							gaz = cellValue.length() > 10 ? cellValue.substring(0, 10) : cellValue;
							break;
						case 13 :
							servicii = cellValue.length() > 10 ? cellValue.substring(0, 10) : cellValue;
							break;
						case 14 :
							gospodaresti = cellValue.length() > 10 ? cellValue.substring(0, 10) : cellValue;
							break;
						case 16 :
							nume = cellValue;
							break;
						case 17 :
							costTotal = cellValue.length() > 10 ? cellValue.substring(0, 10) : cellValue;
							break;
					}
				}
			}

			if (row.getRowNum() > 1) {
				if (aptNumber == null) {
					continue;
				}

				PersonalUpkeepInformation personalUpkeepInformation = new PersonalUpkeepInformation(aptNumber, spatiuComun, suprafataApt, incalzire,
						apaCaldaMenajera, apaReceSiCanalizare, numarPersoane, gunoi, curent, gaz, servicii, gospodaresti, nume, costTotal, luna);
				allUpkeepInformations.add(personalUpkeepInformation);
			}
		}

		workbook.close();

		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = MyConnection.getConnection();

			for (PersonalUpkeepInformation pui : allUpkeepInformations) {
				try {
					String q = "insert into personal_upkeep_information(aptNumber,spatiuComun,suprafataApt,incalzire,apaCaldaMenajera,apaReceSiCanalizare,numarPersoane,gunoi,curent,gaz,servicii,gospodaresti,nume,costTotal,luna) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

					stmt = conn.prepareStatement(q);
					stmt.setString(1, pui.getAptNumber());
					stmt.setString(2, pui.getSpatiuComun());
					stmt.setString(3, pui.getSuprafataApt());
					stmt.setString(4, pui.getIncalzire());
					stmt.setString(5, pui.getApaCaldaMenajera());
					stmt.setString(6, pui.getApaReceSiCanalizare());
					stmt.setString(7, pui.getNumarPersoane());
					stmt.setString(8, pui.getGunoi());
					stmt.setString(9, pui.getCurent());
					stmt.setString(10, pui.getGaz());
					stmt.setString(11, pui.getServicii());
					stmt.setString(12, pui.getGospodaresti());
					stmt.setString(13, pui.getNume());
					stmt.setString(14, pui.getCostTotal());
					stmt.setString(15, luna);

					stmt.executeUpdate();
				} catch (Exception e) {
					throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
				} finally {
					stmt.close();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
		} finally {
			conn.close();
		}

	}

}
