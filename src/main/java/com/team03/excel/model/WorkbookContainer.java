package com.team03.excel.model;

import com.team03.excel.processor.writer.CellWriterFactory;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.util.HashMap;
import java.util.Map;


public class WorkbookContainer {

	private Workbook workbook;

	private Map<ExcelCellType, CellStyle> styles;

	private CellWriterFactory writerFactory;

	public WorkbookContainer() {
		// SXSSFWorkbook is whooping 300 times faster!!!!!!!!!!!!!!
		// !!!DANGER!!!: SXSSFWorkbook has short term memory loss. It can now remember
		// only 500 row at a time. You also cant use formula's
		// Be careful with SXSSFWorkbook
		this.workbook = new SXSSFWorkbook(500);

		// Initialize all the available styles we have defined in the ExcelCellStyle
		// enum
		this.initStyles();

		// Initialize the common writer for this workbook
		this.writerFactory = new CellWriterFactory(this);
	}

	private void initStyles() {
		this.styles = new HashMap<ExcelCellType, CellStyle>();
		for (ExcelCellType type : ExcelCellType.values()) {
			this.styles.put(type, type.getCellStyle(workbook));
		}
	}

	public Workbook getWorkbook() {
		return this.workbook;
	}

	public CellStyle getStyle(ExcelCellType type) {
		return this.styles.get(type);
	}

	public CellWriterFactory getWriterFactory() {
		return this.writerFactory;
	}
}
