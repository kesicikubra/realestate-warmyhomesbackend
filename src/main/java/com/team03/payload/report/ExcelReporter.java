package com.team03.payload.report;

import com.team03.entity.business.Advert;
import com.team03.entity.business.TourRequest;
import com.team03.entity.user.User;
import com.team03.entity.user.UserRole;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.StringJoiner;

public class ExcelReporter {

    static String SHEET_ADVERT = "Adverts";
    static String[] ADVERT_HEADERS = {"Id", "Title", "CreateAt", "Category", "Country", "City", "Distinct", "Price", "Status","ViewCount","BuiltIn"};

    static String SHEET_MOST_POPULAR_ADVERTS = "MostPopulerAdverts";
    static String[] ADVERT_MOST_POPULAR_HEADERS = {"Id", "Title", "CreateAt", "Category", "Country", "City", "Distinct",
            "Price", "Status","ViewCount","BuiltIn","Tour Request"};

    static String SHEET_USER = "Users";
    static String[] USER_HEADERS = {"id", "FirstName", "LastName", "PhoneNumber", "Email", "Create Time", "Built In","Roles"};


    static String SHEET_TOUR_REQUEST = "Tour Requests";
    static String[] TOUR_REQUESTS_HEADERS = {"id", "Status", "Tour Date", "Tour Time", "Create Time", "Update Time", "Owner User","Guest User", "Advert"};

    public static ByteArrayInputStream getAdvertExcelReport(List<Advert> adverts) throws IOException {
        Workbook workbook = new XSSFWorkbook(); // excel creator

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Sheet sheet = workbook.createSheet(SHEET_ADVERT);     // 1- add sheet
        Row headerRow = sheet.createRow(0);         // 2- add header row
        for (int i = 0; i < ADVERT_HEADERS.length; i++) {     // 3- add fields into row
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(ADVERT_HEADERS[i]);
        }

        int rowId = 1;                                      // 4- add data into body rows
        for (Advert advert : adverts
        ) {
            Row row = sheet.createRow(rowId++);
            row.createCell(0).setCellValue(advert.getId());
            row.createCell(1).setCellValue(advert.getTitle());
            row.createCell(2).setCellValue(advert.getCreateAt());
            row.createCell(3).setCellValue(advert.getCategory().getTitle());
            row.createCell(4).setCellValue(advert.getCountry().getName());
            row.createCell(5).setCellValue(advert.getCity().getName());
            row.createCell(6).setCellValue(advert.getDistrict().getName());
            row.createCell(7).setCellValue(advert.getPrice());
            row.createCell(8).setCellValue(advert.getStatus().getName());
            row.createCell(9).setCellValue(advert.getViewCount());
            row.createCell(10).setCellValue(advert.getBuiltIn());
            // roles
            StringJoiner sj = new StringJoiner(",");

            row.createCell(11).setCellValue(sj.toString());

        }

        workbook.write(out);                            // 5- Write data into excel
        workbook.close();                               // 6- Close the workbook

        return new ByteArrayInputStream(out.toByteArray());

    }

    public static ByteArrayInputStream getMostPopularAdvertsExcelReport(List<Advert> adverts) throws IOException {
        Workbook workbook = new XSSFWorkbook(); // excel creator

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Sheet sheet = workbook.createSheet(SHEET_MOST_POPULAR_ADVERTS);     // 1- add sheet
        Row headerRow = sheet.createRow(0);         // 2- add header row
        for (int i = 0; i < ADVERT_MOST_POPULAR_HEADERS.length; i++) {     // 3- add fields into row
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(ADVERT_MOST_POPULAR_HEADERS[i]);
        }

        int rowId = 1;                                      // 4- add data into body rows
        for (Advert advert : adverts
        ) {
            Row row = sheet.createRow(rowId++);
            row.createCell(0).setCellValue(advert.getId());
            row.createCell(1).setCellValue(advert.getTitle());
            row.createCell(2).setCellValue(advert.getCreateAt());
            row.createCell(3).setCellValue(advert.getCategory().getTitle());
            row.createCell(4).setCellValue(advert.getCountry().getName());
            row.createCell(5).setCellValue(advert.getCity().getName());
            row.createCell(6).setCellValue(advert.getDistrict().getName());
            row.createCell(7).setCellValue(advert.getPrice());
            row.createCell(8).setCellValue(advert.getStatus().getName());
            row.createCell(9).setCellValue(advert.getViewCount());
            row.createCell(10).setCellValue(advert.getBuiltIn());
            row.createCell(11).setCellValue(advert.getTourRequests().size());
            // roles
            StringJoiner sj = new StringJoiner(",");

            row.createCell(11).setCellValue(sj.toString());

        }

        workbook.write(out);                            // 5- Write data into excel
        workbook.close();                               // 6- Close the workbook

        return new ByteArrayInputStream(out.toByteArray());

    }


