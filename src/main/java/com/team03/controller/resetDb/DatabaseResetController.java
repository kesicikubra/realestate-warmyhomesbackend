package com.team03.controller.resetDb;


import com.team03.payload.response.business.ResponseMessage;

import com.team03.service.business.ResetDbService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class DatabaseResetController {




    private final ResetDbService resetDbService;


    @PostMapping("/db-reset")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(tags = "SETTINGS", summary = "X01", description = "ADMIN")
    @Transactional
    public ResponseMessage<String> resetDatabase() {

        return resetDbService.resetDatabase();

    }

   // private final DataSource dataSource;
//    public String resetDatabase() {
//
//        try (Connection conn = dataSource.getConnection();
//             Statement stmt = conn.createStatement()) {
//
//
//            stmt.executeUpdate("DROP SCHEMA public CASCADE");
//
//            stmt.executeUpdate("CREATE SCHEMA public");
//
//            return SuccessMessages.DB_RESET_MESSAGES;
//
//        } catch (Exception e) {
//            return ErrorMessages.RESET_DB + e.getMessage();
//        }
//    }




}
