package com.team03.controller.buiness;

import com.team03.payload.response.business.ReportResponse;
import com.team03.payload.response.business.ResponseMessage;
import com.team03.service.business.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequiredArgsConstructor
@RequestMapping("/report")
@RestController
public class ReportsController {


    private final ReportService reportService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    @Operation(tags = "Report",summary = "G01",description = "ADMIN MANAGER ")
    public ResponseMessage<ReportResponse> getStatistics(){

        return reportService.getIstatistics();

    }

    @GetMapping("/download")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    @Operation(tags = "Report",summary = "G02",description = "ADMIN MANAGER ")
    public ResponseMessage<?> getAdvertsByDate (@RequestParam(value = "beginDate" ) String beginDate,
                                  @RequestParam(value = "endDate")String endDate,
                                  @RequestParam(value = "categoryTitle", required = false) Long categoryTitleId,
                                  @RequestParam(value = "typeTitle", required = false) Long typeTitleId,
                                  @RequestParam(value = "status",required = false) Integer status )  {

        return reportService.getAdvertReportsByQuery(beginDate,endDate,categoryTitleId,typeTitleId,status);
    }

    @GetMapping("/most-populer-adverts")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    @Operation(tags = "Report",summary = "G03",description = "ADMIN MANAGER ")
    public ResponseMessage<?> getMostPopulerAdverts (@RequestParam Integer amount ) {


        return reportService.getPopularProperties(amount);
    }

    @GetMapping("/users")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')") //role Customer, Admin vs seklinde gelmeli
    @Operation(tags = "Report",summary = "G04",description = "ADMIN MANAGER -Rol Admin, Manager seklinde gelmeli")
    public ResponseMessage<?> getUsers (@RequestParam String role ){


        return reportService.getUserByRole(role);
    }

    @GetMapping("/tour-requests")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    @Operation(tags = "Report",summary = "G05",description = "ADMIN MANAGER ")
    public ResponseMessage<?> getTourRequestByDateAndStatus (@RequestParam(value = "beginDate" ) String beginDate,
                                                                   @RequestParam(value = "endDate") String endDate,
                                                                   @RequestParam(value = "status",required = false) Integer status ){


        return reportService.getTourRequestByDateAndStatus(beginDate,endDate,status);
    }

}