    public static ByteArrayInputStream getUserExcelReport(List<User> users) throws IOException {
        Workbook workbook = new XSSFWorkbook(); // excel creator

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Sheet sheet = workbook.createSheet(SHEET_USER);     // 1- add sheet
        Row headerRow = sheet.createRow(0);         // 2- add header row
        for (int i = 0; i < USER_HEADERS.length; i++) {     // 3- add fields into row
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(USER_HEADERS[i]);
        }

        int rowId = 1;                                      // 4- add data into body rows
        for (User user : users
        ) {
            Row row = sheet.createRow(rowId++);
            row.createCell(0).setCellValue(user.getId());
            row.createCell(1).setCellValue(user.getFirstName());
            row.createCell(2).setCellValue(user.getLastName());
            row.createCell(3).setCellValue(user.getPhoneNumber());
            row.createCell(4).setCellValue(user.getEmail());
            row.createCell(5).setCellValue(user.getCreateAt().toString());
            row.createCell(6).setCellValue(user.getBuiltIn());
            row.createCell(7).setCellValue(user.getUserRoles().stream().toString());
            // roles
            StringJoiner sj = new StringJoiner(",");
            for (UserRole role : user.getUserRoles()
            ) {
                sj.add(role.getRoleType().getName());
            }
            row.createCell(8).setCellValue(sj.toString());

        }

        workbook.write(out);                            // 5- Write data into excel
        workbook.close();                               // 6- Close the workbook

        return new ByteArrayInputStream(out.toByteArray());

    }


    public static ByteArrayInputStream getTourRequestExcelReport(List<TourRequest> tourRequests) throws IOException {
        Workbook workbook = new XSSFWorkbook(); // excel creator

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Sheet sheet = workbook.createSheet(SHEET_TOUR_REQUEST);     // 1- add sheet
        Row headerRow = sheet.createRow(0);         // 2- add header row
        for (int i = 0; i < TOUR_REQUESTS_HEADERS.length; i++) {     // 3- add fields into row
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(TOUR_REQUESTS_HEADERS[i]);
        }

        int rowId = 1;                                      // 4- add data into body rows
        for (TourRequest tourRequest : tourRequests
        ) {
            Row row = sheet.createRow(rowId++);
            row.createCell(0).setCellValue(tourRequest.getId());
            row.createCell(1).setCellValue(tourRequest.getTourReqStatus().toString());
            row.createCell(2).setCellValue(tourRequest.getTourDate());
            row.createCell(3).setCellValue(tourRequest.getTourTime().toString());
            row.createCell(4).setCellValue(tourRequest.getCreateAt());
            row.createCell(5).setCellValue(tourRequest.getUpdateAt());
            row.createCell(7).setCellValue(String.format("%s %s", tourRequest.getOwnerUser().getFirstName(), tourRequest.getOwnerUser().getLastName()));
            row.createCell(7).setCellValue(String.format("%s %s", tourRequest.getGuestUser().getFirstName(), tourRequest.getGuestUser().getLastName()));
            row.createCell(8).setCellValue(tourRequest.getAdvert().getTitle());
        }

        workbook.write(out);                            // 5- Write data into excel
        workbook.close();                               // 6- Close the workbook

        return new ByteArrayInputStream(out.toByteArray());

    }
}
