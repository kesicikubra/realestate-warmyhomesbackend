package com.team03.service.business;

import com.team03.entity.business.Advert;
import com.team03.entity.business.TourRequest;
import com.team03.entity.enums.AdvertStatus;
import com.team03.entity.user.User;
import com.team03.excel.ExcelWriter;
import com.team03.exception.ConflictException;
import com.team03.i18n.MessageUtil;
import com.team03.payload.mappers.ReportMapper;
import com.team03.payload.response.excelResponses.AdvertExcelResponse;
import com.team03.payload.response.excelResponses.TourRequestExcelResponse;
import com.team03.payload.response.excelResponses.UserExcelResponse;
import com.team03.payload.response.business.ReportResponse;
import com.team03.payload.response.business.ResponseMessage;
import com.team03.repository.business.AdvertRepository;
import com.team03.service.helper.MethodHelper;
import com.team03.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final MethodHelper methodHelper;
    private final AdvertService advertService;
    private final UserService userService;
    private final TourRequestService tourRequestService;
    private final ReportMapper reportMapper;
    private final AdvertRepository advertRepository;

    public ResponseMessage<ReportResponse> getIstatistics() {
      ReportResponse reportResponse =  ReportResponse.builder()
                .advertAmount(methodHelper.getCountAdvert())
                .advertTypeAmount(methodHelper.getCountAdvertType())
                .userAmount(methodHelper.getCountUser())
                .tourRequestAmount(methodHelper.getCountTourRequest())
                .categoryAmount(methodHelper.getCategoryCount())
                .build();
      return ResponseMessage.<ReportResponse>builder()
              .message(MessageUtil.getMessage("istatistics.are.founded"))
              .object(reportResponse)
              .httpStatus(HttpStatus.OK).build();

    }

    public ResponseMessage<?> getAdvertReportsByQuery(String beginDateString, String endDateString,
                                                     Long categoryTitleId, Long typeTitleId,
                                                     Integer status) {

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate beginDate = LocalDate.parse(beginDateString, formatter);
            LocalDate endDate = LocalDate.parse(endDateString, formatter);
            if (beginDate.isAfter(endDate)) {
                throw new ConflictException(MessageUtil.getMessage("format.is.wrong.date.time"));
            }
            String typeTitle=null;
            if (typeTitleId!=null){
                typeTitle= methodHelper.getAdvertTypeById(typeTitleId).getTitle();
            }

            String categoryTitle=null;

            if (categoryTitleId!=null){
                  categoryTitle=methodHelper.getCategoryById(categoryTitleId).getTitle();
            }

            AdvertStatus advertStatus=null;
            if (status != null) {
                switch (status) {
                    case 0 -> advertStatus = AdvertStatus.PENDING;
                    case 1 -> advertStatus = AdvertStatus.ACTIVATED;
                    case 2 -> advertStatus = AdvertStatus.REJECTED;
                }
            }

            List<Advert> adverts = advertRepository.findAdvertsBetweenDatesAndFilters(beginDate,endDate,categoryTitle, typeTitle,advertStatus);

            List<AdvertExcelResponse> advertExcelResponse = adverts.stream()
                    .map(reportMapper::mapToAdvertExcelResponse)
                    .collect(Collectors.toList());
            File file = ExcelWriter.writeExcel("ADVERTS", advertExcelResponse);
            if (file==null){
                return ResponseMessage.builder ()
                        .message (MessageUtil.getMessage ("no.data.for.report"))
                        .build ();
            }

            //   assert file != null;
            byte[] fileBytes = Files.readAllBytes(file.toPath());
            String base64EncodedFile = Base64.getEncoder().encodeToString(fileBytes);

            // Dosyayı base64 olarak gönder
            return ResponseMessage.builder ()
                    .object (base64EncodedFile)
                    .build ();
            // .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
            //.body(base64EncodedFile);




        } catch (DateTimeParseException e) {
            throw new ConflictException(MessageUtil.getMessage("format.is.wrong.date.time"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
        throw new RuntimeException(MessageUtil.getMessage("excel.report.error.message"));
    }

    }

    public ResponseMessage<?> getPopularProperties(Integer amount) {


        try {
            List<Advert> adverts =   advertService.findAdvertsWithMostTourRequests(amount);
            List<AdvertExcelResponse> advertExcelResponse = adverts.stream()
                    .map(reportMapper::mapToAdvertExcelResponse)
                    .collect(Collectors.toList());
            File file = ExcelWriter.writeExcel("POPULER PROPERTIES", advertExcelResponse);
            if (file==null){
                return ResponseMessage.builder ()
                        .message (MessageUtil.getMessage("no.data.for.report"))
                        .build ();

            }
            byte[] fileBytes = Files.readAllBytes(file.toPath());
            String base64EncodedFile = Base64.getEncoder().encodeToString(fileBytes);

            // Dosyayı base64 olarak gönder
            return
                    ResponseMessage.builder ()
                            .object (base64EncodedFile)
                            .build ();
                   // .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")


        } catch (IOException e) {
            throw new RuntimeException(MessageUtil.getMessage("excel.report.error.message"));
        }

    }
    public ResponseMessage <?> getUserByRole(String role) {
        try {
            List<User> users =  userService.findAllUsersByRole(role);
            List<UserExcelResponse> userExcelResponse=users.stream().map(reportMapper::userToUserExcelResponse).collect(Collectors.toList());

            File file = ExcelWriter.writeExcel("USER LIST", userExcelResponse);
            if (file==null){
                return ResponseMessage.builder ()
                        .message (MessageUtil.getMessage ("no.data.for.report"))
                        .build ();
            }
            byte[] fileBytes = Files.readAllBytes(file.toPath());
            String base64EncodedFile = Base64.getEncoder().encodeToString(fileBytes);

            // Dosyayı base64 olarak gönder
            return ResponseMessage.builder ()
                    .object (base64EncodedFile)
                    .httpStatus (HttpStatus.OK)
                    .build ();
                    // .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")

        } catch (IOException e) {
            throw new RuntimeException(MessageUtil.getMessage("excel.report.error.message"));
        }


    }






     public ResponseMessage<?> getTourRequestByDateAndStatus(String beginDateString, String endDateString,
                                                                  Integer requestedStatus) {

     try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate beginDate = LocalDate.parse(beginDateString);
            LocalDate endDate = LocalDate.parse(endDateString);

            if (beginDate.isAfter(endDate)) {
                return ResponseMessage.builder()
                        .message(MessageUtil.getMessage("format.is.wrong.date.time"))
                        .httpStatus(HttpStatus.CONFLICT)
                        .build();
            }

            Integer status=null;
            if (requestedStatus!=null){
                status=requestedStatus;
            }

            List<TourRequest> tourRequests = tourRequestService.findTourRequestByBetweenDatesAndStatus(beginDate, endDate, status);
            List<TourRequestExcelResponse> tourRequestExcelResponses = tourRequests.stream()
                    .map(reportMapper::toTourRequestExcelResource)
                    .toList();

         File file = ExcelWriter.writeExcel("TOUR REQUESTS", tourRequestExcelResponses);
            if (file == null) {
                return ResponseMessage.builder()
                        .message(MessageUtil.getMessage("no.data.for.report"))
                        .httpStatus(HttpStatus.OK)
                        .build();
            }

            byte[] fileBytes = Files.readAllBytes(file.toPath());
            String base64EncodedFile = Base64.getEncoder().encodeToString(fileBytes);

            return ResponseMessage.builder()
                    .object(base64EncodedFile)
                    .httpStatus(HttpStatus.OK)
                    .build();

        } catch (DateTimeParseException e) {
            return ResponseMessage.builder()
                    .message(MessageUtil.getMessage("format.is.wrong.date.time"))
                    .httpStatus(HttpStatus.CONFLICT)
                    .build();
        } catch (IOException e) {
            return ResponseMessage.builder()
                    .message(MessageUtil.getMessage("excel.report.error.message"))
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }








//    public ResponseEntity<?> getTourRequestByDateAndStatus(String beginDateString, String endDateString,
//                                                                  Integer status) {
//
//        try {
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            LocalDate beginDate = LocalDate.parse(beginDateString, formatter);
//            LocalDate endDate = LocalDate.parse(endDateString, formatter);
//            if (beginDate.isAfter(endDate)) {
//                throw new ConflictException(MessageUtil.getMessage("format.is.wrong.date.time"));
//            }
//
//            List<TourRequest> tourRequests = tourRequestService.findTourRequestByBetweenDatesAndStatus(beginDate,endDate,status);
//            List<TourRequestExcelResponse> tourRequestExcelResponses = tourRequests.stream()
//                    .map(reportMapper::toTourRequestExcelResource)
//                    .toList();
//            File file = ExcelWriter.writeExcel("TOUR REQUESTS", tourRequestExcelResponses);
//            if (file==null){
//                return ResponseEntity.ok().body(MessageUtil.getMessage("no.data.for.report"));
//            }
//            byte[] fileBytes = Files.readAllBytes(file.toPath());
//            String base64EncodedFile = Base64.getEncoder().encodeToString(fileBytes);
//
//            // Dosyayı base64 olarak gönder
//            return ResponseEntity.ok()
//                   // .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
//                    .body(base64EncodedFile);
//        } catch (DateTimeParseException e) {
//            throw new ConflictException(MessageUtil.getMessage("format.is.wrong.date.time"));
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(MessageUtil.getMessage("excel.report.error.message"));
//        }
//    }
}
