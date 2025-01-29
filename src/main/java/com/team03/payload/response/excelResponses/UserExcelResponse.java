package com.team03.payload.response.excelResponses;

import com.team03.excel.annotations.ExcelCell;
import com.team03.excel.annotations.ExcelSheet;
import com.team03.excel.model.ExcelCellType;
import lombok.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ExcelSheet
public class UserExcelResponse {

    @ExcelCell( header = "ID",type = ExcelCellType.GENERAL)
    private Long id;

    @ExcelCell(index = 2, header = "USER NAME", type = ExcelCellType.GENERAL)
    private String name;

    @ExcelCell(index = 3, header = "USER EMAIL", type = ExcelCellType.GENERAL)
    private String email;
    @ExcelCell(index = 4, header = "PHONE NUMBER", type = ExcelCellType.GENERAL)
    private String phone;
    @ExcelCell(index = 5, header = "BUILT IN", type = ExcelCellType.GENERAL)
    private boolean builtIn;
    @ExcelCell(index = 6, header = "USERS ROLES", type = ExcelCellType.GENERAL)
    private Set<String> role;
}
