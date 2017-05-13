package com.mvs.server.utils.filemanager;

import com.mvs.server.model.Company;
import com.mvs.server.model.Product;
import com.mvs.server.persistence.CompanyRepository;
import com.mvs.server.persistence.ImageRepository;
import com.mvs.server.persistence.ProductRepository;
import com.mvs.server.persistence.UserRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by fi on 4/3/2017.
 * service
 */
@Service
@Component
public class ExcelManager extends FileManager {

	protected List myExtraMultipart;

	public ExcelManager() {

	}

	// read the excel file and return 2-dim array of strings
	// TODO: may need to add the aspect here
	public List readExcel(String folderName, String fileName, int sheetNumber) {
		// FIXME: exception for invalid file extension
		String[] parts = fileName.split(".");
		if (parts.length == 0 || !parts[parts.length - 1].equalsIgnoreCase("xls") || !parts[parts.length - 1].equalsIgnoreCase("xlsx")) {
			logger.info("Filename invalid for excel: " + fileName);
		}

		ArrayList<ArrayList> output = new ArrayList<>();
		File excelFile = getFile(folderName, fileName);

		try {
			FileInputStream stream = new FileInputStream(excelFile);
			// the workbook
			Workbook workbook = new XSSFWorkbook(stream);
			Sheet sheet = workbook.getSheetAt(sheetNumber);

			for (Row row : sheet) {
				ArrayList<String> rowData = new ArrayList<>();
				for (Cell cell : row) {
					String val;
					switch (cell.getCellType()) {
						case Cell.CELL_TYPE_STRING:
							val = cell.getStringCellValue();
							break;
						case Cell.CELL_TYPE_BLANK:
							val = "";
							break;
						case Cell.CELL_TYPE_BOOLEAN:
							val = String.valueOf(cell.getBooleanCellValue());
							break;
						case Cell.CELL_TYPE_NUMERIC:
							val = String.valueOf(cell.getNumericCellValue());
							break;
						default:
							val = "";

					}
					rowData.add(val);
					System.out.printf("%s ", val);
				}
				output.add(rowData);
				System.out.printf("\n");
			}

			workbook.close();
			stream.close();
			return output;

		} catch (IOException ex) {
			logger.info("Filename not found or unable to open for excel: " + fileName);
			return null;
		}
	}

	// to be override
//	public ArrayList processForCompany(Company company, String folderName, String fileName, int sheetNumber) {
//		return company;
//	}
//
//	public ArrayList processForProduct(Product product, String folderName, String fileName, int sheetNumber) {
//		return product;
//	}

	public List processMultipartFiles() throws IOException {
		return null;
	}

	public List processExcel(long attrId, String folderName, String fileName, int sheetNumber) {
		return null;
	}

	public List getMyExtraMultipart() {
		return myExtraMultipart;
	}

	public void setMyExtraMultipart(List myExtraMultipart) {
		this.myExtraMultipart = myExtraMultipart;
	}
}
