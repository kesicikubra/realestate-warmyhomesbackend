package com.team03.excel.model;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;


public enum ExcelCellType {


	GENERAL("General"),

	INTEGER("0"),


	DECIMAL("0.00"),

	PRECISE("0.000000000"),


	CURRENCY("#,##0.00"),


	DATE("m/d/yyyy"),

	DATETIME("m/d/yyyy h:mm:ss AM/PM"),

	PERCENT("0.00%"),


	DEFAULT("");

	private String format;

	ExcelCellType(String format) {
		this.format = format;
	}

	public String getFormat() {
		return this.format;
	}

	public short getDataFormat(Workbook wb) {
		return wb.createDataFormat().getFormat(this.format);
	}

	public CellStyle getCellStyle(Workbook wb) {
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setDataFormat(this.getDataFormat(wb));
		return cellStyle;
	}

}
