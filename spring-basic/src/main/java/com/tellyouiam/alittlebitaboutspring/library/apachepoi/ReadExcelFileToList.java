package com.tellyouiam.alittlebitaboutspring.library.apachepoi;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReadExcelFileToList {
	 static List<Country> readExcelData(String fileName) {
		List<Country> countriesList = new ArrayList<>();
		
		try {
			//Create the input stream from the xlsx/xls file
			FileInputStream fis = new FileInputStream(new File(fileName));
			
			//Create Workbook instance for xlsx/xls file input stream
			Workbook workbook = null;
			if (fileName.toLowerCase().endsWith("xlsx")) {
				workbook = new XSSFWorkbook(fis);
			} else if(fileName.toLowerCase().endsWith("xls")) {
				workbook = new HSSFWorkbook(fis);
			}
			
			//Get the number of sheets in the xlsx file
			assert workbook != null;
			int numberOfSheets = workbook.getNumberOfSheets();
			
			//loop through each of the sheets
			for(int i = 0; i < numberOfSheets; i++) {
				
				//Get the nth sheet from the workbook
				Sheet sheet = workbook.getSheetAt(i);
				
				//every sheet has rows, iterate over them
				for (Row cells : sheet) {
					String name = "";
					String shortCode = "";
					
					//Get the row object
					//data format
					DataFormat fmt = workbook.createDataFormat();
					CellStyle cellStyle = workbook.createCellStyle();
					cellStyle.setDataFormat(fmt.getFormat("@"));
					
					//Every row has columns, get the column iterator and iterate over them
					Iterator<Cell> cellIterator = cells.cellIterator();
					
					while (cellIterator.hasNext()) {
						//Get the Cell object
						Cell cell = cellIterator.next();
						
						//check the cell type and process accordingly
						switch (cell.getCellType()) {
							case STRING:
								if (shortCode.equalsIgnoreCase("")) {
									shortCode = cell.getStringCellValue().trim();
								} else if (name.equalsIgnoreCase("")) {
									//2nd column
									name = cell.getStringCellValue().trim();
								} else {
									//random data, leave it
									System.out.println("Random data::" + cell.getStringCellValue());
								}
								break;
							case NUMERIC:
								System.out.println("Random data::" + cell.getNumericCellValue());
						}
					} //end of cell iterator
					Country c = new Country(name, shortCode);
					countriesList.add(c);
				} //end of rows iterator
				
				
			} //end of sheets for loop
			
			//close file input stream
			fis.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return countriesList;
	}
	
	public static void main(String[] args){
		List<Country> list = readExcelData("src/main/resources/xlsx-files/Sample.xlsx");
		System.out.println("Country List\n"+list);
	}
}
