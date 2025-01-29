package com.team03.payload.response.excelResponses;

import com.team03.excel.annotations.ExcelCell;
import com.team03.excel.annotations.ExcelSheet;
import com.team03.excel.model.ExcelCellType;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ExcelSheet
public class AdvertExcelResponse {

    @ExcelCell( header = "ID",type = ExcelCellType.GENERAL)
    private Long id;

    @ExcelCell(index = 1, header = "ADVERT TYPE", type = ExcelCellType.GENERAL)
    private String advertType;

    @ExcelCell(index = 2, header = "ADVERT TITLE", type = ExcelCellType.GENERAL)
    private String title;

    @ExcelCell(index = 3, header = "ACTIVE",type = ExcelCellType.DEFAULT)
    private boolean isActive;

    @ExcelCell(index = 4, header = "PRICE", type = ExcelCellType.GENERAL)
    private Double price;

    @ExcelCell(index = 5, header = "ADVERT STATUS", type = ExcelCellType.GENERAL)
    private String advertStatus;

    @ExcelCell(index = 6, header = "BUILT IN",type = ExcelCellType.DEFAULT)
    private boolean builtIn;

    @ExcelCell(index = 7, header = "VIEW COUNT", type = ExcelCellType.INTEGER)
    private int viewCount;

    @ExcelCell(index = 8, header = "COUNTRY", type = ExcelCellType.GENERAL)
    private String countryName;

    @ExcelCell(index = 9, header = "CITY", type = ExcelCellType.GENERAL)
    private String cityName;

    @ExcelCell(index = 10, header = "DISTRICT", type = ExcelCellType.GENERAL)
    private String districtName;

    @ExcelCell(index = 11, header = "OWNER USER", type = ExcelCellType.GENERAL)
    private String userName;

    @ExcelCell(index = 12, header = "CREATE DATE AND TIME", type = ExcelCellType.GENERAL)
    private LocalDateTime createdAt;

    @ExcelCell(index = 13, header = "ADVERT DESCRIPTION",type = ExcelCellType.GENERAL)
    private String description;
}
