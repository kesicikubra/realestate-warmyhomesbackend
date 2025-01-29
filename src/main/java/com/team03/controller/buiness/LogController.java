package com.team03.controller.buiness;

import com.team03.payload.response.business.LogResponse;
import com.team03.payload.response.business.ResponseMessage;
import com.team03.service.business.LogService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/logs")
public class LogController {

    private final LogService logService;


    @GetMapping("/all")
    @Operation(tags = "Log",summary = "getAllLogs",description = "ADMIN MANAGER ")
    @PreAuthorize("hasAnyAuthority('MANAGER','ADMIN')")
    public ResponseMessage<Page<LogResponse>> getAllLogs(@RequestParam(name = "page", defaultValue = "0") int page,
                                                         @RequestParam(name = "size", defaultValue = "20") int size,
                                                         @RequestParam(name = "sort", defaultValue = "category") String sort,
                                                         @RequestParam(name = "type", defaultValue = "asc") String type){
      return logService.getAllPage(page,size,sort,type);
    }

    @GetMapping("/{id}")
    @Operation(tags = "Log",summary = "getByUserLogs",description = "ADMIN MANAGER ")
    @PreAuthorize("hasAnyAuthority('MANAGER','ADMIN')")
    public ResponseMessage<Page<LogResponse>> getByUserLogs(@RequestParam Long id,
                                                            @RequestParam(name = "page", defaultValue = "0") int page,
                                                            @RequestParam(name = "size", defaultValue = "20") int size,
                                                            @RequestParam(name = "sort", defaultValue = "category") String sort,
                                                            @RequestParam(name = "type", defaultValue = "asc") String type){
        return logService.getByUserPage(id,page,size,sort,type);
    }
}
