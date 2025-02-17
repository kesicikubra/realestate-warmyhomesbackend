package com.team03.excel;

import com.team03.excel.annotations.ExcelCell;
import com.team03.excel.annotations.ExcelSheet;
import com.team03.excel.model.SheetContainer;
import com.team03.excel.model.WorkbookContainer;
import freemarker.template.utility.StringUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExcelWriter {
	private final static Logger log = LoggerFactory.getLogger(ExcelWriter.class);

	protected static WorkbookContainer workbookContainer;


	@SafeVarargs
	public static <T> File writeExcel(String fileName, List<? extends T>... data) {
		List<List<?>> filteredData = Arrays.asList(data).stream().filter(nonEmptyData).collect(Collectors.toList());
		// If there is no data in any sheet, do not process further
		if (!filteredData.isEmpty()) {
			// This is important to ensure that we multiple Threads do not call this
			// at-once.
			// The context switching happening here is heavy and may cause the whole system
			// to lag.
			synchronized (ExcelWriter.class) {
				// Reset the workbook
				ExcelWriter.initWorkBook();

				// Process each sheet one by one
				filteredData.forEach(list -> {
					createSheet.andThen(generateName).andThen(giveHeading).andThen(addColumns).andThen(writeData)
							.andThen(autoSizeColumns).andThen(freezePane).andThen(attachFilters).apply(list);
				});

				// Write to actual location
				return writeToFile(fileName);
			}
		}
		return null;
	}


	public static void initWorkBook() {
		ExcelWriter.workbookContainer = new WorkbookContainer();
	}

	protected static Predicate<List<?>> nonEmptyData = data -> data != null && data.size() > 0;


	protected static Function<List<?>, SheetContainer> createSheet = (List<?> data) -> {
		SheetContainer sheetContainer = new SheetContainer();
		sheetContainer.setSheet(workbookContainer.getWorkbook().createSheet());
		sheetContainer.setData(data);
		return sheetContainer;
	};


	protected static Function<SheetContainer, SheetContainer> generateName = (SheetContainer sheetContainer) -> {
		Workbook workbook = workbookContainer.getWorkbook();
		Sheet sheet = sheetContainer.getSheet();

		String sheetName = "";

		try {
			// We have already filtered out stuff where that data size !> 0. So NPE wont
			// occur here.
			Class<?> _class = sheetContainer.getData().get(0).getClass();

			// See if the good people added an Excel Sheet annotation
			if (_class.isAnnotationPresent(ExcelSheet.class)) {
				// And by any chance gave it a name
				sheetName = _class.getAnnotation(ExcelSheet.class).name();

				// Also get the heading for future use
				sheetContainer.setHeading(_class.getAnnotation(ExcelSheet.class).heading());
			}
			// If there are no annotations or no one bothered to give a sheet name, just use
			// the Class Name
			if (sheetName.equals("")) {
				sheetName = parseCamelCase(_class.getSimpleName());
			}
			// If some genius has used an Anonymous class, then just use the index.
			if (sheetName.equals("")) {
				sheetName = "Sheet - ".concat(String.valueOf((workbook.getSheetIndex(sheet))));
			}

			workbook.setSheetName(workbook.getSheetIndex(sheet), sheetName);

		} catch (Exception e2) {
			log.error("Was unable to give sheet its name", e2);
		}

		return sheetContainer;
	};


	protected static Function<SheetContainer, SheetContainer> giveHeading = (SheetContainer sheetContainer) -> {
		Sheet sheet = sheetContainer.getSheet();
		String heading = sheetContainer.getHeading();

		if (!heading.equals("")) {
			try {
				// Line 1
				sheet.createRow(0).createCell(0).setCellValue(heading);

				// Add some styling to the header
				Workbook wb = workbookContainer.getWorkbook();

				Font font = wb.createFont();
				font.setBold(true);
				font.setColor(IndexedColors.PINK.getIndex());
				font.setFontHeightInPoints((short) (heading.length() < 16 ? 18 : 16));

				CellStyle style = wb.createCellStyle();
				style.setFont(font);

				sheet.getRow(0).getCell(0).setCellStyle(style);

				// Line 2
				sheet.createRow(1).createCell(0).setCellValue("Generated on: " + Calendar.getInstance().getTime());

				// Merge cells to make them look decent.
				sheet.addMergedRegion(new CellRangeAddress(0, // first row (0-based)
						0, // last row (0-based)
						0, // first column (0-based)
						5 // last column (0-based)
				));
				sheet.addMergedRegion(new CellRangeAddress(1, // first row (0-based)
						1, // last row (0-based)
						0, // first column (0-based)
						5 // last column (0-based)
				));

				// A spacer row
				sheet.createRow(2).createCell(0);

			} catch (Exception e3) {
				log.error("Was unable to create sheet's header", e3);
			}
		}

		return sheetContainer;
	};

	protected static Function<SheetContainer, SheetContainer> addColumns = (SheetContainer sheetContainer) -> {
		Sheet sheet = sheetContainer.getSheet();
		List<?> data = sheetContainer.getData();

		int rowIndex = sheetContainer.getHeading().equals("") ? 0 : 3;

		// Create a new row after the Heading (give +1 blank space)
		Row row = sheet.createRow(rowIndex);

		try {
			// Get the POJO class of the listed data
			Class<?> _class = data.get(0).getClass();

			// Get all fields (public, protected, anything)
			Field fields[] = _class.getDeclaredFields();

			// Filter out those fields that have Excel Cell annotations
			List<Field> annotatedFields = Arrays.asList(fields).stream().filter((Field field) -> {
				field.setAccessible(true);
				return field.isAnnotationPresent(ExcelCell.class);
			}).collect(Collectors.toList());

			// Get a function that writes the columns
			BiConsumer<Cell, String> columnWriter = workbookContainer.getWriterFactory().getColumnWriter();

			// 0 based column index
			AtomicInteger columnIndex = new AtomicInteger();

			// A custom stub that can be recursively executed
			Consumer<String> addColumn = (String header) -> {
				// Add the header column
				Cell cell = row.createCell(columnIndex.get());
				columnWriter.accept(cell, header);

				// Set min width to make the column accessible
				sheet.setColumnWidth(columnIndex.getAndIncrement(), ((header.length() + 3) * 256) + 200);
			};

			// If at-least 1 annotation is present
			if (annotatedFields.size() > 0) {
				// Process only the fields that have annotations
				annotatedFields.forEach((Field field) -> {

					// Check if the header name is present in the annotation
					String header = field.getAnnotation(ExcelCell.class).header();

					// If not, use the object name itself
					if (header.equals("")) {
						header = parseCamelCase(field.getName());
					}

					// Add the column
					addColumn.accept(header);
				});
			} else {
				// Process Everything

				Arrays.asList(fields).forEach(field -> {
					// As annotation is not present, use the object name itself
					String header = parseCamelCase(field.getName());

					// Add the column
					addColumn.accept(header);
				});
			}

		} catch (Exception e4) {
			log.error("Was Unable to add columns to sheet: " + sheet.getSheetName(), e4);
		}
		return sheetContainer;
	};

	protected static Function<SheetContainer, SheetContainer> writeData = (SheetContainer sheetContainer) -> {
		Sheet sheet = sheetContainer.getSheet();
		List<?> dataList = sheetContainer.getData();
		try {
			// Get the POJO class of the listed data
			Class<?> _class = dataList.get(0).getClass();

			// Get all fields (public, protected, anything)
			Field fields[] = _class.getDeclaredFields();

			// Filter out those fields that have Excel Cell annotations
			List<Field> fieldList = Arrays.asList(fields).stream().filter((Field field) -> {
				field.setAccessible(true);
				return field.isAnnotationPresent(ExcelCell.class);
			}).collect(Collectors.toList());

			// If no annotations are present
			// then use all the fields
			if (fieldList.size() == 0) {
				fieldList = Arrays.asList(fields);
			}

			// Create a local map of how each field should be effectively written.
			Map<Field, BiConsumer<Cell, Object>> fieldWriter = new HashMap<Field, BiConsumer<Cell, Object>>();
			fieldList.forEach(field -> {
				// Create a dynamic function that knows how to write this specific "type"
				// of column in the Excel based on the annotations or its data type.
				BiConsumer<Cell, Object> cellWriter = workbookContainer.getWriterFactory()
						.getAnnotatedFieldWriter(field);
				fieldWriter.put(field, cellWriter);
			});

			// Shift rows down to accommodate for the heading and the column headers
			int shiftIndex = sheetContainer.getHeading().equals("") ? 1 : 4;

			// Write data to each cell.
			for (int rowNum = 0; rowNum < dataList.size(); rowNum++) {
				// + 3 as Row0 and Row1 are filled with the heading. Row2 is a spacer.
				Row row = sheet.createRow(rowNum + shiftIndex);

				// Get whatever the field holds from the object
				Object data = dataList.get(rowNum);

				for (int colNum = 0; colNum < fieldList.size(); colNum++) {
					// Get the current field
					Field field = fieldList.get(colNum);

					try {
						// Create a new cell
						Cell cell = row.createCell(colNum);

						// write the data
						fieldWriter.get(field).accept(cell, data);

					} catch (Exception ex) {
						log.warn("Unable to write data to row: " + (rowNum + 1) + " cell: " + (colNum + 1)
								+ " of sheet: " + sheet.getSheetName(), ex);
					}
				}
			}

		} catch (Exception e) {
			log.error("Was Unable to write data to sheet: " + sheet.getSheetName(), e);
		}
		return sheetContainer;
	};


	protected static Function<SheetContainer, SheetContainer> autoSizeColumns = (SheetContainer sheetContainer) -> {
		Sheet sheet = sheetContainer.getSheet();

		// In case of SXSSFSheet, the row tracking is limited and hence cannot be used
		// to auto-size
		if (!(sheet instanceof SXSSFSheet)) {
			for (int column = 0; column < sheetContainer.getData().size(); column++) {
				sheet.autoSizeColumn(column, false);
			}
		}

		return sheetContainer;
	};


	protected static Function<SheetContainer, SheetContainer> freezePane = (SheetContainer sheetContainer) -> {
		Sheet sheet = sheetContainer.getSheet();
		int frozenRows = sheetContainer.getHeading().equals("") ? 1 : 4;
		sheet.createFreezePane(0, frozenRows);
		return sheetContainer;
	};


	protected static Function<SheetContainer, SheetContainer> attachFilters = (SheetContainer sheetContainer) -> {
		Sheet sheet = sheetContainer.getSheet();

		// Get the row in which filter is to be applied
		int filterRow = sheetContainer.getHeading().equals("") ? 0 : 3;

		// Get the last column upto which the filters are to be applied.
		int lastColumn = sheet.getRow(sheet.getLastRowNum()).getLastCellNum() - 1;

		sheet.setAutoFilter(new CellRangeAddress(filterRow, // 1st row
				filterRow, // Last row
				0, // 1st cell
				lastColumn // Last cell
		));

		return sheetContainer;
	};


	public static File writeToFile(String fileName) {
		FileOutputStream fos = null;
		Workbook workbook = null;
		try {
			String fullPath = Paths.get(System.getProperty("user.dir"), fileName).toString();
			if (fullPath == null || fullPath.equals("")) {
				return null;
			}
			File file = new File(fullPath);

			// Over-ride if the same filename exists.
			if (file.exists()) {
				log.info("As " + fullPath + " already exists, deleting the existing file.");
				file.delete();
			}

			// Add an extension to the file if not provided in the passed name.
			if (!fullPath.endsWith(".xlsx")) {
				fullPath = fullPath.concat(".xlsx");
			}
			fos = new FileOutputStream(file);
			workbook = workbookContainer.getWorkbook();
			workbook.write(fos);
			return file;
		} catch (Exception e) {
			log.error("Write to workbook failed : " + e.getMessage());
			return null;
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					log.warn("Unable to close the workbook due to: " + e);
					log.info("The above exception is not fatal. Will try to continue");
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					log.warn("Unable to close the file stream due to: " + e);
					log.info("The above exception is not fatal. Will try to continue");
				}
			}
		}
	}


	protected static String parseCamelCase(String camelCaseString) {
		if (camelCaseString == null) {
			return "";
		} else {
			return StringUtil.capitalize(String.join(" ", StringUtils.splitByCharacterTypeCamelCase(camelCaseString)));
		}
	}

}
