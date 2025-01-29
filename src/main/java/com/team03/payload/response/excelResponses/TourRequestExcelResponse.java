package com.team03.payload.response.excelResponses;

import com.team03.excel.annotations.ExcelCell;
import com.team03.excel.annotations.ExcelSheet;
import com.team03.excel.model.ExcelCellType;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ExcelSheet
public class TourRequestExcelResponse {

    @ExcelCell( header = "ID",type = ExcelCellType.GENERAL)
    private Long id;

    @ExcelCell(index = 1, header = "TOUR REQUEST STATUS", type = ExcelCellType.GENERAL)
    private String tourReqStatus;

    @ExcelCell(index = 2, header = "TOUR DATE", type = ExcelCellType.GENERAL)
    private LocalDate tourDate;
    @ExcelCell(index = 3, header = "TOUR TIME", type = ExcelCellType.GENERAL)
    private LocalTime tourTime;
    @ExcelCell(index = 8, header = "CREATE TIME OF TOUR REQUEST", type = ExcelCellType.GENERAL)
    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    @ExcelCell(index = 4, header = "ADVERT ID", type = ExcelCellType.GENERAL)
    private Long advertId;
    @ExcelCell(index = 6, header = "OWNER USER NAME", type = ExcelCellType.GENERAL)
    private String ownerUserName;
    @ExcelCell(index = 7, header = "GUEST USER NAME", type = ExcelCellType.GENERAL)
    private String guestUserName;
    @ExcelCell(index = 5, header = "ADVERT TITLE", type = ExcelCellType.GENERAL)
    private String advertTitle;

    private String advertSlug;
    @ExcelCell(index = 12, header = "ADVERT PRICE", type = ExcelCellType.GENERAL)
    private Double advertPrice;

    @ExcelCell(index = 9, header = "COUNTRY NAME", type = ExcelCellType.GENERAL)
    private String countryName;
    @ExcelCell(index = 10, header = "CITY NAME", type = ExcelCellType.GENERAL)
    private String cityName;
    @ExcelCell(index = 11, header = "DISTRICT NAME", type = ExcelCellType.GENERAL)
    private String districtName;
}
